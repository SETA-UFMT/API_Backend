package br.com.projetoApi.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.projetoApi.Entity.User.Service.AppUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AppUserService userService) throws Exception {
        http
            .userDetailsService(userService)
            .authorizeHttpRequests(auth -> auth
                // Rotas públicas
                .requestMatchers("/api/auth/register", "/api/auth/login", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Rotas autenticadas
                .requestMatchers("/api/auth/profile", "/api/auth/logout").authenticated()
                
                // Rotas de administração (apenas ADMIN)
                .requestMatchers("/api/admin/**", "/api/logs/**", "/api/usuarios/gerenciar/**", 
                "/api/sistema/configuracoes/**", "/api/contingencia/**").hasRole("ADMIN")
                
                // Rotas de blocos (acesso compartilhado)
                .requestMatchers("/api/blocos", "/api/blocos/{id}", "/api/blocos/ativos", "/api/blocos/status/**")
                .hasAnyRole("ADMIN", "PROFESSOR", "ALUNO")
                .requestMatchers(HttpMethod.POST, "/api/blocos").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/blocos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/blocos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/blocos/**").hasRole("ADMIN")
                
                // Rotas de salas (acesso compartilhado)
                .requestMatchers(HttpMethod.GET, "/api/salas", "/api/salas/{id}", "/api/salas/livres", "/api/salas/bloco/**")
                .hasAnyRole("ADMIN", "PROFESSOR", "ALUNO")
                .requestMatchers(HttpMethod.POST, "/api/salas").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/salas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/salas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/salas/*/status").hasAnyRole("ADMIN", "PROFESSOR")

                // Rotas de luzes (acesso livre)
                .requestMatchers(HttpMethod.GET, "/api/luzes/**").permitAll()
                 // Rotas de luzes (acesso restrito)
                .requestMatchers(HttpMethod.POST, "/api/luzes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/luzes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/luzes/**").hasAnyRole("ADMIN", "PROFESSOR")
                .requestMatchers(HttpMethod.PATCH, "/api/luzes/**").hasAnyRole("ADMIN", "PROFESSOR")
                

                
                // Todas as demais requisições requerem autenticação
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(headers -> headers.disable())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}