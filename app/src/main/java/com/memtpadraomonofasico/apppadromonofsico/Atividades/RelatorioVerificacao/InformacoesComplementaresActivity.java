package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

/**
 *
 */
public class InformacoesComplementaresActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_complementares);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();


        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase9);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                abrirResultadosFinais();
            }
        });

    }


    private void abrirResultadosFinais() {
        Intent intent = new Intent(this, ResultadosFinaisActivity.class);
        startActivity(intent);
    }

}
