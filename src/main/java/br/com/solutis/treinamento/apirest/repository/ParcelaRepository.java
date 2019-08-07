package br.com.solutis.treinamento.apirest.repository;

import br.com.solutis.treinamento.apirest.model.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela,Long> {

    @Transactional
    Long deleteByContaId(Long contaId);

    @Transactional
    Parcela findByContaId(Long contaId);
}
