package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.CircuitoPotencial;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoConformidade.InspecaoConformidadeActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class CircuitoPotencialActivity extends AppCompatActivity {

    RadioButton normal, reprovado;
    String status, observacaoRegistrador;
    Spinner opcoesReprovados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circuito_potencial);

        Log.d("Circuito Potencial ", String.valueOf(Hawk.count()));

        normal = findViewById(R.id.normal);
        reprovado = findViewById(R.id.Reprovado);


        if(normal.isChecked()){
            status = "Normal/ Não Possui";

        } else if (reprovado.isChecked()){
            status = "Reprovado";

        }

        opcoesReprovados = (Spinner) findViewById(R.id.RegistradorSpinner);
        opcoesReprovados.setEnabled(false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,  R.array.ReprovadoCircuitoPotencial, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        opcoesReprovados.setAdapter(adapter);
        opcoesReprovados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                status = parent.getItemAtPosition(position).toString();
                Log.d("SELECIONADO", status);
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
                Hawk.put("statusCircuitoPotencial", status);
                abrirInspecaoConformidade();

            }
        });
    }

    private void abrirInspecaoConformidade() {

        Intent intent = new Intent(this, InspecaoConformidadeActivity.class);
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
