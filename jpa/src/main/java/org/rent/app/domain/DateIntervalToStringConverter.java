package org.rent.app.domain;

import org.rent.app.utils.DateInterval;
import org.rent.app.utils.DateIntervalProcessor;

import javax.persistence.AttributeConverter;
/**
 * DateIntervalToStringConverter
 * <p>
 *     Serialize/Deserialize date interval to/from string
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
public class DateIntervalToStringConverter implements AttributeConverter<DateInterval, String> {

    @Override
    public String convertToDatabaseColumn(DateInterval attribute) {
        return DateIntervalProcessor.convertDateIntervalToString(attribute);
    }

    @Override
    public DateInterval convertToEntityAttribute(String dbData) {
        return DateIntervalProcessor.convertStringToDateInterval(dbData);
    }
}

