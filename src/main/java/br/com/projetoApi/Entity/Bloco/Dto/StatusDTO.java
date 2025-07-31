package br.com.projetoApi.Entity.Bloco.Dto;
import br.com.projetoApi.Entity.Bloco.Model.Bloco.StatusBloco;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class StatusDTO {
    
    @NotNull(message = "Status é obrigatório")
    private StatusBloco status;
}