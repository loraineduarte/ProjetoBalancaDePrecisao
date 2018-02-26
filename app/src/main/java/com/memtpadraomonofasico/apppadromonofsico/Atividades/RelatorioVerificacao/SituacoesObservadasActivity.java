package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.memtpadraomonofasico.apppadromonofsico.R;

public class SituacoesObservadasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situacoes_observadas);

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase8);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                abrirInformacoesComplementares();
            }
        });


    }

    private void abrirInformacoesComplementares() {
        Intent intent = new Intent(this, InformacoesComplementaresActivity.class);
        startActivity(intent);
    }
}
