package br.com.projetoApi.Entity.Bloco.Model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Bloco {

    @Getter @Setter
    private String id;

    @Getter @Setter
    private String nome;

    @Getter @Setter
    private String status;
    
}

// Explicação:
// Esta classe representa um modelo de Bloco com três atributos: id, nome e status.

