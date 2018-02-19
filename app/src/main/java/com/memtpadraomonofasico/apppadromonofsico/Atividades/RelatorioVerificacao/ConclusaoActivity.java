package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.memtpadraomonofasico.apppadromonofsico.R;

public class ConclusaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusao);

        @SuppressLint("WrongViewCast") Button previous = findViewById(R.id.PreviousFase);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirResultadosFinais();
            }
        });
    }


    private void abrirResultadosFinais() {
        Intent intent = new Intent(this, ResultadosFinaisActivity.class);
        startActivity(intent);
    }
}