package br.com.projetoApi.Entity.ArCondicionado.Dto;

import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.StatusArCondicionado;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArCondicionadoStatusDTO {
    
    @NotNull(message = "Status é obrigatório")
    private StatusArCondicionado status;
}