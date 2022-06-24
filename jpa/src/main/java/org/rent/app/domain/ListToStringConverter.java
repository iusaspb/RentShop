package org.rent.app.domain;

import org.rent.app.utils.DateInterval;
import org.rent.app.utils.DateIntervalProcessor;

import javax.persistence.AttributeConverter;
import java.util.List;
/**
 * ListToStringConverter
 * <p>
 *     Serialize/Deserialize list of date intervals to/from string
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
public class ListToStringConverter implements AttributeConverter<List<DateInterval>, String> {

    @Override
    public String convertToDatabaseColumn(List<DateInterval> attribute) {
        return DateIntervalProcessor.convertDateIntervalListToString(attribute);
    }

    @Override
    public List<DateInterval> convertToEntityAttribute(String dbData) {
        return DateIntervalProcessor.convertStringToDateIntervalList (dbData);
    }
}
