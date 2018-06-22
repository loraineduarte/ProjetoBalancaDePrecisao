package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Medidor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class EditarMedidorActivity extends AppCompatActivity {

    private EditText numeroSerie, numeroGeral, instalacao, modelo, fabricante, tensaoNominal, correnteNominal, tipoMedidor, kdKe, rr, numElementos, anoFabricacao, classe, fios, portariaInmetro;
    private String numeroSerieString, numeroGeralString, instalacaoString, modeloString, fabricanteString, tensaoNominalString, correnteNominalString, tipoMedidorString, kdKeString, rrString,
            numElementosString, anoFabricacaoString, classeString, fiosString, portariaInmetroString;
    private RadioButton radioButtonMecanico, radioButtonEletronico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_medidor);

        final BancoController crud = new BancoController(getBaseContext());
        Intent it = getIntent();
        String numeroSerieAntigo = it.getStringExtra("numeroSerie");
        String numeroGeralAntigo = it.getStringExtra("numeroGeral");
        String instalacaoAntigo = it.getStringExtra("instalacao");
        String modeloAntigo = it.getStringExtra("modelo");
        String fabricanteAntigo = it.getStringExtra("fabricante");
        String tensaoNominalAntigo = it.getStringExtra("tensaoNominal");
        String correnteNominalAntigo = it.getStringExtra("correnteNominal");
        String tipoMedidorAntigo = it.getStringExtra("tipoMedidor");
        String kdKeAntigo = it.getStringExtra("kdKe");
        String rrAntigo = it.getStringExtra("rr");
        String numElementosAntigo = it.getStringExtra("numElementos");
        String anoFabricacaoAntigo = it.getStringExtra("anoFabricacao");
        String classeAntigo = it.getStringExtra("classe");
        String fiosAntigo = it.getStringExtra("fios");
        String portariaInmetroAntigo = it.getStringExtra("portariaInmetro");


        Button botaoCriarMedidor = findViewById(R.id.buttonSalvarAvaliador);

//        nome = findViewById(R.id.nomeAvaliador);
//        nome.setText(nomeAntigoString);
//        matricula = findViewById(R.id.numeroMatriculaAvaliador);
//        matricula.setText(matriculaAntigoString);
//        senha = findViewById(R.id.senhaAvaliador);
//        senha.setText(senhaAntigoString);
//
//        radioButtonAvaliador = findViewById(R.id.radioButtonAvaliador);
//        radioButtonEletronico = findViewById(R.id.radioButtonEletronico);
//        if (tipoUsuarioAntigoString.equals("true")) {
//            radioButtonEletronico.setChecked(true);
//        } else {
//            radioButtonAvaliador.setChecked(true);
//        }
//
//        botaoCriarAvaliador.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                nomeString = nome.getText().toString();
//                matriculaString = matricula.getText().toString();
//                senhaString = senha.getText().toString();
//
//                if (radioButtonAvaliador.isChecked()) {
//                    tipoUsuarioString = "false";
//
//                } else if (radioButtonEletronico.isChecked()) {
//                    tipoUsuarioString = "true";
//                }
//
//
//                if (nomeString.equals("") || matriculaString.equals("") || senhaString.equals("") || tipoUsuarioString.equals("")) {
//                    Toast.makeText(getApplicationContext(), "Campos em branco! ", Toast.LENGTH_LONG).show();
//                } else {
//                    String resultado = crud.updateAvaliador(nomeString, matriculaString, senhaString, Boolean.parseBoolean(tipoUsuarioString), nomeAntigoString, matriculaAntigoString);
//                    Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
//
//                    finish();
//                }
//
//            }
//        });
    }


}
