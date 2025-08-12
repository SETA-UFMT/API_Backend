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

/**
 * Configuração de segurança para a aplicação, definindo regras de autenticação e autorização.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * Configura o gerenciador de autenticação.
     * @param authenticationConfiguration Configuração de autenticação do Spring Security.
     * @return Gerenciador de autenticação.
     * @throws Exception Se houver erro na configuração.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura as regras de segurança para as requisições HTTP.
     * @param http Configuração de segurança HTTP.
     * @param userService Serviço de usuários para autenticação.
     * @return Cadeia de filtros de segurança.
     * @throws Exception Se houver erro na configuração.
     */
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
                .requestMatchers(HttpMethod.GET, "/api/blocos", "/api/blocos/{id}", "/api/blocos/ativos", "/api/blocos/status/**")
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

                // Rotas de luzes (acesso compartilhado)
                .requestMatchers(HttpMethod.GET, "/api/luzes/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/luzes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/luzes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/luzes/**").hasAnyRole("ADMIN", "PROFESSOR")
                .requestMatchers(HttpMethod.PATCH, "/api/luzes/**").hasAnyRole("ADMIN", "PROFESSOR")

                // Rotas de ar condicionados (acesso compartilhado)
                .requestMatchers(HttpMethod.GET, "/api/ar-condicionados", "/api/ar-condicionados/{id}", 
                                "/api/ar-condicionados/status/**", "/api/ar-condicionados/ligados", 
                                "/api/ar-condicionados/manutencao", "/api/ar-condicionados/filtros")
                .hasAnyRole("ADMIN", "PROFESSOR", "ALUNO")
                .requestMatchers(HttpMethod.POST, "/api/ar-condicionados").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/ar-condicionados/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/ar-condicionados/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/ar-condicionados/*/status", 
                                "/api/ar-condicionados/*/temperatura", 
                                "/api/ar-condicionados/*/ventilacao", 
                                "/api/ar-condicionados/*/modo", 
                                "/api/ar-condicionados/*/ligar", 
                                "/api/ar-condicionados/*/desligar")
                .hasAnyRole("ADMIN", "PROFESSOR")
                .requestMatchers(HttpMethod.PATCH, "/api/ar-condicionados/bloco/*/desligar-todos")
                .hasRole("ADMIN")

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