package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class RelatorioVerificacaoActivity extends AppCompatActivity {

    private static final String TAG = "RelatórioVerificação";
    private RadioButton SolicitacaoConsumidor, SEM, TOI;
    private EditText numeroTOI, matriculaAvaliador;
    final CriaBanco banco = new CriaBanco(this);

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_verificacao);

        SolicitacaoConsumidor = findViewById(R.id.SolicitacaoConsumidor);
        SEM = findViewById(R.id.SEM);
        TOI = findViewById(R.id.TOI);
        matriculaAvaliador = findViewById(R.id.MatriculaAvaliador);
        final EditText NomeAvaliador = (EditText) findViewById(R.id.NomeAvaliador);

        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaAvaliadores();
        Log.d(TAG, String.valueOf(cursor.getCount()));

        //clean the editText
        final EditText MatriculaAvaliador = (EditText) findViewById(R.id.MatriculaAvaliador);
        MatriculaAvaliador.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MatriculaAvaliador.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

        final EditText ToiNumero = (EditText) findViewById(R.id.ToiNumero);
        ToiNumero.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ToiNumero.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });

        if (cursor.getCount() > 0) {

            final String[] myData = banco.SelectAllAvaliadores();
            final AutoCompleteTextView autoCom = (AutoCompleteTextView) findViewById(R.id.MatriculaAvaliador);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, myData);
            autoCom.setAdapter(adapter);

        }

        MatriculaAvaliador.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
//                if (s.length() >0)//MUDAR ESSE VALOR DEPOIS{
//                {
//                    String nome = banco.SelecionaAvaliador(String.valueOf(MatriculaAvaliador.getText()));
//                    NomeAvaliador.setText(nome);
//                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        @SuppressLint("WrongViewCast") Button fab = findViewById(R.id.NextFase1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirServicos();
            }
        });
    }

    private void abrirServicos() {
        Log.d(TAG, "Opção de serviços");
        Intent intent = new Intent(this, ServicoActivity.class);
        startActivity(intent);
    }

    public void onCheckboxClicked(View view) {
        numeroTOI = findViewById(R.id.ToiNumero);

        switch (view.getId()) {
            case R.id.SolicitacaoConsumidor:
                SEM.setChecked(false);
                TOI.setChecked(false);
                numeroTOI.setEnabled(false);
                //prender edittext de numero TOI
                break;

            case R.id.SEM:
                SolicitacaoConsumidor.setChecked(false);
                TOI.setChecked(false);
                numeroTOI.setEnabled(false);
                //prender edittext de numero TOI
                break;

            case R.id.TOI:
                SolicitacaoConsumidor.setChecked(false);
                SEM.setChecked(false);
                numeroTOI.setEnabled(true);
                break;

        }
    }
}
