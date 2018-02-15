package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoConformidade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.SituacoesObservadasActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.MarchaVazio.MarchaVazioActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class InspecaoConformidadeActivity extends AppCompatActivity {

    private RadioButton Aprovado, NaoPossibilitaTeste, VariacaoLeitura, Reprovado;

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

        Aprovado = findViewById(R.id.Aprovado);
        NaoPossibilitaTeste = findViewById(R.id.NaoPossibilitaTeste);
        VariacaoLeitura = findViewById(R.id.VariacaoLeitura);
        Reprovado = findViewById(R.id.Reprovado);
    }

    private void abrirSituacoesObservadas() {
        Intent intent = new Intent(this, SituacoesObservadasActivity.class);
        startActivity(intent);
    }

    private void abrirMarchaVazio() {
        Intent intent = new Intent(this, MarchaVazioActivity.class);
        startActivity(intent);
    }

    public void onCheckboxClicked(View view) {

        switch (view.getId()) {
            case R.id.Aprovado:
                NaoPossibilitaTeste.setEnabled(false);
                VariacaoLeitura.setEnabled(false);
                Reprovado.setEnabled(false);

                break;

            case R.id.NaoPossibilitaTeste:
                Aprovado.setEnabled(false);
                VariacaoLeitura.setEnabled(false);
                Reprovado.setEnabled(false);
                break;

            case R.id.VariacaoLeitura:
                Aprovado.setEnabled(false);
                NaoPossibilitaTeste.setEnabled(false);
                Reprovado.setEnabled(false);
                break;

            case R.id.Reprovado:
                Aprovado.setEnabled(false);
                NaoPossibilitaTeste.setEnabled(false);
                VariacaoLeitura.setEnabled(false);
                break;

        }
    }
}

