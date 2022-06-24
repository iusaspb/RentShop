package org.rent.app.repository;

import org.rent.app.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
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
    Collection<Order> findAllByContractorId(Long contractorId);

}
