package br.com.projetoApi.Entity.Tranca.Controller;

import br.com.projetoApi.Entity.Tranca.Dto.TrancaAcaoDTO;
import br.com.projetoApi.Entity.Tranca.Dto.TrancaDTO;
import br.com.projetoApi.Entity.Tranca.Service.TrancaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trancas") // Define a URL base para este controlador
public class TrancaController {

    @Autowired
    private TrancaService trancaService;

    /**
     * Endpoint para MONITORAR o status de uma tranca específica.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrancaDTO> buscarTrancaPorId(@PathVariable Long id) {
        TrancaDTO trancaDTO = trancaService.buscarPorId(id);
        return ResponseEntity.ok(trancaDTO);
    }

    /**
     * Endpoint para MONITORAR o status de todas as trancas.
     */
    @GetMapping
    public ResponseEntity<List<TrancaDTO>> listarTodasTrancas() {
        List<TrancaDTO> trancas = trancaService.listarTodas();
        return ResponseEntity.ok(trancas);
    }

    /**
     * Endpoint para CONTROLAR (abrir/fechar) uma tranca.
     */
    @PostMapping("/{id}/controlar")
    public ResponseEntity<TrancaDTO> controlarTranca(
            @PathVariable Long id, 
            @RequestBody TrancaAcaoDTO acaoDTO) {
                
        TrancaDTO trancaAtualizada = trancaService.controlarTranca(id, acaoDTO);
        return ResponseEntity.ok(trancaAtualizada);
    }
}