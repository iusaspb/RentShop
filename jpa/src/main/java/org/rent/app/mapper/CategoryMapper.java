package org.rent.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.rent.app.domain.Category;
import org.rent.app.dto.CategoryDto;

import java.util.Collection;
/**
 * CategoryMapper
 * <p>
 *     Mappers for Category
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper( CategoryMapper.class );
    @Mapping(target = "parentId", source = "parent.id")
    CategoryDto toDto(Category entity);
    Collection<CategoryDto> toDtos(Collection<Category> entiteis);
}
