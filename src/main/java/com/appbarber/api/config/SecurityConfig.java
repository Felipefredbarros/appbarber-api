package com.appbarber.api.config;

import com.appbarber.api.config.filter.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final SecurityFilter securityFilter;
    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    // oq faz embaralhar a senha
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //confere se a senha digitada bate com o banco
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Diz quais portas estão abertas e quais estão trancadas
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login").permitAll()

                        .requestMatchers(HttpMethod.POST, "/barbearias").hasRole("DONO")
                        .requestMatchers(HttpMethod.POST, "/servicos").hasRole("DONO")
                        .requestMatchers(HttpMethod.POST, "/profissionais").hasRole("DONO")
                        .requestMatchers(HttpMethod.GET, "/barbearias").hasRole("DONO")
                        .requestMatchers(HttpMethod.GET, "/barbearias/vitrine").permitAll()
                        .requestMatchers(HttpMethod.GET, "/barbearias/*").hasRole("DONO")
                        .anyRequest().authenticated()
                )
                // 4. ORDEM: O seu filtro de Token tem que vir ANTES do filtro padrão do Spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
