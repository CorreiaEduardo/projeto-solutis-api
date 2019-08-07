package br.com.solutis.treinamento.apirest.repository;

import br.com.solutis.treinamento.apirest.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<Conta,Long> {
}
