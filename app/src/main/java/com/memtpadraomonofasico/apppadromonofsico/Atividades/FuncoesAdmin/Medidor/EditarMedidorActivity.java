package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Medidor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class EditarMedidorActivity extends AppCompatActivity {

    private EditText numeroSerie, numeroGeral, instalacao, modelo, fabricante, tensaoNominal, correnteNominal, tipoMedidor, kdKe, rr, numElementos, anoFabricacao, classe, fios, portariaInmetro;
    private String numeroSerieString, numeroGeralString, instalacaoString, modeloString, fabricanteString, tensaoNominalString, correnteNominalString, tipoMedidorString, kdKeString, rrString,
            numElementosString, anoFabricacaoString, classeString, fiosString, portariaInmetroString;
    private RadioButton radioButtonMecanico, radioButtonEletronico;
    private Button botaoCriarMedidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_medidor);

        final BancoController crud = new BancoController(getBaseContext());
        final Intent it = getIntent();
        final String numeroSerieAntigo = it.getStringExtra("numeroSerie");
        final String numeroGeralAntigo = it.getStringExtra("numeroGeral");
        final String instalacaoAntigo = it.getStringExtra("instalacao");
        final String modeloAntigo = it.getStringExtra("modelo");
        final String fabricanteAntigo = it.getStringExtra("fabricante");
        final String tensaoNominalAntigo = it.getStringExtra("tensaoNominal");
        final String correnteNominalAntigo = it.getStringExtra("correnteNominal");
        final String tipoMedidorAntigo = it.getStringExtra("tipoMedidor");
        final String kdKeAntigo = it.getStringExtra("kdKe");
        final String rrAntigo = it.getStringExtra("rr");
        final String numElementosAntigo = it.getStringExtra("numElementos");
        final String anoFabricacaoAntigo = it.getStringExtra("anoFabricacao");
        final String classeAntigo = it.getStringExtra("classe");
        final String fiosAntigo = it.getStringExtra("fios");
        final String portariaInmetroAntigo = it.getStringExtra("portariaInmetro");


        botaoCriarMedidor = findViewById(R.id.buttonSalvarMedidor);

        numeroSerie = findViewById(R.id.numSerie);
        numeroSerie.setText(numeroSerieAntigo);
        numeroGeral = findViewById(R.id.NumGeral);
        numeroGeral.setText(numeroGeralAntigo);
        instalacao = findViewById(R.id.Instalacao);
        instalacao.setText(instalacaoAntigo);
        modelo = findViewById(R.id.ModeloMedidor);
        modelo.setText(modeloAntigo);
        fabricante = findViewById(R.id.fabricanteMedidor);
        fabricante.setText(fabricanteAntigo);
        tensaoNominal = findViewById(R.id.TensaoNominal);
        tensaoNominal.setText(tensaoNominalAntigo);
        correnteNominal = findViewById(R.id.CorrenteNominal);
        correnteNominal.setText(correnteNominalAntigo.toString());
        kdKe = findViewById(R.id.KdKe);
        kdKe.setText(kdKeAntigo);
        rr = findViewById(R.id.RR);
        rr.setText(rrAntigo);
        numElementos = findViewById(R.id.NumElementos);
        numElementos.setText(numElementosAntigo);
        anoFabricacao = findViewById(R.id.AnoFabricacao);
        anoFabricacao.setText(anoFabricacaoAntigo);
        classe = findViewById(R.id.Classe);
        classe.setText(classeAntigo);
        fios = findViewById(R.id.Fios);
        fios.setText(fiosAntigo);
        portariaInmetro = findViewById(R.id.PorInmetro);
        portariaInmetro.setText(portariaInmetroAntigo);

        radioButtonMecanico = findViewById(R.id.RadioButtonMecanico);
        radioButtonEletronico = findViewById(R.id.radioButtonEletronico);
        if (tipoMedidorAntigo.startsWith("M")) {
            radioButtonMecanico.setChecked(true);
            radioButtonEletronico.setChecked(false);
        } else {
            radioButtonEletronico.setChecked(true);
            radioButtonMecanico.setChecked(false);
        }

        botaoCriarMedidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                numeroSerieString = numeroSerie.getText().toString();
                numeroGeralString = numeroGeral.getText().toString();
                instalacaoString = instalacao.getText().toString();
                modeloString = modelo.getText().toString();
                fabricanteString = fabricante.getText().toString();
                tensaoNominalString = tensaoNominal.getText().toString();
                correnteNominalString = correnteNominal.getText().toString();
                kdKeString = kdKe.getText().toString();
                rrString = rr.getText().toString();
                numElementosString = numElementos.getText().toString();
                anoFabricacaoString = anoFabricacao.getText().toString();
                classeString = classe.getText().toString();
                fiosString = fios.getText().toString();
                portariaInmetroString = portariaInmetro.getText().toString();
                radioButtonMecanico = findViewById(R.id.RadioButtonMecanico);
                radioButtonMecanico = findViewById(R.id.RadioButtonMecanico);
                radioButtonEletronico = findViewById(R.id.radioButtonEletronico);

                if (radioButtonMecanico.isChecked()) {
                    tipoMedidorString = "Mecânico";

                } else if (radioButtonEletronico.isChecked()) {
                    tipoMedidorString = "Eletrônico";
                }


//                if (nomeString.equals("") || matriculaString.equals("") || senhaString.equals("") || tipoUsuarioString.equals("")) {
//                    Toast.makeText(getApplicationContext(), "Campos em branco! ", Toast.LENGTH_LONG).show();
//                } else {
                String resultado = crud.updateMedidor(numeroSerieAntigo, numeroSerieString, numeroGeralString, instalacaoString, modeloString, fabricanteString, tensaoNominalString,
                        correnteNominalString, tipoMedidorString, kdKeString, rrString, numElementosString, anoFabricacaoString, classeString, fiosString, portariaInmetroString);
                Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();

                finish();
            }

//            }
        });
    }


}
