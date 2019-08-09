package br.com.solutis.treinamento.apirest.repository;

import br.com.solutis.treinamento.apirest.model.Parcela;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ParcelaRepository extends PagingAndSortingRepository<Parcela,Long> {

    @Transactional
    Long deleteByContaId(Long contaId);

    @Transactional
    Parcela findByContaId(Long contaId);

    @Transactional
    Page<Parcela> findAllByContaId(Long contaId, Pageable pageRequest);
}
