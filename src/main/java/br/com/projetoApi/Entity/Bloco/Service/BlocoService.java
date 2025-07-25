package br.com.projetoApi.Entity.Bloco.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projetoApi.Entity.Bloco.Controller.BlocoController.BlocoDTO;
import br.com.projetoApi.Entity.Bloco.Model.Bloco;
import br.com.projetoApi.Entity.Bloco.Repository.BlocoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlocoService {

    @Autowired
    private BlocoRepository blocoRepository;

    public BlocoDTO criarBloco(BlocoDTO blocoDTO) {
        if (blocoRepository.existsByNome(blocoDTO.getNome())) {
            throw new BlocoAlreadyExistsException("Bloco já existe: " + blocoDTO.getNome());
        }
        Bloco bloco = new Bloco();
        bloco.setNome(blocoDTO.getNome());
        bloco.setStatus(blocoDTO.getStatus());
        bloco = blocoRepository.save(bloco);
        return convertToDTO(bloco);
    }

    public List<BlocoDTO> listarBlocos() {
        return blocoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BlocoDTO obterBloco(Long id) {
        Bloco bloco = blocoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bloco não encontrado: " + id));
        return convertToDTO(bloco);
    }

    public BlocoDTO editarBloco(Long id, BlocoDTO blocoDTO) {
        Bloco bloco = blocoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bloco não encontrado: " + id));
        if (!bloco.getNome().equals(blocoDTO.getNome()) && blocoRepository.existsByNome(blocoDTO.getNome())) {
            throw new BlocoAlreadyExistsException("Bloco já existe: " + blocoDTO.getNome());
        }
        bloco.setNome(blocoDTO.getNome());
        bloco.setStatus(blocoDTO.getStatus());
        bloco = blocoRepository.save(bloco);
        return convertToDTO(bloco);
    }

    public void deletarBloco(Long id) {
        if (!blocoRepository.existsById(id)) {
            throw new EntityNotFoundException("Bloco não encontrado: " + id);
        }
        blocoRepository.deleteById(id);
    }

    public BlocoDTO alterarStatusBloco(Long id, StatusDTO statusDTO) {
        Bloco bloco = blocoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bloco não encontrado: " + id));
        bloco.setStatus(statusDTO.getStatus());
        bloco = blocoRepository.save(bloco);
        return convertToDTO(bloco);
    }

    public List<BlocoDTO> listarBlocosPorStatus(String status) {
        return blocoRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BlocoDTO convertToDTO(Bloco bloco) {
        BlocoDTO dto = new BlocoDTO();
        dto.setNome(bloco.getNome());
        dto.setStatus(bloco.getStatus());
        // Adicione outros campos conforme necessário
        return dto;
    }

    // Exceção personalizada
    public static class BlocoAlreadyExistsException extends RuntimeException {
        public BlocoAlreadyExistsException(String message) {
            super(message);
        }
    }
}