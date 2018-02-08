package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.SelecionarMedidorActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoVisual.InspecaoVisualActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.MarchaVazio.MarchaVazioActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class RegistradorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrador);

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase4);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMarchaVazio();
            }
        });

        @SuppressLint("WrongViewCast") Button previous =  findViewById(R.id.PreviousFase3);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirInspecaoVisual();
            }
        });

    }

    private void abrirMarchaVazio() {
        Intent intent = new Intent(this, MarchaVazioActivity.class);
        startActivity(intent);
    }

    private void abrirInspecaoVisual() {
        Intent intent = new Intent(this, InspecaoVisualActivity.class);
        startActivity(intent);
    }
}
