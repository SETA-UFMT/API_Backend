package br.com.projetoApi.Entity.Luz.Dto;

import br.com.projetoApi.Entity.Luz.Model.Luz.StatusLuz;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LuzStatusDTO {
    
    @NotNull(message = "Status é obrigatório")
    private StatusLuz status;
}