package org.rent.app.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
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
public class Product {
    @Id
    Long Id;
    @NotBlank
    String name;
    String description;
    String brand;
    /**
     * @see Category
     */
    @NotNull
    Long categoryId;
    /**
     * is not used in this example
     */
    @NotNull
    Long ownerId;
    @PositiveOrZero
    long price;
    @CreatedDate
    @LastModifiedDate
    LocalDateTime updated;
}
