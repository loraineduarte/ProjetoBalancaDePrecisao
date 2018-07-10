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

class AdapterMedidor extends BaseAdapter {

    private final Activity act;
    private List<Medidor> medidores;

    public AdapterMedidor(List<Medidor> medidores, Activity act) {
        this.medidores = medidores;
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

        View view = act.getLayoutInflater().inflate(R.layout.lista_medidor_personalizada, parent, false);
        Medidor medidor = medidores.get(position);
        //pegando as referências das Views
        TextView numGeral = view.findViewById(R.id.lista_descricao);
        ImageButton editar = view.findViewById(R.id.editar);
        editar.setTag(position);
        ImageButton excluir = view.findViewById(R.id.deletar);
        excluir.setTag(position);

        numGeral.setText(medidor.getNumeroGeral());

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
