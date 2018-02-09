package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoVisual.InspecaoVisualActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class InformacoesComplementaresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_complementares);

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase9);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirResultadosFinais();
            }
        });

        @SuppressLint("WrongViewCast") Button previous =  findViewById(R.id.PreviousFase8);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirSituacoesObservadas();
            }
        });
    }

    private void abrirSituacoesObservadas() {
        Intent intent = new Intent(this, SituacoesObservadasActivity.class);
        startActivity(intent);
    }

    private void abrirResultadosFinais() {
        Intent intent = new Intent(this, ResultadosFinaisActivity.class);
        startActivity(intent);
    }
}
