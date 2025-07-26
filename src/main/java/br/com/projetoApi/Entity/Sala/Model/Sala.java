package br.com.projetoApi.Entity.Sala.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import br.com.projetoApi.Entity.Bloco.Model.Bloco;

@Entity
@Table(name = "salas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Column(nullable = false)
    @NotNull(message = "Capacidade é obrigatória")
    @Min(value = 1, message = "Capacidade deve ser no mínimo 1")
    private Integer capacidade;

    @Column(nullable = false, length = 20)
    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    private StatusSala status;

    @Column(nullable = false, length = 30)
    @NotNull(message = "Tipo da sala é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoSala tipoSala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bloco_id", nullable = false)
    @NotNull(message = "Bloco é obrigatório")
    private Bloco bloco;

    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
        if (status == null) {
            status = StatusSala.LIVRE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }

    // Enum para os possíveis status da sala
    public enum StatusSala {
        LIVRE("Livre"),
        OCUPADA("Ocupada"),
        MANUTENCAO("Em Manutenção"),
        INDISPONIVEL("Indisponível");

        private final String descricao;

        StatusSala(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    // Enum para os possíveis tipos de sala
    public enum TipoSala {
        SALA_DE_AULA("Sala de Aula"),
        LABORATORIO("Laboratório"),
        AUDITORIO("Auditório"),
        BIBLIOTECA("Biblioteca"),
        ESCRITORIO("Escritório"),
        REUNIAO("Sala de Reunião"),
        COORDENACAO("Coordenação"),
        SECRETARIA("Secretaria");

        private final String descricao;

        TipoSala(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}