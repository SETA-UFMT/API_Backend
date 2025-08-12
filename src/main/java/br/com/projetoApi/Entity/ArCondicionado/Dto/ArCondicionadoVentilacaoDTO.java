package br.com.projetoApi.Entity.ArCondicionado.Dto;

import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.VelocidadeVentilacao;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArCondicionadoVentilacaoDTO {
    
    @NotNull(message = "Velocidade de ventilação é obrigatória")
    private VelocidadeVentilacao velocidadeVentilacao;
}
