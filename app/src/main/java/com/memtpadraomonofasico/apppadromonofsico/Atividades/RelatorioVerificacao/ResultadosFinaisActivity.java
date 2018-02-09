package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.memtpadraomonofasico.apppadromonofsico.R;

public class ResultadosFinaisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_finais);

        @SuppressLint("WrongViewCast") Button next =  findViewById(R.id.NextFase10);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirConclusao();
            }
        });

        @SuppressLint("WrongViewCast") Button previous = findViewById(R.id.PreviousFase9);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirInformacoesComplementares();
            }
        });
    }

    private void abrirConclusao() {
        Intent intent = new Intent(this, ConclusaoActivity.class);
        startActivity(intent);
    }

    private void abrirInformacoesComplementares() {
        Intent intent = new Intent(this, InformacoesComplementaresActivity.class);
        startActivity(intent);
    }
}
