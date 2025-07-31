<<<<<<< HEAD
package br.com.projetoApi.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Lógica: Define quais domínios podem acessar sua API.
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "https://meudominio.com"));

        // Lógica: Permite os métodos HTTP (GET, POST, PUT, DELETE, OPTIONS).
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Lógica: Permite todos os cabeçalhos nas requisições.
        // Para maior segurança em produção, especifique apenas os necessários.
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Lógica: Permite o envio de credenciais (cookies, tokens de autorização).
        // Necessário se sua API usa autenticação baseada em sessão ou tokens no cabeçalho.
        configuration.setAllowCredentials(true);

        // Lógica: Expõe o cabeçalho "Authorization" para o navegador do cliente.
        // Útil se você retorna tokens de segurança que o front-end precisa ler.
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Lógica: Aplica essa configuração de CORS a TODAS as URLs da sua API.
        source.registerCorsConfiguration("/**", configuration);

        return source; // Retorna a configuração de CORS.
    }
}

// Explicação:
// Esta classe configura o CORS (Cross-Origin Resource Sharing) para a aplicação Spring.
// O CORS é necessário para permitir que aplicações front-end (como React, Angular, etc.)
=======
package br.com.projetoApi.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Lógica: Define quais domínios podem acessar sua API.
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "http://localhost:5000"));

        // Lógica: Permite os métodos HTTP (GET, POST, PUT, DELETE, OPTIONS).
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Lógica: Permite todos os cabeçalhos nas requisições.
        // Para maior segurança em produção, especifique apenas os necessários.
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Lógica: Permite o envio de credenciais (cookies, tokens de autorização).
        // Necessário se sua API usa autenticação baseada em sessão ou tokens no cabeçalho.
        configuration.setAllowCredentials(true);

        // Lógica: Expõe o cabeçalho "Authorization" para o navegador do cliente.
        // Útil se você retorna tokens de segurança que o front-end precisa ler.
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Lógica: Aplica essa configuração de CORS a TODAS as URLs da sua API.
        source.registerCorsConfiguration("/**", configuration);

        return source; // Retorna a configuração de CORS.
    }
}

// Explicação:
// Esta classe configura o CORS (Cross-Origin Resource Sharing) para a aplicação Spring.
// O CORS é necessário para permitir que aplicações front-end (como React, Angular, etc.)
>>>>>>> 2652b7feed50700430f4e53f1658c46b68741929
// façam requisições para a API de um domínio diferente (cross-origin).