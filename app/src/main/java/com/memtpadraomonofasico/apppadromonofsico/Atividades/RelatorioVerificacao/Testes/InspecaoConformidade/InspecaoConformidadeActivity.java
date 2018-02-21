package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoConformidade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.SituacoesObservadasActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class InspecaoConformidadeActivity extends AppCompatActivity {

    private RadioButton Aprovado, NaoPossibilitaTeste, VariacaoLeitura, Reprovado;
    String statusConformidade, CargaNominalErro, CargaPequenaErro;
    private EditText cargaNominalErro, cargaPequenaErro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspecao_conformidade);

        Hawk.delete("CargaNominalErroConformidade");
        Hawk.delete("CargaPequenaErroConformidade");
        Hawk.delete("statusConformidade");
        Log.d("CONFORMIDADE", String.valueOf(Hawk.count()));


        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase7);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cargaNominalErro =  findViewById(R.id.CargaNominalErro);
                CargaNominalErro = String.valueOf(cargaNominalErro.getText());

                cargaPequenaErro = findViewById(R.id.CargaPequenaErro);
                CargaPequenaErro = String.valueOf(cargaPequenaErro.getText());

                Aprovado = findViewById(R.id.AprovadoMarchaVazio);
                NaoPossibilitaTeste = findViewById(R.id.NaoRealizado);
                VariacaoLeitura = findViewById(R.id.VariacaoLeitura);
                Reprovado = findViewById(R.id.Reprovado);

                if(Aprovado.isChecked()){
                    statusConformidade = "Aprovado";

                } else if (NaoPossibilitaTeste.isChecked()){
                    statusConformidade = "Não Possibilita Teste";

                } else if (VariacaoLeitura.isChecked()){
                    statusConformidade = "Variação de Leitura";

                } else if (Reprovado.isChecked()){
                    statusConformidade = "Reprovado";

                }

                Hawk.put("CargaNominalErroConformidade",CargaNominalErro);
                Hawk.put("CargaPequenaErroConformidade",CargaPequenaErro);
                Hawk.put("statusConformidade",statusConformidade);

                abrirSituacoesObservadas();
            }
        });



    }

    private void abrirSituacoesObservadas() {
        Intent intent = new Intent(this, SituacoesObservadasActivity.class);
        startActivity(intent);
    }


    public void onCheckboxClicked(View view) {

        Aprovado = findViewById(R.id.AprovadoMarchaVazio);
        NaoPossibilitaTeste = findViewById(R.id.NaoRealizado);
        VariacaoLeitura = findViewById(R.id.VariacaoLeitura);
        Reprovado = findViewById(R.id.Reprovado);

        switch (view.getId()) {
            case R.id.AprovadoMarchaVazio:
                NaoPossibilitaTeste.setEnabled(false);
                VariacaoLeitura.setEnabled(false);
                Reprovado.setEnabled(false);
                break;

            case R.id.NaoRealizado:
                Aprovado.setEnabled(false);
                VariacaoLeitura.setEnabled(false);
                Reprovado.setEnabled(false);
                break;

            case R.id.VariacaoLeitura:
                Aprovado.setEnabled(false);
                NaoPossibilitaTeste.setEnabled(false);
                Reprovado.setEnabled(false);
                break;

            case R.id.Reprovado:
                Aprovado.setEnabled(false);
                NaoPossibilitaTeste.setEnabled(false);
                VariacaoLeitura.setEnabled(false);
                break;

        }
    }
}

