package br.com.projetoApi.Entity.Sala.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.projetoApi.Entity.Sala.Model.Sala;
import br.com.projetoApi.Entity.Sala.Model.Sala.StatusSala;
import br.com.projetoApi.Entity.Sala.Model.Sala.TipoSala;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {

    // Busca uma sala pelo nome (case insensitive)
    Optional<Sala> findByNomeIgnoreCase(String nome);
    
    // Busca salas pelo nome contendo a string (case insensitive)
    List<Sala> findByNomeContainingIgnoreCase(String nome);
    
    // Busca salas pelo status
    List<Sala> findByStatus(StatusSala status);
    
    // Busca salas pelo tipo
    List<Sala> findByTipoSala(TipoSala tipoSala);
    
    // Busca salas por bloco
    List<Sala> findByBlocoId(Long blocoId);
    
    // Busca salas por bloco e status
    List<Sala> findByBlocoIdAndStatus(Long blocoId, StatusSala status);
    
    // Busca salas por tipo e status
    List<Sala> findByTipoSalaAndStatus(TipoSala tipoSala, StatusSala status);
    
    // Verifica se existe uma sala com o nome especificado
    boolean existsByNomeIgnoreCase(String nome);
    
    // Verifica se existe uma sala com o nome especificado, excluindo um ID específico
    @Query("SELECT COUNT(s) > 0 FROM Sala s WHERE LOWER(s.nome) = LOWER(:nome) AND s.id != :id")
    boolean existsByNomeIgnoreCaseAndIdNot(@Param("nome") String nome, @Param("id") Long id);
    
    // Busca salas livres
    @Query("SELECT s FROM Sala s WHERE s.status = 'LIVRE'")
    List<Sala> findSalasLivres();
    
    // Busca salas por capacidade mínima
    List<Sala> findByCapacidadeGreaterThanEqual(Integer capacidade);
    
    // Busca salas livres com capacidade mínima
    @Query("SELECT s FROM Sala s WHERE s.status = 'LIVRE' AND s.capacidade >= :capacidade")
    List<Sala> findSalasLivresComCapacidadeMinima(@Param("capacidade") Integer capacidade);
    
    // Busca salas de um bloco específico ordenadas por nome
    List<Sala> findByBlocoIdOrderByNomeAsc(Long blocoId);
    
    // Busca todas as salas ordenadas por nome
    List<Sala> findAllByOrderByNomeAsc();
    
    // Busca salas por status ordenadas por data de criação
    List<Sala> findByStatusOrderByCreatedAtDesc(StatusSala status);
    
    // Conta quantas salas livres existem em um bloco
    @Query("SELECT COUNT(s) FROM Sala s WHERE s.bloco.id = :blocoId AND s.status = 'LIVRE'")
    Long countSalasLivresPorBloco(@Param("blocoId") Long blocoId);
    
    // Busca salas por múltiplos filtros
    @Query("SELECT s FROM Sala s WHERE " +
           "(:blocoId IS NULL OR s.bloco.id = :blocoId) AND " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(:tipoSala IS NULL OR s.tipoSala = :tipoSala) AND " +
           "(:capacidadeMinima IS NULL OR s.capacidade >= :capacidadeMinima)")
    List<Sala> findSalasComFiltros(@Param("blocoId") Long blocoId,
                                   @Param("status") StatusSala status,
                                   @Param("tipoSala") TipoSala tipoSala,
                                   @Param("capacidadeMinima") Integer capacidadeMinima);
}