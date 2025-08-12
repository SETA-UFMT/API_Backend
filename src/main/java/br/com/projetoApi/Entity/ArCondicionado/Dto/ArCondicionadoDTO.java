package br.com.projetoApi.Entity.ArCondicionado.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.StatusArCondicionado;
import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.VelocidadeVentilacao;
import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.ModoOperacao;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArCondicionadoDTO {
    
    private Long id;
    
    @NotNull(message = "Sala é obrigatória")
    private Long salaId;
    
    @NotNull(message = "Status é obrigatório")
    private StatusArCondicionado status;
    
    @NotNull(message = "Temperatura é obrigatória")
    @Min(value = 16, message = "Temperatura mínima é 16°C")
    @Max(value = 30, message = "Temperatura máxima é 30°C")
    private Integer temperatura;
    
    @NotNull(message = "Velocidade de ventilação é obrigatória")
    private VelocidadeVentilacao velocidadeVentilacao;
    
    @NotNull(message = "Modo de operação é obrigatório")
    private ModoOperacao modoOperacao;
    
    private String salaNome;
    private String blocoNome;
    
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}