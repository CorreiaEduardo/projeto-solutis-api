package br.com.solutis.treinamento.apirest.model;

import br.com.solutis.treinamento.apirest.model.abstracts.AbstractEntity;
import br.com.solutis.treinamento.apirest.model.enums.TipoConta;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Conta extends AbstractEntity {

    public Conta() {}
    @NotNull(message = "O nome da conta é um campo obrigatório.")
    @Size(min = 1, max = 60, message = "O nome da conta deve conter entre 1 e 60 caracteres.")
    @Column(name = "nome", nullable = false, length = 60)
    private String nome;

    @NotNull(message = "O valor da conta é um campo obrigatório.")
    @PositiveOrZero(message = "O valor deve ser positivo ou zero.")
    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @NotNull(message = "O atributo fixo é deve ser preenchido.")
    @Column(name = "fixo", nullable = false)
    private boolean fixo;

    @NotNull(message = "O tipo da conta é um campo obrigatório")
    @Column(name = "tipo", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private TipoConta tipo;

    @NotNull(message = "A quantidade de parcelas da conta é um campo obrigatório")
    @PositiveOrZero(message = "A quantidade de parcelas deve ser um número positivo ou zero.")
    @Column(name = "qtdParcelas", nullable = false)
    private Long qtdParcelas;

    @NotNull(message = "A data de operação da conta é um campo obrigatório")
    @Column(name = "data_operacao", nullable = false)
    private LocalDate dataOperacao;

    @ApiModelProperty(hidden = true)
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
