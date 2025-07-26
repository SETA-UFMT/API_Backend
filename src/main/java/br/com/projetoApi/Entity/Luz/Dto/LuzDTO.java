package br.com.projetoApi.Entity.Luz.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import br.com.projetoApi.Entity.Luz.Model.Luz.StatusLuz;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LuzDTO {
    
    private Long id;
    
    @NotNull(message = "Sala é obrigatória")
    private Long salaId;
    
    @NotNull(message = "Status é obrigatório")
    private StatusLuz status;
    
    // Campos adicionais para visualização
    private String salaNome;
    private String blocoNome;
    
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}