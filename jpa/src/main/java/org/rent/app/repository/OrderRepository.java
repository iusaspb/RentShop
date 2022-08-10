package org.rent.app.repository;

import org.rent.app.domain.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * OrderRepository
 * <p>
 *     Order repository
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"items"})
    Optional<Order> findWithItemsById(Long orderId);

    Collection<Order> findAllByClientId(Long clientId);

}
