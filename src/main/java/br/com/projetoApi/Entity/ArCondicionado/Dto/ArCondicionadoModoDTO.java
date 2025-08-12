package br.com.projetoApi.Entity.ArCondicionado.Dto;

import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.ModoOperacao;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArCondicionadoModoDTO {
    
    @NotNull(message = "Modo de operação é obrigatório")
    private ModoOperacao modoOperacao;
}