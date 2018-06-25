package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.util.ArrayList;
import java.util.List;

public class SituacoesObservadasActivity extends AppCompatActivity {

    private final CriaBanco banco = new CriaBanco(this);
    private final BancoController crud = new BancoController(this);
    private AdapterListagemFinal adapter;
    private List<String> mensagens;
    private Cursor cursorMensagem;
    private String observações = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situacoes_observadas);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();

        ListView listaDeMensagens = findViewById(R.id.lista);
        mensagens = todasMensagens();
        adapter = new AdapterListagemFinal(mensagens, this);
        listaDeMensagens.setAdapter(adapter);

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase8);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Hawk.delete("SituacoesObservadas");
                Hawk.put("SituacoesObservadas", observações);

                abrirResultadosFinais();
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reloadAllData();

    }


    private List<String> todasMensagens() {
        List<String> av = new ArrayList<>();
        Cursor cursor = crud.pegaMensagemEspecifica("Situações Observadas");
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String corpoMensagem = cursor.getString(2);
            av.add(corpoMensagem);
        }
        return av;
    }


    private void reloadAllData() {

        mensagens = todasMensagens();
        adapter.updateItens(mensagens);
    }

    public void selecionarMensagem(View view) {

        int position = (int) view.getTag();
        String mensagem = mensagens.get(position);
        observações = observações + "\n" + mensagem;

    }

    private void abrirResultadosFinais() {
        Intent intent = new Intent(this, ResultadosFinaisActivity.class);
        startActivity(intent);
    }

}
