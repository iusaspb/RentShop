package org.rent.app.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.rent.app.utils.DateInterval;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
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
@Entity
@EntityListeners(AuditingEntityListener.class)
public class OrderItem extends EntityId {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    Order order;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    Item item;
    /**
     * rental price at the time of booking
     */
    @PositiveOrZero
    long price;
    /**
     * rental period
     */
    @NotNull
    @Convert(converter = DateIntervalToStringConverter.class)
    DateInterval rentPeriod;
    @CreatedDate
    @LastModifiedDate
    LocalDateTime updated;
}
