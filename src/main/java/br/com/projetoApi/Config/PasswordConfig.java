package br.com.ProjetoApi.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    // Define um bean do tipo PasswordEncoder para ser usado na aplicação
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Retorna uma instância de BCryptPasswordEncoder para codificação e verificação de senhas
        return new BCryptPasswordEncoder();
    }
}

// Explicação:
// Esta classe configura o PasswordEncoder que será usado para codificar senhas de usuários.
// O BCryptPasswordEncoder é uma implementação segura que utiliza o algoritmo BCrypt para hashing de senhas.