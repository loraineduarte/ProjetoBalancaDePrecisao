package com.memtpadraomonofasico.apppadromonofsico.Atividades.Medidor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class CriarMedidorActivity extends AppCompatActivity {

    RadioButton checkEletronico, checkMecanico;
    EditText instalacao, numSerie, numGeral, fabricante, numElementos, modelo, correnteNominal, classe, RR, anoFabricacao, tensaoNominal, KdKe, porInmetro, fios;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medidor);

        final BancoController crud = new BancoController(getBaseContext());
//        Cursor cursor = crud.pegaMedidores();
//        Log.d(TAG, String.valueOf(cursor.getCount()));

        checkEletronico = findViewById(R.id.radioButtonEletronico);
        checkMecanico = findViewById(R.id.RadioButtonMecanico);

        Button botaoCriarAvaliador = findViewById(R.id.buttonSalvarMedidor);
        botaoCriarAvaliador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                instalacao = findViewById(R.id.Instalacao);
                numSerie = findViewById(R.id.numSerie);
                numGeral = findViewById(R.id.NumGeral);
                fabricante = findViewById(R.id.fabricanteMedidor);
                numElementos = findViewById(R.id.NumElementos);
                 modelo = findViewById(R.id.ModeloMedidor);
                 correnteNominal = findViewById(R.id.CorrenteNominal);
                 classe = findViewById(R.id.Classe);
                 RR = findViewById(R.id.RR);
                 anoFabricacao = findViewById(R.id.AnoFabricacao);
                 tensaoNominal = findViewById(R.id.TensaoNominal);
                 KdKe = findViewById(R.id.KdKe);
                 porInmetro =findViewById(R.id.PorInmetro);
                 fios = findViewById(R.id.Fios);

                int instalacaoString = Integer.parseInt(instalacao.getText().toString());
                String numSerieString = numSerie.getText().toString();
                String numGeralString = numGeral.getText().toString();
                String fabricanteString = fabricante.getText().toString();
                int numElementosString = Integer.parseInt(numElementos.getText().toString());
                String modeloString = modelo.getText().toString();
                int correnteNominalString = Integer.parseInt(correnteNominal.getText().toString());
                String classeString = classe.getText().toString();
                int RRString = Integer.parseInt(RR.getText().toString());
                int anoFabricacaoString = Integer.parseInt(anoFabricacao.getText().toString());
                int tensaoNominalString = Integer.parseInt(tensaoNominal.getText().toString());
                int KdKeString = Integer.parseInt(KdKe.getText().toString());
                String porInmetroString = porInmetro.getText().toString();
                int fiosString = Integer.parseInt(fios.getText().toString());
                String tipoMedidorString = " ";
                if(checkEletronico.isChecked())
                {
                    tipoMedidorString = "Eletrônico";
                }
                else if(checkMecanico.isChecked()){
                    tipoMedidorString = "Mecânico";
                }

                if( (instalacao.getText().toString().equals("")) || numSerieString.equals("")|| fabricanteString.equals("")||  modeloString.equals("")||
                        (tensaoNominal.getText().toString().equals(""))|| (correnteNominal.getText().toString().equals("")) || (KdKe.getText().toString().equals(""))
                        ||(numElementos.getText().toString().equals("")) || (anoFabricacao.getText().toString().equals(""))
                        || classeString.equals("")|| porInmetroString.equals("") || tipoMedidorString.equals("")){

                    Toast.makeText(getApplicationContext(), "Campo obrigatório em branco! ", Toast.LENGTH_LONG).show();
                }
                else {
                    String resultado = crud.insereNovoMedidor(instalacaoString, numSerieString,numGeralString, fabricanteString, numElementosString, modeloString, correnteNominalString,
                            classeString,RRString, anoFabricacaoString, tensaoNominalString, KdKeString, porInmetroString,fiosString, tipoMedidorString);
                    Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

        Button botaoLimparCampos = findViewById(R.id.buttonLimparCamposMedidor);
        botaoLimparCampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instalacao = findViewById(R.id.Instalacao);
                numSerie = findViewById(R.id.numSerie);
                numGeral = findViewById(R.id.NumGeral);
                fabricante = findViewById(R.id.fabricanteMedidor);
                numElementos = findViewById(R.id.NumElementos);
                modelo = findViewById(R.id.ModeloMedidor);
                correnteNominal = findViewById(R.id.CorrenteNominal);
                classe = findViewById(R.id.Classe);
                RR = findViewById(R.id.RR);
                anoFabricacao = findViewById(R.id.AnoFabricacao);
                tensaoNominal = findViewById(R.id.TensaoNominal);
                KdKe = findViewById(R.id.KdKe);
                porInmetro = findViewById(R.id.PorInmetro);
                fios = findViewById(R.id.Fios);
                checkEletronico = findViewById(R.id.radioButtonEletronico);
                checkMecanico = findViewById(R.id.RadioButtonMecanico);

                instalacao.getText().clear();
                numSerie.getText().clear();
                numGeral.getText().clear();
                fabricante.getText().clear();
                numElementos.getText().clear();
                modelo.getText().clear();
                correnteNominal.getText().clear();
                classe.getText().clear();
                RR.getText().clear();
                anoFabricacao.getText().clear();
                tensaoNominal.getText().clear();
                KdKe.getText().clear();
                porInmetro.getText().clear();
                fios.getText().clear();
                checkMecanico.setChecked(false);
                checkEletronico.setChecked(false);

            }
        });
    }

    public void onCheckboxClicked(View view) {

        switch(view.getId()) {
            case R.id.radioButtonEletronico:
                checkMecanico.setChecked(false);
                break;

            case R.id.RadioButtonMecanico:
                checkEletronico.setChecked(false);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
