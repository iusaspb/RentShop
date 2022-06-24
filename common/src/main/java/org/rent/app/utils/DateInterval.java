package org.rent.app.utils;

import lombok.Data;

import java.time.LocalDate;
import java.util.Objects;
/**
 * DateInterval
 * <p>
 *     This class presents date interval
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Data
public class DateInterval {
    /**
     *  Included. If it is null, then it was always available.
     */
    LocalDate fromDate;
    /**
     *  Excluded  If it is null, then it will always be available.
     */
    LocalDate toDate;

    public DateInterval(LocalDate fromDate, LocalDate toDate) {
        if (Objects.nonNull(fromDate) && Objects.nonNull(toDate) && !fromDate.isBefore(toDate)) {
            throw new IllegalArgumentException();
        }
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
    public DateInterval() {
        this (null,null);
    }

    public boolean startsAfterStart(DateInterval other) {
        Objects.requireNonNull(other);
        return Objects.nonNull(this.fromDate) && this.fromDate.isAfter(other.fromDate);
    }

    public boolean endsBeforeEnd(DateInterval other) {
        Objects.requireNonNull(other);
        return Objects.nonNull(this.toDate) && this.toDate.isBefore(other.fromDate);
    }

    public boolean contains(DateInterval other) {
        Objects.requireNonNull(other);
        return (Objects.isNull(this.fromDate) ||  !this.fromDate.isAfter(other.fromDate))
                && (Objects.isNull(this.toDate) ||  !this.toDate.isAfter(other.toDate));
    }
}
