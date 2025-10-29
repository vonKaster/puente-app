package com.puente.app.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("Token JWT de autenticación. Obtenerlo mediante /api/auth/login o /api/auth/register. " +
                            "Usar en el header: Authorization: Bearer {token}")))
            .info(new Info()
                .title("PUENTE - Technical Interview Challenge API")
                .version("1.0.0")
                .description("API REST para gestión de instrumentos financieros y favoritos de usuarios. " +
                        "Esta API permite consultar precios de acciones en tiempo real (actualizados cada 5 minutos), " +
                        "gestionar usuarios con autenticación JWT, y mantener una lista personalizada de instrumentos favoritos.")
                .contact(new Contact()
                    .name("Franco Caminos")
                    .email("fran@puente.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}

