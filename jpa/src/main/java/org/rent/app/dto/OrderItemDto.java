package org.rent.app.dto;

import lombok.Data;
import org.rent.app.utils.DateInterval;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
/**
 * OrderItemDto
 * <p>
 *     DTO for OrderItem
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Data
public class OrderItemDto {
    @NotNull
    Long id;
    @PositiveOrZero
    long price;
    /**
     * @see org.rent.app.domain.Product
     */
    @NotNull
    Long productId;
    @NotNull
    DateInterval rentPeriod;
}
