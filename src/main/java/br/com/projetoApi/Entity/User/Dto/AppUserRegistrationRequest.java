package br.com.projetoApi.Entity.User.Dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class AppUserRegistrationRequest {
    
    @Getter @Setter
    private String username;
    
    @Getter @Setter
    private String password;

    @Getter @Setter
    private String cpf;

    // Adicionado o campo email, se você o usa na requisição
    //@Getter @Setter
    //private String email; 

    // Adicionado o campo roles, que será uma lista de strings
    @Getter @Setter
    private List<String> roles;
}