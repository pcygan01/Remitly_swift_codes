package com.remitly.swiftcodes.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("SWIFT Codes API")
                        .description("RESTful API for managing SWIFT/BIC codes for international banking")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Remitly Poland")
                                .url("https://www.remitly.com")
                                .email("contact@remitly.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
} 