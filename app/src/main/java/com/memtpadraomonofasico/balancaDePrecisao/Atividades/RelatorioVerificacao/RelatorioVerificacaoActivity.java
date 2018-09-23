package com.memtpadraomonofasico.balancaDePrecisao.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.memtpadraomonofasico.balancaDePrecisao.BancoDeDados.BancoController;
import com.memtpadraomonofasico.balancaDePrecisao.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.balancaDePrecisao.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RelatorioVerificacaoActivity extends AppCompatActivity  {
    private final CriaBanco banco = new CriaBanco(this);
    private String toiNumero, matricula, nomeAvaliadorString, gerenteAvaliador, modeloPadrao;
    private RadioButton SEM, TOI;
    private EditText MatriculaAvaliador, nomeAvaliador, ToiNumero, nomeGerente;
    private String horaInicialFormatada = null;
    private List<String> av = new ArrayList<>();


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_verificacao);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();


        Date hora = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        horaInicialFormatada = sdf.format(hora);

        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaAvaliadores();

        MatriculaAvaliador = findViewById(R.id.MatriculaAvaliador);
        nomeAvaliador= findViewById(R.id.NomeAvaliador);
        nomeGerente = findViewById(R.id.GerenteAvaliador);
        SEM = findViewById(R.id.SEM);
        TOI = findViewById(R.id.TOI);
        ToiNumero = findViewById(R.id.ToiNumero);

        Spinner opcoesModeloPadrao = findViewById(R.id.PadraoModelo);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.ModelosPadrao, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcoesModeloPadrao.setAdapter(adapter2);
        opcoesModeloPadrao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modeloPadrao = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FloatingActionButton botaoProcurar = findViewById(R.id.ProcurarAvaliador);
        botaoProcurar.setClickable(true);
        botaoProcurar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doMyThing();
            }
        });

        if (cursor.getCount() > 0) {
            final String[] myData = banco.SelectAllAvaliadores();
            final AutoCompleteTextView autoCom = (AutoCompleteTextView) MatriculaAvaliador;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, myData);
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
                gerenteAvaliador = String.valueOf(nomeGerente.getText());

                if(matricula.length()==0 || nomeAvaliadorString.length()==0){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Selecionar o avaliador! ", Toast.LENGTH_LONG).show();
                } else if((!SEM.isChecked() )&& ( !TOI.isChecked()) ){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Marcar o tipo de relatório! ", Toast.LENGTH_LONG).show();

                } else if (((TOI.isChecked()) && (toiNumero.length()==0) )){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Colocar o número do TOI ! ", Toast.LENGTH_LONG).show();

                } else if (modeloPadrao.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Selecionar o Modelo do Padrão! ", Toast.LENGTH_LONG).show();

                } else{
                    Hawk.put("HoraInicial", horaInicialFormatada);
                    Hawk.put("NomeAvaliador", String.valueOf(nomeAvaliador.getText()));
                    Hawk.put("MatriculaAvaliador", String.valueOf(MatriculaAvaliador.getText()));
                    Hawk.put("GerenteAvaliador", String.valueOf(nomeGerente.getText()));
                    Hawk.put("ModeloPadrao", String.valueOf(modeloPadrao));

                    abrirServicos();
                }
            }
        });

    }

    private void abrirServicos() {


        if (SEM.isChecked()){
            Hawk.put("TipoSolicitação", "SEM");
            Hawk.put("TOINumero", "");

        } else if(TOI.isChecked()){
            Hawk.put("TipoSolicitação", "TOI");
            Hawk.put("TOINumero", String.valueOf(ToiNumero.getText()));

        }

        Intent intent = new Intent(this, ServicoActivity.class);
        startActivity(intent);
    }

    public void onCheckboxClicked(View view) {

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
        savedInstanceState.putCharSequence("nomegerente", String.valueOf(nomeGerente.getText()));

        //salvando o Hawk
        savedInstanceState.putCharSequence("HoraInicial",horaInicialFormatada);
        savedInstanceState.putCharSequence("NomeAvaliador",String.valueOf(nomeAvaliador.getText()));
        savedInstanceState.putCharSequence("MatriculaAvaliador", String.valueOf(MatriculaAvaliador.getText()));

        if (SEM.isChecked()){
            savedInstanceState.putCharSequence("TipoSolicitação", "SEM");

        } else if(TOI.isChecked()){
            savedInstanceState.putCharSequence("TipoSolicitação", "TOI");
            savedInstanceState.putCharSequence("TOINumero", String.valueOf(ToiNumero.getText()));

        }

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        toiNumero = savedInstanceState.getString("matricula");
        matricula = savedInstanceState.getString("nomeAvaliador");
        nomeAvaliadorString = savedInstanceState.getString("numeroTOI");

        //restoring hawk
        Hawk.put("HoraInicial", savedInstanceState.getString("HoraInicial"));
        Hawk.put("NomeAvaliador", savedInstanceState.getString("NomeAvaliador"));
        Hawk.put("MatriculaAvaliador", savedInstanceState.getString("MatriculaAvaliador"));
        Hawk.put("TipoSolicitação", savedInstanceState.getString("TipoSolicitação"));
        Hawk.put("TOINumero", savedInstanceState.getString("TOINumero"));


    }

    private void doMyThing() {

        matricula = String.valueOf(MatriculaAvaliador.getText());
        Log.d("MATRICULA", matricula);

        if ((matricula.equals(""))|| (matricula.isEmpty())) {
            Toast.makeText(getApplicationContext(), "Coloque um número de matrícula para a pesquisa. ", Toast.LENGTH_LONG).show();
        }
        if (matricula.length()>0){
            String[] nome = banco.SelecionaAvaliador(matricula);

            if(nome == null){
                Toast.makeText(getApplicationContext(), "Coloque um número de matrícula válido para a pesquisa. ", Toast.LENGTH_LONG).show();
            } else {
                nomeAvaliador.setText(nome[0]);
            }
        }
    }

}
