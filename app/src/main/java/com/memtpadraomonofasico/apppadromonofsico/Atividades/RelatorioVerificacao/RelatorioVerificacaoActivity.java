package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RelatorioVerificacaoActivity extends AppCompatActivity  {

    private static final String TAG = "RelatórioVerificação";

    String matricula, nome, toiNumero;
    private RadioButton SEM, TOI;
    private FloatingActionButton botaoProcurar;
    final CriaBanco banco = new CriaBanco(this);


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_verificacao);

        Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        final String horaInicialFormatada = sdf.format(hora);

        Log.d("Hora", horaInicialFormatada);

        Hawk.init(this).build();


        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaAvaliadores();
        Log.d("Avaliadores", String.valueOf(cursor.getCount()));

        botaoProcurar = (FloatingActionButton)findViewById(R.id.ProcurarAvaliador);
        botaoProcurar.setClickable(true);
        botaoProcurar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doMyThing();
            }
        });

        if (cursor.getCount() > 0) {
            final String[] myData = banco.SelectAllAvaliadores();
            final AutoCompleteTextView autoCom = findViewById(R.id.MatriculaAvaliador);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, myData);
            autoCom.setAdapter(adapter);
        }


        final EditText MatriculaAvaliador = findViewById(R.id.MatriculaAvaliador);
        matricula = String.valueOf(MatriculaAvaliador.getText());

        final EditText nomeAvaliador= findViewById(R.id.NomeAvaliador);
        nome = String.valueOf(nomeAvaliador.getText());

        SEM = findViewById(R.id.SEM);
        TOI = findViewById(R.id.TOI);

        final EditText ToiNumero = findViewById(R.id.ToiNumero);
        toiNumero = String.valueOf(ToiNumero.getText());

        @SuppressLint("WrongViewCast") Button fab = findViewById(R.id.NextFase1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                Log.d("NOME", nome);
//                Log.d("matricula", matricula);

//                if(nome.isEmpty() || matricula.isEmpty()){
//                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Selecionar o avaliador! ", Toast.LENGTH_LONG).show();
//                } else
                if((!SEM.isChecked() )&& ( !TOI.isChecked()) ){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Marcar o tipo de relatório! ", Toast.LENGTH_LONG).show();

                } else if (((TOI.isChecked()) && (toiNumero.equals("")) )){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Colocar o número do TOI ! ", Toast.LENGTH_LONG).show();

                } else{

                    Hawk.deleteAll();
                    Hawk.put("HoraInicial",horaInicialFormatada);
                    Hawk.put("NomeAvaliador",nome);
                    Hawk.put("MatriculaAvaliador", matricula);

                    if (SEM.isChecked()){
                        Hawk.put("TipoSolicitação", "SEM");

                    } else if(TOI.isChecked()){
                        Hawk.put("TipoSolicitação", "TOI");
                        Hawk.put("TOINumbero", toiNumero);

                    }
                    abrirServicos();
                }

            }
        });





    }

    private void abrirServicos() {

        Log.d(TAG, "Opção de serviços");
        Intent intent = new Intent(this, ServicoActivity.class);
        startActivity(intent);
    }

    public void onCheckboxClicked(View view) {

        final EditText ToiNumero = findViewById(R.id.ToiNumero);
        switch (view.getId()) {
            case R.id.SEM:
                TOI.setChecked(false);
                ToiNumero.setEnabled(false);
                break;

            case R.id.TOI:
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



    public void doMyThing() {

        final EditText MatriculaAvaliador = findViewById(R.id.MatriculaAvaliador);
        matricula = String.valueOf(MatriculaAvaliador.getText());

        if (matricula.equals("")) {
            Toast.makeText(getApplicationContext(), "Coloque um número de matrícula para a pesquisa. ", Toast.LENGTH_LONG).show();
        } else {
            nome = banco.SelecionaAvaliador(String.valueOf(MatriculaAvaliador.getText()));
            Log.d("NOME AFTER :", nome);
        }

    }
}
