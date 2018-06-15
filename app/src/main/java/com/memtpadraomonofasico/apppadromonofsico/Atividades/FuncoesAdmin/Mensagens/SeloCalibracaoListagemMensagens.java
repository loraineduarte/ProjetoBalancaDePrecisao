package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Mensagens;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.apppadromonofsico.R;

import java.util.ArrayList;
import java.util.List;

public class SeloCalibracaoListagemMensagens extends AppCompatActivity {

    private final CriaBanco banco = new CriaBanco(this);
    private final BancoController crud = new BancoController(this);
    private AdapterMensagem adapter;
    private List<Mensagem> mensagens;
    private Cursor cursorMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selocalibracao_listagem_mensagens);

        ListView listaDeMensagens = findViewById(R.id.listaMensagem);
        mensagens = todasMensagens();
        adapter = new AdapterMensagem(mensagens, this);
        listaDeMensagens.setAdapter(adapter);
    }

    private List<Mensagem> todasMensagens() {
        List<Mensagem> av = new ArrayList<>();
        Cursor cursor = crud.pegaMensagens("Selos de Calibração");
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String corpoMensagem = cursor.getString(1);
            String localMensagem = cursor.getString(2);
            Log.d("Corpo", corpoMensagem);
            Mensagem mensagem = new Mensagem(corpoMensagem, localMensagem);
            av.add(mensagem);
        }
        return av;
    }
}
