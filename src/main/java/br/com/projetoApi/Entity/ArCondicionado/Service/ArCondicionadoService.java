package br.com.projetoApi.Entity.ArCondicionado.Service;

import br.com.projetoApi.Entity.ArCondicionado.Dto.ArCondicionadoDTO;
import br.com.projetoApi.Entity.ArCondicionado.Dto.ArCondicionadoStatusDTO;
import br.com.projetoApi.Entity.ArCondicionado.Dto.ArCondicionadoTemperaturaDTO;
import br.com.projetoApi.Entity.ArCondicionado.Dto.ArCondicionadoVentilacaoDTO;
import br.com.projetoApi.Entity.ArCondicionado.Dto.ArCondicionadoModoDTO;
import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado;
import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.StatusArCondicionado;
import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.VelocidadeVentilacao;
import br.com.projetoApi.Entity.ArCondicionado.Model.ArCondicionado.ModoOperacao;
import br.com.projetoApi.Entity.ArCondicionado.Repository.ArCondicionadoRepository;
import br.com.projetoApi.Entity.Sala.Model.Sala;
import br.com.projetoApi.Entity.Sala.Repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciamento de aparelhos de ar condicionado.
 */
@Service
@Transactional
public class ArCondicionadoService {

    private static final int TEMPERATURA_MINIMA = 16;
    private static final int TEMPERATURA_MAXIMA = 30;

    @Autowired
    private ArCondicionadoRepository arCondicionadoRepository;

    @Autowired
    private SalaRepository salaRepository;

    /**
     * Cria um novo aparelho de ar condicionado associado a uma sala.
     * @param arCondicionadoDTO DTO com os dados do ar condicionado.
     * @return DTO do ar condicionado criado.
     * @throws ArCondicionadoAlreadyExistsException Se já existir um ar condicionado na sala.
     * @throws ArCondicionadoNotFoundException Se a sala não for encontrada.
     */
    @Transactional
    public ArCondicionadoDTO criarArCondicionado(ArCondicionadoDTO arCondicionadoDTO) {
        validateNotNull(arCondicionadoDTO, "DTO não pode ser nulo.");
        validateNotNull(arCondicionadoDTO.getSalaId(), "ID da sala não pode ser nulo.");

        Sala sala = salaRepository.findById(arCondicionadoDTO.getSalaId())
                .orElseThrow(() -> new ArCondicionadoNotFoundException("Sala não encontrada com ID: " + arCondicionadoDTO.getSalaId()));

        if (arCondicionadoRepository.existsBySalaId(arCondicionadoDTO.getSalaId())) {
            throw new ArCondicionadoAlreadyExistsException("Já existe um ar condicionado cadastrado para a sala: " + sala.getNome());
        }

        ArCondicionado arCondicionado = new ArCondicionado();
        arCondicionado.setSala(sala);
        arCondicionado.setStatus(arCondicionadoDTO.getStatus() != null ? arCondicionadoDTO.getStatus() : StatusArCondicionado.DESLIGADO);
        arCondicionado.setTemperatura(arCondicionadoDTO.getTemperatura() != null ? arCondicionadoDTO.getTemperatura() : 24);
        arCondicionado.setVelocidadeVentilacao(arCondicionadoDTO.getVelocidadeVentilacao() != null ? arCondicionadoDTO.getVelocidadeVentilacao() : VelocidadeVentilacao.MEDIA);
        arCondicionado.setModoOperacao(arCondicionadoDTO.getModoOperacao() != null ? arCondicionadoDTO.getModoOperacao() : ModoOperacao.REFRIGERAR);

        return convertToDTO(arCondicionadoRepository.save(arCondicionado));
    }

