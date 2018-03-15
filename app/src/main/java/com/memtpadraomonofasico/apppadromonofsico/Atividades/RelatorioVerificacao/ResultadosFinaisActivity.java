package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ResultadosFinaisActivity extends AppCompatActivity {

    EditText leituraRetirada, leituraCalibracao, leituraPosCalibracao, DataInicial, DataFinal, HoraInicial, HoraFinal;
    String dataFormatada, horaFinalFormatada, horaInicialFormatada;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_finais);

        Log.d("RESULTADOS FINAIS", String.valueOf(Hawk.count()));

        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        dataFormatada = formataData.format(data);

        Date hora = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        horaFinalFormatada = sdf.format(hora);
        horaInicialFormatada = Hawk.get("HoraInicial");

        Log.d("Data", dataFormatada);
        Log.d("Hora Inicial", horaInicialFormatada);
        Log.d("Hora Final", horaFinalFormatada);

        DataInicial = findViewById(R.id.DataInicial);
        DataInicial.setText(dataFormatada);
        DataFinal = findViewById(R.id.DataFinal);
        DataFinal.setText(dataFormatada);

        HoraInicial = findViewById(R.id.HoraInicio);
        HoraInicial.setText(horaInicialFormatada);
        HoraFinal = findViewById(R.id.HoraFim);
        HoraFinal.setText(horaFinalFormatada);

        @SuppressLint("WrongViewCast") Button next =  findViewById(R.id.NextFase10);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                leituraRetirada = findViewById(R.id.LeituraRetirada);
                leituraCalibracao = findViewById(R.id.leituraCalibracao);
                leituraPosCalibracao = findViewById(R.id.leituraPosCalibracao);

                Hawk.delete("DataInicial");
                Hawk.delete("DataFinal");
                Hawk.delete("HoraInicial");
                Hawk.delete("HoraFinal");
                Hawk.delete("LeituraRetirada");
                Hawk.delete("LeitursCalibracao");
                Hawk.delete("LeituraPosCalibracao");


                if(leituraRetirada.getText().toString().isEmpty() || leituraCalibracao.getText().toString().isEmpty() || leituraPosCalibracao.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - As leituras de calibração não estão completas!", Toast.LENGTH_LONG).show();
                } else {
                    Hawk.put("DataInicial", dataFormatada);
                    Hawk.put("DataFinal", dataFormatada);
                    Hawk.put("HoraInicial", horaInicialFormatada);
                    Hawk.put("HoraFinal", horaFinalFormatada);
                    Hawk.put("LeituraRetirada", leituraRetirada.getText().toString());
                    Hawk.put("LeitursCalibracao", leituraCalibracao.getText().toString());
                    Hawk.put("LeituraPosCalibracao", leituraPosCalibracao.getText().toString());

                    abrirConclusao();
                }
            }
        });
    }

    private void abrirConclusao() {
        Intent intent = new Intent(this, ConclusaoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
