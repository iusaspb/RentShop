package org.rent.app.controller;

import io.swagger.annotations.ApiOperation;
import org.rent.app.dto.AddProdRequest;
import org.rent.app.dto.OrderActionResponseDto;
import org.rent.app.dto.OrderDto;
import org.rent.app.mapper.OrderMapper;
import org.rent.app.service.OrderActionResponse;
import org.rent.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.function.Supplier;
/**
 * OrderController
 * <p>
 *     Order Controller
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@RestController
@RequestMapping("order/")
@Transactional
public class OrderController {
    private static final OrderMapper MAPPER = OrderMapper.INSTANCE;
    @Autowired
    private OrderService service;

    @ApiOperation("Get all")
    @GetMapping
    public Collection<OrderDto> getAll() {
        return MAPPER.toDtos(service.getAll());
    }

    @ApiOperation("Get an entity by id")
    @GetMapping("{id}")
    public OrderDto getById(@PathVariable Long id) {
        return MAPPER.toDto(service.findById(id));
    }

    @ApiOperation("Get client orders")
    @GetMapping("client/{id}")
    public Collection<OrderDto> getByClient(@PathVariable Long clientId) {
        return MAPPER.toDtos(service.findByClient(clientId));
    }

    @ApiOperation("Create new order")
    @PostMapping()
    public OrderDto create(@RequestParam(required=false) Long  clientId) {
        return MAPPER.toDto(service.create(clientId));
    }

    @ApiOperation("Add product to the order")
    @PutMapping("{orderId}")
    public OrderActionResponseDto addProd(@PathVariable Long orderId, @RequestBody @Validated  AddProdRequest addProdReq) {
        return MAPPER.toActionResponseDto(optLockWarp(orderId, ()-> service.addProduct(orderId, addProdReq)));
    }

    @ApiOperation("Remove product from the order")
    @DeleteMapping("{orderId}/{orderItemId}")
    public OrderActionResponseDto removeProd(@PathVariable Long orderId, @PathVariable Long orderItemId ) {
        return MAPPER.toActionResponseDto(optLockWarp(orderId, ()-> service.removeItem(orderId,orderItemId)));
    }

    @PutMapping("{orderId}/complete")
    public OrderDto complete(@PathVariable Long orderId) {
        return  MAPPER.toDto(service.complete(orderId));
    }

    @PutMapping("{orderId}/close")
    public OrderDto close(@PathVariable Long orderId) {
        return  MAPPER.toDto(service.close(orderId));
    }

    /**
     * Handle OptimisticLockingFailure
     *
     * @param orderId of an order to deal with
     * @param action with an order
     * @return fresh instance of an order in any case.
     *  OrderActionResponse has status CONCURRENT_ACCESS in case of optimistic locking failure
     */
    private OrderActionResponse optLockWarp (Long orderId, Supplier<OrderActionResponse> action) {
        try {
            return action.get();
        } catch (ObjectOptimisticLockingFailureException ex) {
            return new OrderActionResponse(OrderActionResponse.Status.CONCURRENT_ACCESS
                    , service.findById(orderId),null);
        }
    };
}