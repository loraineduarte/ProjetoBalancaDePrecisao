package com.balancaDePrecisao;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.balancaDePrecisao.Banco.BancoController;
import com.balancaDePrecisao.Banco.CriaBanco;

import java.util.ArrayList;

public class HistoricoPesosActivity extends AppCompatActivity {

     ListView listaPesos;
     static ArrayList dados = new ArrayList();
     static ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_pesos);

        listaPesos = findViewById(R.id.lista_pesos);

        carregaLista();


    }

    private void carregaLista() {

        BancoController dao = new BancoController(this);
        Cursor cursor = dao.pegaDados();
        dados.clear();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String peso = cursor.getString(1);
            String data = cursor.getString(2);
            dados.add(peso + " - " + data);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dados);
        listaPesos.setAdapter(adapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

}
