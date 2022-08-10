package org.rent.app.service;

import org.junit.jupiter.api.Test;
import org.rent.app.PrepareEnv4Test;
import org.rent.app.domain.Order;
import org.rent.app.domain.OrderItem;
import org.rent.app.dto.AddProdRequest;
import org.rent.app.utils.DateInterval;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class OrderServiceTests extends PrepareEnv4Test {
    private final LocalDate now = LocalDate.now();

    @Test
    public void crudTest() {
        Order foundOrder = orderService.findById(orderId);
        assertNotNull(foundOrder);
    }

    @Test
    public void crudItemTest(){
        DateInterval di = new DateInterval(now,now.plusDays(1L));
        AddProdRequest prodReq =  new AddProdRequest();
        prodReq.setProdId(productId);
        prodReq.setRentPeriod(di);
        OrderActionResponse resp = orderService.addProduct(orderId,prodReq);
        assertNotNull(resp);
        assertEquals(OrderActionResponse.Status.OK,resp.getStatus());
        List<OrderItem> orderItems = orderService.findById(orderId).getItems();
        assertNotNull(orderItems);
        assertEquals(1, orderItems.size());
        Long orderItemId = orderItems.get(0).getId();
        resp = orderService.removeItem(orderId,orderItemId);
        assertNotNull(resp);
        assertEquals(OrderActionResponse.Status.OK,resp.getStatus());
        orderItems = orderService.findById(orderId).getItems();
        assertNotNull(orderItems);
        assertTrue(orderItems.isEmpty());
    }
    @Test
    public void availableIntervalsTest(){
        DateInterval di = new DateInterval(now,now.plusDays(1L));
        AddProdRequest prodReq =  new AddProdRequest();
        prodReq.setProdId(productId);
        prodReq.setRentPeriod(di);
        OrderActionResponse resp = orderService.addProduct(orderId,prodReq);
        assertNotNull(resp);
        assertEquals(OrderActionResponse.Status.OK,resp.getStatus());
        List<OrderItem> orderItems = orderService.findById(orderId).getItems();
        assertNotNull(orderItems);
        assertEquals(1, orderItems.size());
        /*
         *  try again with the same interval
         */
        prodReq =  new AddProdRequest();
        prodReq.setProdId(productId);
        prodReq.setRentPeriod(di);
        resp = orderService.addProduct(orderId,prodReq);
        assertNotNull(resp);
        //
        assertEquals(OrderActionResponse.Status.NO_ITEM,resp.getStatus());
        orderItems = orderService.findById(orderId).getItems();
        assertNotNull(orderItems);
        assertEquals(1, orderItems.size());
        resp = orderService.removeItem(orderId,orderItems.get(0).getId());
        assertNotNull(resp);
        assertEquals(OrderActionResponse.Status.OK,resp.getStatus());
    }
    @Test
    public void checkOrderAmount(){
        assertEquals(0,orderService.findById(orderId).getAmount());
        // add product
        DateInterval di = new DateInterval(now,now.plusDays(1L));
        AddProdRequest prodReq =  new AddProdRequest();
        prodReq.setProdId(productId);
        prodReq.setRentPeriod(di);
        OrderActionResponse resp = orderService.addProduct(orderId,prodReq);
        assertNotNull(resp);
        assertEquals(OrderActionResponse.Status.OK,resp.getStatus());
        var amount = orderService.findById(orderId).getItems().stream().mapToLong(OrderItem::getPrice).sum();
        assertEquals(amount,orderService.findById(orderId).getAmount());
        // add 2nd product
        di = new DateInterval(now.plusDays(1L),now.plusDays(2L));
        prodReq =  new AddProdRequest();
        prodReq.setProdId(productId);
        prodReq.setRentPeriod(di);
        resp = orderService.addProduct(orderId,prodReq);
        assertNotNull(resp);
        assertEquals(OrderActionResponse.Status.OK,resp.getStatus());
        amount = orderService.findById(orderId).getItems().stream().mapToLong(OrderItem::getPrice).sum();
        assertEquals(amount,orderService.findById(orderId).getAmount());
        //remove items
        List<Long> orderItemIds =
                orderService.findById(orderId).getItems().stream()
                        .map(OrderItem::getId).toList();

        Objects.requireNonNull(orderItemIds).forEach(orderItemId->{
            OrderActionResponse.Status status = orderService
                    .removeItem(orderId, orderItemId).getStatus();
            assertEquals(OrderActionResponse.Status.OK,status);
        });
        // amount is 0 without products
        assertEquals(0,orderService.findById(orderId).getAmount());
    }


}