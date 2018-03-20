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
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoConformidade.InspecaoConformidadeActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

/**
 *
 */
@SuppressWarnings("ALL")
public class CircuitoPotencialActivity extends AppCompatActivity {

    private RadioButton normal;
    private RadioButton reprovado;
    private String status;
    private Spinner opcoesReprovados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circuito_potencial);

        Log.d("Circuito Potencial ", String.valueOf(Hawk.count()));

        normal = findViewById(R.id.normal);
        reprovado = findViewById(R.id.Reprovado);

        opcoesReprovados = findViewById(R.id.RegistradorSpinner);
        opcoesReprovados.setEnabled(false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,  R.array.ReprovadoCircuitoPotencial, android.R.layout.simple_spinner_item);
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

                if ((!normal.isChecked()) &&  (!reprovado.isChecked())) {
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Não existe opção de status marcado. ", Toast.LENGTH_LONG).show();

                } else {
                    Hawk.put("statusCircuitoPotencial", status);
                    abrirInspecaoConformidade();
                }


            }
        });
    }

    private void abrirInspecaoConformidade() {

        Intent intent = new Intent(this, InspecaoConformidadeActivity.class);
        startActivity(intent);
    }

    /**
     * @param view
     */
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
