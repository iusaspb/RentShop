package org.rent.app.reposiroty;

import org.rent.app.domain.Item;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
/**
 * ItemRepository
 * <p>
 *     Item repository
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
public interface ItemRepository extends ReactiveCrudRepository<Item, Long> {
    Flux<Item> findAllByProductId(Long productId);
}
