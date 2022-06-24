package org.rent.app.controller;

import io.swagger.annotations.ApiOperation;
import org.rent.app.domain.Order;
import org.rent.app.dto.AddProdRequest;
import org.rent.app.service.OrderActionResponse;
import org.rent.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
public class OrderController {
    @Autowired
    private OrderService service;

    @ApiOperation("Get all")
    @GetMapping
    public Flux<Order> getAll() { return service.getAll();
    }

    @ApiOperation("Get an entity by id")
    @GetMapping("{id}")
    public Mono<Order> getById(@PathVariable Long id) { return service.findById(id);}

    @ApiOperation("Get contractor orders")
    @GetMapping("contractor/{id}")
    public Flux<Order> getByContractor(@PathVariable Long id) {
        return service.findByContractor(id);
    }

    @ApiOperation("Create new order")
    @PostMapping()
    public Mono<Order> create(@RequestParam(required=false) Long  contractorId) {
        return service.create(contractorId);
    }

    @ApiOperation("Add product to the order")
    @PutMapping("{orderId}")
    public Mono<OrderActionResponse> addProd(@PathVariable Long orderId, @RequestBody @Validated AddProdRequest addProdReq) {
        return  optLockWarp(orderId,()->service.addProduct(orderId, addProdReq));
    }

    @ApiOperation("Remove product from the order")
    @DeleteMapping("{orderId}/{orderItemId}")
    public Mono<OrderActionResponse> removeProd(@PathVariable Long orderId, @PathVariable Long orderItemId ) {
        return optLockWarp(orderId,()->service.removeItem(orderId,orderItemId));
    }

    @PutMapping("{orderId}/complete")
    public Mono<Order> complete(@PathVariable Long orderId) {
        return  service.complete(orderId);
    }

    @PutMapping("{orderId}/close")
    public Mono<Order> close(@PathVariable Long orderId) {
        return  service.close(orderId);
    }

    /**
     *  Handle OptimisticLockingFailure
     *
     * @param orderId of an order to deal with
     * @param action with an order
     * @return fresh instance of an order in any case.
     * OrderActionResponse has status CONCURRENT_ACCESS in case of optimistic locking failure
     */
    private Mono<OrderActionResponse> optLockWarp (Long orderId, Supplier<Mono<OrderActionResponse>> action) {
        return action.get().onErrorResume(error->{
            if (error instanceof OptimisticLockingFailureException){
                return service.findById(orderId)
                        .map(order-> OrderActionResponse.builder()
                                .status(OrderActionResponse.Status.CONCURRENT_ACCESS)
                                .message(error.getMessage())
                                .data(order).build());
            } else {
                return Mono.error(error);
            }
        });
    };
}
