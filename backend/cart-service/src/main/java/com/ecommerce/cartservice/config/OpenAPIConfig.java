package com.ecommerce.cartservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI cartServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cart Service REST API")
                        .description("Microservice for managing user shopping cart operations in Distributed E-Commerce Architecture")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("E-Commerce Platform Engineering Team")
                                .email("engineering@ecommerce.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
