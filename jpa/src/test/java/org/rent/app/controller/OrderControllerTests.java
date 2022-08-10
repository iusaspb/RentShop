package org.rent.app.controller;

import org.junit.jupiter.api.Test;
import org.rent.app.PrepareEnv4Test;
import org.rent.app.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class OrderControllerTests extends PrepareEnv4Test {
    @Autowired
    private OrderController orderController;

    @Test
    public void getByClientTest(){
        Collection<OrderDto> orders =  orderController.getByClient(clientId);
        assertNotNull(orders);
        assertEquals(1, orders.size());
        OrderDto order = orders.iterator().next();
        assertEquals(clientId, order.getClientId());
    }
}
