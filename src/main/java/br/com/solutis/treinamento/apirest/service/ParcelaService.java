package br.com.solutis.treinamento.apirest.service;

import br.com.solutis.treinamento.apirest.error.CustomNotFoundException;
import br.com.solutis.treinamento.apirest.model.Conta;
import br.com.solutis.treinamento.apirest.model.Parcela;
import br.com.solutis.treinamento.apirest.model.Resumo;
import br.com.solutis.treinamento.apirest.model.enums.TipoConta;
import br.com.solutis.treinamento.apirest.repository.ContaRepository;
import br.com.solutis.treinamento.apirest.repository.ParcelaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParcelaService {

    private ContaRepository contaRepository;
    private ParcelaRepository parcelaRepository;
    @Autowired
    public ParcelaService(ContaRepository contaRepository, ParcelaRepository parcelaRepository) {
        this.contaRepository = contaRepository;
        this.parcelaRepository = parcelaRepository;
    }

    /**
     *  Método para gerar um objeto resumo contendo as parcelas filtradas por vencimento
     *  e o saldo para esse mes.
     *
     *  <p>Em contas fixas, quando o vencimento recebido como termo de filtro representa
     *     uma data posterior ao vencimento real da parcela ativa dessa conta fixa, uma
     *     predição é feita para que seja exibido a possivel parcela correspondente dessa
     *     conta fixa no vencimento inserido.</p>
     *
     * @param vencimento String - Representa o vencimento (mm-yyyy) que será o termo de filtro.
     * @return ResponseEntity - Representação da resposta HTTP, nela pode conter além do header,
     *         e o status code "OK", um objeto resumo que trás consigo a lista de parcelas
     *         filtradas e o saldo.
     * */

    public ResponseEntity gerarResumoParaVencimento(String vencimento){
        int mes = Integer.parseInt(vencimento.split("-")[0]);
        int ano = Integer.parseInt(vencimento.split("-")[1]);
        List<Parcela> source = new ArrayList<>();
        this.contaRepository.findAll().forEach(conta -> source.addAll(conta.getParcelas()));

        List<Parcela> parcelas = filtrarParcelasPorMes(source, mes, ano);

        /*
        *   As parcelas fixas assumem o vencimento do critério de busca durante a exibição,
        *   representando assim uma predição para essa parcela fixa no mes selecionado.
        * */
        parcelas.forEach(parcela -> {
            if (parcela.getConta().isFixo()){
                parcela.setVencimento(LocalDate.of(ano,mes,parcela.getVencimento().getDayOfMonth()));
            }
        });

        BigDecimal receita = new BigDecimal(0);
        BigDecimal despesa = new BigDecimal(0);

        for (Parcela p : parcelas) {
            if (p.getConta().getTipo() == TipoConta.PAGAR){
                despesa = despesa.add(p.getValor());
                continue;
            }
            receita = receita.add(p.getValor());
        }

        BigDecimal saldo = receita.subtract(despesa);

        Resumo r = new Resumo(saldo,parcelas);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    /**
     * Esse método é usado para filtrar uma lista de parcelas por vencimento (MM & yyyy)
     *
     * <p> O filtro é feito de modo que nessa lista contenha todas as parcelas que:
     *      1. Possuem vencimento condizente com o critério de busca OU
     *      2. Seja uma parcela de conta fixa E a data de vencimento atual dessa parcela seja
     *         anterior a data do critério de busca.
     * @param source Essa é a lista original que contém todas as parcelas a serem filtradas.
     * @param mes Esse é o mes que vai ser usado como filtro.
     * @param ano Esse é o ano que vai ser usado como filtro.
     * @return Retorna uma lista de parcelas que atendem ao critério de busca.
     */
    private List<Parcela> filtrarParcelasPorMes(List<Parcela> source, int mes, int ano){
        List<Parcela> parcelas = new ArrayList<>();

        for (Parcela p : source) {
            final int MES_VENCIMENTO_PARCELA = p.getVencimento().getMonthValue();
            final int ANO_VENCIMENTO_PARCELA = p.getVencimento().getYear();
            if (p.getConta().isFixo()){
                if (ANO_VENCIMENTO_PARCELA < ano || (MES_VENCIMENTO_PARCELA < mes && ANO_VENCIMENTO_PARCELA <= ano)){
                    parcelas.add(p);
                }
            }
            if (MES_VENCIMENTO_PARCELA == mes && ANO_VENCIMENTO_PARCELA == ano){
                parcelas.add(p);
            }

        }
        return parcelas;
    }

    public void inserirParcela(Parcela p){
        this.parcelaRepository.save(p);
    }

    public ResponseEntity deletarPorId(Long id) {
        Optional<Parcela> parcela = this.parcelaRepository.findById(id);
        if (parcela.isPresent()){
            Conta parcelaConta = parcela.get().getConta();
            if (!parcelaConta.isFixo()){
                parcelaConta.setQtdParcelas(parcelaConta.getQtdParcelas()-1);
                this.contaRepository.save(parcelaConta);
                this.parcelaRepository.deleteById(id);
            }else{
                Parcela parcelaPaga = parcela.get();
                parcelaPaga.setVencimento(parcelaPaga.getVencimento().plusMonths(1));
                this.parcelaRepository.save(parcelaPaga);
            }
            return new ResponseEntity<>(this.parcelaRepository.findAll(),HttpStatus.OK);
        }
        throw new CustomNotFoundException("A parcela não foi encontrada.");
    }
}
