package org.rent.app.domain;

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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Order
 * <p>
 *     Order entity is the collection of items to rent
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
@Table(name="\"order\"")
public class Order extends EntityId {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    Client client;
    @ManyToOne(fetch = FetchType.LAZY)
    Store store;
    /**
     * Collection of order items of the order
     */
    @NotNull
    @OneToMany(fetch = FetchType.LAZY,mappedBy="order",cascade = CascadeType.ALL,orphanRemoval=true)
    List<OrderItem> items = new ArrayList<>();
    @PositiveOrZero
    long amount;
    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    OrderStatus status = OrderStatus.IN_PROGRESS;
    @CreatedDate
    @LastModifiedDate
    LocalDateTime updated;
    @Version
    Long version;
    public long incAmount(long price) {
        amount+=price;
        return amount;
    }
    public long decAmount(long price) {
        amount-=price;
        return amount;
    }
}
