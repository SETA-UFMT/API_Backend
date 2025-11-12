package br.com.projetoApi.Entity.Tranca.Service;

import br.com.projetoApi.Entity.Tranca.Dto.AcaoTranca;
import br.com.projetoApi.Entity.Tranca.Dto.TrancaAcaoDTO;
import br.com.projetoApi.Entity.Tranca.Dto.TrancaDTO;
import br.com.projetoApi.Entity.Tranca.Model.Tranca;
import br.com.projetoApi.Entity.Tranca.Model.Tranca.StatusTranca;
import br.com.projetoApi.Entity.Tranca.Repository.TrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrancaService {

    @Autowired
    private TrancaRepository trancaRepository;

    // Monitoramento: Buscar uma tranca por ID
    @Transactional(readOnly = true)
    public TrancaDTO buscarPorId(Long id) {
        Tranca tranca = trancaRepository.findById(id)
                .orElseThrow(() -> new TrancaNotFoundException("Tranca não encontrada com ID: " + id));
        return new TrancaDTO(tranca);
    }

    // Monitoramento: Listar todas as trancas
    @Transactional(readOnly = true)
    public List<TrancaDTO> listarTodas() {
        return trancaRepository.findAll().stream()
                .map(TrancaDTO::new)
                .collect(Collectors.toList());
    }

    // Controle: Abrir ou Fechar a tranca
    @Transactional
    public TrancaDTO controlarTranca(Long id, TrancaAcaoDTO acaoDTO) {
        Tranca tranca = trancaRepository.findById(id)
                .orElseThrow(() -> new TrancaNotFoundException("Tranca não encontrada com ID: " + id));

        // Não permite ação se a tranca estiver com defeito
        if (tranca.getStatus() == StatusTranca.COM_DEFEITO) {
            throw new IllegalStateException("A tranca está com defeito e não pode ser operada.");
        }

        if (acaoDTO.getAcao() == AcaoTranca.ABRIR) {
            if (tranca.getStatus() == StatusTranca.FECHADA) {
                tranca.setStatus(StatusTranca.ABERTA);
            }
        } else if (acaoDTO.getAcao() == AcaoTranca.FECHAR) {
            if (tranca.getStatus() == StatusTranca.ABERTA) {
                tranca.setStatus(StatusTranca.FECHADA);
            }
        }

        Tranca trancaSalva = trancaRepository.save(tranca);
        return new TrancaDTO(trancaSalva);
    }
}