package br.com.ProjetoApi.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // Chave secreta para assinatura do token, carregada do arquivo de configuração
    @Value("${jwt.secret}")
    private String secret;

    // Tempo de expiração do token em milissegundos, carregado do arquivo de configuração
    @Value("${jwt.expiration}")
    private Long expiration;

    // Gera a chave de assinatura a partir da string secreta codificada em UTF-8
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extrai o nome de usuário (subject) do token JWT
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Extrai a data de expiração do token JWT
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Extrai uma claim específica do token usando uma função de resolução
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // Obtém todas as claims do token após validá-lo com a chave de assinatura
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Verifica se o token está expirado comparando a data de expiração com a data atual
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Gera um novo token JWT para o usuário, incluindo suas roles como claim
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Adiciona as roles (permissões) do usuário ao token como uma lista
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return doGenerateToken(claims, userDetails.getUsername());
    }

    // Cria o token JWT com as claims, sujeito, data de emissão e expiração
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) // Define as claims personalizadas (ex.: roles)
                .setSubject(subject) // Define o sujeito (nome de usuário)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Data de expiração
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Assina o token com a chave e algoritmo HS256
                .compact(); // Gera o token como string
    }

    // Valida o token verificando o nome de usuário e se não está expirado
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

// Explicação:
// Esta classe é responsável por criar, validar e extrair informações de tokens JWT.
// Ela utiliza uma chave secreta para assinar os tokens e inclui informações como o nome de usuário e roles do usuário.
// É usada para autenticar usuários em requisições HTTP, garantindo que apenas usuários válidos