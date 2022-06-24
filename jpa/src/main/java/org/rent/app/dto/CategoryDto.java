package org.rent.app.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/**
 * CategoryDto
 * <p>
 *     DTO for Category
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Data
public class CategoryDto {
    @NotNull
    Long id;
    Long parentId;
    @NotBlank
    String name;

}
