package org.rent.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.rent.app.utils.DateInterval;

import javax.validation.constraints.NotNull;
/**
 * AddProdRequest
 * <p>
 *     Request to add a prod to the order data
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@ApiModel(description = "Request to add a prod to the order data")
@Data
public class AddProdRequest {
   /**
    * @see org.rent.app.domain.Product
    */
   @NotNull
   Long prodId;
   @NotNull
   @ApiModelProperty("rental period: fromDate included toDate excluded")
   DateInterval rentPeriod;
}
