package br.com.projetoApi.Entity.User.Dto;

import lombok.Getter;
import lombok.Setter;

public class AppUserRegistrationRequest {
    
    // Nome de usuário fornecido na requisição de registro
    // Anotações @Getter e @Setter geram automaticamente os métodos getUsername e setUsername
    @Getter @Setter
    private String username;
    
    // Senha fornecida na requisição de registro
    // Anotações @Getter e @Setter geram automaticamente os métodos getPassword e setPassword
    @Getter @Setter
    private String password;

    // CPF fornecido na requisição de registro
    // Anotações @Getter e @Setter geram automaticamente os métodos getCpf e set
    @Getter @Setter
    private String cpf;
}

// Explicação
// A classe AppUserRegistrationRequest é um DTO (Data Transfer Object) usado para encapsular
// os dados necessários para o registro de um usuário no sistema.