package com.balancaDePrecisao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class HistoricoPesosActivity extends AppCompatActivity {

    private ListView listaPesos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_pesos);


        listaPesos = findViewById(R.id.lista_pesos);

        listaPesos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {

            }
        });

        registerForContextMenu(listaPesos);
    }

    private void carregaLista() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

}
