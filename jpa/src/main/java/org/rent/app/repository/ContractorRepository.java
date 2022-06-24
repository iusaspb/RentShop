package org.rent.app.repository;

import org.rent.app.domain.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * ContractorRepository
 * <p>
 *     Contractor repository
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
public interface ContractorRepository extends JpaRepository<Contractor, Long> {

}
