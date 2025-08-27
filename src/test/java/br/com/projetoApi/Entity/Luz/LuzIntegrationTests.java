package br.com.projetoApi.Entity.Luz;

import br.com.projetoApi.Entity.Bloco.Model.Bloco;
import br.com.projetoApi.Entity.Bloco.Repository.BlocoRepository;
import br.com.projetoApi.Entity.Luz.Dto.LuzDTO;
import br.com.projetoApi.Entity.Luz.Dto.LuzStatusDTO;
import br.com.projetoApi.Entity.Luz.Model.Luz;
import br.com.projetoApi.Entity.Luz.Model.Luz.StatusLuz;
import br.com.projetoApi.Entity.Luz.Repository.LuzRepository;
import br.com.projetoApi.Entity.Luz.Service.LuzService;
import br.com.projetoApi.Entity.Sala.Model.Sala;
import br.com.projetoApi.Entity.Sala.Model.Sala.StatusSala;
import br.com.projetoApi.Entity.Sala.Model.Sala.TipoSala;
import br.com.projetoApi.Entity.Sala.Repository.SalaRepository;
import br.com.projetoApi.main.BeckEndApplication;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BeckEndApplication.class)
@ActiveProfiles("test")
public class LuzIntegrationTests {

    @Autowired
    private LuzService luzService;
    
    @Autowired
    private LuzRepository luzRepository;

    @Autowired
    private SalaRepository salaRepository;
    
    @Autowired
    private BlocoRepository blocoRepository;

    private Bloco blocoDeTeste;
    private Sala salaDeTeste;

    @BeforeEach
    public void setup() {
        // Limpa o banco de dados antes de cada teste
        luzRepository.deleteAll();
        salaRepository.deleteAll();
        blocoRepository.deleteAll();

        // Cria um Bloco e uma Sala de teste para as luzes
        blocoDeTeste = new Bloco();
        blocoDeTeste.setNome("Bloco C");
        blocoDeTeste.setDescricao("Descrição do Bloco C");
        blocoDeTeste = blocoRepository.save(blocoDeTeste);

        salaDeTeste = new Sala();
        salaDeTeste.setNome("Sala 301");
        salaDeTeste.setCapacidade(50);
        salaDeTeste.setStatus(StatusSala.LIVRE);
        salaDeTeste.setTipoSala(TipoSala.AUDITORIO);
        salaDeTeste.setBloco(blocoDeTeste);
        salaDeTeste = salaRepository.save(salaDeTeste);
    }
    
    // --- Testes de Criação ---
    
    @Test
    void testCriarLuzSuccessfully() {
        // Cenário: Cria uma nova luz para uma sala e verifica se foi salva.
        LuzDTO luzDTO = new LuzDTO();
        luzDTO.setSalaId(salaDeTeste.getId());
        luzDTO.setStatus(StatusLuz.LIGADO);

        LuzDTO novaLuz = luzService.criarLuz(luzDTO);
        
        assertNotNull(novaLuz);
        assertNotNull(novaLuz.getId());
        assertEquals(StatusLuz.LIGADO, novaLuz.getStatus());
        assertEquals(salaDeTeste.getId(), novaLuz.getSalaId());
    }

    @Test
    void testCriarLuzWithDefaultStatus() {
        // Cenário: Cria uma luz sem status e verifica se o status padrão (DESLIGADO) é aplicado.
        LuzDTO luzDTO = new LuzDTO();
        luzDTO.setSalaId(salaDeTeste.getId());
        // status não é setado aqui

        LuzDTO novaLuz = luzService.criarLuz(luzDTO);
        
        assertNotNull(novaLuz);
        assertEquals(StatusLuz.DESLIGADO, novaLuz.getStatus());
    }

    @Test
    void testCriarLuzThrowsExceptionWhenLuzAlreadyExistsInSala() {
        // Cenário: Tenta criar uma segunda luz para a mesma sala.
        LuzDTO luzDTO = new LuzDTO();
        luzDTO.setSalaId(salaDeTeste.getId());
        luzService.criarLuz(luzDTO); // Cria a primeira luz

        LuzDTO luzDTOInvalida = new LuzDTO();
        luzDTOInvalida.setSalaId(salaDeTeste.getId());
        
        assertThrows(LuzService.LuzAlreadyExistsException.class, () -> {
            luzService.criarLuz(luzDTOInvalida); // Tenta criar uma segunda luz
        });
    }

