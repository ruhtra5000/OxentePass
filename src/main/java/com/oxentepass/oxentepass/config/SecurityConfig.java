package com.oxentepass.oxentepass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    // Configuração básica do spring security, só para as rotas
    // funcionarem (provavelmente será modificado posteriormente)
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
        httpSecurity
        .csrf(csrf -> csrf.disable()) // TODO: Habilitar CSRF e configurar restrições de acesso
        .authorizeHttpRequests(
            authorize -> authorize
            .requestMatchers("/**").permitAll()
        );

        return httpSecurity.build();
    }

    // Bean para codificação de senhas
    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder(12);
    }

}
