package br.com.ProjetoApi.Entity.User.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ProjetoApi.Entity.User.Model.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    
    // Busca um usuário pelo nome de usuário, retornando um Optional para lidar com casos em que o usuário não existe
    Optional<AppUser> findByUsername(String username);
    
    // Verifica se existe um usuário com o nome de usuário fornecido
    boolean existsByUsername(String username);
}

// Explicação:
// Esta interface estende JpaRepository, fornecendo métodos para operações CRUD básicas.
// Pode ser implementado filros combinados com consultas personalizadas. 