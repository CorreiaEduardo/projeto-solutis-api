package br.com.solutis.treinamento.apirest.service;

import br.com.solutis.treinamento.apirest.model.Conta;
import br.com.solutis.treinamento.apirest.model.Parcela;
import br.com.solutis.treinamento.apirest.repository.ContaRepository;
import br.com.solutis.treinamento.apirest.repository.ParcelaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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

    public ResponseEntity buscarTodas(){
        return new ResponseEntity<>(this.contaRepository.findAll(),HttpStatus.OK);
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
                this.parcelaRepository.save(
                        new Parcela(vencimento, valor, c)
                );
            }
        }else{
            LocalDate vencimento = c.getDataOperacao().plusMonths(1);
            this.parcelaRepository.save(
                    new Parcela(vencimento, c.getValor(), c)
            );
        }

        return new ResponseEntity<>(c,HttpStatus.CREATED);
    }

    public ResponseEntity atualizarConta(Conta c){
        Parcela parcelaConta = this.parcelaRepository.findByContaId(c.getId());
        this.contaRepository.save(c);
        parcelaConta.setValor(c.getValor());
        this.parcelaRepository.save(parcelaConta);
        return new ResponseEntity<>(c,HttpStatus.OK);
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
            return new ResponseEntity<>(this.buscarTodas(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}
