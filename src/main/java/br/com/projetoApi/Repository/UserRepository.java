package br.com.projetoApi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.projetoApi.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    
}
