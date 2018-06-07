package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Medidor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Avaliador.ListagemAvaliadores;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class MedidorDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medidor_dashboard);
        @SuppressLint("WrongViewCast") ImageView cadastrar = findViewById(R.id.Cadastrar);
        @SuppressLint("WrongViewCast") ImageView listar = findViewById(R.id.Listar);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCadastroAvaliador();
            }
        });

        listar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirListagemAvaliador();
            }
        });
    }

    private void abrirListagemAvaliador() {
        Intent intent = new Intent(this, ListagemAvaliadores.class);
        startActivity(intent);
    }

    private void abrirCadastroAvaliador() {

        Intent intent = new Intent(this, CriarMedidorActivity.class);
        startActivity(intent);
    }
}
