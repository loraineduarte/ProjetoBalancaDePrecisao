package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.memtpadraomonofasico.apppadromonofsico.R;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultadosFinaisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_finais);

        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        String dataFormatada = formataData.format(data);
        Log.d("Data", dataFormatada);
        final EditText DataInicial = (EditText) findViewById(R.id.DataInicial);
        DataInicial.setText(dataFormatada);
        final EditText DataFinal = (EditText) findViewById(R.id.DataFinal);
        DataFinal.setText(dataFormatada);

        @SuppressLint("WrongViewCast") Button next =  findViewById(R.id.NextFase10);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirConclusao();
            }
        });


    }

    private void abrirConclusao() {
        Intent intent = new Intent(this, ConclusaoActivity.class);
        startActivity(intent);
    }

}
