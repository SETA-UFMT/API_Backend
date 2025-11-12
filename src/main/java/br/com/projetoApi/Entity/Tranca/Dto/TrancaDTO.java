package br.com.projetoApi.Entity.Tranca.Dto;

import br.com.projetoApi.Entity.Tranca.Model.Tranca;
import br.com.projetoApi.Entity.Tranca.Model.Tranca.StatusTranca;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO para retornar dados da Tranca (Monitoramento)
@Getter
@Setter
@NoArgsConstructor
public class TrancaDTO {

    private Long id;
    private StatusTranca status;

    // Construtor para mapear facilmente da Entidade para o DTO
    public TrancaDTO(Tranca tranca) {
        this.id = tranca.getId();
        this.status = tranca.getStatus();
    }
}