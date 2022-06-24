package org.rent.app.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/**
 * Owner
 * <p>
 *     Owner entity
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "\"owner\"")
public class Owner  extends EntityId {
    @NotBlank
    String name;
    Long latitude;
    Long longitude;
}
