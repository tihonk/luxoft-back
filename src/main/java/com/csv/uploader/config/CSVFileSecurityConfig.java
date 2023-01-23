package com.csv.uploader.config;

import static org.springframework.http.HttpMethod.DELETE;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class CSVFileSecurityConfig  {

    private static final String ADMIN_ROLE = "ADMIN";
    private static final String MANGE_CSV_FILE_URL = "/csvfiles/*";
    private static final String MANGE_CSV_RECORD_URL = "/records/*";

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {
        http.csrf().disable()
            .authorizeExchange()
            .pathMatchers(DELETE, MANGE_CSV_FILE_URL, MANGE_CSV_RECORD_URL).hasRole(ADMIN_ROLE)
            .pathMatchers("/**").permitAll()
            .and()
            .httpBasic();
        return http.build();
    }
}
