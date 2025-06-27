package io.getarrays.securecapita.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @project securecapita7
 */
@Configuration
public class OpenAPIConfig {
    //swagger:
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                .servers(List.of(
                                new Server().url("http://localhost:8080")
                        )
                )
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description)
                        .termsOfService(termsOfServiceUrl)
                        .contact(new Contact().name(contactName).url(contactUrl))
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Value("${swagger.api.title}")
    private String title;

    @Value("${swagger.api.description}")
    private String description;

    @Value("${swagger.api.version}")
    private String version;

    @Value("${swagger.api.termsOfServiceUrl}")
    private String termsOfServiceUrl;

    @Value("${swagger.api.contact.name}")
    private String contactName;

    @Value("${swagger.api.contact.url}")
    private String contactUrl;
}
