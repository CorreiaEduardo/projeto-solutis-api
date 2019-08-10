package br.com.solutis.treinamento.apirest.repository;

import br.com.solutis.treinamento.apirest.model.Conta;
import br.com.solutis.treinamento.apirest.model.enums.TipoConta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContaRepository extends JpaRepository<Conta,Long> {

    List<Conta> findByTipo(TipoConta tipo);
}
