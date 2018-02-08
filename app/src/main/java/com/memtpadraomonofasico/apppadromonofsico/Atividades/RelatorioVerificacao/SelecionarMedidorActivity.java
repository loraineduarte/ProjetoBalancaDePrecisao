package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoVisual.InspecaoVisualActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class SelecionarMedidorActivity extends AppCompatActivity {

    private static final String TAG = "Selecionar Medidor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_medidor);

        @SuppressLint("WrongViewCast") Button next =  findViewById(R.id.NextFase3);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirInspecaoVisual();
            }
        });

        @SuppressLint("WrongViewCast") Button previous = findViewById(R.id.PreviousFase2);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirServicos();
            }
        });

    }

    private void abrirInspecaoVisual() {
        Log.d(TAG, "Opção de serviços");
        Intent intent = new Intent(this, InspecaoVisualActivity.class);
        startActivity(intent);
    }

    private void abrirServicos() {
        Log.d(TAG, "Opção de serviços");
        Intent intent = new Intent(this, ServicoActivity.class);
        startActivity(intent);
    }
}
