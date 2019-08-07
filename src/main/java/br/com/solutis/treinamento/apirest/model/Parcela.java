package br.com.solutis.treinamento.apirest.model;

import br.com.solutis.treinamento.apirest.model.abstracts.AbstractEntity;
import br.com.solutis.treinamento.apirest.model.enums.TipoConta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "parcela")
public class Parcela extends AbstractEntity{

    public Parcela() {}

    public Parcela(LocalDate vencimento, BigDecimal valor, Conta conta) {
        this.vencimento = vencimento;
        this.valor = valor;
        this.conta = conta;
    }

    @Column(name = "vencimento", nullable = false)
    private LocalDate vencimento;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @ManyToOne
    @JoinColumn(name="conta_id", updatable=false, referencedColumnName = "id", nullable = false)
    private Conta conta;


    //GETTERS&SETTERS
    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @JsonIgnore
    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public String getNomeConta(){
        return this.conta.getNome();
    }

    public TipoConta getTipoConta(){
        return this.conta.getTipo();
    }


}
