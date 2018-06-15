package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Mensagens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.memtpadraomonofasico.apppadromonofsico.R;

public class ListagemTodasMensagensActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_todas_mensagens);


        @SuppressLint("WrongViewCast") ImageView seloCalibracao = findViewById(R.id.Selin);
        @SuppressLint("WrongViewCast") ImageView registrador = findViewById(R.id.Regist);
        @SuppressLint("WrongViewCast") ImageView circPotencial = findViewById(R.id.Circuito);
        @SuppressLint("WrongViewCast") ImageView sitObservadas = findViewById(R.id.Situacoes);
        @SuppressLint("WrongViewCast") ImageView infComplementares = findViewById(R.id.infr);

        seloCalibracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirListagemSeloCalibração();
            }
        });

        registrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirListagemRegistrador();
            }
        });

        circPotencial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirListagemCircuitoPotencial();
            }
        });

        sitObservadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirListagemSituaçõesObservadas();
            }
        });

        infComplementares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirListagemInformacoesComplementares();
            }
        });

    }

    private void abrirListagemInformacoesComplementares() {
        Intent intent = new Intent(this, InfComplementaresListagemMensagens.class);
        startActivity(intent);
    }

    private void abrirListagemSituaçõesObservadas() {
        Intent intent = new Intent(this, SitObservadasListagemMensagens.class);
        startActivity(intent);
    }

    private void abrirListagemCircuitoPotencial() {
        Intent intent = new Intent(this, CircuitoPotencialListagemMensagens.class);
        startActivity(intent);
    }

    private void abrirListagemRegistrador() {
        Intent intent = new Intent(this, RegistradorListagemMensagens.class);
        startActivity(intent);
    }

    private void abrirListagemSeloCalibração() {
        Intent intent = new Intent(this, SeloCalibracaoListagemMensagens.class);
        startActivity(intent);
    }


}