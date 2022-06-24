package org.rent.app.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
/**
 * Order
 * <p>
 *     Order entity is the collection of items to rent
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Builder(toBuilder=true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name="\"order\"")
public class Order {
    @Id
    Long id;
    /**
     *  is not used in this example
     */
    @NotNull
    Long contractorId;
    /**
     *  is not used in this example
     */
    Long pickupCenterId;
    /**
     *  current total of the order
     */
    @PositiveOrZero
    long amount;
    @Builder.Default
    OrderStatus status = OrderStatus.IN_PROGRESS;
    @CreatedDate
    @LastModifiedDate
    LocalDateTime updated;
    @Version
    Long version;

    @Deprecated
    public long incAmount(long price) {
        amount+=price;
        return amount;
    }

    @Deprecated
    public long decAmount(long price) {
        amount-=price;
        return amount;
    }
}
