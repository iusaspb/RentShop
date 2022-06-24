package org.rent.app.config;

import io.r2dbc.spi.ConnectionFactory;
import org.rent.app.domain.ReadDateIntervalConverter;
import org.rent.app.domain.WriteDateIntervalConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;

import java.util.List;
/**
 * DBConfig
 * <p>
 *     Config custom db settings
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Configuration
@EnableR2dbcAuditing
public class DBConfig {
    /**
     * Config converters for the class
     * @see org.rent.app.utils.DateInterval
     * @return the list of cponverter to register
     */
    @Bean
    public R2dbcCustomConversions customConversions() {
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, List.of(new ReadDateIntervalConverter(),new WriteDateIntervalConverter()));
    }

    /**
     *  Config the transaction manager
     * @param connectionFactory witch is under the transaction manager
     * @return transaction manager
     */
    @Bean
    ReactiveTransactionManager transactionManager(@Autowired ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}
