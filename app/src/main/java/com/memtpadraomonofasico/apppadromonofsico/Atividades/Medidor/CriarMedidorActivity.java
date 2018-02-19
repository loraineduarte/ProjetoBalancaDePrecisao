package com.memtpadraomonofasico.apppadromonofsico.Atividades.Medidor;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class CriarMedidorActivity extends AppCompatActivity {

    private static final String TAG = "Criar Medidor";
    private RadioButton checkEletronico, checkMecanico;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medidor);

        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaMedidores();
        Log.d(TAG, String.valueOf(cursor.getCount()));

        checkEletronico = findViewById(R.id.radioButtonEletronico);
        checkMecanico = findViewById(R.id.RadioButtonMecanico);

        final EditText numSerie = (EditText) findViewById( R.id.numSerie );
        numSerie.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    numSerie.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText ModeloMedidor = (EditText) findViewById( R.id.ModeloMedidor );
        ModeloMedidor.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    ModeloMedidor.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText fabricanteMedidor = (EditText) findViewById( R.id.fabricanteMedidor );
        fabricanteMedidor.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    fabricanteMedidor.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText TensaoNominal = (EditText) findViewById( R.id.TensaoNominal );
        TensaoNominal.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    TensaoNominal.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText CorrenteNominal = (EditText) findViewById( R.id.CorrenteNominal );
        CorrenteNominal.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    CorrenteNominal.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText KdKe = (EditText) findViewById( R.id.KdKe );
        KdKe.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    KdKe.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText Classe = (EditText) findViewById( R.id.Classe );
        Classe.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    Classe.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText NumElementos = (EditText) findViewById( R.id.NumElementos );
        NumElementos.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    NumElementos.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText AnoFabricacao = (EditText) findViewById( R.id.AnoFabricacao );
        AnoFabricacao.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    AnoFabricacao.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText PorInmetro = (EditText) findViewById( R.id.PorInmetro );
        PorInmetro.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    PorInmetro.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );


        Button botaoCriarAvaliador = (Button)findViewById(R.id.buttonSalvarMedidor);

        botaoCriarAvaliador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BancoController crud = new BancoController(getBaseContext());

                EditText numSerie = (EditText)findViewById(R.id.numSerie);
                EditText fabricante = (EditText)findViewById(R.id.fabricanteMedidor);
                EditText numElementos = (EditText)findViewById(R.id.NumElementos);
                EditText modelo = (EditText)findViewById(R.id.ModeloMedidor);
                EditText correnteNominal = (EditText)findViewById(R.id.CorrenteNominal);
                EditText classe = (EditText)findViewById(R.id.Classe);
                EditText anoFabricacao = (EditText)findViewById(R.id.AnoFabricacao);
                EditText tensaoNominal = (EditText)findViewById(R.id.TensaoNominal);
                EditText KdKe = (EditText)findViewById(R.id.KdKe);
                EditText porInmetro = (EditText)findViewById(R.id.PorInmetro);

                String numSerieString = numSerie.getText().toString();
                String fabricanteString = fabricante.getText().toString();
                String numElementosString = numElementos.getText().toString();
                String modeloString = modelo.getText().toString();
                String correnteNominalString = correnteNominal.getText().toString();
                String classeString = classe.getText().toString();
                String anoFabricacaoString = anoFabricacao.getText().toString();
                String tensaoNominalString = tensaoNominal.getText().toString();
                String KdKeString = KdKe.getText().toString();
                String porInmetroString = porInmetro.getText().toString();
                String tipoMedidorString = " ";
                if(checkEletronico.isChecked())
                {
                    tipoMedidorString = "Eletrônico";
                }
                else if(checkMecanico.isChecked()){
                    tipoMedidorString = "Mecânico";
                }

                if(numSerieString.equals("")|| fabricanteString.equals("")|| numElementosString.equals("")|| modeloString.equals("")||
                        correnteNominalString.equals("")|| classeString.equals("")|| anoFabricacaoString.equals("")|| tensaoNominalString.equals("")||
                        KdKeString.equals("")|| porInmetroString.equals("") || tipoMedidorString.equals("")){

                    Toast.makeText(getApplicationContext(), "Campos em branco! ", Toast.LENGTH_LONG).show();
                }
                else {
                    String resultado = crud.insereNovoMedidor(numSerieString,fabricanteString, numElementosString, modeloString, correnteNominalString,
                            classeString, anoFabricacaoString, tensaoNominalString, KdKeString, porInmetroString, tipoMedidorString);
                    Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

        Button botaoLimparCampos = (Button)findViewById(R.id.buttonLimparCamposMedidor);

        botaoLimparCampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText numSerie = (EditText)findViewById(R.id.numSerie);
                EditText fabricante = (EditText)findViewById(R.id.fabricanteMedidor);
                EditText numElementos = (EditText)findViewById(R.id.NumElementos);
                EditText modelo = (EditText)findViewById(R.id.ModeloMedidor);
                EditText correnteNominal = (EditText)findViewById(R.id.CorrenteNominal);
                EditText classe = (EditText)findViewById(R.id.Classe);
                EditText anoFabricacao = (EditText)findViewById(R.id.AnoFabricacao);
                EditText tensaoNominal = (EditText)findViewById(R.id.TensaoNominal);
                EditText KdKe = (EditText)findViewById(R.id.KdKe);
                EditText porInmetro = (EditText)findViewById(R.id.PorInmetro);
                checkEletronico = findViewById(R.id.radioButtonEletronico);
                checkMecanico = findViewById(R.id.RadioButtonMecanico);

                numSerie.getText().clear();
                fabricante.getText().clear();
                numElementos.getText().clear();
                modelo.getText().clear();
                correnteNominal.getText().clear();
                classe.getText().clear();
                anoFabricacao.getText().clear();
                tensaoNominal.getText().clear();
                KdKe.getText().clear();
                porInmetro.getText().clear();
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
}
