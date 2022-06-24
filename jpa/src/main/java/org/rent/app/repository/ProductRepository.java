package org.rent.app.repository;

import org.rent.app.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
/**
 * ProductRepository
 * <p>
 *     Product repository
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    Collection<Product> findAllByCategoryId(Long categoryId);
    Collection<Product> findAllByOwnerId(Long ownerId);
    @Deprecated
    Collection<Product> findAllByNameLikeOrDescriptionLikeOrCategoryNameLike(String name, String description, String category);
    /**
     *  Find all products in the name, the description or category they are associated with,
     *  the search text is included
     * @param search text
     * @return products
     */
    @Query(value= """
            with recursive full_cat as (
            select * from category where "name" like ?1
            union all
            select  nl.*  from full_cat, category nl
            where full_cat.id = nl.parent_id\s
            )
            select product.* from product join full_cat on product.category_id = full_cat.id
            union\s
            select * from product where "name" like ?1 or description like ?1""", nativeQuery = true)
    Collection<Product> recursiveSearch(String search);

}
