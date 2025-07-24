package br.com.projetoApi.Entity.User.Model;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Classe de entidade JPA que representa um usuário no banco de dados
@Entity
// Gera automaticamente os métodos getters para todos os campos
@Getter
// Gera automaticamente os métodos setters para todos os campos
@Setter
// Gera um construtor padrão (sem argumentos)
@NoArgsConstructor
// Gera um construtor com todos os campos
@AllArgsConstructor
public class AppUser {

    // Identificador único do usuário, gerado automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    // Nome de usuário, armazenado como uma coluna no banco
    private String username;
    
    // Senha do usuário, armazenada como uma coluna no banco (deve ser criptografada)
    private String password;
    
    // Lista de papéis (roles) do usuário, armazenada como uma coleção no banco
    @ElementCollection
    private List<String> roles;

    // Obtém a lista de papéis do usuário, retornada como Optional para tratar casos nulos
    public Optional<List<String>> getRoles() {
        return Optional.ofNullable(roles);
    }
}

// Explicação:
// Esta classe representa um usuário no sistema, incluindo seu ID, nome de usuário, senha e papéis.
// É usada pelo JPA para mapear a entidade para uma tabela no banco de dados.
// Os métodos getters e setters são gerados automaticamente pelas anotações do Lombok.
// A lista de papéis é armazenada como uma coleção, permitindo que um usuário tenha múltiplos papéis no sistema.
