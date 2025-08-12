package br.com.projetoApi.Entity.ArCondicionado.Repository;

import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado;
import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.StatusArCondicionado;
import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.VelocidadeVentilacao;
import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.ModoOperacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para operações de banco de dados relacionadas a ArCondicionado.
 */
@Repository
public interface ArCondicionadoRepository extends JpaRepository<ArCondicionado, Long> {

    /**
     * Verifica se existe um ar condicionado associado a uma sala.
     * @param salaId ID da sala.
     * @return true se existir, false caso contrário.
     */
    boolean existsBySalaId(Long salaId);

    /**
     * Lista todos os aparelhos de ar condicionado ordenados por nome da sala.
     * @return Lista de aparelhos.
     */
    @Query("SELECT ac FROM ArCondicionado ac ORDER BY ac.sala.nome ASC")
    List<ArCondicionado> findAllOrderBySalaNome();

    /**
     * Lista aparelhos de ar condicionado por status.
     * @param status Status desejado.
     * @return Lista de aparelhos.
     */
    List<ArCondicionado> findByStatus(StatusArCondicionado status);

    /**
     * Lista aparelhos de ar condicionado com status LIGADO.
     * @return Lista de aparelhos ligados.
     */
    @Query("SELECT ac FROM ArCondicionado ac WHERE ac.status = 'LIGADO'")
    List<ArCondicionado> findArCondicionadosLigados();

    /**
     * Lista aparelhos de ar condicionado com status MANUTENCAO.
     * @return Lista de aparelhos em manutenção.
     */
    @Query("SELECT ac FROM ArCondicionado ac WHERE ac.status = 'MANUTENCAO'")
    List<ArCondicionado> findArCondicionadosManutencao();

    /**
     * Lista aparelhos de ar condicionado por bloco.
     * @param blocoId ID do bloco.
     * @return Lista de aparelhos.
     */
    @Query("SELECT ac FROM ArCondicionado ac WHERE ac.sala.bloco.id = :blocoId")
    List<ArCondicionado> findByBlocoId(@Param("blocoId") Long blocoId);

    /**
     * Busca aparelhos de ar condicionado com filtros opcionais.
     * @param blocoId ID do bloco (opcional).
     * @param status Status do ar condicionado (opcional).
     * @param temperaturaMin Temperatura mínima (opcional).
     * @param temperaturaMax Temperatura máxima (opcional).
     * @param velocidade Velocidade de ventilação (opcional).
     * @param modo Modo de operação (opcional).
     * @return Lista de aparelhos filtrados.
     */
    @Query("SELECT ac FROM ArCondicionado ac WHERE " +
           "(:blocoId IS NULL OR ac.sala.bloco.id = :blocoId) AND " +
           "(:status IS NULL OR ac.status = :status) AND " +
           "(:temperaturaMin IS NULL OR ac.temperatura >= :temperaturaMin) AND " +
           "(:temperaturaMax IS NULL OR ac.temperatura <= :temperaturaMax) AND " +
           "(:velocidade IS NULL OR ac.velocidadeVentilacao = :velocidade) AND " +
           "(:modo IS NULL OR ac.modoOperacao = :modo)")
    List<ArCondicionado> findArCondicionadosComFiltros(
            @Param("blocoId") Long blocoId,
            @Param("status") StatusArCondicionado status,
            @Param("temperaturaMin") Integer temperaturaMin,
            @Param("temperaturaMax") Integer temperaturaMax,
            @Param("velocidade") VelocidadeVentilacao velocidade,
            @Param("modo") ModoOperacao modo);
}