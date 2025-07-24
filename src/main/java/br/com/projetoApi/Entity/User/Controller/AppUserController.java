package br.com.ProjetoApi.Entity.User.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ProjetoApi.Config.JwtUtil;
import br.com.ProjetoApi.Entity.User.Dto.AppUserLoginRequest;
import br.com.ProjetoApi.Entity.User.Dto.AppUserRegistrationRequest;
import br.com.ProjetoApi.Entity.User.Dto.AppUserResponse;
import br.com.ProjetoApi.Entity.User.Model.AppUser;
import br.com.ProjetoApi.Entity.User.Service.AppUserService;

@RestController
// Define o prefixo base para todas as rotas do controlador
@RequestMapping("/api/auth")
public class AppUserController {

    // Injeção do serviço de usuário para operações de criação e busca
    @Autowired
    private AppUserService userService;
    
    // Injeção do gerenciador de autenticação para validar credenciais
    @Autowired
    private AuthenticationManager authenticationManager;

    // Injeção da utilidade JWT para geração de tokens
    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint para registro de novos usuários
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AppUserRegistrationRequest request) {
        try {
            // Cria um novo usuário com base no nome de usuário e senha fornecidos
            AppUser user = userService.createUser(request.getUsername(), request.getPassword());
            
            // Prepara a resposta com os dados do usuário registrado
            AppUserResponse response = new AppUserResponse();
            response.setMessage("Usuário registrado com sucesso!");
            response.setUsername(user.getUsername());
            response.setId(user.getId());
            
            // Retorna resposta com status 201 (Created)
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            // Trata erros, como usuário já existente, retornando status 400 (Bad Request)
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // Endpoint para autenticação de usuários (login)
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AppUserLoginRequest request) {
        try {
            // Autentica o usuário com as credenciais fornecidas
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            
            // Define a autenticação no contexto de segurança
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Obtém os detalhes do usuário autenticado
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Gera um token JWT para o usuário autenticado
            String token = jwtUtil.generateToken(userDetails);
            
            // Prepara a resposta com os dados do login e o token JWT
            AppUserResponse response = new AppUserResponse();
            response.setMessage("Login realizado com sucesso!");
            response.setUsername(request.getUsername());
            response.setAuthenticated(true);
            response.setToken(token); // Inclui o token na resposta
            
            // Retorna resposta com status 200 (OK)
            return ResponseEntity.ok(response);
            
        } catch (BadCredentialsException e) {
            // Trata erro de credenciais inválidas, retornando status 401 (Unauthorized)
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciais inválidas.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            // Trata outros erros de autenticação, retornando status 401 (Unauthorized)
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro de autenticação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    // Endpoint para logout de usuários
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // Limpa o contexto de segurança, encerrando a sessão do usuário no servidor
        // Nota: Em APIs stateless com JWT, o cliente deve descartar o token localmente
        SecurityContextHolder.clearContext();
        
        // Prepara a resposta de logout
        AppUserResponse response = new AppUserResponse();
        response.setMessage("Logout realizado com sucesso.");
        
        // Retorna resposta com status 200 (OK)
        return ResponseEntity.ok(response);
    }

    // Endpoint para obter o perfil do usuário autenticado
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        // Obtém o objeto de autenticação do contexto de segurança
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Verifica se o usuário está autenticado e não é anônimo
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuário não autenticado.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        
        // Obtém o nome de usuário do contexto de autenticação
        String username = authentication.getName();
        // Busca o usuário no serviço com base no nome de usuário
        AppUser user = userService.findByUsername(username).orElse(null);
        
        // Verifica se o usuário foi encontrado
        if (user == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Usuário não encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        
        // Prepara a resposta com os dados do perfil do usuário
        AppUserResponse response = new AppUserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setMessage("Perfil do usuário recuperado com sucesso.");
        
        // Retorna resposta com status 200 (OK)
        return ResponseEntity.ok(response);
    }
}

// Explicação:
// Este controlador REST gerencia as operações de autenticação e perfil de usuários.
// Ele permite registrar novos usuários, autenticar usuários existentes, realizar logout e obter o perfil do usuário autenticado.
// As respostas são formatadas em objetos AppUserResponse, que incluem mensagens e dados relevantes.
// O controlador utiliza o AppUserService para interagir com os dados dos usuários e o JwtUtil para gerar tokens JWT durante o login.
// estejam autorizados a acessar recursos protegidos.
