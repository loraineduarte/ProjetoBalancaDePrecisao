package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoConformidade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.SituacoesObservadasActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.MarchaVazio.MarchaVazioActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class InspecaoConformidadeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspecao_conformidade);


        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase7);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirSituacoesObservadas();
            }
        });

        @SuppressLint("WrongViewCast") Button previous =  findViewById(R.id.PreviousFase6);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMarchaVazio();
            }
        });
    }

    private void abrirSituacoesObservadas() {
        Intent intent = new Intent(this, SituacoesObservadasActivity.class);
        startActivity(intent);
    }

    private void abrirMarchaVazio() {
        Intent intent = new Intent(this, MarchaVazioActivity.class);
        startActivity(intent);
    }
}
