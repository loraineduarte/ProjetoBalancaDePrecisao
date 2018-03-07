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

    String[] nome;
    String toiNumero , matricula, nomeAvaliadorString;
    private RadioButton SEM, TOI;
    private FloatingActionButton botaoProcurar;
    EditText MatriculaAvaliador, nomeAvaliador, ToiNumero;
    final CriaBanco banco = new CriaBanco(this);


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_verificacao);

        Date hora = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        final String horaInicialFormatada = sdf.format(hora);

        Hawk.init(this).build();

        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaAvaliadores();

        MatriculaAvaliador = findViewById(R.id.MatriculaAvaliador);
        nomeAvaliador= findViewById(R.id.NomeAvaliador);
        SEM = findViewById(R.id.SEM);
        TOI = findViewById(R.id.TOI);
        ToiNumero = findViewById(R.id.ToiNumero);

        botaoProcurar = findViewById(R.id.ProcurarAvaliador);
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




        @SuppressLint("WrongViewCast") Button fab = findViewById(R.id.NextFase1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Hawk.deleteAll();
                matricula = String.valueOf(MatriculaAvaliador.getText());
                nomeAvaliadorString = String.valueOf(nomeAvaliador.getText());
                toiNumero = String.valueOf(ToiNumero.getText());

                if(matricula.length()==0 || nomeAvaliadorString.length()==0){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Selecionar o avaliador! ", Toast.LENGTH_LONG).show();
                } else
                if((!SEM.isChecked() )&& ( !TOI.isChecked()) ){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Marcar o tipo de relatório! ", Toast.LENGTH_LONG).show();

                } else if (((TOI.isChecked()) && (toiNumero.length()==0) )){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Colocar o número do TOI ! ", Toast.LENGTH_LONG).show();

                } else{

                    MatriculaAvaliador = findViewById(R.id.MatriculaAvaliador);
                    nomeAvaliador= findViewById(R.id.NomeAvaliador);
                    SEM = findViewById(R.id.SEM);
                    TOI = findViewById(R.id.TOI);
                    ToiNumero = findViewById(R.id.ToiNumero);

                    Log.d("Nome", String.valueOf(nomeAvaliador.getText()));
                    Log.d("MATRICULA",String.valueOf(MatriculaAvaliador.getText()));

                    Hawk.put("HoraInicial",horaInicialFormatada);
                    Hawk.put("NomeAvaliador",String.valueOf(nomeAvaliador.getText()));
                    Hawk.put("MatriculaAvaliador", String.valueOf(MatriculaAvaliador.getText()));



                    if (SEM.isChecked()){
                        Hawk.put("TipoSolicitação", "SEM");

                    } else if(TOI.isChecked()){
                        Hawk.put("TipoSolicitação", "TOI");
                        Hawk.put("TOINumero", String.valueOf(ToiNumero.getText()));
                    }

                    abrirServicos();
                }
            }
        });

    }

    private void abrirServicos() {

        Intent intent = new Intent(this, ServicoActivity.class);
        startActivity(intent);
    }

    public void onCheckboxClicked(View view) {



        ToiNumero = findViewById(R.id.ToiNumero);
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

        savedInstanceState.putCharSequence("matricula", String.valueOf(MatriculaAvaliador.getText()));
        savedInstanceState.putCharSequence("nomeAvaliador", String.valueOf(nomeAvaliador.getText()));
        savedInstanceState.putCharSequence("numeroTOI", String.valueOf(ToiNumero.getText()));

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }



    public void doMyThing() {

        MatriculaAvaliador = findViewById(R.id.MatriculaAvaliador);
        matricula = String.valueOf(MatriculaAvaliador.getText());

        if (matricula.equals("")) {
            Toast.makeText(getApplicationContext(), "Coloque um número de matrícula para a pesquisa. ", Toast.LENGTH_LONG).show();
        }
        if (matricula.length()>0){
            nome = banco.SelecionaAvaliador(matricula);
            nomeAvaliador.setText(nome[0]);
        }

    }
}
