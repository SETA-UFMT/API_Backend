    package br.com.ProjetoApi.Config;

    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.security.core.AuthenticationException;
    import org.springframework.security.web.AuthenticationEntryPoint;
    import org.springframework.stereotype.Component;

    import java.io.IOException;
    import java.io.Serializable;

    @Component
        public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

        // Um número de série para a classe, importante para a serialização (salvar/carregar o estado do objeto).
            private static final long serialVersionUID = -7858869558953243875L;

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                            AuthenticationException authException) throws IOException {

            // Lógica: Define o tipo de conteúdo que será enviado na resposta como JSON.
            // Isso é crucial para que o aplicativo que fez a requisição saiba como ler a mensagem de erro.
            response.setContentType("application/json");

            // Lógica: Configura o status HTTP da resposta para 401 (Não Autorizado).
            // Isso sinaliza claramente ao cliente que a requisição não foi autenticada ou o token é inválido/ausente.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // Lógica: Escreve uma mensagem de erro em formato JSON diretamente na resposta.
            // Inclui uma mensagem genérica "Unauthorized" e a mensagem específica da exceção de autenticação,
            // o que ajuda a depurar o problema no lado do cliente.
            response.getOutputStream().println(
                "{ \"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\" }"
            );
     }
}

// explicação:
// Esta classe é um ponto de entrada para autenticação JWT no Spring Security.
// Ela intercepta requisições que falham na autenticação e retorna uma resposta JSON com o erro.