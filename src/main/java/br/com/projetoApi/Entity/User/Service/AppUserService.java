package br.com.projetoApi.Entity.User.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.projetoApi.Entity.User.Dto.AppUserRegistrationRequest;
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
        String[] roles = userEntity.getRoles().orElse(List.of("USER")).toArray(new String[0]);

        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(roles)
                .build();
    }

    public AppUser createUser(AppUserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Usuário já existe: " + request.getUsername());
        }
        if (userRepository.existsByCpf(request.getCpf())) {
            throw new RuntimeException("CPF já cadastrado: " + request.getCpf());
        }

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCpf(request.getCpf());

        List<String> roles = request.getRoles() != null && !request.getRoles().isEmpty()
                                ? request.getRoles()
                                : Arrays.asList("USER");
        user.setRoles(roles);

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