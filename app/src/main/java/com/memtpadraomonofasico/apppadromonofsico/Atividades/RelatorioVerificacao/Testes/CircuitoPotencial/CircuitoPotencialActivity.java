package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.CircuitoPotencial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Exatidao.ExatidaoActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.MarchaVazio.MarchaVazioActivity;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.util.ArrayList;
import java.util.List;

public class CircuitoPotencialActivity extends AppCompatActivity {

    private RadioButton normal;
    private RadioButton reprovado;
    private String status;
    private Spinner opcoesReprovados;
    private List<String> av = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circuito_potencial);
        final BancoController crud = new BancoController(getBaseContext());

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();


        normal = findViewById(R.id.normal);
        reprovado = findViewById(R.id.Reprovado);

        opcoesReprovados = findViewById(R.id.RegistradorSpinner);
        opcoesReprovados.setEnabled(false);

        av = todasMensagens();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, av);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcoesReprovados.setAdapter(adapter);
        opcoesReprovados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase4);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hawk.delete("statusCircuitoPotencial");

                normal = findViewById(R.id.normal);
                reprovado = findViewById(R.id.Reprovado);

                if(normal.isChecked()){
                    status = "Normal/ Não Possui";
                }
                if (reprovado.isChecked()){
                    status = "Reprovado";
                }
                    Hawk.put("statusCircuitoPotencial", status);
                abrirMarchaVazio();


            }
        });
    }

    private List<String> todasMensagens() {

        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaMensagemEspecifica("Circuito de potencial");
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String corpoMensagem = cursor.getString(2);
            av.add(corpoMensagem);
        }
        return av;
    }

    private void abrirMarchaVazio() {
        Intent intent = new Intent(this, MarchaVazioActivity.class);
        startActivity(intent);
    }


    private void abrirInspecaoConformidade() {

        Intent intent = new Intent(this, ExatidaoActivity.class);
        startActivity(intent);
    }


    public void onCheckboxClicked(View view) {

        switch (view.getId()) {
            case R.id.normal:
                reprovado.setChecked(false);
                opcoesReprovados.setEnabled(false);
                break;


            case R.id.Reprovado:
                normal.setChecked(false);
                opcoesReprovados.setEnabled(true);
                break;

        }
    }


}
