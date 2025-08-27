package br.com.projetoApi.Entity.Bloco;

import br.com.projetoApi.Entity.Bloco.Dto.BlocoDTO;
import br.com.projetoApi.Entity.Bloco.Model.Bloco;
import br.com.projetoApi.Entity.Bloco.Repository.BlocoRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BeckEndApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BlocoControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlocoRepository blocoRepository;
    
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        blocoRepository.deleteAll();
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCriarBlocoComSucessoRetorna201() throws Exception {
        // Cenário: Criação de um novo bloco via API com dados válidos.
        BlocoDTO blocoDTO = new BlocoDTO();
        blocoDTO.setNome("Bloco C");
        blocoDTO.setDescricao("Bloco do Controller Teste");
        
        String blocoJson = objectMapper.writeValueAsString(blocoDTO);
        
        mockMvc.perform(post("/api/blocos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(blocoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Bloco C"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testObterBlocoPorIdComSucessoRetorna200() throws Exception {
        // Cenário: Obtém um bloco por ID que existe.
        Bloco blocoSalvo = new Bloco();
        blocoSalvo.setNome("Bloco D");
        blocoSalvo.setDescricao("Outro Bloco");
        blocoSalvo = blocoRepository.save(blocoSalvo);
        
        mockMvc.perform(get("/api/blocos/{id}", blocoSalvo.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Bloco D"));
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testObterBlocoPorIdInexistenteRetorna404() throws Exception {
        // Cenário: Tenta obter um bloco com um ID que não existe.
        mockMvc.perform(get("/api/blocos/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Bloco não encontrado com ID: 9999"));
    }
}