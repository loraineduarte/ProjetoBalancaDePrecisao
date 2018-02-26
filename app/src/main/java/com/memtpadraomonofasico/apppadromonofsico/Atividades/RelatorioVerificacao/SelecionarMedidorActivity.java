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

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoVisual.InspecaoVisualActivity;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class SelecionarMedidorActivity extends AppCompatActivity {


    private static final String TAG = "Selecionar Medidor";
    final CriaBanco banco = new CriaBanco(this);
    String numeroSerieMedidor, modeloMedidor, fabricanteMedidor, tensaoNominalMedidor, correnteNominalMedidor, tipoMedidor,
            kdkeMedidor, classeMedidor, numElementosMedidor, anoFabricacaoMedidor, portariaInmetroMedidor, fiosMedidor, rrMedidor, instalacaoMedidor, numeroGeralMedidor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Hawk.delete("NumeroSerieMedidor");
        Hawk.delete("NumeroGeralMedidor");
        Hawk.delete("InstalacaoMedidor");
        Hawk.delete("ModeloMedidor");
        Hawk.delete("FaricanteMedidor");
        Hawk.delete("TensaoNominalMedidor");
        Hawk.delete("CorrenteNominalMedidor");
        Hawk.delete("TipoMedidor");
        Hawk.delete("KdKeMedidor");
        Hawk.delete("rrMedidor");
        Hawk.delete("ClasseMedidor");
        Hawk.delete("NumElementosMedidor");
        Hawk.delete("AnoFabricacaoMedidor");
        Hawk.delete("FiosMedidor");
        Hawk.delete("PortariaInmetroMedidor");
        Log.d("MEDIDOR", String.valueOf(Hawk.count()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_medidor);

        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaMedidores();
        Log.d(TAG, String.valueOf(cursor.getCount()));

        if (cursor.getCount() > 0) {

            final String[] myData = banco.SelectAllMedidores();
            @SuppressLint("WrongViewCast") final AutoCompleteTextView autoCom = (AutoCompleteTextView) findViewById(R.id.numSerie);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, myData);
            autoCom.setAdapter(adapter);
        }

        final EditText numSerie = (EditText) findViewById( R.id.numSerie );
        numeroSerieMedidor = String.valueOf(numSerie.getText());

        final EditText numGeral = (EditText) findViewById( R.id.NumGeral );
        numeroGeralMedidor = String.valueOf(numGeral.getText());

        final EditText instalacao = (EditText) findViewById( R.id.Instalacao );
        instalacaoMedidor = String.valueOf(instalacao.getText());

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

        final EditText RR = (EditText) findViewById( R.id.RR );
        rrMedidor = String.valueOf(RR.getText());

        final EditText ClasseMedidor = (EditText) findViewById( R.id.RR);
        classeMedidor = String.valueOf(ClasseMedidor.getText());

        final EditText NumElementos = (EditText) findViewById( R.id.NumElementos );
        numElementosMedidor = String.valueOf(NumElementos.getText());

        final EditText AnoFabricacao = (EditText) findViewById( R.id.AnoFabricacao );
        anoFabricacaoMedidor = String.valueOf(AnoFabricacao.getText());

        final EditText Fios = (EditText) findViewById( R.id.Fios );
        fiosMedidor = String.valueOf(Fios.getText());

        final EditText PortariaInmetro = (EditText) findViewById( R.id.PorInmetro );
        portariaInmetroMedidor = String.valueOf(PortariaInmetro.getText());


        @SuppressLint("WrongViewCast") Button next =  findViewById(R.id.NextFase3);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(numeroSerieMedidor.isEmpty()|| instalacaoMedidor.isEmpty()|| modeloMedidor.isEmpty()|| fabricanteMedidor.isEmpty()|| tensaoNominalMedidor.isEmpty()|| correnteNominalMedidor.isEmpty() ||
//                        kdkeMedidor.isEmpty()|| rrMedidor.isEmpty()|| classeMedidor.isEmpty()|| numElementosMedidor.isEmpty()|| anoFabricacaoMedidor.isEmpty() || fiosMedidor.isEmpty() || portariaInmetroMedidor.isEmpty()){
//                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Campo em Branco! ", Toast.LENGTH_LONG).show();
//
//                }else if ((!eletronico.isChecked()) && (!mecanico.isChecked())){
//                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Selecione o tipo de medidor! ", Toast.LENGTH_LONG).show();
//
//                } else{
                    abrirInspecaoVisual();
              //  }
            }
        });
    }

    private void abrirInspecaoVisual() {
        Log.d(TAG, "Opção de serviços");

        Hawk.put("NumeroSerieMedidor",numeroSerieMedidor);
        Hawk.put("NumeroGeralMedidor",numeroGeralMedidor);
        Hawk.put("InstalacaoMedidor",instalacaoMedidor);
        Hawk.put("ModeloMedidor", modeloMedidor);
        Hawk.put("FaricanteMedidor", fabricanteMedidor);
        Hawk.put("TensaoNominalMedidor", tensaoNominalMedidor);
        Hawk.put("CorrenteNominalMedidor", correnteNominalMedidor);
        Hawk.put("TipoMedidor", tipoMedidor);
        Hawk.put("KdKeMedidor", kdkeMedidor);
        Hawk.put("rrMedidor", rrMedidor);
        Hawk.put("ClasseMedidor", classeMedidor);
        Hawk.put("NumElementosMedidor", numElementosMedidor);
        Hawk.put("AnoFabricacaoMedidor", anoFabricacaoMedidor);
        Hawk.put("FiosMedidor", fiosMedidor);
        Hawk.put("PortariaInmetroMedidor", portariaInmetroMedidor);

        Intent intent = new Intent(this, InspecaoVisualActivity.class);
        startActivity(intent);
    }


}
