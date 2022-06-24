package org.rent.app.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.rent.app.service.OrderActionResponse;

import javax.validation.constraints.NotNull;

@ApiModel(description = "Response")
@Data
public class OrderActionResponseDto {
    @NotNull
    private OrderActionResponse.Status status;
    @NotNull
    private OrderDto data;
}
