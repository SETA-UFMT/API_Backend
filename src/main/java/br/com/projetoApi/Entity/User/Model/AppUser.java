package br.com.projetoApi.Entity.User.Model;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato 123.456.789-01")
    private String cpf;

    @NotNull(message = "Nome de usuário é obrigatório")
    private String username;

    @NotNull(message = "Senha é obrigatória")
    private String password;

    @ElementCollection
    private List<String> roles;

    public Optional<List<String>> getRoles() {
        return Optional.ofNullable(roles);
    }
}