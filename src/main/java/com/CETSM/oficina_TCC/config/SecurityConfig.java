package com.CETSM.oficina_TCC.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desativa CSRF (necessário para APIs REST stateless)
            .csrf(csrf -> csrf.disable())
            
            // Regras de autorização das rotas
            .authorizeHttpRequests(auth -> auth
                // Libera o acesso à documentação do Swagger e aos Relatórios (ou mude conforme preferir)
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/relatorios/**").permitAll()
                
                // Exige autenticação para todas as outras rotas da oficina (Clientes, Veículos, OS, etc.)
                .anyRequest().authenticated()
            )
            
            // Habilita autenticação básica HTTP temporariamente (ótimo para testar no Postman com user/password)
            .httpBasic(httpBasic -> {});

        return http.build();
    }
}