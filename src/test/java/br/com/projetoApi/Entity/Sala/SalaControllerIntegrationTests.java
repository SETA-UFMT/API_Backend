package br.com.projetoApi.Entity.Sala;

import br.com.projetoApi.Entity.Bloco.Model.Bloco;
import br.com.projetoApi.Entity.Bloco.Repository.BlocoRepository;
import br.com.projetoApi.Entity.Sala.Dto.SalaDTO;
import br.com.projetoApi.Entity.Sala.Model.Sala.StatusSala;
import br.com.projetoApi.Entity.Sala.Model.Sala.TipoSala;
import br.com.projetoApi.main.BeckEndApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BeckEndApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SalaControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlocoRepository blocoRepository;
    
    @Autowired
    private ObjectMapper objectMapper;

    private Bloco blocoDeTeste;

    @BeforeEach
    public void setup() {
        // Garantir que a entidade Bloco exista para os testes
        blocoRepository.deleteAll();
        blocoDeTeste = new Bloco();
        blocoDeTeste.setNome("Bloco Z");
        blocoDeTeste.setDescricao("Bloco de testes para o controller");
        blocoDeTeste = blocoRepository.save(blocoDeTeste);
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Usuário com permissão de ADMIN
    void testCriarSalaComSucessoRetorna201() throws Exception {
        // Cenário: Criação de uma nova sala via API com dados válidos.
        
        SalaDTO salaDTO = new SalaDTO();
        salaDTO.setNome("Sala do Controller");
        salaDTO.setCapacidade(40);
        salaDTO.setStatus(StatusSala.LIVRE);
        salaDTO.setTipoSala(TipoSala.SALA_DE_AULA);
        salaDTO.setBlocoId(blocoDeTeste.getId());
        
        String salaJson = objectMapper.writeValueAsString(salaDTO);
        
        mockMvc.perform(post("/api/salas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(salaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Sala do Controller"))
                .andExpect(jsonPath("$.capacidade").value(40));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN") // Usuário com permissão de ADMIN
    void testCriarSalaComBlocoInexistenteRetorna404() throws Exception {
        // Cenário: Criação de uma sala com um bloco que não existe.

        SalaDTO salaDTO = new SalaDTO();
        salaDTO.setNome("Sala Sem Bloco");
        salaDTO.setCapacidade(15);
        salaDTO.setStatus(StatusSala.LIVRE);
        salaDTO.setTipoSala(TipoSala.ESCRITORIO);
        salaDTO.setBlocoId(9999L);
        
        String salaJson = objectMapper.writeValueAsString(salaDTO);
        
        mockMvc.perform(post("/api/salas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(salaJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Bloco não encontrado com ID: 9999"));
    }
}