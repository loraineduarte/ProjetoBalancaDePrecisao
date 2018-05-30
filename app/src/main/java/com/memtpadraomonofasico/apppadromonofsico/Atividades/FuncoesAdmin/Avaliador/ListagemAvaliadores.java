package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Avaliador;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.apppadromonofsico.R;

import java.util.ArrayList;
import java.util.List;

public class ListagemAvaliadores extends AppCompatActivity {
    private final CriaBanco banco = new CriaBanco(this);
    private Cursor cursorAvaliador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_avaliadores);


        ListView listaDeAvaliadores = findViewById(R.id.lista);
        List<Avaliador> avaliadores = todosAvaliadores();
        AdapterAvaliador adapter = new AdapterAvaliador(avaliadores, this);
        listaDeAvaliadores.setAdapter(adapter);


    }

    private List<Avaliador> todosAvaliadores() {
        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaAvaliadores();
        List<Avaliador> av = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String nome = cursor.getString(1);
            String matricula = cursor.getString(2);
            Avaliador avaliador = new Avaliador(nome, matricula);
            av.add(avaliador);

        }
        return av;
    }


}
