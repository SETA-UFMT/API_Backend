package br.com.projetoApi.Entity.User;

import br.com.projetoApi.Entity.User.Dto.AppUserLoginRequest;
import br.com.projetoApi.Entity.User.Dto.AppUserRegistrationRequest;
import br.com.projetoApi.Entity.User.Model.AppUser;
import br.com.projetoApi.Entity.User.Repository.AppUserRepository;
import br.com.projetoApi.Entity.User.Service.AppUserService;
import br.com.projetoApi.main.BeckEndApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BeckEndApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AppUserControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        // Limpa o banco de dados antes de cada teste
        appUserRepository.deleteAll();
    }

    @Test
    void testRegisterUserSuccessfullyReturns201() throws Exception {
        // Cenário: Registro de um novo usuário com sucesso.
        
        // 1. Prepara a requisição de registro.
        AppUserRegistrationRequest request = new AppUserRegistrationRequest();
        request.setUsername("novo_user_api");
        request.setPassword("senhasegura");
        request.setCpf("11111111111");
        
        // 2. Converte o DTO para JSON.
        String jsonRequest = objectMapper.writeValueAsString(request);
        
        // 3. Simula a requisição POST para o endpoint /register.
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated()) // Espera o status 201 Created.
                .andExpect(jsonPath("$.message").value("Usuário registrado com sucesso!"));
    }
    
    @Test
    void testRegisterUserWithExistingUsernameReturns400() throws Exception {
        // Cenário: Tentativa de registrar um usuário com um nome de usuário já existente.
        
        // 1. Cria um usuário inicial.
        AppUser existingUser = new AppUser();
        existingUser.setUsername("user_existente");
        existingUser.setPassword("senha_hash");
        existingUser.setCpf("00000000000");
        existingUser.setRoles(Arrays.asList("USER"));
        appUserRepository.save(existingUser);
        
        // 2. Prepara a requisição com o mesmo username.
        AppUserRegistrationRequest request = new AppUserRegistrationRequest();
        request.setUsername("user_existente");
        request.setPassword("nova_senha");
        request.setCpf("22222222222");
        
        String jsonRequest = objectMapper.writeValueAsString(request);
        
        // 3. Simula a requisição e espera o status 400 Bad Request.
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Usuário já existe: user_existente"));
    }
    
    @Test
    void testLoginUserSuccessfullyReturns200AndToken() throws Exception {
        // Cenário: Autenticação de um usuário com credenciais válidas.
        
        // 1. Cria um usuário que será autenticado.
        AppUserRegistrationRequest userToRegister = new AppUserRegistrationRequest();
        userToRegister.setUsername("login_user");
        userToRegister.setPassword("login_password");
        userToRegister.setCpf("33333333333");
        appUserService.createUser(userToRegister);
        
        // 2. Prepara a requisição de login.
        AppUserLoginRequest loginRequest = new AppUserLoginRequest();
        loginRequest.setUsername("login_user");
        loginRequest.setPassword("login_password");
        
        String jsonRequest = objectMapper.writeValueAsString(loginRequest);
        
        // 3. Simula a requisição de login e espera o status 200 OK.
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login realizado com sucesso!"))
                .andExpect(jsonPath("$.token").exists()); // Verifica se o token JWT foi retornado.
    }
    
    @Test
    void testLoginUserWithInvalidCredentialsReturns401() throws Exception {
        // Cenário: Tentativa de login com senha incorreta.
        
        // 1. Cria um usuário.
        AppUserRegistrationRequest userToRegister = new AppUserRegistrationRequest();
        userToRegister.setUsername("invalid_login");
        userToRegister.setPassword("correct_password");
        userToRegister.setCpf("44444444444");
        appUserService.createUser(userToRegister);
        
        // 2. Prepara a requisição de login com a senha errada.
        AppUserLoginRequest loginRequest = new AppUserLoginRequest();
        loginRequest.setUsername("invalid_login");
        loginRequest.setPassword("incorrect_password");
        
        String jsonRequest = objectMapper.writeValueAsString(loginRequest);
        
        // 3. Simula a requisição e espera o status 401 Unauthorized.
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Credenciais inválidas."));
    }
}