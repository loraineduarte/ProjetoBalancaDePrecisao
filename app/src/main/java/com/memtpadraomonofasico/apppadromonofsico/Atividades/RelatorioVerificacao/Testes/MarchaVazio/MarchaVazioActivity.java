package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.MarchaVazio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoConformidade.InspecaoConformidadeActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador.RegistradorActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class MarchaVazioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcha_vazio);


        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase5);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirInspecaoConformidade();
            }
        });

        @SuppressLint("WrongViewCast") Button previous =  findViewById(R.id.PreviousFase4);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirRegistrador();
            }
        });

    }

    private void abrirInspecaoConformidade() {
        Intent intent = new Intent(this, InspecaoConformidadeActivity.class);
        startActivity(intent);
    }

    private void abrirRegistrador() {
        Intent intent = new Intent(this, RegistradorActivity.class);
        startActivity(intent);
    }
}
