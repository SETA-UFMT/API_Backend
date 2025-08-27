package br.com.projetoApi.Entity.Bloco;

import br.com.projetoApi.Entity.Bloco.Dto.BlocoDTO;
import br.com.projetoApi.Entity.Bloco.Dto.StatusDTO;
import br.com.projetoApi.Entity.Bloco.Model.Bloco;
import br.com.projetoApi.Entity.Bloco.Model.Bloco.StatusBloco;
import br.com.projetoApi.Entity.Bloco.Repository.BlocoRepository;
import br.com.projetoApi.Entity.Bloco.Service.BlocoService;
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
public class BlocoIntegrationTests {

    @Autowired
    private BlocoService blocoService;

    @Autowired
    private BlocoRepository blocoRepository;

    @BeforeEach
    public void setup() {
        // Limpa o banco de dados antes de cada teste
        blocoRepository.deleteAll();
    }
    
    // --- Testes de Criação ---

    @Test
    void testCriarBlocoSuccessfully() {
        // Cenário: Cria um novo bloco com sucesso.
        BlocoDTO blocoDTO = new BlocoDTO();
        blocoDTO.setNome("Bloco A");
        blocoDTO.setDescricao("Descrição do Bloco A");
        blocoDTO.setStatus(StatusBloco.ATIVO);

        BlocoDTO novoBloco = blocoService.criarBloco(blocoDTO);

        assertNotNull(novoBloco);
        assertNotNull(novoBloco.getId());
        assertEquals("Bloco A", novoBloco.getNome());
        assertEquals(StatusBloco.ATIVO, novoBloco.getStatus());
    }

    @Test
    void testCriarBlocoWithDefaultStatus() {
        // Cenário: Cria um bloco sem definir o status para verificar se o valor padrão (INATIVO) é aplicado.
        BlocoDTO blocoDTO = new BlocoDTO();
        blocoDTO.setNome("Bloco B");
        blocoDTO.setDescricao("Descrição do Bloco B");

        BlocoDTO novoBloco = blocoService.criarBloco(blocoDTO);

        assertNotNull(novoBloco);
        assertEquals(StatusBloco.INATIVO, novoBloco.getStatus());
    }

    @Test
    void testCriarBlocoThrowsExceptionWhenNomeAlreadyExists() {
        // Cenário: Tenta criar um bloco com um nome já existente.
        BlocoDTO blocoDTOExistente = new BlocoDTO();
        blocoDTOExistente.setNome("Bloco C");
        blocoDTOExistente.setStatus(StatusBloco.ATIVO);
        blocoService.criarBloco(blocoDTOExistente);

        BlocoDTO blocoDTOInvalido = new BlocoDTO();
        blocoDTOInvalido.setNome("Bloco C"); // Nome duplicado
        blocoDTOInvalido.setStatus(StatusBloco.INATIVO);

        assertThrows(BlocoService.BlocoAlreadyExistsException.class, () -> {
            blocoService.criarBloco(blocoDTOInvalido);
        });
    }

    // --- Testes de Busca e Listagem ---

    @Test
    void testListarBlocosAtivos() {
        // Cenário: Lista apenas os blocos com status ATIVO.
        Bloco blocoAtivo = new Bloco();
        blocoAtivo.setNome("Bloco Ativo");
        blocoAtivo.setStatus(StatusBloco.ATIVO);
        blocoRepository.save(blocoAtivo);
        
        Bloco blocoInativo = new Bloco();
        blocoInativo.setNome("Bloco Inativo");
        blocoInativo.setStatus(StatusBloco.INATIVO);
        blocoRepository.save(blocoInativo);

        List<BlocoDTO> blocosAtivos = blocoService.listarBlocosAtivos();

        assertEquals(1, blocosAtivos.size());
        assertEquals("Bloco Ativo", blocosAtivos.get(0).getNome());
        assertEquals(StatusBloco.ATIVO, blocosAtivos.get(0).getStatus());
    }
    
    // --- Testes de Atualização ---

    @Test
    void testAlterarStatusBlocoSuccessfully() {
        // Cenário: Altera o status de um bloco e verifica a mudança.
        BlocoDTO blocoDTO = new BlocoDTO();
        blocoDTO.setNome("Bloco D");
        blocoDTO.setStatus(StatusBloco.ATIVO);
        BlocoDTO blocoCriado = blocoService.criarBloco(blocoDTO);

        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setStatus(StatusBloco.MANUTENCAO);
        
        BlocoDTO blocoAtualizado = blocoService.alterarStatusBloco(blocoCriado.getId(), statusDTO);

        assertEquals(StatusBloco.MANUTENCAO, blocoAtualizado.getStatus());
    }
    
    @Test
    void testDeletarBlocoSuccessfully() {
        // Cenário: Deleta um bloco e verifica se ele foi removido.
        BlocoDTO blocoDTO = new BlocoDTO();
        blocoDTO.setNome("Bloco para Deletar");
        blocoDTO.setStatus(StatusBloco.ATIVO);
        BlocoDTO blocoCriado = blocoService.criarBloco(blocoDTO);

        blocoService.deletarBloco(blocoCriado.getId());
        
        Optional<Bloco> blocoDeletado = blocoRepository.findById(blocoCriado.getId());
        assertTrue(blocoDeletado.isEmpty());
    }
}