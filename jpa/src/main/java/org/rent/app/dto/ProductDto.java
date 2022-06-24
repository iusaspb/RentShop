package org.rent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
/**
 * ProductDto
 * <p>
 *     DTO for Product
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Data
public class ProductDto {
    @NotBlank
    String name;
    String description;
    String brand;
    /**
     * @see org.rent.app.domain.Category
     */
    @NotNull
    Long categoryId;
    /**
     * @see org.rent.app.domain.Owner
     */
    @NotNull
    Long ownerId;
    @PositiveOrZero
    long price;

}
