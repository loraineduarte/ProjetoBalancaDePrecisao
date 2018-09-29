package com.balancaDePrecisao.Banco;

/**
 * Created by loraine.duarte on 29/09/2018.
 */

public class Dado {

    public String dataHora;
    public String peso;
    public String descricao;

    public Dado(String peso, String dataHora, String descricao) {
        this.dataHora = dataHora;
        this.peso = peso;
        this.descricao = descricao;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Dado{" +
                "dataHora='" + dataHora + '\'' +
                ", peso=" + peso +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
