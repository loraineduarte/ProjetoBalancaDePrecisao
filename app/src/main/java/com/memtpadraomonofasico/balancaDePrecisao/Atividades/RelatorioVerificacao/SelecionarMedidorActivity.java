package com.memtpadraomonofasico.balancaDePrecisao.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.memtpadraomonofasico.balancaDePrecisao.BancoDeDados.BancoController;
import com.memtpadraomonofasico.balancaDePrecisao.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.balancaDePrecisao.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

public class SelecionarMedidorActivity extends AppCompatActivity {

    private final CriaBanco banco = new CriaBanco(this);
    private String tipoMedidor;
    private EditText numGeral, ModeloMedidor, FabricanteMedidor, TensaoNominalMedidor, CorrenteNominalMedidor, KDKE, RR, ClasseMedidor, NumElementos, AnoFabricacao, Fios, PortariaInmetro;
    private RadioButton eletronico, mecanico;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_medidor);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();


        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaMedidores();


        numGeral = findViewById( R.id.NumGeral );

        ModeloMedidor = findViewById( R.id.ModeloMedidor );
        FabricanteMedidor = findViewById( R.id.fabricanteMedidor );
        TensaoNominalMedidor = findViewById( R.id.TensaoNominal );
        CorrenteNominalMedidor = findViewById( R.id.CorrenteNominal );
        eletronico = findViewById( R.id.radioButtonEletronico );
        mecanico = findViewById( R.id.RadioButtonMecanico );
        KDKE = findViewById( R.id.KdKe );
        RR = findViewById( R.id.RR );
        ClasseMedidor = findViewById( R.id.Classe);
        NumElementos = findViewById( R.id.NumElementos );
        AnoFabricacao = findViewById( R.id.AnoFabricacao );
        Fios = findViewById( R.id.Fios );
        PortariaInmetro = findViewById( R.id.PorInmetro );

        if (cursor.getCount() > 0) {

            final String[] myData = banco.SelectAllMedidores();
            @SuppressLint("WrongViewCast") final AutoCompleteTextView autoCom = (AutoCompleteTextView) numGeral;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, myData);
            autoCom.setAdapter(adapter);
        }

        ModeloMedidor.setEnabled(false);
        FabricanteMedidor.setEnabled(false);
        TensaoNominalMedidor.setEnabled(false);
        CorrenteNominalMedidor.setEnabled(false);
        eletronico.setEnabled(false);
        mecanico.setEnabled(false);
        KDKE.setEnabled(false);
        RR.setEnabled(false);
        ClasseMedidor.setEnabled(false);
        NumElementos.setEnabled(false);
        AnoFabricacao.setEnabled(false);
        Fios.setEnabled(false);
        PortariaInmetro.setEnabled(false);

        FloatingActionButton botaoProcurar = findViewById(R.id.ProcurarMedidor);
        botaoProcurar.setClickable(true);
        botaoProcurar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doMyThing();
            }
        });

        @SuppressLint("WrongViewCast") Button next =  findViewById(R.id.NextFase3);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((numGeral.getText().length() == 0) || (ModeloMedidor.getText().length() == 0) ||
                        (FabricanteMedidor.getText().length()==0) || (TensaoNominalMedidor.getText().length()==0) || (CorrenteNominalMedidor.getText().length()==0) ||
                        (KDKE.getText().length()==0) || (RR.getText().length()==0) || (ClasseMedidor.getText().length()==0) || ( NumElementos.getText().length()==0) ||
                        (AnoFabricacao.getText().length()==0) || (Fios.getText().length()==0) || (PortariaInmetro.getText().length()==0)){

                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Campo em Branco! ", Toast.LENGTH_LONG).show();

                } else {


                    Hawk.delete("NumeroGeralMedidor");
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

                    if(eletronico.isChecked()){
                        tipoMedidor = "Eletrônico";
                    }
                    if(mecanico.isChecked()){
                        tipoMedidor = "Mecânico";
                    }


                    Hawk.put("NumeroGeralMedidor", String.valueOf(numGeral.getText()));
                    Hawk.put("ModeloMedidor", String.valueOf(ModeloMedidor.getText()));
                    Hawk.put("FaricanteMedidor", String.valueOf(FabricanteMedidor.getText()));
                    Hawk.put("TensaoNominalMedidor", String.valueOf(TensaoNominalMedidor.getText()));
                    Hawk.put("CorrenteNominalMedidor", String.valueOf(CorrenteNominalMedidor.getText()));
                    Hawk.put("TipoMedidor", tipoMedidor);
                    Hawk.put("KdKeMedidor", String.valueOf(KDKE.getText()));
                    Hawk.put("rrMedidor", String.valueOf(RR.getText()));
                    Hawk.put("ClasseMedidor", String.valueOf(ClasseMedidor.getText()));
                    Hawk.put("NumElementosMedidor", String.valueOf(NumElementos.getText()));
                    Hawk.put("AnoFabricacaoMedidor", String.valueOf(AnoFabricacao.getText()));
                    Hawk.put("FiosMedidor", String.valueOf(Fios.getText()));
                    Hawk.put("PortariaInmetroMedidor", String.valueOf(PortariaInmetro.getText()));

                    abrirInspecaoVisual();

              }
            }
        });
    }

    private void abrirInspecaoVisual() {

      //  Intent intent = new Intent(this, InspecaoVisualActivity.class);
     //   startActivity(intent);
    }


    private void doMyThing() {

        numGeral = findViewById(R.id.NumGeral);
        String numGeral1 = String.valueOf(numGeral.getText());

        if ((numGeral1.equals("")) || (numGeral1.isEmpty())) {
            Toast.makeText(getApplicationContext(), "Coloque um número de matrícula para a pesquisa. ", Toast.LENGTH_LONG).show();
        }
        if (numGeral1.length() > 0) {
            String[] nome = banco.SelecionaMedidor(numGeral1);

            if(nome == null){
                Toast.makeText(getApplicationContext(), "Coloque um número de matrícula válido para a pesquisa. ", Toast.LENGTH_LONG).show();
            } else {

                //vetor que vem do banco está na ordem :
//                arrData[0] = cursor.getString(cursor.getColumnIndexOrThrow(NUM_SERIE));
//                arrData[2] = cursor.getString(cursor.getColumnIndexOrThrow(MODELO));
//                arrData[3] = cursor.getString(cursor.getColumnIndexOrThrow(FABRICANTE));
//                arrData[4] = cursor.getString(cursor.getColumnIndexOrThrow(TENSAO_NOMINAL));
//                arrData[5] = String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(CORRENTE_NOMINAL)));
//                arrData[6] = cursor.getString(cursor.getColumnIndexOrThrow(TIPO_MEDIDOR));
//                arrData[7] = String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(KDKE)));
//                arrData[8] = cursor.getString(cursor.getColumnIndexOrThrow(RR));
//                arrData[9] = cursor.getString(cursor.getColumnIndexOrThrow(NUM_ELEMENTOS));
//                arrData[10] = cursor.getString(cursor.getColumnIndexOrThrow(CLASSE));
//                arrData[11] = cursor.getString(cursor.getColumnIndexOrThrow(FIOS));

                numGeral.setText(nome[0]);
                ModeloMedidor.setEnabled(true);
                ModeloMedidor.setText(nome[2]);
                FabricanteMedidor.setEnabled(true);
                FabricanteMedidor.setText(nome[3]);
                TensaoNominalMedidor.setEnabled(true);
                TensaoNominalMedidor.setText(nome[4]);
                CorrenteNominalMedidor.setEnabled(true);
                CorrenteNominalMedidor.setText(nome[5]);
                eletronico = findViewById(R.id.radioButtonEletronico);
                eletronico.setEnabled(true);
                mecanico = findViewById(R.id.RadioButtonMecanico);
                mecanico.setEnabled(true);
                if (nome[6].startsWith("E")) {
                    eletronico.setChecked(true);
                    mecanico.setChecked(false);
                }
                if (nome[6].startsWith("Mecânico")) {
                    mecanico.setChecked(true);
                    eletronico.setChecked(false);
                }
                KDKE.setEnabled(true);
                KDKE.setText(nome[7]);
                RR.setEnabled(true);
                RR.setText(nome[8]);
                NumElementos.setEnabled(true);
                NumElementos.setText(nome[9]);
                ClasseMedidor.setEnabled(true);
                ClasseMedidor.setText(nome[10]);
                Fios.setEnabled(true);
                Fios.setText(nome[11]);
                AnoFabricacao.setEnabled(true);
                PortariaInmetro.setEnabled(true);

            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putCharSequence("NumeroGeralMedidor", String.valueOf(numGeral.getText()));
        savedInstanceState.putCharSequence("ModeloMedidor", String.valueOf(ModeloMedidor.getText()));
        savedInstanceState.putCharSequence("FaricanteMedidor", String.valueOf(FabricanteMedidor.getText()));
        savedInstanceState.putCharSequence("TensaoNominalMedidor", String.valueOf(TensaoNominalMedidor.getText()));
        savedInstanceState.putCharSequence("CorrenteNominalMedidor", String.valueOf(CorrenteNominalMedidor.getText()));
        savedInstanceState.putCharSequence("TipoMedidor", tipoMedidor);
        savedInstanceState.putCharSequence("KdKeMedidor", String.valueOf(KDKE.getText()));
        savedInstanceState.putCharSequence("rrMedidor", String.valueOf(RR.getText()));
        savedInstanceState.putCharSequence("ClasseMedidor", String.valueOf(ClasseMedidor.getText()));
        savedInstanceState.putCharSequence("NumElementosMedidor", String.valueOf(NumElementos.getText()));
        savedInstanceState.putCharSequence("AnoFabricacaoMedidor", String.valueOf(AnoFabricacao.getText()));
        savedInstanceState.putCharSequence("FiosMedidor", String.valueOf(Fios.getText()));

        super.onSaveInstanceState(savedInstanceState);
    }

    public void onCheckboxClicked(View view) {

        switch (view.getId()) {
            case R.id.radioButtonEletronico:
                mecanico.setChecked(false);
                break;

            case R.id.RadioButtonMecanico:
                eletronico.setChecked(false);
                break;

        }
    }
}
