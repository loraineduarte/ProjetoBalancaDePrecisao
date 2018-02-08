package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoVisual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.SelecionarMedidorActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador.RegistradorActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class InspecaoVisualActivity extends AppCompatActivity {

    private static final String TAG = "Inspeção Visual";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspecao_visual);

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase4);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirRegistrador();
            }
        });

        @SuppressLint("WrongViewCast") Button previous = findViewById(R.id.PreviousFase3);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMedidor();
            }
        });

        @SuppressLint("WrongViewCast") Button addObs = findViewById(R.id.addObservacao);
        addObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAddObs();
            }
        });

        @SuppressLint("WrongViewCast") Button foto = findViewById(R.id.tirarFotoInspecao);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFoto();
            }
        });


    }

    private void tirarFoto() {
    }

    private void abrirAddObs() {
    }

    private void abrirMedidor() {
        Log.d(TAG, "Selecionar Medidor");
        Intent intent = new Intent(this, SelecionarMedidorActivity.class);
        startActivity(intent);
    }

    private void abrirRegistrador() {

        Log.d(TAG, "Teste de Registrador");
        Intent intent = new Intent(this, RegistradorActivity.class);
        startActivity(intent);
    }
}
