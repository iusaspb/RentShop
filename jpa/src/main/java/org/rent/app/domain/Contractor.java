package org.rent.app.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
/**
 * Contractor
 * <p>
 *     Contractor entity
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Contractor extends EntityId {
    @NotBlank
    String name;
    Long latitude;
    Long longitude;
    /**
     * Collection of contractor's orders
     */
    @NotNull
    @OneToMany(fetch = FetchType.LAZY,mappedBy="contractor",cascade = CascadeType.ALL,orphanRemoval=true)
    List<Order> orders = new ArrayList<>();
}
