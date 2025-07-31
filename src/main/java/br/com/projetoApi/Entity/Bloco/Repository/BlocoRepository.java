package br.com.projetoApi.Entity.Bloco.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.projetoApi.Entity.Bloco.Model.Bloco;
import br.com.projetoApi.Entity.Bloco.Model.Bloco.StatusBloco;

@Repository
public interface BlocoRepository extends JpaRepository<Bloco, Long> {

    // Busca um bloco pelo nome (case insensitive)
    Optional<Bloco> findByNomeIgnoreCase(String nome);
    
    // Busca blocos pelo nome contendo a string (case insensitive)
    List<Bloco> findByNomeContainingIgnoreCase(String nome);
    
    // Busca blocos pelo status
    List<Bloco> findByStatus(StatusBloco status);
    
    // Verifica se existe um bloco com o nome especificado
    boolean existsByNomeIgnoreCase(String nome);
    
    // Verifica se existe um bloco com o nome especificado, excluindo um ID específico
    @Query("SELECT COUNT(b) > 0 FROM Bloco b WHERE LOWER(b.nome) = LOWER(:nome) AND b.id != :id")
    boolean existsByNomeIgnoreCaseAndIdNot(@Param("nome") String nome, @Param("id") Long id);
    
    // Busca blocos ativos
    @Query("SELECT b FROM Bloco b WHERE b.status = 'ATIVO'")
    List<Bloco> findBlocosAtivos();
    
    // Busca blocos ordenados por nome
    List<Bloco> findAllByOrderByNomeAsc();
    
    // Busca blocos por status ordenados por data de criação
    List<Bloco> findByStatusOrderByCreatedAtDesc(StatusBloco status);
}