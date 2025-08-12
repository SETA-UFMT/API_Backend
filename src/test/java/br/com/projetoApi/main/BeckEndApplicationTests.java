package br.com.projetoApi.main;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles; // Adicione este import

@SpringBootTest
@ActiveProfiles("test") // Adicione esta anotação
class BeckEndApplicationTests {

    @Test
    void contextLoads() {
        // Este é um teste padrão do Spring Boot para garantir que o contexto seja carregado.
    }
}