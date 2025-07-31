package br.com.projetoApi.Entity.User.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.projetoApi.Config.JwtUtil;
import br.com.projetoApi.Entity.User.Dto.AppUserLoginRequest;
import br.com.projetoApi.Entity.User.Dto.AppUserRegistrationRequest;
import br.com.projetoApi.Entity.User.Dto.AppUserResponse;
import br.com.projetoApi.Entity.User.Model.AppUser;
import br.com.projetoApi.Entity.User.Service.AppUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class AppUserController {

    @Autowired
    private AppUserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Registrar um novo usuário", 
               description = "Cria um novo usuário com base no nome de usuário, senha e CPF fornecidos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso",
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = AppUserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Erro na requisição, como usuário ou CPF já existente",
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody 
        @Schema(example = "{\"username\": \"testuser\", \"password\": \"testpassword\", \"cpf\": \"12345678901\"}") 
        AppUserRegistrationRequest request) {
        try {
            AppUser user = userService.createUser(request);
            
            AppUserResponse response = new AppUserResponse();
            response.setMessage("Usuário registrado com sucesso!");
            response.setUsername(user.getUsername());
            response.setCpf(user.getCpf());
            response.setId(user.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @Operation(summary = "Autenticar um usuário", 
               description = "Autentica um usuário com base no nome de usuário e senha fornecidos, retornando um token JWT.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = AppUserResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou erro de autenticação",
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody 
        @Schema(example = "{\"username\": \"testuser\", \"password\": \"testpassword\"}") 
        AppUserLoginRequest request) {
        try {
            org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            String token = jwtUtil.generateToken(userDetails);
            
            AppUser user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado após autenticação"));
            
            AppUserResponse response = new AppUserResponse();
            response.setMessage("Login realizado com sucesso!");
            response.setUsername(user.getUsername());
            response.setCpf(user.getCpf());
            response.setId(user.getId());
            response.setToken(token);
            
            return ResponseEntity.ok(response);
            
        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciais inválidas.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro de autenticação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}