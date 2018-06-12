package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Avaliador.AvaliadorDashboard;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Medidor.MedidorDashboard;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Mensagens.MensagensDashboard;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        @SuppressLint("WrongViewCast") ImageView avaliadores = findViewById(R.id.Avaliador);
        @SuppressLint("WrongViewCast") ImageView medidores = findViewById(R.id.Medidor);
        @SuppressLint("WrongViewCast") ImageView mensagens = findViewById(R.id.Mensagens);

        avaliadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAvaliador();
            }
        });

        medidores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMedidores();
            }
        });

        mensagens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMensagens();
            }
        });

    }

    private void abrirMensagens() {
        Intent intent = new Intent(this, MensagensDashboard.class);
        startActivity(intent);
    }


    //Views
    private void abrirAvaliador() {
        Intent intent = new Intent(this, AvaliadorDashboard.class);
        startActivity(intent);
    }

    private void abrirMedidores() {
        Intent intent = new Intent(this, MedidorDashboard.class);
        startActivity(intent);
    }

}
