package org.rent.app.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.rent.app.utils.DateInterval;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

/**
 * OrderItem
 * <p>
 *     OrderItem connects an Item with an Order
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
public class OrderItem {
    @Id
    Long id;
    /**
     * @see Order
     */
    @NotNull
    Long orderId;
    /**
     * @see Item
     */
    @NotNull
    Long itemId;
    /**
     * rental price at the time of booking
     */
    @PositiveOrZero
    long price;
    /**
     * rental period
     */
    @NotNull
    DateInterval rentPeriod;
    @CreatedDate
    @LastModifiedDate
    LocalDateTime updated;
}
