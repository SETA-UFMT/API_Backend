package br.com.projetoApi.Entity.Bloco.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import br.com.projetoApi.Entity.Bloco.Model.Bloco.StatusBloco;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlocoDTO {
    
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    private String descricao;
    
    @NotNull(message = "Status é obrigatório")
    private StatusBloco status;
    
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}