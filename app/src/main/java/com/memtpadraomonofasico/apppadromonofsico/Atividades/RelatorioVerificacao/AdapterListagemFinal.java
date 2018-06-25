package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.memtpadraomonofasico.apppadromonofsico.R;

import java.util.List;

class AdapterListagemFinal extends BaseAdapter {

    private final Activity act;
    private List<String> mensagens;

    public AdapterListagemFinal(List<String> mensagens, Activity act) {
        this.mensagens = mensagens;
        this.act = act;
    }

    public List<String> getData() {
        return mensagens;
    }

    @Override
    public int getCount() {
        return mensagens.size();
    }

    @Override
    public Object getItem(int position) {
        return mensagens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String mensagem = mensagens.get(position);
        View view = act.getLayoutInflater().inflate(R.layout.listagem_mensagem_final, parent, false);
        TextView mensag = view.findViewById(R.id.Mensagem);
        mensag.setText(mensagem);

        return view;
    }

    public void removeItem(int positionToRemove) {
        mensagens.remove(positionToRemove);
        notifyDataSetChanged();
    }

    public void updateItens(List<String> itens) {
        this.mensagens = itens;
        notifyDataSetChanged();
    }
}
