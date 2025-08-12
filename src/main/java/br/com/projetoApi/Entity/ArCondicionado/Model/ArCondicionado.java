package br.com.projetoApi.Entity.ArCondicionado.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import br.com.projetoApi.Entity.Sala.Model.Sala;

@Entity
@Table(name = "ar_condicionados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArCondicionado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    @NotNull(message = "Sala é obrigatória")
    private Sala sala;

    @Column(nullable = false, length = 20)
    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    private StatusArCondicionado status;

    @Column(name = "temperatura", nullable = false)
    @NotNull(message = "Temperatura é obrigatória")
    @Min(value = 16, message = "Temperatura mínima é 16°C")
    @Max(value = 30, message = "Temperatura máxima é 30°C")
    private Integer temperatura;

    @Column(name = "velocidade_ventilacao", nullable = false)
    @NotNull(message = "Velocidade de ventilação é obrigatória")
    @Enumerated(EnumType.STRING)
    private VelocidadeVentilacao velocidadeVentilacao;

    @Column(name = "modo_operacao", nullable = false)
    @NotNull(message = "Modo de operação é obrigatório")
    @Enumerated(EnumType.STRING)
    private ModoOperacao modoOperacao;

    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
        if (status == null) {
            status = StatusArCondicionado.DESLIGADO;
        }
        if (temperatura == null) {
            temperatura = 24; // Temperatura padrão
        }
        if (velocidadeVentilacao == null) {
            velocidadeVentilacao = VelocidadeVentilacao.MEDIA;
        }
        if (modoOperacao == null) {
            modoOperacao = ModoOperacao.REFRIGERAR;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }

    // Enum para os possíveis status do ar condicionado
    public enum StatusArCondicionado {
        LIGADO("Ligado"),
        DESLIGADO("Desligado"),
        MANUTENCAO("Em Manutenção"),
        DEFEITO("Com Defeito");

        private final String descricao;

        StatusArCondicionado(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    // Enum para velocidade de ventilação
    public enum VelocidadeVentilacao {
        BAIXA("Baixa"),
        MEDIA("Média"),
        ALTA("Alta"),
        AUTO("Automática");

        private final String descricao;

        VelocidadeVentilacao(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    // Enum para modo de operação
    public enum ModoOperacao {
        REFRIGERAR("Refrigerar"),
        AQUECER("Aquecer"),
        VENTILAR("Ventilar"),
        DESUMIDIFICAR("Desumidificar"),
        AUTO("Automático");

        private final String descricao;

        ModoOperacao(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}