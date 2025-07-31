package br.com.projetoApi.Entity.Sala.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import br.com.projetoApi.Entity.Sala.Model.Sala.StatusSala;
import br.com.projetoApi.Entity.Sala.Model.Sala.TipoSala;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalaDTO {
    
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotNull(message = "Capacidade é obrigatória")
    @Min(value = 1, message = "Capacidade deve ser no mínimo 1")
    private Integer capacidade;
    
    @NotNull(message = "Status é obrigatório")
    private StatusSala status;
    
    @NotNull(message = "Tipo da sala é obrigatório")
    private TipoSala tipoSala;
    
    @NotNull(message = "Bloco é obrigatório")
    private Long blocoId;
    
    // Campos adicionais para visualização
    private String blocoNome;
    
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}