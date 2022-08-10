package org.rent.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.rent.app.domain.Client;
import org.rent.app.domain.Order;
import org.rent.app.repository.OrderRepository;
import org.rent.app.repository.ProductRepository;
import org.rent.app.service.ClientService;
import org.rent.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PrepareEnv4Test {

    protected static final Long clientId = 1L;
    protected static final Long storeId = 1L;
    @Autowired
    protected ClientService clientService;
    @Autowired
    protected OrderService orderService;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected ProductRepository productRepository;
    protected Client client;
    protected Long productId;
    protected Long orderId;

    @BeforeEach
    void createOrder() {
        client = clientService.findById(clientId);
        assertNotNull(client);
        assertEquals(clientId,client.getId());
        var products = productRepository.findAll();
        assertNotNull(products);
        assertNotEquals(0, products.size());
        productId = products.get(0).getId();
        Order order = orderService.create(clientId);
        assertNotNull(order);
        assertEquals(clientId, order.getClient().getId());
        orderId = order.getId();
    }

    @AfterEach
    void deleteOrder() {
        assertNotNull(orderService.findById(orderId));
        orderRepository.deleteById(orderId);
        assertNull(orderService.findById(orderId));
    }

}