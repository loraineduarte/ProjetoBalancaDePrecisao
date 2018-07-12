package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoVisual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.memtpadraomonofasico.apppadromonofsico.R;

public class ObservacaoInspecaoVisualActivity extends AppCompatActivity {

    private EditText Observacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observacao_inspecao_visual);

        Observacao = findViewById(R.id.Observacao);
        Observacao.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Observacao.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

        Button AdicionarObservacao = findViewById(R.id.AddObs);
        AdicionarObservacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("RESULT_STRING", Observacao.getText().toString());
                setResult(RESULT_OK, returnIntent);
                finish();

            }
        });

        Button limpar = findViewById(R.id.limpar);
        limpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observacao.getText().clear();

            }
        });

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.RetornarInspecao);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retornarInspecaoVisual();
            }
        });
    }

    private void retornarInspecaoVisual() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("RESULT_STRING", Observacao.getText().toString());
        setResult(RESULT_OK, returnIntent);
        finish();
    }


}
