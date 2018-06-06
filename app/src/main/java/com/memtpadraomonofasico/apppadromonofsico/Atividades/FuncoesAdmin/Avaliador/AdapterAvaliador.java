package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Avaliador;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.memtpadraomonofasico.apppadromonofsico.R;

import java.util.List;

/**
 * Created by loraine.duarte on 25/05/2018.
 */

class AdapterAvaliador extends BaseAdapter {

    private final Activity act;
    private List<Avaliador> avaliadores;

    public AdapterAvaliador(List<Avaliador> avaliadores, Activity act) {
        this.avaliadores = avaliadores;
        this.act = act;
    }

    public List<Avaliador> getData() {
        return avaliadores;
    }

    @Override
    public int getCount() {
        return avaliadores.size();
    }

    @Override
    public Object getItem(int position) {
        return avaliadores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.lista_avaliador_personalizada, parent, false);
        Avaliador avaliador = avaliadores.get(position);
        //pegando as referências das Views
        TextView nome = view.findViewById(R.id.lista_curso_personalizada_nome);
        TextView matricula = view.findViewById(R.id.lista_curso_personalizada_descricao);
        ImageButton editar = view.findViewById(R.id.editar);
        editar.setTag(position);
        ImageButton excluir = view.findViewById(R.id.deletar);
        excluir.setTag(position);

        //populando as Views
        nome.setText(avaliador.getNome());
        matricula.setText(avaliador.getMatricula());

        return view;
    }

    public void removeItem(int positionToRemove) {
        avaliadores.remove(positionToRemove);
        notifyDataSetChanged();
    }

    public void updateItens(List<Avaliador> itens) {
        this.avaliadores = itens;
        notifyDataSetChanged();
    }
}
