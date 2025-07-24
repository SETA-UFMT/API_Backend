package br.com.projetoApi.Entity.User.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.projetoApi.Entity.User.Model.AppUser;
import br.com.projetoApi.Entity.User.Repository.AppUserRepository;

@Service
public class AppUserService implements UserDetailsService {

    // Injeção do repositório para operações de persistência de usuários
    @Autowired
    private AppUserRepository userRepository;
    
    // Injeção do codificador de senhas para criptografar e verificar senhas
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Implementação do método UserDetailsService para carregar detalhes do usuário pelo nome de usuário
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário pelo nome de usuário no repositório
        Optional<AppUser> user = userRepository.findByUsername(username);
        
        // Lança exceção se o usuário não for encontrado
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        
        // Obtém a entidade do usuário
        AppUser userEntity = user.get();
        
        // Constrói um objeto UserDetails para uso pelo Spring Security
        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles("USER") // Define o papel padrão como "USER"
                .build();
    }
    
    // Cria um novo usuário com nome de usuário e senha fornecidos
    public AppUser createUser(String username, String password) {
        // Verifica se o nome de usuário já existe para evitar duplicatas
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Usuário já existe: " + username);
        }
        
        // Cria uma nova instância de AppUser
        AppUser user = new AppUser();
        user.setUsername(username);
        // Criptografa a senha antes de salvar
        user.setPassword(passwordEncoder.encode(password));
        
        // Salva o usuário no banco de dados e retorna a entidade
        return userRepository.save(user);
    }
    
    // Busca um usuário pelo nome de usuário, retornando um Optional
    public Optional<AppUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    // Verifica se as credenciais fornecidas são válidas
    public boolean authenticateUser(String username, String rawPassword) {
        // Busca o usuário pelo nome de usuário
        Optional<AppUser> user = userRepository.findByUsername(username);
        
        // Retorna false se o usuário não for encontrado
        if (user.isEmpty()) {
            return false;
        }
        
        // Verifica se a senha fornecida corresponde à senha criptografada
        return passwordEncoder.matches(rawPassword, user.get().getPassword());
    }
}