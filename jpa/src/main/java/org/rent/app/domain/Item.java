package org.rent.app.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.rent.app.utils.DateInterval;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
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
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Item extends EntityId {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    Store store;
    String serialNum;
    /**
     * a collection of intervals in witch an item can be rented
     */
    @NotNull
    @Convert(converter = ListToStringConverter.class)
    List<DateInterval> availableIntervals;
    @Version
    Long version;
}
