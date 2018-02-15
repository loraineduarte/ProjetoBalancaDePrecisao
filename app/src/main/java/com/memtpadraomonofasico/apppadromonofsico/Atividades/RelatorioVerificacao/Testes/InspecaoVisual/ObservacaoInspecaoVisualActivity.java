package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoVisual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class ObservacaoInspecaoVisualActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observacao_inspecao_visual);

        //clean the editText
        final EditText Observacao = (EditText) findViewById(R.id.Observacao);
        Observacao.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Observacao.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

        Button AdicionarObservacao = (Button)findViewById(R.id.AddObs);
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

        Button limpar = (Button)findViewById(R.id.limpar);
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

        Log.d("Adicionar Observação", "Retornando Para inspeção visual");
        Intent intent = new Intent(this, InspecaoVisualActivity.class);
        startActivity(intent);
    }

}
