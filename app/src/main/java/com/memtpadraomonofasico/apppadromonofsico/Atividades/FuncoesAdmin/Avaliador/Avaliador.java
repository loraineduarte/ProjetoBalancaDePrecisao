package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Avaliador;

/**
 * Created by loraine.duarte on 29/05/2018.
 */

class Avaliador {

    private String nome;
    private String matricula;

    public Avaliador(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public String toString() {
        return "Avaliador: " + nome + "\nMatrícula: " +
                matricula;
    }
}
