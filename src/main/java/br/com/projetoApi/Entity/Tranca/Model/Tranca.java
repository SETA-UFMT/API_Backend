package br.com.projetoApi.Entity.Tranca.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trancas")
@Getter
@Setter
@NoArgsConstructor
public class Tranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTranca status;

    public Tranca(String localizacao) {
        this.status = StatusTranca.FECHADA; // Estado inicial padrão
    }

    // Enum para controlar os estados possíveis da tranca
    public enum StatusTranca {
        ABERTA,
        FECHADA,
        COM_DEFEITO 
    }
}