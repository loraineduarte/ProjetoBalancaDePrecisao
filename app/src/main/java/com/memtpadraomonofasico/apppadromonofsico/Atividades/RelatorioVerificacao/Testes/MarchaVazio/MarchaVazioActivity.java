package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.MarchaVazio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.CircuitoPotencial.CircuitoPotencialActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

import java.sql.Time;

public class MarchaVazioActivity extends AppCompatActivity {

    RadioButton aprovado, naoRealizado, reprovado;
    String statusMarchaVazio;
    EditText tempoReprovado;
    Time tempoReprovadoMarchaVazio;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcha_vazio);

        tempoReprovado = findViewById(R.id.TempoMarchaVazio);
        tempoReprovado.setEnabled(false);
        Log.d("MARCHA VAZIO ", String.valueOf(Hawk.count()));


        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase5);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Hawk.delete("statusMarchaVazio");
                Hawk.delete("tempoReprovadoMarchaVazio");

                aprovado = findViewById(R.id.tampasolidarizada);
                naoRealizado = findViewById(R.id.sinaisCarbonizacao);
                reprovado = findViewById(R.id.Reprovado);



                if(aprovado.isChecked()){
                    statusMarchaVazio = "Aprovado";
                    tempoReprovadoMarchaVazio = Time.valueOf("00:00:00");

                } else if (naoRealizado.isChecked()){
                    statusMarchaVazio = "Não Realizado";
                    tempoReprovadoMarchaVazio = Time.valueOf("00:00:00");

                } else if (reprovado.isChecked()){
                    statusMarchaVazio = "Reprovado";
                    tempoReprovado = (findViewById(R.id.TempoMarchaVazio));
                    tempoReprovadoMarchaVazio = (Time) tempoReprovado.getText();

                }

                Hawk.put("statusMarchaVazio", statusMarchaVazio);
                Hawk.put("tempoReprovadoMarchaVazio", tempoReprovadoMarchaVazio);
                abrirCircuitoPotencial();
            }
        });

    }

    private void abrirCircuitoPotencial() {
        Intent intent = new Intent(this, CircuitoPotencialActivity.class);
        startActivity(intent);
    }


    public void onCheckboxClicked(View view) {

        tempoReprovado = (findViewById(R.id.TempoMarchaVazio));
        aprovado = findViewById(R.id.tampasolidarizada);
        naoRealizado = findViewById(R.id.sinaisCarbonizacao);
        reprovado = findViewById(R.id.Reprovado);

        switch (view.getId()) {
            case R.id.tampasolidarizada:
                naoRealizado.setChecked(false);
                reprovado.setChecked(false);
                tempoReprovado.setEnabled(false);
                break;

            case R.id.sinaisCarbonizacao:
                aprovado.setChecked(false);
                reprovado.setChecked(false);
                tempoReprovado.setEnabled(false);
                break;

            case R.id.Reprovado:
                aprovado.setChecked(false);
                naoRealizado.setChecked(false);
                tempoReprovado.setEnabled(true);
                break;

        }
    }

}
