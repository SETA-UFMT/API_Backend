package br.com.projetoApi.Entity.ArCondicionado.Controller;

import br.com.projetoApi.Entity.ArCondicionado.Dto.ArCondicionadoDTO;
import br.com.projetoApi.Entity.ArCondicionado.Dto.ArCondicionadoStatusDTO;
import br.com.projetoApi.Entity.ArCondicionado.Dto.ArCondicionadoTemperaturaDTO;
import br.com.projetoApi.Entity.ArCondicionado.Dto.ArCondicionadoVentilacaoDTO;
import br.com.projetoApi.Entity.ArCondicionado.Dto.ArCondicionadoModoDTO;
import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.StatusArCondicionado;
import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.VelocidadeVentilacao;
import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.ModoOperacao;
import br.com.projetoApi.Entity.ArCondicionado.Service.ArCondicionadoService;
import br.com.projetoApi.Entity.ArCondicionado.Service.ArCondicionadoService.ArCondicionadoNotFoundException;
import br.com.projetoApi.Entity.ArCondicionado.Service.ArCondicionadoService.ArCondicionadoAlreadyExistsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para gerenciamento de ar condicionados.
 */
@RestController
@RequestMapping("/api/ar-condicionados")
@Tag(name = "Ar Condicionados", description = "Endpoints para gerenciamento de ar condicionados")
public class ArCondicionadoController {

    @Autowired
    private ArCondicionadoService arCondicionadoService;

    @Operation(summary = "Listar todos os ar condicionados", description = "Retorna uma lista de todos os ar condicionados cadastrados, ordenados por nome da sala.")
    @ApiResponse(responseCode = "200", description = "Lista de ar condicionados retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ArCondicionadoDTO>> listarArCondicionados() {
        return ResponseEntity.ok(arCondicionadoService.listarArCondicionados());
    }

