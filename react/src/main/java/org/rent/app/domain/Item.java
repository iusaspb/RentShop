package org.rent.app.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.rent.app.utils.DateInterval;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
/**
 * Item
 * <p>
 *     Item entity
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
public class Item {
    @Id
    Long id;
    /**
     * @see Product
     */
    @NotNull
    Long productId;
    /**
     * is not used in this example
     */
    Long pickupCenterId;
    /**
     * an identifier among instances of a given
     * @see Product
     */
    String serialNum;
    /**
     * a collection of intervals in witch an item can be rented
     */
    @NotNull
    @Column("available_intervals_react")
    List<DateInterval> availableIntervals;
    @Version
    Long version;
    /**
     * true if this item is rented in current transaction
     */
    @Transient
    boolean reserved = false;
}
