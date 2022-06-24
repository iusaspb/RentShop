package org.rent.app.reposiroty;

import org.rent.app.domain.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
/**
 * CategoryRepository
 * <p>
 *     Category repository
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
    Flux<Category> findAllByParentId(Long parentId);
    /**
     *  Get all root categories
     * @return root categories
     */
    Flux<Category>  findAllByParentIdIsNull();
}
