package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

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
    private RadioButton tampasolidarizada;
    private RadioButton semTampa;
    private RadioButton tampaQuebrada;
    private RadioButton tampaQuebradaTransporte;
    private RadioButton seloRompido;
    private RadioButton terminaisOxidados;
    private RadioButton leituraDivergente;
    private String tampasolidarizadaStatus;
    private String semTampaStatus;
    private String tampaQuebradaStatus;
    private String tampaQuebradaTransporteStatus;
    private String seloRompidoStatus;
    private String terminaisOxidadosStatus;
    private String leituraDivergenteStatus ;
    private String informacoesComplementares ="";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_finais);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();


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

        @SuppressLint("WrongViewCast") Button next =  findViewById(R.id.NextFase9);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Hawk.delete("DataInicial");
                Hawk.delete("DataFinal");
                Hawk.delete("HoraInicial");
                Hawk.delete("HoraFinal");
                Hawk.delete("InformacoesComplementares");


                tampasolidarizada = findViewById(R.id.tampasolidarizada);
                semTampa = findViewById(R.id.semTampa);
                tampaQuebrada = findViewById(R.id.tampaQuebrada);
                tampaQuebradaTransporte = findViewById(R.id.tampaQuebradaTransporte);
                seloRompido = findViewById(R.id.seloRompido);
                terminaisOxidados = findViewById(R.id.terminaisOxidados);
                leituraDivergente = findViewById(R.id.leituraDivergente);

                if(tampasolidarizada.isChecked()){
                    tampasolidarizadaStatus = "Tampa do medidor solidarizada";
                    informacoesComplementares = informacoesComplementares + tampasolidarizadaStatus + " - ";

                }
                if (semTampa.isChecked()){
                    semTampaStatus = "Sem tampa do bloco de terminais";
                    informacoesComplementares = informacoesComplementares + semTampaStatus + " - ";

                }
                if (tampaQuebrada.isChecked()){
                    tampaQuebradaStatus = "Tampa quebrada";
                    informacoesComplementares = informacoesComplementares + tampaQuebradaStatus + " - ";

                }
                if (tampaQuebradaTransporte.isChecked()){
                    tampaQuebradaTransporteStatus = "Tampa quebrada no transporte";
                    informacoesComplementares = informacoesComplementares + tampaQuebradaTransporteStatus + " - ";

                }
                if (seloRompido.isChecked()){
                    seloRompidoStatus = "Selo rompido no laboratório";
                    informacoesComplementares = informacoesComplementares + seloRompidoStatus + " - ";

                }
                if (terminaisOxidados.isChecked()){
                    terminaisOxidadosStatus = "Terminais de corrente oxidados";
                    informacoesComplementares = informacoesComplementares + terminaisOxidadosStatus + " - ";

                }
                if (leituraDivergente.isChecked()){
                    leituraDivergenteStatus = "Leitura Divergente";
                    informacoesComplementares = informacoesComplementares + leituraDivergenteStatus;
                }

                Hawk.put("InformacoesComplementares", informacoesComplementares);


                    Hawk.put("DataInicial", dataFormatada);
                    Hawk.put("DataFinal", dataFormatada);
                    Hawk.put("HoraInicial", horaInicialFormatada);
                    Hawk.put("HoraFinal", horaFinalFormatada);

                abrirConclusao();

            }
        });
    }

    private void abrirConclusao() {
        Intent intent = new Intent(this, ConclusaoActivity.class);
        startActivity(intent);
    }

}
