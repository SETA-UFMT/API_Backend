package br.com.projetoApi.Entity.Bloco.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.projetoApi.Entity.Bloco.Model.Bloco;

@Repository
public interface BlocoRepository  extends JpaRepository<Bloco, String> {

    // Busca um bloco pelo nome, retornando uma lista de blocos que correspondem ao nome fornecido
    List<Bloco> findByNome(String nome);
    // Busca blocos pelo status, retornando uma lista de blocos que correspondem ao status fornecido
    List<Bloco> findByStatus(String status);
    boolean existsByNome(String nome);
    
}

// Explicação:
// Esta interface estende JpaRepository, fornecendo métodos para operações CRUD básicas.
// Além disso, define métodos personalizados para buscar blocos por nome e status.
// Pode ser implementado filtros combinados com consultas personalizadas.
