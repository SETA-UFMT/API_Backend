package br.com.projetoApi.Entity.User.Dto;

import lombok.Getter;
import lombok.Setter;

public class AppUserLoginRequest {
    
    // Nome de usuário fornecido na requisição de login
    // Anotações @Getter e @Setter geram automaticamente os métodos getUsername e setUsername
    @Getter @Setter
    private String username;
    
    // Senha fornecida na requisição de login
    // Anotações @Getter e @Setter geram automaticamente os métodos getPassword e setPassword
    @Getter @Setter
    private String password;

    // CPF fornecido na requisição de login
    // Anotações @Getter e @Setter geram automaticamente os métodos getCpf e set
    @Getter @Setter
    private String cpf;
}

// Explicação
// A classe AppUserLoginRequest é um DTO (Data Transfer Object) usado para encapsular os dados de login do usuário.