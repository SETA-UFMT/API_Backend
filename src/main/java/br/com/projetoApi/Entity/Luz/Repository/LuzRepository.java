package br.com.projetoApi.Entity.Luz.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.projetoApi.Entity.Luz.Model.Luz;
import br.com.projetoApi.Entity.Luz.Model.Luz.StatusLuz;

@Repository
public interface LuzRepository extends JpaRepository<Luz, Long> {

    // Busca luzes pelo ID da sala
    List<Luz> findBySalaId(Long salaId);
    
    // Busca luzes pelo status
    List<Luz> findByStatus(StatusLuz status);
    
    // Busca luzes por sala e status
    List<Luz> findBySalaIdAndStatus(Long salaId, StatusLuz status);
    
    // Busca luzes por bloco através do relacionamento com sala
    @Query("SELECT l FROM Luz l WHERE l.sala.bloco.id = :blocoId")
    List<Luz> findByBlocoId(@Param("blocoId") Long blocoId);
    
    // Busca luzes ligadas
    @Query("SELECT l FROM Luz l WHERE l.status = 'LIGADO'")
    List<Luz> findLuzesLigadas();
    
    // Busca luzes desligadas
    @Query("SELECT l FROM Luz l WHERE l.status = 'DESLIGADO'")
    List<Luz> findLuzesDesligadas();
    
    // Conta quantas luzes estão ligadas em um bloco
    @Query("SELECT COUNT(l) FROM Luz l WHERE l.sala.bloco.id = :blocoId AND l.status = 'LIGADO'")
    Long countLuzesLigadasPorBloco(@Param("blocoId") Long blocoId);
    
    // Conta quantas luzes estão ligadas em uma sala
    @Query("SELECT COUNT(l) FROM Luz l WHERE l.sala.id = :salaId AND l.status = 'LIGADO'")
    Long countLuzesLigadasPorSala(@Param("salaId") Long salaId);
    
    // Busca luzes por bloco e status
    @Query("SELECT l FROM Luz l WHERE l.sala.bloco.id = :blocoId AND l.status = :status")
    List<Luz> findByBlocoIdAndStatus(@Param("blocoId") Long blocoId, @Param("status") StatusLuz status);
    
    // Busca todas as luzes ordenadas por sala
    @Query("SELECT l FROM Luz l ORDER BY l.sala.nome ASC")
    List<Luz> findAllOrderBySalaNome();
    
    // Verifica se existe luz na sala
    boolean existsBySalaId(Long salaId);
}