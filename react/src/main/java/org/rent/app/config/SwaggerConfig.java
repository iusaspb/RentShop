package org.rent.app.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;
/**
 * SwaggerConfig
 * <p>
 *     Config swagger
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.PUT, List.of(
                        new ResponseBuilder().code(Integer.toString(HttpStatus.PRECONDITION_FAILED.value()))
                                .description("Optimistic lock violation").build()
                ))
                .globalResponses(HttpMethod.POST, List.of(
                        new ResponseBuilder().code(Integer.toString(HttpStatus.PRECONDITION_FAILED.value()))
                                .description("Optimistic lock violation").build()
                ))
                ;
    }
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Rent Shop",
                "React Rent Cart",
                "API 0.0.1",
                "localhost:8080",
                new Contact("Sergey Yurkevich", "localhost:8080", "ysaspb@gmail.com"),
                "License of API", "API license URL", Collections.emptyList());
    }
}
