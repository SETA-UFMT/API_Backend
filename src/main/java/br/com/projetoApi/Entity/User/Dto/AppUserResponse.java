package br.com.projetoApi.Entity.User.Dto;

import lombok.Getter;
import lombok.Setter;

public class AppUserResponse {
    
    // Mensagem descritiva sobre o resultado da operação (ex.: sucesso ou erro)
    // Anotações @Getter e @Setter geram automaticamente os métodos getMessage e setMessage
    @Getter @Setter
    private String message;
    
    // Nome de usuário retornado na resposta
    // Anotações @Getter e @Setter geram automaticamente os métodos getUsername e setUsername
    @Getter @Setter
    private String username;
    
    // Identificador único do usuário
    // Anotações @Getter e @Setter geram automaticamente os métodos getId e setId
    @Getter @Setter
    private Long id;
    
    // CPF do usuário retornado na resposta
    // Anotações @Getter e @Setter geram automaticamente os métodos getCpf e setCpf
    @Getter @Setter
    private String cpf;
    
    // Token JWT retornado após autenticação bem-sucedida
    // Anotações @Getter e @Setter geram automaticamente os métodos getToken e setToken
    @Getter @Setter
    private String token;
}

// Explicação:
// Esta classe é usada para transferir dados entre camadas da aplicação, 
// especialmente ao retornar informações sobre o usuário após operações como login ou registro.