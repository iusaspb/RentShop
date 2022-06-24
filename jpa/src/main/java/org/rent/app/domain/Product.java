package org.rent.app.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Product
 * <p>
 *     Product entity
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Product  extends EntityId {
    @NotBlank
    String name;
    String description;
    String brand;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    Category category;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    Owner owner;
    @PositiveOrZero
    long price;
    /**
     * Collection of product's items
     */
    @NotNull
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,mappedBy="product",cascade = CascadeType.ALL)
    Collection<Item> items;
    @CreatedDate
    @LastModifiedDate
    LocalDateTime updated;
}
