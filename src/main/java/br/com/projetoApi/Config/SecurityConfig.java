package br.com.projetoApi.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.projetoApi.Entity.User.Service.AppUserService;

// Classe de configuração do Spring Security para definir regras de segurança e autenticação
@Configuration
// Habilita o suporte ao Spring Security na aplicação
@EnableWebSecurity
public class SecurityConfig {

    // Injeção do filtro JWT para validar tokens em requisições
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    // Injeção do ponto de entrada para tratar erros de autenticação JWT
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // Define o bean AuthenticationManager para gerenciar autenticações
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Retorna o AuthenticationManager configurado a partir da configuração fornecida
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Configura a cadeia de filtros de segurança para requisições HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AppUserService userService) throws Exception {
        http
            // Define o serviço de detalhes do usuário para autenticação
            .userDetailsService(userService)
            // Configura as regras de autorização para as requisições
            .authorizeHttpRequests(auth -> auth
                // Permite acesso público às rotas de registro e login
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Exige autenticação para rotas de perfil, logout e protegidas
                .requestMatchers("/api/auth/profile", "/api/auth/logout").authenticated()
                .requestMatchers("/blocos", "blocos/**").authenticated()
                .requestMatchers("/protected").authenticated()
                
                // Todas as demais requisições requerem autenticação
                .anyRequest().authenticated()
            )
            // Desativa a proteção CSRF, já que JWT não depende de sessões
            .csrf(csrf -> csrf.disable())
            // Configura a política de gerenciamento de sessões como stateless (sem estado)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Desativa cabeçalhos de segurança padrão, se não necessários
            .headers(headers -> headers.disable())
            // Desativa o formulário de login padrão do Spring Security
            .formLogin(form -> form.disable())
            // Desativa autenticação básica HTTP
            .httpBasic(basic -> basic.disable())
            // Configura o ponto de entrada para tratar falhas de autenticação (ex.: token inválido)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            // Adiciona o filtro JWT antes do filtro padrão de autenticação do Spring
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // Constrói e retorna a cadeia de filtros de segurança
        return http.build();
    }
}

// Explicação:
// Esta classe configura o Spring Security para proteger a aplicação, definindo quais rotas são públicas 
// e quais requerem autenticação. Ela utiliza JWT para autenticação sem estado, desabilitando CSRF e sessões.
