package br.com.projetoApi.Entity.Luz.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import br.com.projetoApi.Entity.Sala.Model.Sala;

@Entity
@Table(name = "luzes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Luz {

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
    private StatusLuz status;

    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
        if (status == null) {
            status = StatusLuz.DESLIGADO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }

    // Enum para os possíveis status da luz
    public enum StatusLuz {
        LIGADO("Ligado"),
        DESLIGADO("Desligado");

        private final String descricao;

        StatusLuz(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}