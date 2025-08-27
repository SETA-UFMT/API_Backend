package br.com.projetoApi.Entity.Luz;

import br.com.projetoApi.Entity.Bloco.Model.Bloco;
import br.com.projetoApi.Entity.Bloco.Repository.BlocoRepository;
import br.com.projetoApi.Entity.Luz.Dto.LuzDTO;
import br.com.projetoApi.Entity.Luz.Model.Luz.StatusLuz;
import br.com.projetoApi.Entity.Sala.Model.Sala;
import br.com.projetoApi.Entity.Sala.Model.Sala.StatusSala;
import br.com.projetoApi.Entity.Sala.Model.Sala.TipoSala;
import br.com.projetoApi.Entity.Sala.Repository.SalaRepository;
import br.com.projetoApi.main.BeckEndApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BeckEndApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LuzControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlocoRepository blocoRepository;

    @Autowired
    private SalaRepository salaRepository;
    
    @Autowired
    private ObjectMapper objectMapper;

    private Bloco blocoDeTeste;
    private Sala salaDeTeste;

    @BeforeEach
    public void setup() {
        blocoRepository.deleteAll();
        salaRepository.deleteAll();

        blocoDeTeste = new Bloco();
        blocoDeTeste.setNome("Bloco D");
        blocoDeTeste.setDescricao("Bloco para testes de luz");
        blocoDeTeste = blocoRepository.save(blocoDeTeste);

        salaDeTeste = new Sala();
        salaDeTeste.setNome("Sala 404");
        salaDeTeste.setCapacidade(35);
        salaDeTeste.setStatus(StatusSala.LIVRE);
        salaDeTeste.setTipoSala(TipoSala.SALA_DE_AULA);
        salaDeTeste.setBloco(blocoDeTeste);
        salaDeTeste = salaRepository.save(salaDeTeste);
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCriarLuzComSucessoRetorna201() throws Exception {
        // Cenário: Criação de uma nova luz para uma sala existente.
        
        LuzDTO luzDTO = new LuzDTO();
        luzDTO.setSalaId(salaDeTeste.getId());
        luzDTO.setStatus(StatusLuz.DESLIGADO);

        mockMvc.perform(post("/api/luzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(luzDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.salaId").value(salaDeTeste.getId()));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCriarLuzComSalaInexistenteRetorna404() throws Exception {
        // Cenário: Tenta criar uma luz para uma sala que não existe.
        
        LuzDTO luzDTO = new LuzDTO();
        luzDTO.setSalaId(9999L); // ID de sala inexistente
        luzDTO.setStatus(StatusLuz.DESLIGADO);

        mockMvc.perform(post("/api/luzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(luzDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Sala não encontrada com ID: 9999"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testLigarLuzRetorna200() throws Exception {
        // Cenário: Liga uma luz existente.
        LuzDTO luzDTO = new LuzDTO();
        luzDTO.setSalaId(salaDeTeste.getId());
        luzDTO.setStatus(StatusLuz.DESLIGADO);
        LuzDTO luzCriada = (LuzDTO) objectMapper.readValue(mockMvc.perform(post("/api/luzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(luzDTO)))
                .andReturn().getResponse().getContentAsString(), LuzDTO.class);
        
        mockMvc.perform(patch("/api/luzes/{id}/ligar", luzCriada.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("LIGADO"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testDesligarLuzRetorna200() throws Exception {
        // Cenário: Desliga uma luz existente.
        LuzDTO luzDTO = new LuzDTO();
        luzDTO.setSalaId(salaDeTeste.getId());
        luzDTO.setStatus(StatusLuz.LIGADO);
        LuzDTO luzCriada = (LuzDTO) objectMapper.readValue(mockMvc.perform(post("/api/luzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(luzDTO)))
                .andReturn().getResponse().getContentAsString(), LuzDTO.class);

        mockMvc.perform(patch("/api/luzes/{id}/desligar", luzCriada.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DESLIGADO"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testLigarLuzInexistenteRetorna404() throws Exception {
        // Cenário: Tenta ligar uma luz com um ID que não existe.
        mockMvc.perform(patch("/api/luzes/{id}/ligar", 9999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Luz não encontrada com ID: 9999"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDesligarLuzInexistenteRetorna404() throws Exception {
        // Cenário: Tenta desligar uma luz com um ID que não existe.
        mockMvc.perform(patch("/api/luzes/{id}/desligar", 9999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Luz não encontrada com ID: 9999"));
    }

}