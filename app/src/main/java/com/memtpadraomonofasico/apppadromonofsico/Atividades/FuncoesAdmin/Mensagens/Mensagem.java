package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Mensagens;

/**
 * Created by loraine.duarte on 13/06/2018.
 */

class Mensagem {

    private String tabela;
    private String corpoMensagem;

    public Mensagem(String tabela, String corpoMensagem) {
        this.tabela = tabela;
        this.corpoMensagem = corpoMensagem;
    }

    public String getTabela() {
        return tabela;
    }

    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    public String getCorpoMensagem() {
        return corpoMensagem;
    }

    public void setCorpoMensagem(String corpoMensagem) {
        this.corpoMensagem = corpoMensagem;
    }

    @Override
    public String toString() {
        return "Mensagem: " + corpoMensagem;
    }

}
