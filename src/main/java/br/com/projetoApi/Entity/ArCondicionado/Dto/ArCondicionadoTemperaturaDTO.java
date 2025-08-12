package br.com.projetoApi.Entity.ArCondicionado.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArCondicionadoTemperaturaDTO {
    
    @NotNull(message = "Temperatura é obrigatória")
    @Min(value = 16, message = "Temperatura mínima é 16°C")
    @Max(value = 30, message = "Temperatura máxima é 30°C")
    private Integer temperatura;
}