package br.com.solutis.treinamento.apirest.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Classe para agrupar dados a serem exibidos em relatórios mensais.
 * */
public class Resumo {

    public Resumo(BigDecimal saldo, List<Parcela> parcelas) {
        this.saldo = saldo;
        this.parcelas = parcelas;
    }

    private BigDecimal saldo;
    private List<Parcela> parcelas;

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }
}
