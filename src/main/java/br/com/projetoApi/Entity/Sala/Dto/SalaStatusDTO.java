package br.com.projetoApi.Entity.Sala.Dto;

import br.com.projetoApi.Entity.Sala.Model.Sala.StatusSala;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalaStatusDTO {
    
    @NotNull(message = "Status é obrigatório")
    private StatusSala status;
}