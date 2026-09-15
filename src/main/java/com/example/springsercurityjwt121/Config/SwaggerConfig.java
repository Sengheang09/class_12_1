package com.example.springsercurityjwt121.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Login and register with jwt",
                version = "1.0",
                description = "JWT Authentication Demo"
        )
)


public class SwaggerConfig {

}