    @Operation(summary = "Obter ar condicionado por ID", description = "Retorna os detalhes de um ar condicionado específico pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ar condicionado encontrado"),
        @ApiResponse(responseCode = "404", description = "Ar condicionado não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obterArCondicionado(
            @Parameter(description = "ID do ar condicionado", required = true)
            @PathVariable Long id) {
        try {
            ArCondicionadoDTO arCondicionado = arCondicionadoService.obterArCondicionado(id);
            return ResponseEntity.ok(arCondicionado);
        } catch (ArCondicionadoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Criar novo ar condicionado", description = "Cria um novo ar condicionado com os dados fornecidos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ar condicionado criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou ar condicionado já existente na sala"),
        @ApiResponse(responseCode = "404", description = "Sala não encontrada")
    })
    @PostMapping
    public ResponseEntity<?> criarArCondicionado(
            @Valid @RequestBody ArCondicionadoDTO arCondicionadoDTO) {
        try {
            ArCondicionadoDTO novoArCondicionado = arCondicionadoService.criarArCondicionado(arCondicionadoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoArCondicionado);
        } catch (ArCondicionadoAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(e.getMessage()));
        } catch (ArCondicionadoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Atualizar ar condicionado", description = "Atualiza os dados de um ar condicionado existente pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ar condicionado atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Ar condicionado ou sala não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> editarArCondicionado(
            @Parameter(description = "ID do ar condicionado", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ArCondicionadoDTO arCondicionadoDTO) {
        try {
            ArCondicionadoDTO arCondicionadoAtualizado = arCondicionadoService.editarArCondicionado(id, arCondicionadoDTO);
            return ResponseEntity.ok(arCondicionadoAtualizado);
        } catch (ArCondicionadoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        } catch (ArCondicionadoAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Deletar ar condicionado", description = "Remove um ar condicionado do sistema pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ar condicionado deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Ar condicionado não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarArCondicionado(
            @Parameter(description = "ID do ar condicionado", required = true)
            @PathVariable Long id) {
        try {
            arCondicionadoService.deletarArCondicionado(id);
            return ResponseEntity.ok(createSuccessResponse("Ar condicionado deletado com sucesso"));
        } catch (ArCondicionadoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Alterar status do ar condicionado", description = "Atualiza o status de um ar condicionado (ex.: LIGADO, DESLIGADO, MANUTENCAO).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Ar condicionado não encontrado")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> alterarStatusArCondicionado(
            @Parameter(description = "ID do ar condicionado", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ArCondicionadoStatusDTO statusDTO) {
        try {
            ArCondicionadoDTO arCondicionadoAtualizado = arCondicionadoService.alterarStatusArCondicionado(id, statusDTO);
            return ResponseEntity.ok(arCondicionadoAtualizado);
        } catch (ArCondicionadoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Ligar ar condicionado", description = "Liga um ar condicionado específico pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ar condicionado ligado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Ar condicionado não encontrado")
    })
    @PatchMapping("/{id}/ligar")
    public ResponseEntity<?> ligarArCondicionado(
            @Parameter(description = "ID do ar condicionado", required = true)
            @PathVariable Long id) {
        try {
            ArCondicionadoDTO arCondicionadoAtualizado = arCondicionadoService.ligarArCondicionado(id);
            return ResponseEntity.ok(arCondicionadoAtualizado);
        } catch (ArCondicionadoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Desligar ar condicionado", description = "Desliga um ar condicionado específico pelo seu ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ar condicionado desligado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Ar condicionado não encontrado")
    })
    @PatchMapping("/{id}/desligar")
    public ResponseEntity<?> desligarArCondicionado(
            @Parameter(description = "ID do ar condicionado", required = true)
            @PathVariable Long id) {
        try {
            ArCondicionadoDTO arCondicionadoAtualizado = arCondicionadoService.desligarArCondicionado(id);
            return ResponseEntity.ok(arCondicionadoAtualizado);
        } catch (ArCondicionadoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Alterar temperatura", description = "Define uma nova temperatura para um ar condicionado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Temperatura alterada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Temperatura inválida"),
        @ApiResponse(responseCode = "404", description = "Ar condicionado não encontrado")
    })
    @PatchMapping("/{id}/temperatura")
    public ResponseEntity<?> alterarTemperatura(
            @Parameter(description = "ID do ar condicionado", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ArCondicionadoTemperaturaDTO temperaturaDTO) {
        try {
            ArCondicionadoDTO arCondicionadoAtualizado = arCondicionadoService.alterarTemperatura(id, temperaturaDTO);
            return ResponseEntity.ok(arCondicionadoAtualizado);
        } catch (ArCondicionadoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Aumentar temperatura", description = "Aumenta a temperatura de um ar condicionado em 1°C.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Temperatura aumentada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Ar condicionado não encontrado")
    })
    @PatchMapping("/{id}/temperatura/aumentar")
    public ResponseEntity<?> aumentarTemperatura(
            @Parameter(description = "ID do ar condicionado", required = true)
            @PathVariable Long id) {
        try {
            ArCondicionadoDTO arCondicionadoAtualizado = arCondicionadoService.aumentarTemperatura(id);
            return ResponseEntity.ok(arCondicionadoAtualizado);
        } catch (ArCondicionadoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Diminuir temperatura", description = "Diminui a temperatura de um ar condicionado em 1°C.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Temperatura diminuída com sucesso"),
        @ApiResponse(responseCode = "404", description = "Ar condicionado não encontrado")
    })
    @PatchMapping("/{id}/temperatura/diminuir")
    public ResponseEntity<?> diminuirTemperatura(
            @Parameter(description = "ID do ar condicionado", required = true)
            @PathVariable Long id) {
        try {
            ArCondicionadoDTO arCondicionadoAtualizado = arCondicionadoService.diminuirTemperatura(id);
            return ResponseEntity.ok(arCondicionadoAtualizado);
        } catch (ArCondicionadoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Alterar velocidade de ventilação", description = "Define uma nova velocidade de ventilação para um ar condicionado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Velocidade de ventilação alterada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Velocidade inválida"),
        @ApiResponse(responseCode = "404", description = "Ar condicionado não encontrado")
    })
    @PatchMapping("/{id}/ventilacao")
    public ResponseEntity<?> alterarVelocidadeVentilacao(
            @Parameter(description = "ID do ar condicionado", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ArCondicionadoVentilacaoDTO ventilacaoDTO) {
        try {
            ArCondicionadoDTO arCondicionadoAtualizado = arCondicionadoService.alterarVelocidadeVentilacao(id, ventilacaoDTO);
            return ResponseEntity.ok(arCondicionadoAtualizado);
        } catch (ArCondicionadoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Alterar modo de operação", description = "Define um novo modo de operação para um ar condicionado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Modo de operação alterado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Modo inválido"),
        @ApiResponse(responseCode = "404", description = "Ar condicionado não encontrado")
    })
    @PatchMapping("/{id}/modo")
    public ResponseEntity<?> alterarModoOperacao(
            @Parameter(description = "ID do ar condicionado", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ArCondicionadoModoDTO modoDTO) {
        try {
            ArCondicionadoDTO arCondicionadoAtualizado = arCondicionadoService.alterarModoOperacao(id, modoDTO);
            return ResponseEntity.ok(arCondicionadoAtualizado);
        } catch (ArCondicionadoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Desligar todos os ar condicionados do bloco", description = "Desliga todos os ar condicionados de um bloco específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ar condicionados desligados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum ar condicionado encontrado no bloco")
    })
    @PatchMapping("/bloco/{blocoId}/desligar-todos")
    public ResponseEntity<?> desligarTodosArCondicionadosDoBloco(
            @Parameter(description = "ID do bloco", required = true)
            @PathVariable Long blocoId) {
        try {
            List<ArCondicionadoDTO> arCondicionados = arCondicionadoService.desligarTodosArCondicionadosDoBloco(blocoId);
            return ResponseEntity.ok(arCondicionados);
        } catch (ArCondicionadoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Listar ar condicionados por status", description = "Retorna uma lista de ar condicionados filtrados por status.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ar condicionados retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Status inválido")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<?> listarArCondicionadosPorStatus(
            @Parameter(description = "Status do ar condicionado (ex.: LIGADO, DESLIGADO, MANUTENCAO)", required = true)
            @PathVariable StatusArCondicionado status) {
        try {
            List<ArCondicionadoDTO> arCondicionados = arCondicionadoService.listarArCondicionadosPorStatus(status);
            return ResponseEntity.ok(arCondicionados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Listar ar condicionados ligados", description = "Retorna uma lista de ar condicionados com status LIGADO.")
    @ApiResponse(responseCode = "200", description = "Lista de ar condicionados ligados retornada com sucesso")
    @GetMapping("/ligados")
    public ResponseEntity<List<ArCondicionadoDTO>> listarArCondicionadosLigados() {
        return ResponseEntity.ok(arCondicionadoService.listarArCondicionadosLigados());
    }

    @Operation(summary = "Listar ar condicionados em manutenção", description = "Retorna uma lista de ar condicionados com status MANUTENCAO.")
    @ApiResponse(responseCode = "200", description = "Lista de ar condicionados em manutenção retornada com sucesso")
    @GetMapping("/manutencao")
    public ResponseEntity<List<ArCondicionadoDTO>> listarArCondicionadosManutencao() {
        return ResponseEntity.ok(arCondicionadoService.listarArCondicionadosManutencao());
    }

    @Operation(summary = "Buscar ar condicionados com filtros", description = "Retorna uma lista de ar condicionados filtrados por múltiplos critérios (bloco, status, temperatura, ventilação, modo).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ar condicionados retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Filtros inválidos")
    })
    @GetMapping("/filtros")
    public ResponseEntity<?> buscarArCondicionadosComFiltros(
            @Parameter(description = "ID do bloco (opcional)")
            @RequestParam(required = false) Long blocoId,
            @Parameter(description = "Status do ar condicionado (opcional)")
            @RequestParam(required = false) StatusArCondicionado status,
            @Parameter(description = "Temperatura mínima (opcional)")
            @RequestParam(required = false) Integer temperaturaMin,
            @Parameter(description = "Temperatura máxima (opcional)")
            @RequestParam(required = false) Integer temperaturaMax,
            @Parameter(description = "Velocidade de ventilação (opcional)")
            @RequestParam(required = false) VelocidadeVentilacao velocidade,
            @Parameter(description = "Modo de operação (opcional)")
            @RequestParam(required = false) ModoOperacao modo) {
        try {
            List<ArCondicionadoDTO> arCondicionados = arCondicionadoService.buscarArCondicionadosComFiltros(
                    blocoId, status, temperaturaMin, temperaturaMax, velocidade, modo);
            return ResponseEntity.ok(arCondicionados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * Cria uma resposta de erro padronizada.
     * @param message Mensagem de erro.
     * @return Mapa com a mensagem de erro.
     */
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }

    /**
     * Cria uma resposta de sucesso padronizada.
     * @param message Mensagem de sucesso.
     * @return Mapa com a mensagem de sucesso.
     */
    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }
}