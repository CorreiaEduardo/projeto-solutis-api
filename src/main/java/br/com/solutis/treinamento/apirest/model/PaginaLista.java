package br.com.solutis.treinamento.apirest.model;

import java.util.List;

public class PaginaLista {

    public PaginaLista(List<Conta> conteudo, int totalPaginas, boolean ultima) {
        this.conteudo = conteudo;
        this.totalPaginas = totalPaginas;
        this.ultima = ultima;
    }

    private List<Conta> conteudo;
    private int totalPaginas;
    private boolean ultima;

    public List<Conta> getConteudo() {
        return conteudo;
    }

    public void setConteudo(List<Conta> conteudo) {
        this.conteudo = conteudo;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public boolean isUltima() {
        return ultima;
    }

    public void setUltima(boolean ultima) {
        this.ultima = ultima;
    }
}
