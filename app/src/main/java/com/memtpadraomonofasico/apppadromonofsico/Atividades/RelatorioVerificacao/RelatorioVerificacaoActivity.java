package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class RelatorioVerificacaoActivity extends AppCompatActivity {

    private static final String TAG = "RelatórioVerificação";

    String matricula, nome, toiNumero;
    private RadioButton SolicitacaoConsumidor, SEM, TOI;
    final CriaBanco banco = new CriaBanco(this);


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Hawk.init(this).build();
        Hawk.deleteAll();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_verificacao);

        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaAvaliadores();
        Log.d(TAG, String.valueOf(cursor.getCount()));

        final EditText nomeAvaliador= findViewById(R.id.NomeAvaliador);
        nome = String.valueOf(nomeAvaliador.getText());

        final EditText MatriculaAvaliador = findViewById(R.id.MatriculaAvaliador);
        matricula = String.valueOf(MatriculaAvaliador.getText());
        MatriculaAvaliador.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MatriculaAvaliador.setText("", TextView.BufferType.EDITABLE);
                    nomeAvaliador.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

        SolicitacaoConsumidor = findViewById(R.id.SolicitacaoConsumidor);
        SEM = findViewById(R.id.SEM);
        TOI = findViewById(R.id.TOI);

        final EditText ToiNumero = findViewById(R.id.ToiNumero);
        toiNumero = String.valueOf(ToiNumero.getText());
        ToiNumero.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ToiNumero.setText("", TextView.BufferType.EDITABLE);
                }
            }

        });

        if (cursor.getCount() > 0) {
            final String[] myData = banco.SelectAllAvaliadores();
            final AutoCompleteTextView autoCom = findViewById(R.id.MatriculaAvaliador);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, myData);
            autoCom.setAdapter(adapter);

        }

        @SuppressLint("WrongViewCast") Button fab = findViewById(R.id.NextFase1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nome.isEmpty() || matricula.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Selecionar o avaliador! ", Toast.LENGTH_LONG).show();
                } else if((!SolicitacaoConsumidor.isChecked())&& (!SEM.isChecked() )&& ( !TOI.isChecked()) ){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Marcar o tipo de relatório! ", Toast.LENGTH_LONG).show();
                } else if (((TOI.isChecked()) && (toiNumero.equals("")) )){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Colocar o número do TOI ! ", Toast.LENGTH_LONG).show();
                } else{
                    abrirServicos();
                }

            }
        });


    }

    private void abrirServicos() {
        //salvando no vetor geral - para gerar relatório
        Hawk.put("NomeAvaliador",nome);
        Hawk.put("MatriculaAvaliador", matricula);

        if(SolicitacaoConsumidor.isChecked()){
            Hawk.put("TipoSolicitação", "Solicitação do Consumidor");

        } else if (SEM.isChecked()){
            Hawk.put("TipoSolicitação", "SEM");

        } else if(TOI.isChecked()){
            Hawk.put("TipoSolicitação", "TOI");
            Hawk.put("TOINumbero", toiNumero);

        }

        Log.d(TAG, "Opção de serviços");
        Intent intent = new Intent(this, ServicoActivity.class);
        startActivity(intent);
    }



    public void onCheckboxClicked(View view) {

        final EditText ToiNumero = findViewById(R.id.ToiNumero);
        switch (view.getId()) {
            case R.id.SolicitacaoConsumidor:
                SEM.setChecked(false);
                TOI.setChecked(false);
                ToiNumero.setEnabled(false);
                break;

            case R.id.SEM:
                SolicitacaoConsumidor.setChecked(false);
                TOI.setChecked(false);
                ToiNumero.setEnabled(false);
                break;

            case R.id.TOI:
                SolicitacaoConsumidor.setChecked(false);
                SEM.setChecked(false);
                ToiNumero.setEnabled(true);
                break;

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putCharSequence("matricula", matricula);
        savedInstanceState.putCharSequence("nomeAvaliador", nome);
        savedInstanceState.putCharSequence("numeroTOI", toiNumero);

        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, String.valueOf(savedInstanceState));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

    }


}
