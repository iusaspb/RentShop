package org.rent.app.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
/**
 * Category
 * <p>
 *     Category entity
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Data
@Entity
public class Category  extends EntityId {
    @NotBlank
    String name;
    /**
     * null for root categories
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    Category parent;
}
