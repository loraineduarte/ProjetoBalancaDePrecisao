package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoVisual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.SelecionarMedidorActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador.RegistradorActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class InspecaoVisualActivity extends AppCompatActivity {

    private static final String TAG = "Inspeção Visual";
    private RadioButton VioladosInpecao, AusentesInspecao, ReconstituidosInspecao, NaoPadronizadosInpecao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspecao_visual);

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase4);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirRegistrador();
            }
        });

        @SuppressLint("WrongViewCast") Button previous = findViewById(R.id.PreviousFase3);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMedidor();
            }
        });

        @SuppressLint("WrongViewCast") Button addObs = findViewById(R.id.addObservacao);
        addObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAddObs();
            }
        });

        @SuppressLint("WrongViewCast") Button foto = findViewById(R.id.tirarFotoInspecao);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFoto();
            }
        });

        VioladosInpecao = findViewById(R.id.VioladosInpecao);
        AusentesInspecao = findViewById(R.id.AusentesInspecao);
        ReconstituidosInspecao = findViewById(R.id.ReconstituidosInspecao);
        NaoPadronizadosInpecao = findViewById(R.id.NaoPadronizadosInpecao);

        //clean the editText
        final EditText Selo1 = (EditText) findViewById(R.id.Selo1);
        Selo1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Selo1.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

        final EditText Selo2 = (EditText) findViewById(R.id.Selo2);
        Selo2.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Selo2.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

        final EditText Selo3 = (EditText) findViewById(R.id.Selo3);
        Selo3.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Selo3.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

        final EditText Selo4 = (EditText) findViewById(R.id.Selo4);
        Selo4.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Selo4.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

    }

    private void tirarFoto() {
    }

    private void abrirAddObs() {
    }

    private void abrirMedidor() {
        Log.d(TAG, "Selecionar Medidor");
        Intent intent = new Intent(this, SelecionarMedidorActivity.class);
        startActivity(intent);
    }

    private void abrirRegistrador() {

        Log.d(TAG, "Teste de Registrador");
        Intent intent = new Intent(this, RegistradorActivity.class);
        startActivity(intent);
    }

    public void onCheckboxClicked(View view) {

        switch (view.getId()) {
            case R.id.AprovadoInspecaoVisual:
                VioladosInpecao.setEnabled(false);
                AusentesInspecao.setEnabled(false);
                ReconstituidosInspecao.setEnabled(false);
                NaoPadronizadosInpecao.setEnabled(false);

                break;

            case R.id.ReprovadoInspecaoVisual:
                VioladosInpecao.setEnabled(true);
                AusentesInspecao.setEnabled(true);
                ReconstituidosInspecao.setEnabled(true);
                NaoPadronizadosInpecao.setEnabled(true);
                break;

        }
    }
}
