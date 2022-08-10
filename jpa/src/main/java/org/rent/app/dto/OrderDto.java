package org.rent.app.dto;

import lombok.Data;
import org.rent.app.domain.Client;
import org.rent.app.domain.OrderStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
/**
 * OrderDto
 * <p>
 *     DTO for Order
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Data
public class OrderDto {
    @NotNull
    Long id;
    /**
     * @see Client
     */
    @NotNull
    Long clientId;
    @NotNull
    Collection<OrderItemDto> items;
    @PositiveOrZero
    long amount;
    @NotNull
    OrderStatus status;
}
