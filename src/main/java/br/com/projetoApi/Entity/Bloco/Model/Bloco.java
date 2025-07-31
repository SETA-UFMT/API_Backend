package br.com.projetoApi.Entity.Bloco.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import br.com.projetoApi.Entity.Sala.Model.Sala;

import java.util.List;

@Entity
@Table(name = "blocos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bloco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false, length = 20)
    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    private StatusBloco status;

    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    // Relacionamento com Salas - Um bloco pode ter várias salas
    @OneToMany(mappedBy = "bloco", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sala> salas;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now();
        updatedAt = java.time.LocalDateTime.now();
        if (status == null) {
            status = StatusBloco.INATIVO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = java.time.LocalDateTime.now();
    }

    // Enum para os possíveis status do bloco
    public enum StatusBloco {
        ATIVO("Ativo"),
        INATIVO("Inativo"),
        MANUTENCAO("Em Manutenção"),
        BLOQUEADO("Bloqueado");

        private final String descricao;

        StatusBloco(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}