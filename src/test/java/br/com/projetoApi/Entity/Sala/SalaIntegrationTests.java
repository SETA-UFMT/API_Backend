package br.com.projetoApi.Entity.Sala;

import br.com.projetoApi.Entity.Bloco.Model.Bloco;
import br.com.projetoApi.Entity.Bloco.Repository.BlocoRepository;
import br.com.projetoApi.Entity.Sala.Dto.SalaDTO;
import br.com.projetoApi.Entity.Sala.Dto.SalaStatusDTO;
import br.com.projetoApi.Entity.Sala.Model.Sala;
import br.com.projetoApi.Entity.Sala.Model.Sala.StatusSala;
import br.com.projetoApi.Entity.Sala.Model.Sala.TipoSala;
import br.com.projetoApi.Entity.Sala.Repository.SalaRepository;
import br.com.projetoApi.Entity.Sala.Service.SalaService;
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
public class SalaIntegrationTests {

    @Autowired
    private SalaService salaService;

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private BlocoRepository blocoRepository;

    private Bloco blocoDeTeste;

    @BeforeEach
    public void setup() {
        // Limpa o banco de dados antes de cada teste para garantir isolamento
        salaRepository.deleteAll();
        blocoRepository.deleteAll();

        // Cria um Bloco de teste que será usado nas salas
        blocoDeTeste = new Bloco();
        blocoDeTeste.setNome("Bloco A");
        blocoDeTeste.setDescricao("Descrição do Bloco A");
        blocoDeTeste = blocoRepository.save(blocoDeTeste);
    }

    // --- Testes de Criação ---

    @Test
    void testCriarSalaSuccessfully() {
        // Cenário: Cria uma nova sala com dados válidos e verifica se foi salva.
        
        // 1. Prepara o DTO de criação
        SalaDTO salaDTO = new SalaDTO();
        salaDTO.setNome("Sala 101");
        salaDTO.setCapacidade(30);
        salaDTO.setStatus(StatusSala.LIVRE);
        salaDTO.setTipoSala(TipoSala.SALA_DE_AULA);
        salaDTO.setBlocoId(blocoDeTeste.getId());

        // 2. Executa a operação de criação
        SalaDTO novaSala = salaService.criarSala(salaDTO);

        // 3. Verifica o resultado
        assertNotNull(novaSala);
        assertNotNull(novaSala.getId());
        assertEquals("Sala 101", novaSala.getNome());
        assertEquals(StatusSala.LIVRE, novaSala.getStatus());
        assertEquals(TipoSala.SALA_DE_AULA, novaSala.getTipoSala());
    }

    @Test
    void testCriarSalaWithDefaultStatus() {
        // Cenário: Cria uma sala sem definir o status para verificar se o valor padrão (LIVRE) é aplicado.
        
        // 1. Prepara o DTO sem o status
        SalaDTO salaDTO = new SalaDTO();
        salaDTO.setNome("Sala 102");
        salaDTO.setCapacidade(25);
        salaDTO.setTipoSala(TipoSala.SALA_DE_AULA);
        salaDTO.setBlocoId(blocoDeTeste.getId());

        // 2. Executa a operação
        SalaDTO novaSala = salaService.criarSala(salaDTO);

        // 3. Verifica o resultado
        assertNotNull(novaSala);
        assertEquals(StatusSala.LIVRE, novaSala.getStatus());
    }
    
    @Test
    void testCriarSalaThrowsExceptionWhenNomeAlreadyExists() {
        // Cenário: Tenta criar uma sala com um nome já existente.
        
        // 1. Cria uma sala inicial com um nome conhecido
        SalaDTO salaDTOExistente = new SalaDTO();
        salaDTOExistente.setNome("Sala de Reunião A");
        salaDTOExistente.setCapacidade(10);
        salaDTOExistente.setStatus(StatusSala.LIVRE);
        salaDTOExistente.setTipoSala(TipoSala.REUNIAO);
        salaDTOExistente.setBlocoId(blocoDeTeste.getId());
        salaService.criarSala(salaDTOExistente);

        // 2. Prepara o DTO para o teste de falha
        SalaDTO salaDTOInvalida = new SalaDTO();
        salaDTOInvalida.setNome("Sala de Reunião A"); // Nome duplicado
        salaDTOInvalida.setCapacidade(15);
        salaDTOInvalida.setStatus(StatusSala.OCUPADA);
        salaDTOInvalida.setTipoSala(TipoSala.REUNIAO);
        salaDTOInvalida.setBlocoId(blocoDeTeste.getId());

        // 3. Verifica se a exceção SalaAlreadyExistsException é lançada
        assertThrows(SalaService.SalaAlreadyExistsException.class, () -> {
            salaService.criarSala(salaDTOInvalida);
        });
    }

    // --- Testes de Busca ---

    @Test
    void testObterSalaByIdSuccessfully() {
        // Cenário: Obtém uma sala por ID e verifica se os dados estão corretos.
        
        // 1. Cria e salva uma sala
        SalaDTO salaDTO = new SalaDTO();
        salaDTO.setNome("Sala de Teste");
        salaDTO.setCapacidade(20);
        salaDTO.setStatus(StatusSala.OCUPADA);
        salaDTO.setTipoSala(TipoSala.LABORATORIO);
        salaDTO.setBlocoId(blocoDeTeste.getId());
        SalaDTO salaCriada = salaService.criarSala(salaDTO);

        // 2. Obtém a sala por ID
        SalaDTO salaEncontrada = salaService.obterSala(salaCriada.getId());

        // 3. Verifica os dados
        assertEquals("Sala de Teste", salaEncontrada.getNome());
        assertEquals(StatusSala.OCUPADA, salaEncontrada.getStatus());
    }

