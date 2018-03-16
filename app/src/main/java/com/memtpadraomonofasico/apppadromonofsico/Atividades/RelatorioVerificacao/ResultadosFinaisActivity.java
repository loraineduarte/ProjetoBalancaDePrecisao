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
import java.util.Locale;

public class ResultadosFinaisActivity extends AppCompatActivity {

    private EditText leituraRetirada;
    private EditText leituraCalibracao;
    private EditText leituraPosCalibracao;
    private String dataFormatada;
    private String horaFinalFormatada;
    private String horaInicialFormatada;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_finais);

        Log.d("RESULTADOS FINAIS", String.valueOf(Hawk.count()));

        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy" , Locale.getDefault());
        Date data = new Date();
        dataFormatada = formataData.format(data);

        Date hora = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss" , Locale.getDefault());
        horaFinalFormatada = sdf.format(hora);
        horaInicialFormatada = Hawk.get("HoraInicial");

        Log.d("Data", dataFormatada);
        Log.d("Hora Inicial", horaInicialFormatada);
        Log.d("Hora Final", horaFinalFormatada);

        EditText dataInicial = findViewById(R.id.DataInicial);
        dataInicial.setText(dataFormatada);
        EditText dataFinal = findViewById(R.id.DataFinal);
        dataFinal.setText(dataFormatada);

        EditText horaInicial = findViewById(R.id.HoraInicio);
        horaInicial.setText(horaInicialFormatada);
        EditText horaFinal = findViewById(R.id.HoraFim);
        horaFinal.setText(horaFinalFormatada);

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

}
