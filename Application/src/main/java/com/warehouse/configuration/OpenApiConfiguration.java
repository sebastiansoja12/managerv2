package com.warehouse.configuration;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;

import com.warehouse.auth.configuration.ApiExposureProperties;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Manager 2.0",
                        email = "sebastian5152@wp.pl",
                        url = "https://gitlab.com/sebastiansoja"
                ),
                description = "OpenApi documentation for Spring Security",
                title = "OpenApi specification - Manager Spring Boot Project",
                version = "2026.3",
                license = @License(
                        name = "Licence name",
                        url = ""
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local environment",
                        url = "http://localhost:8080/v2/api"
                ),
                @Server(
                        description = "Development environment",
                        url = "https://inparcel-dev-v2.herokuapp.com/v2/api"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "cookieAuth"
                )
        }
)
@SecurityScheme(
        name = "cookieAuth",
        description = "HttpOnly access token cookie",
        paramName = "AUTH-TOKEN",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE
)
public class OpenApiConfiguration {

    @Bean
    public OpenApiCustomizer publicApiCustomizer(final ApiExposureProperties apiExposureProperties) {
        return openApi -> {
            if (openApi.getPaths() != null) {
                openApi.getPaths()
                        .keySet()
                        .removeIf(apiExposureProperties::isInternalControllerPath);
            }
        };
    }
}
