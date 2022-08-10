package org.rent.app.repository;

import org.rent.app.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * ClientRepository
 * <p>
 *     Client repository
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
public interface ClientRepository extends JpaRepository<Client, Long> {

}
