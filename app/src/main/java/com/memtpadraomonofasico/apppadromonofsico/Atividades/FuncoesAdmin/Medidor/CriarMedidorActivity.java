package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Medidor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 */
@SuppressWarnings("ALL")
public class CriarMedidorActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    private RadioButton checkEletronico;
    private RadioButton checkMecanico;
    private EditText instalacao;
    private EditText numSerie;
    private EditText numGeral;
    private EditText fabricante;
    private EditText numElementos, modelo, correnteNominal, classe, RR, tensaoNominal, KdKe, fios;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medidor);

        final BancoController crud = new BancoController(getBaseContext());

        checkEletronico = findViewById(R.id.radioButtonEletronico);
        checkMecanico = findViewById(R.id.RadioButtonMecanico);

        Button botaoCriarAvaliador = findViewById(R.id.buttonSalvarMedidor);
        botaoCriarAvaliador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                numGeral = findViewById(R.id.NumGeral);
                fabricante = findViewById(R.id.fabricanteMedidor);
                numElementos = findViewById(R.id.NumElementos);
                modelo = findViewById(R.id.ModeloMedidor);
                correnteNominal = findViewById(R.id.CorrenteNominal);
                classe = findViewById(R.id.Classe);
                RR = findViewById(R.id.RR);
                tensaoNominal = findViewById(R.id.TensaoNominal);
                KdKe = findViewById(R.id.KdKe);
                fios = findViewById(R.id.Fios);


                if (numGeral.getText().toString().equals("") || fabricante.getText().toString().equals("") || numElementos.getText().toString().equals("") || modelo.getText().toString().equals("") ||
                        (correnteNominal.getText().toString().equals("")) || (tensaoNominal.getText().toString().equals("")) || (KdKe.getText().toString().equals(""))
                        || RR.getText().toString().equals("") || classe.getText().toString().equals("") || ((!checkEletronico.isChecked()) && (!checkMecanico.isChecked())) || fios.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), "Campo obrigatório em branco! ", Toast.LENGTH_LONG).show();
                } else {

                    String numGeralString = numGeral.getText().toString();
                    String fabricanteString = fabricante.getText().toString();
                    String numElementosString = (numElementos.getText().toString());
                    String modeloString = modelo.getText().toString();
                    double tensaoNominalString = Double.parseDouble((tensaoNominal.getText().toString()));
                    double correnteNominalString = Double.parseDouble((correnteNominal.getText().toString()));
                    String classeString = classe.getText().toString();
                    String RRString = RR.getText().toString();
                    double KdKeString = Double.parseDouble((KdKe.getText().toString()));
                    int fiosString = Integer.parseInt(fios.getText().toString());
                    String tipoMedidorString = " ";
                    if (checkEletronico.isChecked()) {
                        tipoMedidorString = "Eletrônico";
                    } else if (checkMecanico.isChecked()) {
                        tipoMedidorString = "Mecânico";
                    }

                    if ((tipoMedidorString.startsWith("M") && RR.getText().toString().equals(""))) {

                        Toast.makeText(getApplicationContext(), "Campo RR é obrigatório quando o modelo de medidor é eletrônico! ", Toast.LENGTH_LONG).show();
                    }
                    String resultado = crud.insereNovoMedidor(numGeralString, fabricanteString, numElementosString, modeloString, correnteNominalString,
                            classeString, RRString, tensaoNominalString, KdKeString, fiosString, tipoMedidorString);
                    Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

        Button botaoImportarDoExcel = findViewById(R.id.ImportarExcel);

        botaoImportarDoExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importarExcel();
            }
        });

        Button botaoLimparCampos = findViewById(R.id.buttonLimparCamposMedidor);
        botaoLimparCampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                numGeral = findViewById(R.id.NumGeral);
                fabricante = findViewById(R.id.fabricanteMedidor);
                numElementos = findViewById(R.id.NumElementos);
                modelo = findViewById(R.id.ModeloMedidor);
                correnteNominal = findViewById(R.id.CorrenteNominal);
                classe = findViewById(R.id.Classe);
                RR = findViewById(R.id.RR);

                tensaoNominal = findViewById(R.id.TensaoNominal);
                KdKe = findViewById(R.id.KdKe);
                fios = findViewById(R.id.Fios);
                checkEletronico = findViewById(R.id.radioButtonEletronico);
                checkMecanico = findViewById(R.id.RadioButtonMecanico);

                numGeral.getText().clear();
                fabricante.getText().clear();
                numElementos.getText().clear();
                modelo.getText().clear();
                correnteNominal.getText().clear();
                classe.getText().clear();
                RR.getText().clear();

                tensaoNominal.getText().clear();
                KdKe.getText().clear();
                fios.getText().clear();
                checkMecanico.setChecked(false);
                checkEletronico.setChecked(false);

            }
        });
    }

    /**
     * @param view
     */
    public void onCheckboxClicked(View view) {

        switch (view.getId()) {
            case R.id.radioButtonEletronico:
                checkMecanico.setChecked(false);
                break;

            case R.id.RadioButtonMecanico:
                checkEletronico.setChecked(false);
                break;

        }
    }


    private void importarExcel() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (data != null) {
                Uri uri = data.getData();
                final BancoController crud = new BancoController(getBaseContext());

                try {
                    File csvfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/medidor.csv");
                    BufferedReader reader = new BufferedReader(new FileReader(csvfile));
                    String nextLine;
                    int cont = 0;
                    while ((nextLine = reader.readLine()) != null) {
                        String linhaFinal = nextLine.replace(",", ".");
                        String[] row = linhaFinal.split(";");

                        if (cont == 0) {

                        } else {

                            //codigo - fabricante - modelo - corrente nominal - num elementos - tensao nominal - rr - kdke - fios - classe - erro adminissivel (para olhar se é eletrico ou mecanico)
                            if (row[0].toString().equals("")) {

                            } else {
                                String numGeral = row[0];
                                Log.d("NUMGERAL", numGeral);

                                String fabricante = "  ";
                                if (row[1].toString().equals("")) {
                                    fabricante = "  ";
                                    Log.d("FABRICANTE", fabricante);
                                } else {
                                    fabricante = row[1];
                                    Log.d("FABRICANTE", fabricante);
                                }

                                String modelo = " ";
                                if (row[2].toString().equals("")) {
                                    modelo = " ";
                                    Log.d("MODELO", modelo);
                                } else {
                                    modelo = row[2];
                                    Log.d("MODELO", modelo);
                                }

                                double correnteNominal = 0;
                                if (row[3].toString().equals("")) {
                                    correnteNominal = 0;
                                    Log.d("CORRENTE", String.valueOf(correnteNominal));
                                } else {
                                    correnteNominal = Double.valueOf(row[3]);
                                    Log.d("CORRENTE", String.valueOf(correnteNominal));
                                }

                                String numElementos = "0";
                                if (row[4].toString().equals("")) {
                                    numElementos = "0";
                                    Log.d("numElementos", String.valueOf(numElementos));
                                } else {
                                    numElementos = String.valueOf(row[4]);
                                    Log.d("numElementos", String.valueOf(numElementos));
                                }

                                double tensaoNominal = 0;
                                if (row[5].toString().equals("")) {
                                    tensaoNominal = 0;
                                    Log.d("tensaoNominal", String.valueOf(tensaoNominal));
                                } else {
                                    tensaoNominal = Double.valueOf(row[5]);
                                    Log.d("tensaoNominal", String.valueOf(tensaoNominal));
                                }

                                String RR = "";
                                if (row[6].toString().equals("")) {
                                    RR = "";
                                    Log.d("RR", String.valueOf(RR));
                                } else {
                                    RR = row[6];
                                    Log.d("RR", String.valueOf(RR));
                                }

                                double KdKe = 0;
                                if (row[7].toString().equals("")) {
                                    KdKe = 0;
                                    Log.d("KdKe", String.valueOf(KdKe));
                                } else {
                                    KdKe = Double.parseDouble(row[7]);
                                    Log.d("KdKe", String.valueOf(KdKe));
                                }

                                int fios = 0;
                                if (row[8].toString().equals("")) {
                                    fios = 0;
                                    Log.d("fios", String.valueOf(fios));
                                } else {
                                    fios = Integer.parseInt(row[8]);
                                    Log.d("fios", String.valueOf(fios));
                                }

                                String classe = "";
                                if (row[9].toString().equals("")) {
                                    classe = "";
                                    Log.d("classe", String.valueOf(classe));
                                } else {
                                    classe = row[9];
                                    Log.d("classe", String.valueOf(classe));
                                }

                                String tipoMedidorString = " ";

                                if (row[10].toString().equals("")) {
                                    tipoMedidorString = "";
                                    Log.d("tipoMedidorString", String.valueOf(tipoMedidorString));
                                } else {
                                    if (row[10].toString().startsWith("4")) {
                                        tipoMedidorString = "Mecânico";

                                    } else {
                                        tipoMedidorString = "Eletrônico";
                                    }
                                    Log.d("tipoMedidorString", String.valueOf(tipoMedidorString));
                                }


                                String resultado = crud.insereNovoMedidor(numGeral, fabricante, numElementos, modelo, correnteNominal,
                                        classe, RR, tensaoNominal, KdKe, fios, tipoMedidorString);
                                Log.d("RESULTADO", resultado);
                            }
                        }
                        finish();
                        cont++;
                    }
                    Toast.makeText(getApplicationContext(), "Foram inseridos " + cont + " registros.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Erro ao ler todo o arquivo", Toast.LENGTH_SHORT).show();
                }


            }
        }
    }

}
