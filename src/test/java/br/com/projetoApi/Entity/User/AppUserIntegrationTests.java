package br.com.projetoApi.Entity.User;

import br.com.projetoApi.Entity.User.Dto.AppUserRegistrationRequest;
import br.com.projetoApi.Entity.User.Model.AppUser;
import br.com.projetoApi.Entity.User.Repository.AppUserRepository;
import br.com.projetoApi.Entity.User.Service.AppUserService;
import br.com.projetoApi.main.BeckEndApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.Arrays;

@SpringBootTest(classes = BeckEndApplication.class)
@ActiveProfiles("test")
public class AppUserIntegrationTests {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        // Limpa o banco de dados antes de cada teste
        appUserRepository.deleteAll();
    }

    // --- Testes de Serviço (AppUserService) ---

    @Test
    void testCreateUserSuccessfully() {
        // Cenário: Criação de um novo usuário com sucesso.
        AppUserRegistrationRequest request = new AppUserRegistrationRequest();
        request.setUsername("novo_usuario");
        request.setPassword("senhaSegura123");
        request.setCpf("12345678901");
        
        AppUser createdUser = appUserService.createUser(request);
        
        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals("novo_usuario", createdUser.getUsername());
        assertNotEquals("senhaSegura123", createdUser.getPassword());
        assertEquals("12345678901", createdUser.getCpf());
    }

    @Test
    void testCreateUserWithExistingUsernameThrowsException() {
        // Cenário: Tentativa de criar um usuário com um nome de usuário já existente.
        AppUser existingUser = new AppUser();
        existingUser.setUsername("usuario_existente");
        existingUser.setPassword("senha_hash");
        existingUser.setCpf("00000000000");
        appUserRepository.save(existingUser);

        AppUserRegistrationRequest request = new AppUserRegistrationRequest();
        request.setUsername("usuario_existente");
        request.setPassword("outra_senha");
        request.setCpf("11111111111");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appUserService.createUser(request);
        });
        
        assertEquals("Usuário já existe: usuario_existente", exception.getMessage());
    }

    @Test
    void testCreateUserWithExistingCpfThrowsException() {
        // Cenário: Tentativa de criar um usuário com um CPF já existente.
        AppUser existingUser = new AppUser();
        existingUser.setUsername("outro_usuario");
        existingUser.setPassword("senha_hash");
        existingUser.setCpf("99999999999");
        appUserRepository.save(existingUser);

        AppUserRegistrationRequest request = new AppUserRegistrationRequest();
        request.setUsername("novo_usuario_com_cpf_existente");
        request.setPassword("outra_senha");
        request.setCpf("99999999999");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appUserService.createUser(request);
        });
        
        assertEquals("CPF já cadastrado: 99999999999", exception.getMessage());
    }

    // --- Novos Testes Adicionados ---

    @Test
    void testLoadUserByUsernameSuccessfully() {
        // Cenário: O método loadUserByUsername encontra e retorna um usuário.
        AppUser userToSave = new AppUser();
        userToSave.setUsername("load_user_test");
        userToSave.setPassword(passwordEncoder.encode("secret_password"));
        userToSave.setCpf("11111111111");
        userToSave.setRoles(Arrays.asList("USER", "ADMIN"));
        appUserRepository.save(userToSave);

        UserDetails userDetails = appUserService.loadUserByUsername("load_user_test");
        
        assertNotNull(userDetails);
        assertEquals("load_user_test", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testLoadUserByUsernameThrowsExceptionWhenUserNotFound() {
        // Cenário: O método loadUserByUsername não encontra um usuário.
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            appUserService.loadUserByUsername("non_existent_user");
        });

        assertEquals("Usuário não encontrado: non_existent_user", exception.getMessage());
    }

    @Test
    void testAuthenticateUserWithCorrectPasswordReturnsTrue() {
        // Cenário: Autenticação de um usuário com a senha correta.
        AppUser userToSave = new AppUser();
        userToSave.setUsername("auth_user");
        userToSave.setPassword(passwordEncoder.encode("correct_password"));
        userToSave.setCpf("22222222222");
        appUserRepository.save(userToSave);

        boolean isAuthenticated = appUserService.authenticateUser("auth_user", "correct_password");

        assertTrue(isAuthenticated);
    }

    @Test
    void testAuthenticateUserWithIncorrectPasswordReturnsFalse() {
        // Cenário: Autenticação de um usuário com a senha incorreta.
        AppUser userToSave = new AppUser();
        userToSave.setUsername("auth_user_fail");
        userToSave.setPassword(passwordEncoder.encode("correct_password"));
        userToSave.setCpf("33333333333");
        appUserRepository.save(userToSave);

        boolean isAuthenticated = appUserService.authenticateUser("auth_user_fail", "incorrect_password");

        assertFalse(isAuthenticated);
    }

    @Test
    void testCreateUserWithCustomRoles() {
        // Cenário: Criação de um usuário com roles personalizadas.
        AppUserRegistrationRequest request = new AppUserRegistrationRequest();
        request.setUsername("custom_role_user");
        request.setPassword("senha123");
        request.setCpf("44444444444");
        request.setRoles(Arrays.asList("SUPER_USER", "MANAGER"));
        
        AppUser createdUser = appUserService.createUser(request);

        assertNotNull(createdUser.getRoles().get());
        assertTrue(createdUser.getRoles().get().contains("SUPER_USER"));
        assertTrue(createdUser.getRoles().get().contains("MANAGER"));
    }

    // --- Testes de Repositório (AppUserRepository) ---

    @Test
    void testFindByUsername() {
        // Cenário: Busca de um usuário pelo nome de usuário.
        AppUser userToSave = new AppUser();
        userToSave.setUsername("usuario_teste_find");
        userToSave.setPassword("senha_hash");
        userToSave.setCpf("00011122233");
        appUserRepository.save(userToSave);

        Optional<AppUser> foundUser = appUserRepository.findByUsername("usuario_teste_find");

        assertTrue(foundUser.isPresent());
        assertEquals("usuario_teste_find", foundUser.get().getUsername());
    }

    @Test
    void testExistsByCpf() {
        // Cenário: Verificação da existência de um usuário por CPF.
        AppUser userToSave = new AppUser();
        userToSave.setUsername("usuario_cpf_existente");
        userToSave.setPassword("senha_hash");
        userToSave.setCpf("44455566677");
        appUserRepository.save(userToSave);

        boolean exists = appUserRepository.existsByCpf("44455566677");
        
        assertTrue(exists);
    }
}