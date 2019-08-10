package br.com.solutis.treinamento.apirest.service;

import br.com.solutis.treinamento.apirest.error.CustomBadRequestException;
import br.com.solutis.treinamento.apirest.error.CustomNotFoundException;
import br.com.solutis.treinamento.apirest.model.Conta;
import br.com.solutis.treinamento.apirest.model.PaginaLista;
import br.com.solutis.treinamento.apirest.model.Parcela;
import br.com.solutis.treinamento.apirest.model.enums.TipoConta;
import br.com.solutis.treinamento.apirest.repository.ContaRepository;
import br.com.solutis.treinamento.apirest.repository.ParcelaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    private ContaRepository contaRepository;
    private ParcelaRepository parcelaRepository;
    @Autowired
    public ContaService(ContaRepository contaRepository, ParcelaRepository parcelaRepository) {
        this.contaRepository = contaRepository;
        this.parcelaRepository = parcelaRepository;
    }

    public ResponseEntity buscarTodas(Pageable pageable){
        List<Conta> contas = this.contaRepository.findAll();
        contas.sort(Comparator.comparing(conta -> conta.getId()));

        List<Conta> contasPaginadas = obterContasPaginadas(contas, pageable);
        int totalPaginas = obterTotalPaginas(contas, pageable);

        PaginaLista pagina = new PaginaLista(contasPaginadas,totalPaginas,pageable.getPageNumber()>=totalPaginas-1);
        return new ResponseEntity<>(pagina,HttpStatus.OK);
    }

    /**
     * Método para obter uma lista de contas, com suas parcelas paginadas.
     * @param contas 'List<Conta>' - Lista de contas a ser paginada.
     * @return 'List<Conta>' - A lista de contas com suas parcelas paginadas.
     * */
    private List<Conta> obterContasPaginadas(List<Conta> contas,Pageable pageable){
        List<Conta> contasPaginadas = new ArrayList<>();
        for (Conta conta:contas){
            if (pageable.getPageNumber() == 0){
                if (conta.isFixo()) contasPaginadas.add(conta);
            }
            if (conta.getQtdParcelas() > (pageable.getPageSize()*pageable.getPageNumber())){
                Page<Parcela> paginaAtual = this.parcelaRepository.findAllByContaId(conta.getId(),pageable);
                conta.setParcelas(paginaAtual.getContent());
                contasPaginadas.add(conta);
            }
        }
        return contasPaginadas;
    }

    /**
     * Método para obter o total de páginas que serão necessárias para exibir
     * todo o conteúdo que poderá ser paginado.
     * @param contas 'List<Conta>' - Lista de contas que podem ser paginadas.
     * @return int - Total de páginas que serão necessárias para exibir
     * todo o conteúdo da lista de contas.
     * */
    private int obterTotalPaginas(List<Conta> contas,Pageable pageable){
        int totalPaginas = 0;
        for (Conta conta:contas){
            if (conta.getQtdParcelas() > (pageable.getPageSize()*pageable.getPageNumber())){
                Page<Parcela> paginaAtual = this.parcelaRepository.findAllByContaId(conta.getId(),pageable);
                if(paginaAtual.getTotalPages() > totalPaginas) totalPaginas=paginaAtual.getTotalPages();
            }
        }
        return totalPaginas;
    }

    /**
     * Método para persistir uma conta, bem como gerar suas parcelas.
     * @param c Conta - Conta a ser persistida.
     * @return ResponseEntity - Representação da resposta HTTP, nela contém além do header,
     *         a própria conta que foi inserida e o status code "OK".
     * */
    public ResponseEntity inserirConta(Conta c){
        this.contaRepository.save(c);
        if (c.getQtdParcelas() != 0){
            long qtdParcelas = (long) c.getQtdParcelas();
            for (int i = 1 ; i <= qtdParcelas ; i++){
                BigDecimal valor = c.getValor().divide(new BigDecimal(qtdParcelas), RoundingMode.UP);
                LocalDate vencimento = c.getDataOperacao().plusMonths(i);
                Parcela p = new Parcela(vencimento, valor, c);
                this.parcelaRepository.save(p);
                c.getParcelas().add(p);
            }
        }else{
            LocalDate vencimento = c.getDataOperacao().plusMonths(1);
            this.parcelaRepository.save(
                    new Parcela(vencimento, c.getValor(), c)
            );
        }

        return new ResponseEntity<>(c,HttpStatus.CREATED);
    }

    public ResponseEntity atualizarConta(Conta c, Long id){
        c.setId(id);
        if (c.isFixo()){
            Parcela parcelaConta = this.parcelaRepository.findByContaId(c.getId());
            this.contaRepository.save(c);
            parcelaConta.setValor(c.getValor());
            this.parcelaRepository.save(parcelaConta);
            return new ResponseEntity<>(c,HttpStatus.OK);
        }
        throw new CustomBadRequestException("Só contas fixas podem ser atualizadas");

    }

    public ResponseEntity buscarPorId(Long id){
        return new ResponseEntity<>(this.contaRepository.findById(id),HttpStatus.OK);
    }

    /**
     * Método para deletar uma conta por id, bem como gerar suas parcelas.
     * @param id Long - Id da Conta a ser deletada.
     * @return ResponseEntity - Representação da resposta HTTP, nela pode conter além do header,
     *         a lista de contas restantes e o status code "OK" OU uma resposta sem corpo e com status code
     *         "NOT FOUND".
     * */

    public ResponseEntity deletarContaPorId(Long id){

        Optional<Conta> opConta = this.contaRepository.findById(id);
        if (opConta.isPresent()){
            Conta conta = opConta.get();
            if (conta.isFixo()){
                Parcela parcelaPaga = conta.getParcelas().get(0);
                parcelaPaga.setVencimento(parcelaPaga.getVencimento().plusMonths(1));
                this.parcelaRepository.save(parcelaPaga);
            }else{
                this.parcelaRepository.deleteByContaId(conta.getId());
                this.contaRepository.deleteById(id);
            }
            return new ResponseEntity<>(this.contaRepository.findAll(), HttpStatus.OK);
        }
        throw new CustomNotFoundException("A parcela não foi encontrada.");
    }

    public ResponseEntity buscarPorTipo(TipoConta tipoConta) {
        List<Conta> source = this.contaRepository.findByTipo(tipoConta);
        source.forEach(conta -> conta.getParcelas().sort(Comparator.comparing(parcela -> parcela.getVencimento())));
        return new ResponseEntity<>(this.contaRepository.findByTipo(tipoConta),HttpStatus.OK);
    }
}