    /**
     * Lista todos os aparelhos de ar condicionado, ordenados por nome da sala.
     * @return Lista de DTOs dos aparelhos.
     */
    @Transactional(readOnly = true)
    public List<ArCondicionadoDTO> listarArCondicionados() {
        return arCondicionadoRepository.findAllOrderBySalaNome()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtém um aparelho de ar condicionado pelo ID.
     * @param id ID do ar condicionado.
     * @return DTO do ar condicionado.
     * @throws ArCondicionadoNotFoundException Se o ar condicionado não for encontrado.
     */
    @Transactional(readOnly = true)
    public ArCondicionadoDTO obterArCondicionado(Long id) {
        validateNotNull(id, "ID não pode ser nulo.");
        ArCondicionado arCondicionado = arCondicionadoRepository.findById(id)
                .orElseThrow(() -> new ArCondicionadoNotFoundException("Ar condicionado não encontrado com ID: " + id));
        return convertToDTO(arCondicionado);
    }

    /**
     * Edita um aparelho de ar condicionado existente.
     * @param id ID do ar condicionado.
     * @param arCondicionadoDTO DTO com os novos dados.
     * @return DTO do ar condicionado atualizado.
     * @throws ArCondicionadoNotFoundException Se o ar condicionado ou a nova sala não forem encontrados.
     * @throws ArCondicionadoAlreadyExistsException Se a nova sala já tiver um ar condicionado.
     */
    @Transactional
    public ArCondicionadoDTO editarArCondicionado(Long id, ArCondicionadoDTO arCondicionadoDTO) {
        validateNotNull(id, "ID não pode ser nulo.");
        validateNotNull(arCondicionadoDTO, "DTO não pode ser nulo.");
        validateNotNull(arCondicionadoDTO.getSalaId(), "ID da sala não pode ser nulo.");

        ArCondicionado arCondicionado = arCondicionadoRepository.findById(id)
                .orElseThrow(() -> new ArCondicionadoNotFoundException("Ar condicionado não encontrado com ID: " + id));

        if (!arCondicionado.getSala().getId().equals(arCondicionadoDTO.getSalaId())) {
            Sala novaSala = salaRepository.findById(arCondicionadoDTO.getSalaId())
                    .orElseThrow(() -> new ArCondicionadoNotFoundException("Sala não encontrada com ID: " + arCondicionadoDTO.getSalaId()));
            if (arCondicionadoRepository.existsBySalaId(arCondicionadoDTO.getSalaId())) {
                throw new ArCondicionadoAlreadyExistsException("Já existe um ar condicionado cadastrado para a sala: " + novaSala.getNome());
            }
            arCondicionado.setSala(novaSala);
        }

        if (arCondicionadoDTO.getStatus() != null) {
            arCondicionado.setStatus(arCondicionadoDTO.getStatus());
        }
        if (arCondicionadoDTO.getTemperatura() != null) {
            validateTemperatura(arCondicionadoDTO.getTemperatura());
            arCondicionado.setTemperatura(arCondicionadoDTO.getTemperatura());
        }
        if (arCondicionadoDTO.getVelocidadeVentilacao() != null) {
            arCondicionado.setVelocidadeVentilacao(arCondicionadoDTO.getVelocidadeVentilacao());
        }
        if (arCondicionadoDTO.getModoOperacao() != null) {
            arCondicionado.setModoOperacao(arCondicionadoDTO.getModoOperacao());
        }

        return convertToDTO(arCondicionadoRepository.save(arCondicionado));
    }

    /**
     * Deleta um aparelho de ar condicionado pelo ID.
     * @param id ID do ar condicionado.
     * @throws ArCondicionadoNotFoundException Se o ar condicionado não for encontrado.
     */
    @Transactional
    public void deletarArCondicionado(Long id) {
        validateNotNull(id, "ID não pode ser nulo.");
        if (!arCondicionadoRepository.existsById(id)) {
            throw new ArCondicionadoNotFoundException("Ar condicionado não encontrado com ID: " + id);
        }
        arCondicionadoRepository.deleteById(id);
    }

    /**
     * Altera o status de um aparelho de ar condicionado.
     * @param id ID do ar condicionado.
     * @param statusDTO DTO com o novo status.
     * @return DTO do ar condicionado atualizado.
     * @throws ArCondicionadoNotFoundException Se o ar condicionado não for encontrado.
     */
    @Transactional
    public ArCondicionadoDTO alterarStatusArCondicionado(Long id, ArCondicionadoStatusDTO statusDTO) {
        validateNotNull(id, "ID não pode ser nulo.");
        validateNotNull(statusDTO, "Status DTO não pode ser nulo.");
        validateNotNull(statusDTO.getStatus(), "Status não pode ser nulo.");

        ArCondicionado arCondicionado = arCondicionadoRepository.findById(id)
                .orElseThrow(() -> new ArCondicionadoNotFoundException("Ar condicionado não encontrado com ID: " + id));

        arCondicionado.setStatus(statusDTO.getStatus());
        return convertToDTO(arCondicionadoRepository.save(arCondicionado));
    }

    /**
     * Liga um aparelho de ar condicionado.
     * @param id ID do ar condicionado.
     * @return DTO do ar condicionado atualizado.
     * @throws ArCondicionadoNotFoundException Se o ar condicionado não for encontrado.
     */
    @Transactional
    public ArCondicionadoDTO ligarArCondicionado(Long id) {
        validateNotNull(id, "ID não pode ser nulo.");
        ArCondicionado arCondicionado = arCondicionadoRepository.findById(id)
                .orElseThrow(() -> new ArCondicionadoNotFoundException("Ar condicionado não encontrado com ID: " + id));

        arCondicionado.setStatus(StatusArCondicionado.LIGADO);
        return convertToDTO(arCondicionadoRepository.save(arCondicionado));
    }

    /**
     * Desliga um aparelho de ar condicionado.
     * @param id ID do ar condicionado.
     * @return DTO do ar condicionado atualizado.
     * @throws ArCondicionadoNotFoundException Se o ar condicionado não for encontrado.
     */
    @Transactional
    public ArCondicionadoDTO desligarArCondicionado(Long id) {
        validateNotNull(id, "ID não pode ser nulo.");
        ArCondicionado arCondicionado = arCondicionadoRepository.findById(id)
                .orElseThrow(() -> new ArCondicionadoNotFoundException("Ar condicionado não encontrado com ID: " + id));

        arCondicionado.setStatus(StatusArCondicionado.DESLIGADO);
        return convertToDTO(arCondicionadoRepository.save(arCondicionado));
    }

    /**
     * Altera a temperatura de um aparelho de ar condicionado.
     * @param id ID do ar condicionado.
     * @param temperaturaDTO DTO com a nova temperatura.
     * @return DTO do ar condicionado atualizado.
     * @throws ArCondicionadoNotFoundException Se o ar condicionado não for encontrado.
     * @throws IllegalArgumentException Se a temperatura for inválida.
     */
    @Transactional
    public ArCondicionadoDTO alterarTemperatura(Long id, ArCondicionadoTemperaturaDTO temperaturaDTO) {
        validateNotNull(id, "ID não pode ser nulo.");
        validateNotNull(temperaturaDTO, "Temperatura DTO não pode ser nulo.");
        validateNotNull(temperaturaDTO.getTemperatura(), "Temperatura não pode ser nula.");
        validateTemperatura(temperaturaDTO.getTemperatura());

        ArCondicionado arCondicionado = arCondicionadoRepository.findById(id)
                .orElseThrow(() -> new ArCondicionadoNotFoundException("Ar condicionado não encontrado com ID: " + id));

        arCondicionado.setTemperatura(temperaturaDTO.getTemperatura());
        return convertToDTO(arCondicionadoRepository.save(arCondicionado));
    }

    /**
     * Aumenta a temperatura de um aparelho de ar condicionado em 1 grau, até o limite de 30°C.
     * @param id ID do ar condicionado.
     * @return DTO do ar condicionado atualizado.
     * @throws ArCondicionadoNotFoundException Se o ar condicionado não for encontrado.
     */
    @Transactional
    public ArCondicionadoDTO aumentarTemperatura(Long id) {
        validateNotNull(id, "ID não pode ser nulo.");
        ArCondicionado arCondicionado = arCondicionadoRepository.findById(id)
                .orElseThrow(() -> new ArCondicionadoNotFoundException("Ar condicionado não encontrado com ID: " + id));

        int novaTemperatura = Math.min(arCondicionado.getTemperatura() + 1, TEMPERATURA_MAXIMA);
        arCondicionado.setTemperatura(novaTemperatura);
        return convertToDTO(arCondicionadoRepository.save(arCondicionado));
    }

    /**
     * Diminui a temperatura de um aparelho de ar condicionado em 1 grau, até o limite de 16°C.
     * @param id ID do ar condicionado.
     * @return DTO do ar condicionado atualizado.
     * @throws ArCondicionadoNotFoundException Se o ar condicionado não for encontrado.
     */
    @Transactional
    public ArCondicionadoDTO diminuirTemperatura(Long id) {
        validateNotNull(id, "ID não pode ser nulo.");
        ArCondicionado arCondicionado = arCondicionadoRepository.findById(id)
                .orElseThrow(() -> new ArCondicionadoNotFoundException("Ar condicionado não encontrado com ID: " + id));

        int novaTemperatura = Math.max(arCondicionado.getTemperatura() - 1, TEMPERATURA_MINIMA);
        arCondicionado.setTemperatura(novaTemperatura);
        return convertToDTO(arCondicionadoRepository.save(arCondicionado));
    }

    /**
     * Altera a velocidade de ventilação de um aparelho de ar condicionado.
     * @param id ID do ar condicionado.
     * @param ventilacaoDTO DTO com a nova velocidade de ventilação.
     * @return DTO do ar condicionado atualizado.
     * @throws ArCondicionadoNotFoundException Se o ar condicionado não for encontrado.
     */
    @Transactional
    public ArCondicionadoDTO alterarVelocidadeVentilacao(Long id, ArCondicionadoVentilacaoDTO ventilacaoDTO) {
        validateNotNull(id, "ID não pode ser nulo.");
        validateNotNull(ventilacaoDTO, "Ventilação DTO não pode ser nulo.");
        validateNotNull(ventilacaoDTO.getVelocidadeVentilacao(), "Velocidade de ventilação não pode ser nula.");

        ArCondicionado arCondicionado = arCondicionadoRepository.findById(id)
                .orElseThrow(() -> new ArCondicionadoNotFoundException("Ar condicionado não encontrado com ID: " + id));

        arCondicionado.setVelocidadeVentilacao(ventilacaoDTO.getVelocidadeVentilacao());
        return convertToDTO(arCondicionadoRepository.save(arCondicionado));
    }

    /**
     * Altera o modo de operação de um aparelho de ar condicionado.
     * @param id ID do ar condicionado.
     * @param modoDTO DTO com o novo modo de operação.
     * @return DTO do ar condicionado atualizado.
     * @throws ArCondicionadoNotFoundException Se o ar condicionado não for encontrado.
     */
    @Transactional
    public ArCondicionadoDTO alterarModoOperacao(Long id, ArCondicionadoModoDTO modoDTO) {
        validateNotNull(id, "ID não pode ser nulo.");
        validateNotNull(modoDTO, "Modo DTO não pode ser nulo.");
        validateNotNull(modoDTO.getModoOperacao(), "Modo de operação não pode ser nulo.");

        ArCondicionado arCondicionado = arCondicionadoRepository.findById(id)
                .orElseThrow(() -> new ArCondicionadoNotFoundException("Ar condicionado não encontrado com ID: " + id));

        arCondicionado.setModoOperacao(modoDTO.getModoOperacao());
        return convertToDTO(arCondicionadoRepository.save(arCondicionado));
    }

    /**
     * Desliga todos os aparelhos de ar condicionado de um bloco.
     * @param blocoId ID do bloco.
     * @return Lista de DTOs dos aparelhos atualizados.
     * @throws ArCondicionadoNotFoundException Se não houver aparelhos no bloco.
     */
    @Transactional
    public List<ArCondicionadoDTO> desligarTodosArCondicionadosDoBloco(Long blocoId) {
        validateNotNull(blocoId, "ID do bloco não pode ser nulo.");
        List<ArCondicionado> arCondicionados = arCondicionadoRepository.findByBlocoId(blocoId);
        if (arCondicionados.isEmpty()) {
            throw new ArCondicionadoNotFoundException("Nenhum ar condicionado encontrado no bloco com ID: " + blocoId);
        }

        arCondicionados.forEach(ac -> ac.setStatus(StatusArCondicionado.DESLIGADO));
        arCondicionadoRepository.saveAll(arCondicionados);
        return arCondicionados.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista aparelhos de ar condicionado por status.
     * @param status Status desejado.
     * @return Lista de DTOs dos aparelhos.
     */
    @Transactional(readOnly = true)
    public List<ArCondicionadoDTO> listarArCondicionadosPorStatus(StatusArCondicionado status) {
        validateNotNull(status, "Status não pode ser nulo.");
        return arCondicionadoRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista aparelhos de ar condicionado ligados.
     * @return Lista de DTOs dos aparelhos ligados.
     */
    @Transactional(readOnly = true)
    public List<ArCondicionadoDTO> listarArCondicionadosLigados() {
        return arCondicionadoRepository.findArCondicionadosLigados()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista aparelhos de ar condicionado em manutenção.
     * @return Lista de DTOs dos aparelhos.
     */
    @Transactional(readOnly = true)
    public List<ArCondicionadoDTO> listarArCondicionadosManutencao() {
        return arCondicionadoRepository.findArCondicionadosManutencao()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca aparelhos de ar condicionado com filtros opcionais.
     * @param blocoId ID do bloco (opcional).
     * @param status Status do ar condicionado (opcional).
     * @param temperaturaMin Temperatura mínima (opcional).
     * @param temperaturaMax Temperatura máxima (opcional).
     * @param velocidade Velocidade de ventilação (opcional).
     * @param modo Modo de operação (opcional).
     * @return Lista de DTOs dos aparelhos filtrados.
     */
    @Transactional(readOnly = true)
    public List<ArCondicionadoDTO> buscarArCondicionadosComFiltros(Long blocoId, StatusArCondicionado status,
                                                                  Integer temperaturaMin, Integer temperaturaMax,
                                                                  VelocidadeVentilacao velocidade, ModoOperacao modo) {
        if (temperaturaMin != null) {
            validateTemperatura(temperaturaMin, "A temperatura mínima deve estar entre 16 e 30 graus.");
        }
        if (temperaturaMax != null) {
            validateTemperatura(temperaturaMax, "A temperatura máxima deve estar entre 16 e 30 graus.");
        }
        if (temperaturaMin != null && temperaturaMax != null && temperaturaMin > temperaturaMax) {
            throw new IllegalArgumentException("A temperatura mínima não pode ser maior que a máxima.");
        }
        return arCondicionadoRepository.findArCondicionadosComFiltros(blocoId, status, temperaturaMin, temperaturaMax, velocidade, modo)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converte um objeto ArCondicionado em ArCondicionadoDTO.
     * @param arCondicionado Objeto a ser convertido.
     * @return DTO correspondente.
     */
    private ArCondicionadoDTO convertToDTO(ArCondicionado arCondicionado) {
        ArCondicionadoDTO dto = new ArCondicionadoDTO();
        dto.setId(arCondicionado.getId());
        dto.setSalaId(arCondicionado.getSala().getId());
        dto.setSalaNome(arCondicionado.getSala().getNome());
        dto.setBlocoNome(arCondicionado.getSala().getBloco().getNome());
        dto.setStatus(arCondicionado.getStatus());
        dto.setTemperatura(arCondicionado.getTemperatura());
        dto.setVelocidadeVentilacao(arCondicionado.getVelocidadeVentilacao());
        dto.setModoOperacao(arCondicionado.getModoOperacao());
        dto.setCreatedAt(arCondicionado.getCreatedAt());
        dto.setUpdatedAt(arCondicionado.getUpdatedAt());
        return dto;
    }

    /**
     * Valida se um objeto não é nulo, lançando uma exceção se for.
     * @param obj Objeto a ser validado.
     * @param mensagem Mensagem de erro.
     */
    private void validateNotNull(Object obj, String mensagem) {
        if (obj == null) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    /**
     * Valida se a temperatura está dentro do intervalo permitido.
     * @param temperatura Temperatura a ser validada.
     */
    private void validateTemperatura(Integer temperatura) {
        validateTemperatura(temperatura, "A temperatura deve estar entre 16 e 30 graus.");
    }

    /**
     * Valida se a temperatura está dentro do intervalo permitido, com mensagem personalizada.
     * @param temperatura Temperatura a ser validada.
     * @param mensagem Mensagem de erro.
     */
    private void validateTemperatura(Integer temperatura, String mensagem) {
        if (temperatura < TEMPERATURA_MINIMA || temperatura > TEMPERATURA_MAXIMA) {
            throw new IllegalArgumentException(mensagem);
        }
    }

    /**
     * Exceção lançada quando um ar condicionado já existe em uma sala.
     */
    public static class ArCondicionadoAlreadyExistsException extends RuntimeException {
        public ArCondicionadoAlreadyExistsException(String message) {
            super(message);
        }
    }

    /**
     * Exceção lançada quando um ar condicionado ou sala não é encontrado.
     */
    public static class ArCondicionadoNotFoundException extends RuntimeException {
        public ArCondicionadoNotFoundException(String message) {
            super(message);
        }
    }
}