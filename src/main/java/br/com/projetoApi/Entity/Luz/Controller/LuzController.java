package br.com.projetoApi.Entity.Luz.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import br.com.projetoApi.Entity.Luz.Dto.LuzDTO;
import br.com.projetoApi.Entity.Luz.Dto.LuzStatusDTO;
import br.com.projetoApi.Entity.Luz.Model.Luz.StatusLuz;
import br.com.projetoApi.Entity.Luz.Service.LuzService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/luzes")
@Tag(name = "Luzes", description = "Endpoints para gerenciamento de luzes")
public class LuzController {

    @Autowired
    private LuzService luzService;

    @Operation(summary = "Listar todas as luzes", description = "Retorna uma lista de todas as luzes cadastradas ordenadas por sala")
    @ApiResponse(responseCode = "200", description = "Lista de luzes retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<LuzDTO>> listarLuzes() {
        List<LuzDTO> luzes = luzService.listarLuzes();
        return ResponseEntity.ok(luzes);
    }

    @Operation(summary = "Obter luz por ID", description = "Retorna os detalhes de uma luz específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Luz encontrada"),
        @ApiResponse(responseCode = "404", description = "Luz não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obterLuz(
            @Parameter(description = "ID da luz", required = true)
            @PathVariable Long id) {
        try {
            LuzDTO luz = luzService.obterLuz(id);
            return ResponseEntity.ok(luz);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @Operation(summary = "Criar nova luz", description = "Cria uma nova luz com os dados fornecidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Luz criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou luz já existente na sala"),
        @ApiResponse(responseCode = "404", description = "Sala não encontrada")
    })
    @PostMapping
    public ResponseEntity<?> criarLuz(@Valid @RequestBody LuzDTO luzDTO) {
        try {
            LuzDTO novaLuz = luzService.criarLuz(luzDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaLuz);
        } catch (LuzService.LuzAlreadyExistsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @Operation(summary = "Atualizar luz", description = "Atualiza os dados de uma luz existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Luz atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Luz ou sala não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> editarLuz(
            @Parameter(description = "ID da luz", required = true)
            @PathVariable Long id, 
            @Valid @RequestBody LuzDTO luzDTO) {
        try {
            LuzDTO luzAtualizada = luzService.editarLuz(id, luzDTO);
            return ResponseEntity.ok(luzAtualizada);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (LuzService.LuzAlreadyExistsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @Operation(summary = "Deletar luz", description = "Remove uma luz do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Luz deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Luz não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarLuz(
            @Parameter(description = "ID da luz", required = true)
            @PathVariable Long id) {
        try {
            luzService.deletarLuz(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Luz deletada com sucesso");
            return ResponseEntity.ok(response);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @Operation(summary = "Alterar status da luz", description = "Atualiza apenas o status de uma luz")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Luz não encontrada")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> alterarStatusLuz(
            @Parameter(description = "ID da luz", required = true)
            @PathVariable Long id, 
            @Valid @RequestBody LuzStatusDTO statusDTO) {
        try {
            LuzDTO luzAtualizada = luzService.alterarStatusLuz(id, statusDTO);
            return ResponseEntity.ok(luzAtualizada);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @Operation(summary = "Ligar luz", description = "Liga uma luz específica")
    @PatchMapping("/{id}/ligar")
    public ResponseEntity<?> ligarLuz(
            @Parameter(description = "ID da luz", required = true)
            @PathVariable Long id) {
        try {
            LuzDTO luzAtualizada = luzService.ligarLuz(id);
            return ResponseEntity.ok(luzAtualizada);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @Operation(summary = "Desligar luz", description = "Desliga uma luz específica")
    @PatchMapping("/{id}/desligar")
    public ResponseEntity<?> desligarLuz(
            @Parameter(description = "ID da luz", required = true)
            @PathVariable Long id) {
        try {
            LuzDTO luzAtualizada = luzService.desligarLuz(id);
            return ResponseEntity.ok(luzAtualizada);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @Operation(summary = "Alternar status da luz", description = "Alterna o status da luz (ligado/desligado)")
    @PatchMapping("/{id}/alternar")
    public ResponseEntity<?> alternarStatusLuz(
            @Parameter(description = "ID da luz", required = true)
            @PathVariable Long id) {
        try {
            LuzDTO luzAtualizada = luzService.alternarStatusLuz(id);
            return ResponseEntity.ok(luzAtualizada);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @Operation(summary = "Listar luzes por status", description = "Retorna uma lista de luzes filtradas por status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LuzDTO>> listarLuzesPorStatus(
            @Parameter(description = "Status da luz", required = true)
            @PathVariable StatusLuz status) {
        List<LuzDTO> luzes = luzService.listarLuzesPorStatus(status);
        return ResponseEntity.ok(luzes);
    }

    @Operation(summary = "Listar luzes ligadas", description = "Retorna uma lista de luzes com status LIGADO")
    @GetMapping("/ligadas")
    public ResponseEntity<List<LuzDTO>> listarLuzesLigadas() {
        List<LuzDTO> luzes = luzService.listarLuzesLigadas();
        return ResponseEntity.ok(luzes);
    }

    @Operation(summary = "Listar luzes desligadas", description = "Retorna uma lista de luzes com status DESLIGADO")
    @GetMapping("/desligadas")
    public ResponseEntity<List<LuzDTO>> listarLuzesDesligadas() {
        List<LuzDTO> luzes = luzService.listarLuzesDesligadas();
        return ResponseEntity.ok(luzes);
    }

    @Operation(summary = "Listar luzes por bloco", description = "Retorna uma lista de luzes de um bloco específico")
    @GetMapping("/bloco/{blocoId}")
    public ResponseEntity<List<LuzDTO>> listarLuzesPorBloco(
            @Parameter(description = "ID do bloco", required = true)
            @PathVariable Long blocoId) {
        List<LuzDTO> luzes = luzService.listarLuzesPorBloco(blocoId);
        return ResponseEntity.ok(luzes);
    }

    @Operation(summary = "Listar luzes por sala", description = "Retorna uma lista de luzes de uma sala específica")
    @GetMapping("/sala/{salaId}/todas")
    public ResponseEntity<List<LuzDTO>> listarLuzesPorSala(
            @Parameter(description = "ID da sala", required = true)
            @PathVariable Long salaId) {
        List<LuzDTO> luzes = luzService.listarLuzesPorSala(salaId);
        return ResponseEntity.ok(luzes);
    }

    @Operation(summary = "Contar luzes ligadas por bloco", description = "Retorna o número de luzes ligadas em um bloco específico")
    @GetMapping("/bloco/{blocoId}/ligadas/contar")
    public ResponseEntity<Map<String, Long>> contarLuzesLigadasPorBloco(
            @Parameter(description = "ID do bloco", required = true)
            @PathVariable Long blocoId) {
        Long count = luzService.contarLuzesLigadasPorBloco(blocoId);
        Map<String, Long> response = new HashMap<>();
        response.put("luzesLigadas", count);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Contar luzes ligadas por sala", description = "Retorna o número de luzes ligadas em uma sala específica")
    @GetMapping("/sala/{salaId}/ligadas/contar")
    public ResponseEntity<Map<String, Long>> contarLuzesLigadasPorSala(
            @Parameter(description = "ID da sala", required = true)
            @PathVariable Long salaId) {
        Long count = luzService.contarLuzesLigadasPorSala(salaId);
        Map<String, Long> response = new HashMap<>();
        response.put("luzesLigadas", count);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Ligar todas as luzes do bloco", description = "Liga todas as luzes de um bloco específico")
    @PatchMapping("/bloco/{blocoId}/ligar-todas")
    public ResponseEntity<List<LuzDTO>> ligarTodasLuzesDoBloco(
            @Parameter(description = "ID do bloco", required = true)
            @PathVariable Long blocoId) {
        List<LuzDTO> luzes = luzService.ligarTodasLuzesDoBloco(blocoId);
        return ResponseEntity.ok(luzes);
    }

    @Operation(summary = "Desligar todas as luzes do bloco", description = "Desliga todas as luzes de um bloco específico")
    @PatchMapping("/bloco/{blocoId}/desligar-todas")
    public ResponseEntity<List<LuzDTO>> desligarTodasLuzesDoBloco(
            @Parameter(description = "ID do bloco", required = true)
            @PathVariable Long blocoId) {
        List<LuzDTO> luzes = luzService.desligarTodasLuzesDoBloco(blocoId);
        return ResponseEntity.ok(luzes);
    }
}