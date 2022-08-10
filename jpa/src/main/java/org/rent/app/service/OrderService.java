package org.rent.app.service;

import lombok.extern.slf4j.Slf4j;
import org.rent.app.domain.Client;
import org.rent.app.domain.Order;
import org.rent.app.domain.OrderItem;
import org.rent.app.domain.OrderStatus;
import org.rent.app.domain.Product;
import org.rent.app.dto.AddProdRequest;
import org.rent.app.repository.OrderRepository;
import org.rent.app.repository.ProductRepository;
import org.rent.app.utils.DateInterval;
import org.rent.app.utils.DateIntervalProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.ListIterator;
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
@Slf4j
public class OrderService {
    @Autowired
    private ClientService ClientService;
    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    public Order findById(@NotNull Long orderId) {
        return repository.findWithItemsById(orderId).orElse(null);
    }

    public Collection<Order> findByClient(@NotNull Long clientId) {
        return repository.findAllByClientId(clientId);
    }
    public  Collection<Order> getAll() {
        return repository.findAll();
    }

    /**
     * Create new order for
     * @param clientId
     * @return created order
     */
    @Transactional
    public Order create(Long clientId) {
        Client client = Objects.nonNull(clientId)
                ?ClientService.findById(clientId)
                :ClientService.getCurrent();
        return repository.save(Order.builder()
                .client(client)
                .build());
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
    public OrderActionResponse addProduct(@NotNull Long orderId, @NotNull AddProdRequest prodReq) {
        Order order = repository.getReferenceById(orderId);
        if (!OrderStatus.IN_PROGRESS.equals(order.getStatus())) {
            throw new IllegalStateException(String.format("Incorrect Status %s",order.getStatus()));
        }
        Product prod = productRepository.getReferenceById(prodReq.getProdId());
        DateInterval rentPeriod = prodReq.getRentPeriod();
        boolean isAccepted = false;
        for (var item : prod.getItems()) { // iterate items
            var availableIntervals = item.getAvailableIntervals();
            if (CollectionUtils.isEmpty(availableIntervals)) continue; // skip if there is no available intervals
            if (DateIntervalProcessor.reserve(availableIntervals, rentPeriod)){
                // found item
                long price = prod.getPrice();
                // create order item
                OrderItem orderItem = OrderItem.builder()
                        .order(order)
                        .item(item)
                        .price(price)
                        .rentPeriod(rentPeriod)
                        .build();
                order.getItems().add(orderItem);
                //increase the order amount for the added product
                order.incAmount(price);
                isAccepted = true;
                break;
            }
        }
        log.info(isAccepted ? "Accept {}" : "Rejected {}",prodReq);
        return new OrderActionResponse(isAccepted ? OrderActionResponse.Status.OK: OrderActionResponse.Status.NO_ITEM
                , order,null);
    }
    /**
     * Remove an product from the order
     * @param orderId
     * @param orderItemId
     * @return OrderActionResponse with status OK or IllegalArgumentException if
     * there is no an item in the order
     */
    @Transactional
    public OrderActionResponse removeItem(@NotNull Long orderId, @NotNull Long orderItemId){
        Order order = repository.getReferenceById(orderId );
        if (!OrderStatus.IN_PROGRESS.equals(order.getStatus())) {
            throw new IllegalStateException(String.format("Incorrect Status %s",order.getStatus()));
        }
        ListIterator<OrderItem> li = order.getItems().listIterator();
        while (li.hasNext()) { // iterate order items
            var orderItem = li.next();
            if (orderItemId.equals(orderItem.getId())) {
                var item = orderItem.getItem();
                var intervals = item.getAvailableIntervals();
                intervals = DateIntervalProcessor.release(intervals,orderItem.getRentPeriod());
                //save item with changed available intervals
                item.setAvailableIntervals(intervals);
                //decrease the order amount with the removed product
                order.decAmount(orderItem.getPrice());
                // delete an order item for the removed product
                li.remove();
                return  new OrderActionResponse(OrderActionResponse.Status.OK, order,null);
            }
        }
        throw new EntityNotFoundException(String.format("Could not find orderId=%d, itemId=%d",orderId,orderItemId));
    }
    /**
     * Complete an order. The order is closed for add/remove since this call.
     * @param orderId
     * @return
     */
    @Transactional
    public Order complete(@NotNull Long orderId) {
        Order order = repository.getReferenceById(orderId );
        if (!OrderStatus.IN_PROGRESS.equals(order.getStatus())) {
            throw new IllegalStateException(String.format("Incorrect Status %s",order.getStatus()));
        }
        order.setStatus(OrderStatus.COMPLETED);
        return order;
    }
    /**
     * Close an order. All items from the order are ready to rent.
     * @param orderId
     * @return
     */
    @Transactional
    public Order close(@NotNull Long orderId) {
        Order order = repository.getReferenceById(orderId );
        if (!OrderStatus.COMPLETED.equals(order.getStatus())) {
            throw new IllegalStateException(String.format("Incorrect Status %s",order.getStatus()));
        }
        for (var orderItem : order.getItems()){
            var item = orderItem.getItem();
            var intervals = item.getAvailableIntervals();
            intervals = DateIntervalProcessor.release(intervals,orderItem.getRentPeriod());
            item.setAvailableIntervals(intervals);
        }
        order.getItems().clear();
        order.setStatus(OrderStatus.CLOSED);
        return order;
    }
}
