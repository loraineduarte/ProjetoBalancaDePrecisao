package com.balancaDePrecisao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.balancaDePrecisao.Banco.DadoDAO;

import java.util.ArrayList;

public class HistoricoPesosActivity extends AppCompatActivity {

    private ListView listaPesos;
    private static ArrayList dados = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_pesos);

        carregaLista();

        listaPesos = findViewById(R.id.lista_pesos);
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, dados);
        listaPesos.setAdapter(adapter);


    }

    private void carregaLista() {

        DadoDAO dao = new DadoDAO(this);
        dados= dao.pegaDados();
        dao.close();

    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

}
