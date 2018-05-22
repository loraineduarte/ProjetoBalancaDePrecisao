package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Cadastro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Cadastro.Avaliador.CriarAvaliadorActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Cadastro.Medidor.CriarMedidorActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Button avaliadores = findViewById(R.id.Avaliador);
        Button medidores = findViewById(R.id.Medidor);

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
    }

    //Views
    private void abrirAvaliador() {
        Intent intent = new Intent(this, CriarAvaliadorActivity.class);
        startActivity(intent);
    }

    private void abrirMedidores() {
        Intent intent = new Intent(this, CriarMedidorActivity.class);
        startActivity(intent);
    }
}
