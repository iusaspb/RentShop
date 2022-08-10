package org.rent.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.rent.app.utils.DateInterval;

import javax.validation.constraints.NotNull;
/**
 * AddProdRequest
 * <p>
 *     Request to add a product to the order with rentPeriod
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@ApiModel(description = "Request to add a product to the order")
@Data
public class AddProdRequest {
   /**
    * @see org.rent.app.domain.Product
    */
   @NotNull
   private Long prodId;
   @ApiModelProperty("rental period: fromDate included toDate excluded")
   @NotNull
   private DateInterval rentPeriod;
}