    @Test
    void testObterSalaThrowsExceptionWhenNotFound() {
        // Cenário: Tenta obter uma sala com um ID que não existe.
        assertThrows(EntityNotFoundException.class, () -> {
            salaService.obterSala(9999L); // ID que não existe
        });
    }

    @Test
    void testListarSalasPorStatus() {
        // Cenário: Lista salas por um status específico.
        
        // 1. Cria várias salas com diferentes status
        Sala sala1 = new Sala();
        sala1.setNome("Sala Ocupada");
        sala1.setCapacidade(25);
        sala1.setStatus(StatusSala.OCUPADA);
        sala1.setTipoSala(TipoSala.SALA_DE_AULA);
        sala1.setBloco(blocoDeTeste);
        salaRepository.save(sala1);

        Sala sala2 = new Sala();
        sala2.setNome("Sala Livre");
        sala2.setCapacidade(30);
        sala2.setStatus(StatusSala.LIVRE);
        sala2.setTipoSala(TipoSala.SALA_DE_AULA);
        sala2.setBloco(blocoDeTeste);
        salaRepository.save(sala2);

        // 2. Lista as salas com status LIVRE
        List<SalaDTO> salasLivres = salaService.listarSalasPorStatus(StatusSala.LIVRE);

        // 3. Verifica se apenas a sala esperada foi encontrada
        assertEquals(1, salasLivres.size());
        assertEquals("Sala Livre", salasLivres.get(0).getNome());
    }

    // --- Testes de Atualização ---

    @Test
    void testEditarSalaSuccessfully() {
        // Cenário: Atualiza os dados de uma sala existente.
        
        // 1. Cria uma sala inicial
        SalaDTO salaDTO = new SalaDTO();
        salaDTO.setNome("Sala para Editar");
        salaDTO.setCapacidade(20);
        salaDTO.setStatus(StatusSala.LIVRE);
        salaDTO.setTipoSala(TipoSala.AUDITORIO);
        salaDTO.setBlocoId(blocoDeTeste.getId());
        SalaDTO salaCriada = salaService.criarSala(salaDTO);

        // 2. Prepara o DTO com os novos dados
        SalaDTO salaDTOAtualizacao = new SalaDTO();
        salaDTOAtualizacao.setNome("Sala Editada");
        salaDTOAtualizacao.setCapacidade(50);
        salaDTOAtualizacao.setStatus(StatusSala.OCUPADA);
        salaDTOAtualizacao.setTipoSala(TipoSala.REUNIAO);
        salaDTOAtualizacao.setBlocoId(blocoDeTeste.getId());

        // 3. Executa a atualização
        SalaDTO salaAtualizada = salaService.editarSala(salaCriada.getId(), salaDTOAtualizacao);

        // 4. Verifica os campos atualizados
        assertNotNull(salaAtualizada);
        assertEquals("Sala Editada", salaAtualizada.getNome());
        assertEquals(50, salaAtualizada.getCapacidade());
        assertEquals(StatusSala.OCUPADA, salaAtualizada.getStatus());
        assertEquals(TipoSala.REUNIAO, salaAtualizada.getTipoSala());
    }

    @Test
    void testAlterarStatusSalaSuccessfully() {
        // Cenário: Altera apenas o status de uma sala.
        
        // 1. Cria uma sala inicial com status LIVRE
        SalaDTO salaDTO = new SalaDTO();
        salaDTO.setNome("Sala de Status");
        salaDTO.setCapacidade(15);
        salaDTO.setStatus(StatusSala.LIVRE);
        salaDTO.setTipoSala(TipoSala.ESCRITORIO);
        salaDTO.setBlocoId(blocoDeTeste.getId());
        SalaDTO salaCriada = salaService.criarSala(salaDTO);

        // 2. Prepara o DTO de status
        SalaStatusDTO statusDTO = new SalaStatusDTO();
        statusDTO.setStatus(StatusSala.MANUTENCAO);
        
        // 3. Executa a alteração
        SalaDTO salaAtualizada = salaService.alterarStatusSala(salaCriada.getId(), statusDTO);

        // 4. Verifica o status e se outros campos não foram alterados
        assertEquals(StatusSala.MANUTENCAO, salaAtualizada.getStatus());
        assertEquals("Sala de Status", salaAtualizada.getNome());
    }

    // --- Testes de Exclusão ---

    @Test
    void testDeletarSalaSuccessfully() {
        // Cenário: Deleta uma sala e verifica se ela foi removida.
        
        // 1. Cria e salva uma sala
        SalaDTO salaDTO = new SalaDTO();
        salaDTO.setNome("Sala para Deletar");
        salaDTO.setCapacidade(10);
        salaDTO.setStatus(StatusSala.OCUPADA);
        salaDTO.setTipoSala(TipoSala.REUNIAO);
        salaDTO.setBlocoId(blocoDeTeste.getId());
        SalaDTO salaCriada = salaService.criarSala(salaDTO);

        // 2. Deleta a sala
        salaService.deletarSala(salaCriada.getId());
        
        // 3. Tenta buscar a sala deletada e verifica se não existe
        Optional<Sala> salaDeletada = salaRepository.findById(salaCriada.getId());
        assertTrue(salaDeletada.isEmpty());
    }
}