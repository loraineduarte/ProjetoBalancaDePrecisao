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
import android.widget.Toast;

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

        Log.d("CONFORMIDADE", String.valueOf(Hawk.count()));

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase7);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.delete("CargaNominalErroConformidade");
                Hawk.delete("CargaPequenaErroConformidade");
                Hawk.delete("statusConformidade");

                cargaNominalErro =  findViewById(R.id.CargaNominalErro);
                CargaNominalErro = String.valueOf(cargaNominalErro.getText());

                cargaPequenaErro = findViewById(R.id.CargaPequenaErro);
                CargaPequenaErro = String.valueOf(cargaPequenaErro.getText());

                Aprovado = findViewById(R.id.tampasolidarizada);
                NaoPossibilitaTeste = findViewById(R.id.sinaisCarbonizacao);
                VariacaoLeitura = findViewById(R.id.VariacaoLeitura);
                Reprovado = findViewById(R.id.Reprovado);

                if(Aprovado.isChecked()){
                    statusConformidade = "Aprovado";

                } if (NaoPossibilitaTeste.isChecked()){
                    statusConformidade = "Não Possibilita Teste";

                }if (VariacaoLeitura.isChecked()){
                    statusConformidade = "Variação de Leitura";

                } if (Reprovado.isChecked()){
                    statusConformidade = "Reprovado";
                }

                if ((!Aprovado.isChecked()) && (!NaoPossibilitaTeste.isChecked()) && (!VariacaoLeitura.isChecked()) && (!Reprovado.isChecked()))
                {
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Não existe opção de status marcado. ", Toast.LENGTH_LONG).show();

                } else{
                    Hawk.put("CargaNominalErroConformidade",CargaNominalErro);
                    Hawk.put("CargaPequenaErroConformidade",CargaPequenaErro);
                    Hawk.put("statusConformidade",statusConformidade);

                    abrirSituacoesObservadas();
                }
            }
        });
    }

    private void abrirSituacoesObservadas() {
        Intent intent = new Intent(this, SituacoesObservadasActivity.class);
        startActivity(intent);
    }


    public void onCheckboxClicked(View view) {

        Aprovado = findViewById(R.id.tampasolidarizada);
        NaoPossibilitaTeste = findViewById(R.id.sinaisCarbonizacao);
        VariacaoLeitura = findViewById(R.id.VariacaoLeitura);
        Reprovado = findViewById(R.id.Reprovado);

        switch (view.getId()) {
            case R.id.tampasolidarizada:
                NaoPossibilitaTeste.setChecked(false);
                VariacaoLeitura.setChecked(false);
                Reprovado.setChecked(false);
                break;

            case R.id.sinaisCarbonizacao:
                Aprovado.setChecked(false);
                VariacaoLeitura.setChecked(false);
                Reprovado.setChecked(false);
                break;

            case R.id.VariacaoLeitura:
                Aprovado.setChecked(false);
                NaoPossibilitaTeste.setChecked(false);
                Reprovado.setChecked(false);
                break;

            case R.id.Reprovado:
                Aprovado.setChecked(false);
                NaoPossibilitaTeste.setChecked(false);
                VariacaoLeitura.setChecked(false);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

