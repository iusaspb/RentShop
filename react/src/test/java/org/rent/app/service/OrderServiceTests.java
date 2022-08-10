package org.rent.app.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rent.app.domain.Order;
import org.rent.app.domain.OrderItem;
import org.rent.app.domain.Product;
import org.rent.app.dto.AddProdRequest;
import org.rent.app.reposiroty.OrderItemRepository;
import org.rent.app.reposiroty.OrderRepository;
import org.rent.app.reposiroty.ProductRepository;
import org.rent.app.utils.DateInterval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class OrderServiceTests {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;
    private static final Long clientId = 1L;
    private Long productId;
    private Long orderId;
    private final LocalDate now = LocalDate.now();

    @BeforeEach
    void createOrder(){
        productId = Objects.requireNonNull(productRepository.findAll().map(Product::getId).blockFirst());
        var order = orderService.create(clientId).block();
        assertNotNull(order);
        assertEquals(clientId,order.getClientId());
        orderId = order.getId();
    }

    @AfterEach
    void deleteOrder(){
        assertNotNull(orderService.findById(orderId).block());
        orderRepository.deleteById(orderId).block();
        assertNull(orderService.findById(orderId).block());
    }


    @Test
    public void crudTest(){
        Order foundOrder =  orderService.findById(orderId).block();
        assertNotNull(foundOrder);
    }

    @Test
    public void crudItemTest(){
        DateInterval di = new DateInterval(now,now.plusDays(1L));
        AddProdRequest prodReq =  new AddProdRequest();
        prodReq.setProdId(productId);
        prodReq.setRentPeriod(di);
        OrderActionResponse resp = orderService.addProduct(orderId,prodReq).block();
        assertNotNull(resp);
        assertEquals(OrderActionResponse.Status.OK,resp.getStatus());
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId).collectList().block();
        assertNotNull(orderItems);
        assertEquals(1, orderItems.size());
        Long orderItemId = orderItems.get(0).getId();
        resp = orderService.removeItem(orderId,orderItemId).block();
        assertNotNull(resp);
        assertEquals(OrderActionResponse.Status.OK,resp.getStatus());
        orderItems = orderItemRepository.findAllByOrderId(orderId).collectList().block();
        assertNotNull(orderItems);
        assertTrue(orderItems.isEmpty());
    }
    @Test
    public void availableIntervalsTest(){
        DateInterval di = new DateInterval(now,now.plusDays(1L));
        AddProdRequest prodReq =  new AddProdRequest();
        prodReq.setProdId(productId);
        prodReq.setRentPeriod(di);
        OrderActionResponse resp = orderService.addProduct(orderId,prodReq).block();
        assertNotNull(resp);
        assertEquals(OrderActionResponse.Status.OK,resp.getStatus());
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId).collectList().block();
        assertNotNull(orderItems);
        assertEquals(1, orderItems.size());
        /*
         *  try again with the same interval
         */
        prodReq =  new AddProdRequest();
        prodReq.setProdId(productId);
        prodReq.setRentPeriod(di);
        resp = orderService.addProduct(orderId,prodReq).block();
        assertNotNull(resp);
        //
        assertEquals(OrderActionResponse.Status.NO_ITEM,resp.getStatus());
        orderItems = orderItemRepository.findAllByOrderId(orderId).collectList().block();
        assertNotNull(orderItems);
        assertEquals(1, orderItems.size());
        resp = orderService.removeItem(orderId,orderItems.get(0).getId()).block();
        assertNotNull(resp);
        assertEquals(OrderActionResponse.Status.OK,resp.getStatus());
    }
    @Test
    public void checkOrderAmount(){
        assertEquals(0,orderService.findById(orderId).map(Order::getAmount).block());
        // add product
        DateInterval di = new DateInterval(now,now.plusDays(1L));
        AddProdRequest prodReq =  new AddProdRequest();
        prodReq.setProdId(productId);
        prodReq.setRentPeriod(di);
        OrderActionResponse resp = orderService.addProduct(orderId,prodReq).block();
        assertNotNull(resp);
        assertEquals(OrderActionResponse.Status.OK,resp.getStatus());
        var amount = orderItemRepository.findAllByOrderId(orderId)
                .map(OrderItem::getPrice).reduce(0L, Long::sum).block();
        assertEquals(amount,orderService.findById(orderId).map(Order::getAmount).block());
        // add 2nd product
        di = new DateInterval(now.plusDays(1L),now.plusDays(2L));
        prodReq =  new AddProdRequest();
        prodReq.setProdId(productId);
        prodReq.setRentPeriod(di);
        resp = orderService.addProduct(orderId,prodReq).block();
        assertNotNull(resp);
        assertEquals(OrderActionResponse.Status.OK,resp.getStatus());
        amount = orderItemRepository.findAllByOrderId(orderId)
                .map(OrderItem::getPrice).reduce(0L, Long::sum).block();
        assertEquals(amount,orderService.findById(orderId).map(Order::getAmount).block());
        //remove items
        List<Long> orderItemIds =
        orderItemRepository
                .findAllByOrderId(orderId)
                .map(OrderItem::getId).collectList().block();

        Objects.requireNonNull(orderItemIds).forEach(orderItemId->{
            OrderActionResponse.Status status = orderService
                                                    .removeItem(orderId, orderItemId)
                                                    .map(OrderActionResponse::getStatus).block();
            assertEquals(OrderActionResponse.Status.OK,status);
        });
        // amount is 0 without products
        assertEquals(0,orderService.findById(orderId).map(Order::getAmount).block());
    }
}
