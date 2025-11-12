package br.com.projetoApi.Entity.Tranca.Repository;

import br.com.projetoApi.Entity.Tranca.Model.Tranca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrancaRepository extends JpaRepository<Tranca, Long> {
    // O JpaRepository já fornece métodos como findById(), findAll(), save(), etc.
}