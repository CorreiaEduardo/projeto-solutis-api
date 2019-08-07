package br.com.solutis.treinamento.apirest.model;

import br.com.solutis.treinamento.apirest.model.abstracts.AbstractEntity;
import br.com.solutis.treinamento.apirest.model.enums.TipoConta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Conta extends AbstractEntity {

    public Conta() {}

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "fixo", nullable = false)
    private boolean fixo;

    @Column(name = "tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoConta tipo;

    @Column(name = "qtdParcelas", nullable = true)
    private Long qtdParcelas;

    @Column(name = "data_operacao", nullable = false)
    private LocalDate dataOperacao;

    @OneToMany(mappedBy="conta")
    private List<Parcela> parcelas;


    //GETTERS&SETTERS
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean isFixo() {
        return fixo;
    }

    public void setFixo(boolean fixo) {
        this.fixo = fixo;
    }

    public TipoConta getTipo() {
        return tipo;
    }

    public void setTipo(TipoConta tipo) {
        this.tipo = tipo;
    }

    public Long getQtdParcelas() {
        return qtdParcelas;
    }

    public void setQtdParcelas(Long qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    public LocalDate getDataOperacao() {
        return dataOperacao;
    }

    public void setDataOperacao(LocalDate dataOperacao) {
        this.dataOperacao = dataOperacao;
    }

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }
}
