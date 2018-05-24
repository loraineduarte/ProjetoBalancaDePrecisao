package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Medidor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.opencsv.CSVReader;

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
    private EditText numElementos, modelo, correnteNominal, classe, RR, anoFabricacao, tensaoNominal, KdKe, porInmetro, fios;

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

                int instalacaoString = Integer.parseInt(String.valueOf(instalacao.getText()));
                String numSerieString = numSerie.getText().toString();
                String numGeralString = numGeral.getText().toString();
                String fabricanteString = fabricante.getText().toString();
                int numElementosString = Integer.parseInt(numElementos.getText().toString());
                String modeloString = modelo.getText().toString();
                int correnteNominalString = Integer.parseInt(correnteNominal.getText().toString());
                String classeString = classe.getText().toString();
                String RRString =RR.getText().toString();
                int anoFabricacaoString = Integer.parseInt(anoFabricacao.getText().toString());
                int tensaoNominalString = Integer.parseInt(tensaoNominal.getText().toString());
                Double KdKeString = Double.valueOf(KdKe.getText().toString());
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

        FloatingActionButton botaoImportarDoExcel = findViewById(R.id.ImportarExcel);

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

    /**
     * @param view
     */
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


    private void importarExcel() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        // Filter to show only images, using the image MIME data type.
//        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
//        // To search for all documents available via installed storage providers,
//        // it would be "*/*".
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            if (data != null) {
                Uri uri = data.getData();
                final BancoController crud = new BancoController(getBaseContext());

                try {
                    File csvfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/medidor.csv");
                    CSVReader reader = new CSVReader(new FileReader(csvfile));
                    String[] nextLine;
                    int cont = 0;
                    while ((nextLine = reader.readNext()) != null) {

                        if (cont == 0) {

                        } else {
                            int instalacao = Integer.parseInt(nextLine[0].toString());
                            String numSerie = nextLine[1];
                            String numGeral = nextLine[2];
                            String fabricante = nextLine[3];
                            int numElementos = Integer.parseInt(nextLine[4]);
                            String modelo = nextLine[5];
                            int correnteNominal = Integer.parseInt(nextLine[6]);
                            String classe = nextLine[7];
                            String RR = nextLine[8];
                            int anoFabricacao = Integer.parseInt(nextLine[9]);
                            int tensaoNominal = Integer.parseInt(nextLine[10]);
                            double KdKe = Double.parseDouble(nextLine[11]);
                            String porInmetro = nextLine[12];
                            int fios = Integer.parseInt(nextLine[13]);
                            String tipoMedidorString = " ";
                            if (nextLine[14].toString().startsWith("mec")) {
                                tipoMedidorString = "Mecânico";
                            } else if (nextLine[14].toString().startsWith("ele")) {
                                tipoMedidorString = "Eletrônico";
                            }
                            String resultado = crud.insereNovoMedidor(instalacao, numSerie, numGeral, fabricante, numElementos, modelo, correnteNominal,
                                    classe, RR, anoFabricacao, tensaoNominal, KdKe, porInmetro, fios, tipoMedidorString);
                            Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();

                        }
                        finish();
                        cont++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Erro ao abrir o arquivo", Toast.LENGTH_SHORT).show();
                }


            }
        }
    }

}
