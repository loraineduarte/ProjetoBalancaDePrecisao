package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Mensagens;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.apppadromonofsico.R;

import java.util.ArrayList;
import java.util.List;

public class ListagemTodasMensagensActivity extends AppCompatActivity {

    private final CriaBanco banco = new CriaBanco(this);
    private final BancoController crud = new BancoController(this);
    private AdapterMensagem adapter;
    private List<Mensagem> mensagens;
    private Cursor cursorMensagem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_todas_mensagens);

        ListView listaDeMensagens = findViewById(R.id.listaMensagem);
        mensagens = todasMensagens();
        adapter = new AdapterMensagem(mensagens, this);
        listaDeMensagens.setAdapter(adapter);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("OI", "entrou2");
        reloadAllData();

    }


    private List<Mensagem> todasMensagens() {
        List<Mensagem> av = new ArrayList<>();
        Cursor cursor = crud.pegaMensagens();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String localMensagem = cursor.getString(1);
            String corpoMensagem = cursor.getString(2);
            Log.d("Corpo", corpoMensagem);
            Mensagem mensagem = new Mensagem(localMensagem, corpoMensagem);
            av.add(mensagem);
        }
        return av;
    }


    private void deletarMensagemNoBanco(String msg, String tabela, View view) {

        BancoController crud = new BancoController(getBaseContext());
        String cursor = crud.deletaMensagem(msg, tabela);
        Toast.makeText(getApplicationContext(), cursor, Toast.LENGTH_LONG).show();
        reloadAllData();
    }

    private void reloadAllData() {

        mensagens = todasMensagens();
        adapter.updateItens(mensagens);
    }


    public void editarMensagem(View view) {

        int position = (int) view.getTag();
        Mensagem mensagem = mensagens.get(position);

        final BancoController crud = new BancoController(getBaseContext());
        String msg = mensagem.getCorpoMensagem();
        String tabela = mensagem.getTabela();


        Intent intent = new Intent(this, EditarMensagemActivity.class);
        intent.putExtra("mensagem", msg);
        intent.putExtra("tabela", tabela);
        startActivity(intent);
    }

    public void deletarMensagem(View view) {

        int position = (int) view.getTag();
        Mensagem mensagem = mensagens.get(position);
        String msg = mensagem.getCorpoMensagem();
        String tabela = mensagem.getTabela();
        deletarMensagemNoBanco(msg, tabela, view);
    }
}
