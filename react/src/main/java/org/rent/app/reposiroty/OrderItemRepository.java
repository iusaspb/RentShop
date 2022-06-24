package org.rent.app.reposiroty;

import org.rent.app.domain.OrderItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/**
 * OrderItemRepository
 * <p>
 *     OrderItem repository
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
public interface OrderItemRepository extends ReactiveCrudRepository<OrderItem, Long> {
    Flux<OrderItem> findAllByOrderId(Long orderId);
    Mono<Void> deleteAllByOrderId(Long orderId);
}
