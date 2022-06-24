package org.rent.app.reposiroty;

import org.rent.app.domain.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
/**
 * OrderRepository
 * <p>
 *     Order repository
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
    Flux<Order> findAllByContractorId(Long contractorId);
}
