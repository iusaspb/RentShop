package org.rent.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.rent.app.domain.Product;
import org.rent.app.dto.ProductDto;

import java.util.Collection;
/**
 * ProductMapper
 * <p>
 *     Mappers for Product
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper( ProductMapper.class );
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "ownerId", source = "owner.id")
    ProductDto toDto(Product entity);
    Collection<ProductDto> toDtos(Collection<Product> entiteis);
}
