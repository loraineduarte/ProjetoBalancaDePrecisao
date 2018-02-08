package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.memtpadraomonofasico.apppadromonofsico.R;

public class ServicoActivity extends AppCompatActivity {

    private static final String TAG = "Serviço Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servico);

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMedidor();
            }
        });

        @SuppressLint("WrongViewCast") Button previous =  findViewById(R.id.PreviousFase1);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDadosGerais();
            }
        });

    }

    private void abrirMedidor() {
        Intent intent = new Intent(this, SelecionarMedidorActivity.class);
        startActivity(intent);
    }

    private void abrirDadosGerais() {
        Intent intent = new Intent(this, RelatorioVerificacaoActivity.class);
        startActivity(intent);
    }
}
