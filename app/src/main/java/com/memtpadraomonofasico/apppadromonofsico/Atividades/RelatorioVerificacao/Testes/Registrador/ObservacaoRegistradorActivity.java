package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.memtpadraomonofasico.apppadromonofsico.R;

public class ObservacaoRegistradorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observacao_registrador);

        //clean the editText
        final EditText Observacao = findViewById(R.id.ObservacaoRegistrador);
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
                Log.d("Campo:", String.valueOf(Observacao.getText()));
                returnIntent.putExtra("btDevName",  String.valueOf(Observacao.getText()));
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
                retornarRegistrador();
            }
        });
    }

    private void retornarRegistrador() {

        Log.d("Adicionar Observação", "Retornando Para inspeção visual");
        Intent intent = new Intent(this, RegistradorActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
