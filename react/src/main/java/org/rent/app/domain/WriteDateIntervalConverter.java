package org.rent.app.domain;

import org.rent.app.utils.DateInterval;
import org.rent.app.utils.DateIntervalProcessor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
/**
 * WriteDateIntervalConverter
 * <p>
 *     Write DateInterval into db
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@WritingConverter
public class WriteDateIntervalConverter implements Converter<DateInterval, String> {
    @Override
    public String convert(DateInterval source) {
        return DateIntervalProcessor.convertDateIntervalToString(source);
    }
}
