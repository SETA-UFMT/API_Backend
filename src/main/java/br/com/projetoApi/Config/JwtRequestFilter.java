package br.com.projetoApi.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.projetoApi.Entity.User.Service.AppUserService;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    // Injeção de dependência do serviço de usuário para carregar detalhes do usuário
    @Autowired
    private AppUserService userDetailsService;

    // Injeção de dependência da utilidade para manipulação de JWT
    @Autowired
    private JwtUtil jwtUtil;

    // Método principal que processa cada requisição HTTP
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain chain) throws ServletException, IOException {

        // Obtém o cabeçalho "Authorization" da requisição
        final String requestTokenHeader = request.getHeader("Authorization");

        // Variáveis para armazenar o nome de usuário e o token JWT
        String username = null;
        String jwtToken = null;

        // Verifica se o cabeçalho Authorization existe e começa com "Bearer "
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            // Extrai o token JWT, removendo a palavra "Bearer " (7 primeiros caracteres)
            jwtToken = requestTokenHeader.substring(7);
            try {
                // Obtém o nome de usuário a partir do token JWT
                username = jwtUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                // Loga erro se o token JWT for inválido ou malformado
                logger.error("Não foi possível obter o token JWT", e);
            } catch (Exception e) {
                // Loga erro se o token estiver expirado ou inválido
                logger.error("Token JWT expirado ou inválido", e);
            }
        } else {
            // Loga aviso se o cabeçalho não contém um token válido com "Bearer "
            logger.warn("JWT Token não começa com Bearer String");
        }

        // Verifica se o nome de usuário foi extraído e se não há autenticação ativa no contexto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carrega os detalhes do usuário com base no nome de usuário
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Valida o token JWT com os detalhes do usuário
            if (jwtUtil.validateToken(jwtToken, userDetails)) {

                // Cria um objeto de autenticação para o Spring Security
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                        
                // Define detalhes adicionais da requisição (como endereço IP, sessão, etc.)
                usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                // Define o usuário como autenticado no contexto de segurança do Spring
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // Passa a requisição para o próximo filtro na cadeia
        chain.doFilter(request, response);
    }
}

// Explicação:
// Esta classe é um filtro que intercepta requisições HTTP para verificar se elas contêm um token JWT válido.
// Se o token for válido, ela autentica o usuário no contexto de segurança do Spring.