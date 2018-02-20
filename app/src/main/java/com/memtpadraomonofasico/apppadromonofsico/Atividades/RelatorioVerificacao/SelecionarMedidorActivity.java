package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoVisual.InspecaoVisualActivity;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class SelecionarMedidorActivity extends AppCompatActivity {


    private static final String TAG = "Selecionar Medidor";
    final CriaBanco banco = new CriaBanco(this);
    String numeroSerieMedidor, modeloMedidor, fabricanteMedidor, tensaoNominalMedidor, correnteNominalMedidor, tipoMedidor,
            kdkeMedidor, classeMedidor, numElementosMedidor, anoFabricacaoMedidor, portariaInmetroMedidor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Hawk.delete("NumeroSerieMedidor");
        Hawk.delete("ModeloMedidor");
        Hawk.delete("FaricanteMedidor");
        Hawk.delete("TensaoNominalMedidor");
        Hawk.delete("CorrenteNominalMedidor");
        Hawk.delete("TipoMedidor");
        Hawk.delete("KdKeMedidor");
        Hawk.delete("ClasseMedidor");
        Hawk.delete("NumElementosMedidor");
        Hawk.delete("AnoFabricacaoMedidor");
        Hawk.delete("PortariaInmetroMedidor");

        Log.d("MEDIDOR", String.valueOf(Hawk.count()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_medidor);

        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaMedidores();
        Log.d(TAG, String.valueOf(cursor.getCount()));

        if (cursor.getCount() > 0) {

            final String[] myData = banco.SelectAllMedidores();
            final AutoCompleteTextView autoCom = (AutoCompleteTextView) findViewById(R.id.NumSerieMedidor);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, myData);
            autoCom.setAdapter(adapter);
        }


        final EditText numSerie = (EditText) findViewById( R.id.NumSerieMedidor );
        numeroSerieMedidor = String.valueOf(numSerie.getText());
        numSerie.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    numSerie.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText ModeloMedidor = (EditText) findViewById( R.id.ModeloMedidor );
        modeloMedidor = String.valueOf(ModeloMedidor.getText());

        final EditText FabricanteMedidor = (EditText) findViewById( R.id.fabricanteMedidor );
        fabricanteMedidor = String.valueOf(FabricanteMedidor.getText());

        final EditText TensaoNominalMedidor = (EditText) findViewById( R.id.TensaoNominal );
        tensaoNominalMedidor = String.valueOf(TensaoNominalMedidor.getText());

        final EditText CorrenteNominalMedidor = (EditText) findViewById( R.id.CorrenteNominal );
        correnteNominalMedidor = String.valueOf(CorrenteNominalMedidor.getText());

        final RadioButton eletronico = (RadioButton) findViewById( R.id.radioButtonEletronico );
        final RadioButton mecanico = (RadioButton) findViewById( R.id.RadioButtonMecanico );
        if(eletronico.isChecked()){
            tipoMedidor = "Mecãnico";
        }
        else if(mecanico.isChecked()){
            tipoMedidor = "Eletrônico";
        }

        final EditText KDKE = (EditText) findViewById( R.id.KdKe );
        kdkeMedidor = String.valueOf(KDKE.getText());

        final EditText ClasseMedidor = (EditText) findViewById( R.id.Classe );
        classeMedidor = String.valueOf(ClasseMedidor.getText());

        final EditText NumElementos = (EditText) findViewById( R.id.numElementos );
        numElementosMedidor = String.valueOf(NumElementos.getText());

        final EditText AnoFabricacao = (EditText) findViewById( R.id.AnoFabricacao );
        anoFabricacaoMedidor = String.valueOf(AnoFabricacao.getText());

        final EditText PortariaInmetro = (EditText) findViewById( R.id.PorInmetro );
        portariaInmetroMedidor = String.valueOf(PortariaInmetro.getText());


        @SuppressLint("WrongViewCast") Button next =  findViewById(R.id.NextFase3);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numeroSerieMedidor.isEmpty()|| modeloMedidor.isEmpty()|| fabricanteMedidor.isEmpty()|| tensaoNominalMedidor.isEmpty()|| correnteNominalMedidor.isEmpty() ||
                        kdkeMedidor.isEmpty()|| classeMedidor.isEmpty()|| numElementosMedidor.isEmpty()|| anoFabricacaoMedidor.isEmpty()|| portariaInmetroMedidor.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Campo em Branco! ", Toast.LENGTH_LONG).show();

                }else if ((!eletronico.isChecked()) && (!mecanico.isChecked())){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Selecione o tipo de medidor! ", Toast.LENGTH_LONG).show();

                } else{
                    abrirInspecaoVisual();
                }

            }
        });
    }



    private void abrirInspecaoVisual() {
        Log.d(TAG, "Opção de serviços");

        Hawk.put("NumeroSerieMedidor",numeroSerieMedidor);
        Hawk.put("ModeloMedidor", modeloMedidor);
        Hawk.put("FaricanteMedidor", fabricanteMedidor);
        Hawk.put("TensaoNominalMedidor", tensaoNominalMedidor);
        Hawk.put("CorrenteNominalMedidor", correnteNominalMedidor);
        Hawk.put("TipoMedidor", tipoMedidor);
        Hawk.put("KdKeMedidor", kdkeMedidor);
        Hawk.put("ClasseMedidor", classeMedidor);
        Hawk.put("NumElementosMedidor", numElementosMedidor);
        Hawk.put("AnoFabricacaoMedidor", anoFabricacaoMedidor);
        Hawk.put("PortariaInmetroMedidor", portariaInmetroMedidor);

        Intent intent = new Intent(this, InspecaoVisualActivity.class);
        startActivity(intent);
    }


}