    @Test
    void testCriarLuzThrowsExceptionWhenSalaNotFound() {
        // Cenário: Tenta criar uma luz para uma sala que não existe.
        LuzDTO luzDTOInvalida = new LuzDTO();
        luzDTOInvalida.setSalaId(9999L);
        luzDTOInvalida.setStatus(StatusLuz.LIGADO);

        assertThrows(EntityNotFoundException.class, () -> {
            luzService.criarLuz(luzDTOInvalida);
        });
    }
    
    // --- Testes de Busca e Listagem ---

    @Test
    void testListarLuzes() {
        // Cenário: Lista todas as luzes cadastradas.
        luzService.criarLuz(new LuzDTO(null, salaDeTeste.getId(), StatusLuz.LIGADO, null, null, null, null));
        
        List<LuzDTO> luzes = luzService.listarLuzes();
        
        assertFalse(luzes.isEmpty());
        assertEquals(1, luzes.size());
        assertEquals(salaDeTeste.getId(), luzes.get(0).getSalaId());
    }

    @Test
    void testObterLuzSuccessfully() {
        // Cenário: Obtém uma luz por ID.
        LuzDTO luzCriada = luzService.criarLuz(new LuzDTO(null, salaDeTeste.getId(), StatusLuz.DESLIGADO, null, null, null, null));
        
        LuzDTO luzObtida = luzService.obterLuz(luzCriada.getId());
        
        assertNotNull(luzObtida);
        assertEquals(luzCriada.getId(), luzObtida.getId());
        assertEquals(StatusLuz.DESLIGADO, luzObtida.getStatus());
    }

    // --- Testes de Alteração de Status ---
    
    @Test
    void testLigarLuzSuccessfully() {
        // Cenário: Altera o status de uma luz para LIGADO.
        LuzDTO luzCriada = luzService.criarLuz(new LuzDTO(null, salaDeTeste.getId(), StatusLuz.DESLIGADO, null, null, null, null));

        luzService.ligarLuz(luzCriada.getId());

        Luz luzAtualizada = luzRepository.findById(luzCriada.getId()).get();
        assertEquals(StatusLuz.LIGADO, luzAtualizada.getStatus());
    }

    @Test
    void testDesligarLuzSuccessfully() {
        // Cenário: Altera o status de uma luz para DESLIGADO.
        LuzDTO luzCriada = luzService.criarLuz(new LuzDTO(null, salaDeTeste.getId(), StatusLuz.LIGADO, null, null, null, null));

        luzService.desligarLuz(luzCriada.getId());

        Luz luzAtualizada = luzRepository.findById(luzCriada.getId()).get();
        assertEquals(StatusLuz.DESLIGADO, luzAtualizada.getStatus());
    }

    @Test
    void testAlternarStatusLuzFromLigadoToDesligado() {
        // Cenário: Alterna o status de uma luz de LIGADO para DESLIGADO.
        LuzDTO luzCriada = luzService.criarLuz(new LuzDTO(null, salaDeTeste.getId(), StatusLuz.LIGADO, null, null, null, null));

        luzService.alternarStatusLuz(luzCriada.getId());

        Luz luzAtualizada = luzRepository.findById(luzCriada.getId()).get();
        assertEquals(StatusLuz.DESLIGADO, luzAtualizada.getStatus());
    }

    @Test
    void testAlternarStatusLuzFromDesligadoToLigado() {
        // Cenário: Alterna o status de uma luz de DESLIGADO para LIGADO.
        LuzDTO luzCriada = luzService.criarLuz(new LuzDTO(null, salaDeTeste.getId(), StatusLuz.DESLIGADO, null, null, null, null));

        luzService.alternarStatusLuz(luzCriada.getId());

        Luz luzAtualizada = luzRepository.findById(luzCriada.getId()).get();
        assertEquals(StatusLuz.LIGADO, luzAtualizada.getStatus());
    }
}