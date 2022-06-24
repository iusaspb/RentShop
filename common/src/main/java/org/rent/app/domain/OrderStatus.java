package org.rent.app.domain;
/**
 * OrderStatus
 * <p>
 *     Status of an order
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
public enum OrderStatus {
    /**
     *  in the process of choosing products.
     */
    IN_PROGRESS,
    /**
     * An order is completed. Items under lease.
     */
    COMPLETED,
    /**
     * Items are returned and ready for rent.
     */
    CLOSED
}
