package org.rent.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
/**
 * ReactApplication
 * <p>
 *     Main class
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@SpringBootApplication
@EnableR2dbcRepositories
public class ReactApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReactApplication.class, args);
	}
}
