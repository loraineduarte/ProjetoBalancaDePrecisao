package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Mensagens;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.memtpadraomonofasico.apppadromonofsico.R;

import java.util.List;

class AdapterMensagem extends BaseAdapter {

    private final Activity act;
    private List<Mensagem> mensagens;

    public AdapterMensagem(List<Mensagem> mensagens, Activity act) {
        this.mensagens = mensagens;
        this.act = act;
    }

    public List<Mensagem> getData() {
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
        View view = act.getLayoutInflater().inflate(R.layout.lista_mensagem_personalizada, parent, false);
        Mensagem mensagem = mensagens.get(position);
//        //pegando as referências das Views
        TextView mensag = view.findViewById(R.id.lista_mensaagem);

        ImageButton editar = view.findViewById(R.id.editar);
        editar.setTag(position);
        ImageButton excluir = view.findViewById(R.id.deletar);
        excluir.setTag(position);
//
//        //populando as Views
        mensag.setText(mensagem.getCorpoMensagem());

        return view;
    }

    public void removeItem(int positionToRemove) {
        mensagens.remove(positionToRemove);
        notifyDataSetChanged();
    }

    public void updateItens(List<Mensagem> itens) {
        this.mensagens = itens;
        notifyDataSetChanged();
    }
}
