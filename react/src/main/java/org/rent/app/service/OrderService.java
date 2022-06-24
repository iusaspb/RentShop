package org.rent.app.service;

import org.rent.app.domain.Item;
import org.rent.app.domain.Order;
import org.rent.app.domain.OrderItem;
import org.rent.app.domain.OrderStatus;
import org.rent.app.dto.AddProdRequest;
import org.rent.app.reposiroty.ItemRepository;
import org.rent.app.reposiroty.OrderItemRepository;
import org.rent.app.reposiroty.OrderRepository;
import org.rent.app.reposiroty.ProductRepository;
import org.rent.app.utils.DateInterval;
import org.rent.app.utils.DateIntervalProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.Objects;
/**
 * OrderService
 * <p>
 *     Order Service
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Service
public class OrderService {
    @Autowired
    private ContractorService contractorService;
    @Autowired
    private OrderRepository repository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;

    public Mono<Order> findById(@NotNull Long orderId) {
        return repository.findById(orderId);
    }
    public Flux<Order> findByContractor(@NotNull Long contractorId) {
        return repository.findAllByContractorId(contractorId);
    }
    public Flux<Order> getAll() {
        return repository.findAll();
    }

    /**
     * Create new order for
     * @param contractorId
     * @return created order
     */
    @Transactional
    public Mono<Order> create(Long contractorId) {
        return (Objects.nonNull(contractorId)
                ? Mono.just(contractorId)
                : contractorService.getCurrent()
                ).flatMap(id -> repository.save((Order.builder().contractorId(id).build())));
    }

    /**
     *
     * Add an product to
     * @param orderId
     * @param prodReq
     * @return OrderActionResponse with status
     * <ul>
     * <li>OK if there is an available item</li>
     * <li>NO_ITEM otherwise</li>
     * </ul>
     */
    @Transactional
    public Mono<OrderActionResponse> addProduct(@NotNull Long orderId, @NotNull AddProdRequest prodReq) {
        DateInterval rentPeriod = prodReq.getRentPeriod();
        Long prodId = prodReq.getProdId();
        return getOrderOfStatus(orderId, OrderStatus.IN_PROGRESS)
                .thenMany(itemRepository.findAllByProductId(prodId))
                .map(item -> tryToReserve(item, rentPeriod)) //find an item available in rentPeriod
                .filter(Item::isReserved)
                .next()// take the first as and save it with changed available intervals
                .flatMap(itemRepository::save)
                // create order item
                .zipWith(productRepository.findById(prodId), (item, prod) ->
                        OrderItem.builder()
                                .orderId(orderId)
                                .itemId(item.getId())
                                .rentPeriod(rentPeriod)
                                .price(prod.getPrice())
                                .build())
                .flatMap(orderItemRepository::save)
                //increase the order amount for the added product
                .zipWith(repository.findById(orderId), (orderItem, order) ->
                        this.incOrderAmount(order,orderItem.getPrice()))
                .flatMap(repository::save)
                .map(order -> OrderActionResponse.builder()
                        .status(OrderActionResponse.Status.OK)
                        .data(order)
                        .build())
                // There are no items available in rentPeriod. Returns response with NO_ITEM status
                .switchIfEmpty(
                        repository.findById(orderId)
                                .map(order -> OrderActionResponse.builder().status(OrderActionResponse.Status.NO_ITEM).data(order).build()));
    }

    /**
     * Remove an product from the order
     * @param orderId
     * @param orderItemId
     * @return OrderActionResponse with status OK or IllegalArgumentException if
     * there is no an item in the order
     */
    @Transactional
    public Mono<OrderActionResponse> removeItem(@NotNull Long orderId, @NotNull Long orderItemId) {
        return getOrderOfStatus(orderId, OrderStatus.IN_PROGRESS)
                .then(orderItemRepository.findById(orderItemId))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Could not find orderItem")))
                //save item with changed available intervals
                .flatMap(orderItem ->
                        itemRepository.findById(orderItem.getItemId()).zipWith(Mono.just(orderItem.getRentPeriod()),
                                this::releaseRentPeriod))
                .flatMap(itemRepository::save)
                //decrease the order amount with the removed product
                .then(repository.findById(orderId).zipWith(orderItemRepository.findById(orderItemId).map(OrderItem::getPrice),
                        this::decOrderAmount
                ))
                .flatMap(repository::save)
                // delete an order item for the removed product
                .then(orderItemRepository.deleteById(orderItemId))
                .then(repository.findById(orderId))
                .map(order -> OrderActionResponse.builder().data(order).status(OrderActionResponse.Status.OK).build());
    }

    /**
     * Complete an order. The order is closed for add/remove since this call.
     * @param orderId
     * @return
     */
    @Transactional
    public Mono<Order> complete(@NotNull Long orderId) {
        return getOrderOfStatus(orderId, OrderStatus.IN_PROGRESS)
                .map(order -> order.toBuilder().status(OrderStatus.COMPLETED).build())
                .flatMap(repository::save);
    }
    /**
     * Close an order. All items from the order are ready to rent.
     * @param orderId
     * @return
     */
    @Transactional
    public Mono<Order> close(@NotNull Long orderId) {
        return getOrderOfStatus(orderId, OrderStatus.COMPLETED)
                .thenMany(orderItemRepository.findAllByOrderId(orderId))
                // save items from the order with changed available intervals
                .flatMap(orderItem -> itemRepository.findById(orderItem.getItemId())
                        .zipWith(Mono.just(orderItem.getRentPeriod()), this::releaseRentPeriod))
                .flatMap(itemRepository::save)
                // delete all order items from the order
                .then(orderItemRepository.deleteAllByOrderId(orderId))
                .then(repository.findById(orderId))
                .map(order -> order.toBuilder().status(OrderStatus.CLOSED).build())
                .flatMap(repository::save);
    }

    private Item tryToReserve(Item item, final DateInterval rentPeriod) {
        final var availableIntervals = item.getAvailableIntervals();
        if (CollectionUtils.isEmpty(availableIntervals)) return item;
        return DateIntervalProcessor.reserve(availableIntervals, rentPeriod)
                ? item.toBuilder().availableIntervals(availableIntervals).reserved(true).build()
                : item;
    }

    private Item releaseRentPeriod(Item item, final DateInterval rentPeriod) {
        var availableIntervals = DateIntervalProcessor.release(item.getAvailableIntervals(), rentPeriod);
        return item.toBuilder().availableIntervals(availableIntervals).build();
    }

    private Mono<Order> getOrderOfStatus(Long orderId, OrderStatus expectedStatus) {
        return repository.findById(orderId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Could not find order")))
                .flatMap(order ->
                        expectedStatus.equals(order.getStatus())
                                ? Mono.just(order)
                                : Mono.error(new IllegalStateException(String.format("Incorrect Status %s", order.getStatus())))
                );
    }
    private Order incOrderAmount(Order order, long price) {
        return order.toBuilder().amount(order.getAmount()+price).build();
    }

    private Order decOrderAmount(Order order, long price) {
        return order.toBuilder().amount(order.getAmount()-price).build();
    }
}
