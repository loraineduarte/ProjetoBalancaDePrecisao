package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.memtpadraomonofasico.apppadromonofsico.R;

public class RelatorioVerificacaoActivity extends AppCompatActivity {

    private static final String TAG = "RelatórioVerificação";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_verificacao);

        @SuppressLint("WrongViewCast") Button fab = findViewById(R.id.NextFase1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirServicos();
            }
        });
    }

    private void abrirServicos() {
        Log.d(TAG, "Opção de serviços");
        Intent intent = new Intent(this, ServicoActivity.class);
        startActivity(intent);
    }
}
