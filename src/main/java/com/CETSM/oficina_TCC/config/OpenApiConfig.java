package com.CETSM.oficina_TCC.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão para Oficina Mecânica - TCC")
                        .version("1.0.0")
                        .description("Backend profissional desenvolvido em Spring Boot 3 para gerenciamento completo de oficinas.")
                        .contact(new Contact()
                                .name("Equipe do TCC")
                                .email("contato@oficina.com")));
    }
}