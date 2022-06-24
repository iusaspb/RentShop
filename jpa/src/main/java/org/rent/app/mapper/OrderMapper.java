package org.rent.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.rent.app.domain.Order;
import org.rent.app.domain.OrderItem;
import org.rent.app.dto.OrderActionResponseDto;
import org.rent.app.dto.OrderDto;
import org.rent.app.dto.OrderItemDto;
import org.rent.app.service.OrderActionResponse;

import java.util.Collection;
/**
 * OrderMapper
 * <p>
 *     Mappers for Order
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper( OrderMapper.class );
    @Mapping(target = "contractorId", source = "contractor.id")
    OrderDto toDto(Order entity);

    @Mapping(target = "productId", source = "item.product.id")
    OrderItemDto toItemDto(OrderItem entity);

    Collection<OrderDto> toDtos(Collection<Order> entities);

    OrderActionResponseDto toActionResponseDto(OrderActionResponse actionREsponse);
}
