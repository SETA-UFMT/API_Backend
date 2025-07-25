package br.com.projetoApi.Entity.Bloco.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.projetoApi.Entity.Bloco.Service.BlocoService;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/blocos")
public class BlocoController {

    private final BlocoService blocoService;

    @GetMapping
    public ResponseEntity<String> listarBlocos() {
        // Em uma aplicação real, retornar uma lista de DTOs Bloco
        return ResponseEntity.ok("Lista de blocos");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> obterBloco(@PathVariable Long id) {
        // Buscar bloco por ID no serviço
        return ResponseEntity.ok("Detalhes do bloco com ID: " + id);
    }

    @PostMapping
    public ResponseEntity<String> criarBloco(@RequestBody BlocoDTO blocoDTO) {
        // Chamar serviço para criar bloco
        return ResponseEntity.status(HttpStatus.CREATED).body("Bloco criado com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editarBloco(@PathVariable Long id, @RequestBody BlocoDTO blocoDTO) {
        // Chamar serviço para atualizar bloco
        return ResponseEntity.ok("Bloco com ID " + id + " editado com sucesso");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarBloco(@PathVariable Long id) {
        // Chamar serviço para excluir bloco
        return ResponseEntity.ok("Bloco com ID " + id + " deletado com sucesso");
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> alterarStatusBloco(@PathVariable Long id, @RequestBody StatusDTO statusDTO) {
        // Chamar serviço para atualizar status
        return ResponseEntity.ok("Status do bloco com ID " + id + " alterado com sucesso");
    }

    @GetMapping("/status")
    public ResponseEntity<String> listarBlocosPorStatus(@RequestParam String status) {
        // Buscar blocos por status no serviço
        return ResponseEntity.ok("Lista de blocos com status: " + status);
    }

    // Classes DTO de exemplo (crie em um pacote separado, ex.: br.com.projetoapi.dto)
    public static class BlocoDTO {
        private String nome;
        private String descricao;
        // Adicione getters, setters e outros campos conforme necessário
        public String getNome() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getNome'");
        }
        public String getStatus() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getStatus'");
        }
    }

    public static class StatusDTO {
        private String status;
        // Adicione getters, setters
    }
}