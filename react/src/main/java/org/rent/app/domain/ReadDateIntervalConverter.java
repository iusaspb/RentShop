package org.rent.app.domain;

import org.rent.app.utils.DateInterval;
import org.rent.app.utils.DateIntervalProcessor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
/**
 * ReadDateIntervalConverter
 * <p>
 *     Read DateInterval from db
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@ReadingConverter
public class ReadDateIntervalConverter implements Converter<String, DateInterval> {
    @Override
    public DateInterval convert(String source) {
        return DateIntervalProcessor.convertStringToDateInterval(source);
    }
}
