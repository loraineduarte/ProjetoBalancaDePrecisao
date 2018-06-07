package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Medidor;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.memtpadraomonofasico.apppadromonofsico.R;

import java.util.List;

/**
 * Created by loraine.duarte on 07/06/2018.
 */

public class AdapterMedidor extends BaseAdapter {

    private final Activity act;
    private List<Medidor> medidores;

    public AdapterMedidor(List<Medidor> avaliadores, Activity act) {
        this.medidores = avaliadores;
        this.act = act;
    }

    public List<Medidor> getData() {
        return medidores;
    }

    @Override
    public int getCount() {
        return medidores.size();
    }

    @Override
    public Object getItem(int position) {
        return medidores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.lista_avaliador_personalizada, parent, false);
        Medidor medidor = medidores.get(position);
        //pegando as referências das Views
        TextView nome = view.findViewById(R.id.lista_curso_personalizada_nome);
        TextView matricula = view.findViewById(R.id.lista_curso_personalizada_descricao);
        ImageButton editar = view.findViewById(R.id.editar);
        editar.setTag(position);
        ImageButton excluir = view.findViewById(R.id.deletar);
        excluir.setTag(position);

//        //populando as Views
//        nome.setText(medidores.getNome());
//        matricula.setText(medidores.getMatricula());

        return view;
    }

    public void removeItem(int positionToRemove) {
        medidores.remove(positionToRemove);
        notifyDataSetChanged();
    }

    public void updateItens(List<Medidor> itens) {
        this.medidores = itens;
        notifyDataSetChanged();
    }
}
