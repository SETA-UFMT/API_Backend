package br.com.projetoApi.Entity.Tranca.Service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Exceção customizada para quando a tranca não for encontrada
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TrancaNotFoundException extends RuntimeException {
    public TrancaNotFoundException(String message) {
        super(message);
    }
}