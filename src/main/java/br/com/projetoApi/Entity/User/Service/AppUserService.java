package br.com.projetoApi.Entity.User.Service;

import java.util.List;
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

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        AppUser userEntity = user.get();
        // Usa os papéis (roles) da entidade, se disponíveis, ou define um padrão
        String[] roles = userEntity.getRoles().orElse(List.of("USER")).toArray(new String[0]);

        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(roles) // Usa os papéis da entidade
                .build();
    }

    public AppUser createUser(String username, String password, String cpf) {
        // Verifica se o nome de usuário já existe
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Usuário já existe: " + username);
        }
        // Verifica se o CPF já existe
        if (userRepository.existsByCpf(cpf)) {
            throw new RuntimeException("CPF já cadastrado: " + cpf);
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setCpf(cpf);
        user.setRoles(List.of("USER")); // Define um papel padrão

        return userRepository.save(user);
    }

    public Optional<AppUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean authenticateUser(String username, String rawPassword) {
        Optional<AppUser> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, user.get().getPassword());
    }
}