package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Avaliador;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.memtpadraomonofasico.apppadromonofsico.R;

import java.util.List;

/**
 * Created by loraine.duarte on 25/05/2018.
 */

class AdapterAvaliador extends BaseAdapter {

    private final List<Avaliador> avaliadores;
    private final Activity act;

    public AdapterAvaliador(List<Avaliador> avaliadores, Activity act) {
        this.avaliadores = avaliadores;
        this.act = act;
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

        Log.d("VIEW", avaliadores.toString());

        View view = act.getLayoutInflater().inflate(R.layout.lista_avaliador_personalizada, parent, false);
        Avaliador avaliador = avaliadores.get(position);
        //pegando as referências das Views
        TextView nome = view.findViewById(R.id.lista_curso_personalizada_nome);
        TextView matricula = view.findViewById(R.id.lista_curso_personalizada_descricao);

        //populando as Views
        nome.setText(avaliador.getNome());
        matricula.setText(avaliador.getMatricula());

        return view;
    }
}
