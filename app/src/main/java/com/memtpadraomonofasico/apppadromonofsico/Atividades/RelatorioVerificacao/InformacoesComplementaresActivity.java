package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class InformacoesComplementaresActivity extends AppCompatActivity {

    RadioButton tampasolidarizada, semTampa, tampaQuebrada, tampaQuebradaTransporte, seloRompido, terminaisOxidados, leituraDivergente;
    String tampasolidarizadaStatus, semTampaStatus, tampaQuebradaStatus, tampaQuebradaTransporteStatus, seloRompidoStatus, terminaisOxidadosStatus, leituraDivergenteStatus ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_complementares);

        Hawk.delete("tampasolidarizada");
        Hawk.delete("semTampa");
        Hawk.delete("tampaQuebrada");
        Hawk.delete("tampaQuebradaTransporte");
        Hawk.delete("seloRompido");
        Hawk.delete("terminaisOxidados");
        Hawk.delete("leituraDivergente");

        Log.d("INFORMAÇÕEs COMPLE", String.valueOf(Hawk.count()));

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase9);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                tampasolidarizada = findViewById(R.id.tampasolidarizada);
                semTampa = findViewById(R.id.semTampa);
                tampaQuebrada = findViewById(R.id.tampaQuebrada);
                tampaQuebradaTransporte = findViewById(R.id.tampaQuebradaTransporte);
                seloRompido = findViewById(R.id.seloRompido);
                terminaisOxidados = findViewById(R.id.terminaisOxidados);
                leituraDivergente = findViewById(R.id.leituraDivergente);

                if(tampasolidarizada.isChecked()){
                    tampasolidarizadaStatus = "Tampa do medidor solidarizada";
                    Hawk.put("tampasolidarizada",tampasolidarizadaStatus);

                }
                if (semTampa.isChecked()){
                    semTampaStatus = "Sem tampa do bloco de terminais";
                    Hawk.put("semTampa",semTampaStatus);

                }
                if (tampaQuebrada.isChecked()){
                    tampaQuebradaStatus = "Tampa quebrada";
                    Hawk.put("tampaQuebrada",tampaQuebradaStatus);

                }
                if (tampaQuebradaTransporte.isChecked()){
                    tampaQuebradaTransporteStatus = "Tampa quebrada no transporte";
                    Hawk.put("tampaQuebradaTransporte",tampaQuebradaTransporteStatus);

                }
                if (seloRompido.isChecked()){
                    seloRompidoStatus = "Selo rompido no laboratório";
                    Hawk.put("seloRompido",seloRompidoStatus);

                }
                if (terminaisOxidados.isChecked()){
                    terminaisOxidadosStatus = "Terminais de corrente oxidados";
                    Hawk.put("terminaisOxidados",terminaisOxidadosStatus);

                }
                if (leituraDivergente.isChecked()){
                    leituraDivergenteStatus = "Leitura Divergente";
                    Hawk.put("leituraDivergente",leituraDivergenteStatus);

                }

                abrirResultadosFinais();
            }
        });

    }


    private void abrirResultadosFinais() {
        Intent intent = new Intent(this, ResultadosFinaisActivity.class);
        startActivity(intent);
    }
}
