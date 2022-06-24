package org.rent.app.service;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.rent.app.domain.Order;
/**
 * OrderActionResponse
 * <p>
 *     Response to an action with an order.
 * </p>
 *
 * There are 2 actions at the moment
 * <ul>
 * <li>Add a product to the order</li>
 * <li>Remove a product from the order</li>
 * </ul>
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@ApiModel(description = "Response")
@Data
@AllArgsConstructor
public class OrderActionResponse {
    public enum Status {OK,
        NO_ITEM, // no items of the selected product for rent in given interval
        CONCURRENT_ACCESS // optimistic lock takes place
    };
    private Status status;
    private Order data;
    private String message;
}
