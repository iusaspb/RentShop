package org.rent.app.repository;

import org.rent.app.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
/**
 * CategoryRepository
 * <p>
 *     Category repository
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Collection<Category> findAllByParentId(Long parentId);
    /**
     *  Get all root categories
     * @return root categories
     */
    Collection<Category>  findAllByParentIsNull();
}
