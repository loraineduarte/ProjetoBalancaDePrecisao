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
import com.orhanobut.hawk.Hawk;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ResultadosFinaisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_finais);

        Log.d("RESULTADOS FINAIS", String.valueOf(Hawk.count()));

        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        String dataFormatada = formataData.format(data);

        Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String horaFinalFormatada = sdf.format(hora);
        String horaInicialFormatada = Hawk.get("HoraInicial");

        Log.d("Data", dataFormatada);
        Log.d("Hora Inicial", horaInicialFormatada);
        Log.d("Hora Final", horaFinalFormatada);

        final EditText DataInicial = (EditText) findViewById(R.id.DataInicial);
        DataInicial.setText(dataFormatada);
        final EditText DataFinal = (EditText) findViewById(R.id.DataFinal);
        DataFinal.setText(dataFormatada);

        final EditText HoraInicial = (EditText) findViewById(R.id.HoraInicio);
        HoraInicial.setText(horaInicialFormatada);
        final EditText HoraFinal = (EditText) findViewById(R.id.HoraFim);
        HoraFinal.setText(horaFinalFormatada);


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
