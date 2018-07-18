package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Medidor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

//                int instalacaoString = Integer.parseInt(String.valueOf(instalacao.getText()));
//                String numSerieString = numSerie.getText().toString();
                String numGeralString = numGeral.getText().toString();
                String fabricanteString = fabricante.getText().toString();
                int numElementosString = Integer.parseInt(numElementos.getText().toString());
                String modeloString = modelo.getText().toString();
                Double correnteNominalString = Double.valueOf(Integer.parseInt(correnteNominal.getText().toString()));
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

                if ((tipoMedidorString.startsWith("M") && RR.getText().toString().equals(""))) {

                    Toast.makeText(getApplicationContext(), "Campo RR é obrigatório quando o modelo de medidor é eletrônico! ", Toast.LENGTH_LONG).show();
                } else if (fabricanteString.equals("") || modeloString.equals("") ||
                        (tensaoNominal.getText().toString().equals(""))|| (correnteNominal.getText().toString().equals("")) || (KdKe.getText().toString().equals(""))
                        ||(numElementos.getText().toString().equals("")) || (anoFabricacao.getText().toString().equals(""))
                        || classeString.equals("")|| porInmetroString.equals("") || tipoMedidorString.equals("")){

                    Toast.makeText(getApplicationContext(), "Campo obrigatório em branco! ", Toast.LENGTH_LONG).show();
                }
                else {
                    String resultado = crud.insereNovoMedidor(numGeralString, fabricanteString, numElementosString, modeloString, correnteNominalString,
                            classeString, RRString, String.valueOf(anoFabricacaoString), tensaoNominalString, KdKeString, porInmetroString, fiosString, tipoMedidorString);
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
                anoFabricacao = findViewById(R.id.AnoFabricacao);
                tensaoNominal = findViewById(R.id.TensaoNominal);
                KdKe = findViewById(R.id.KdKe);
                porInmetro = findViewById(R.id.PorInmetro);
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
                    CSVReader reader = new CSVReader(new FileReader(csvfile));
                    String[] nextLine;
                    int cont = 0;
                    while ((nextLine = reader.readNext()) != null) {

                        if (cont == 0) {

                        } else {

                            //codigo - fabricante - modelo - corrente nominal - num elementos - tensao nominal - rr - kdke - fios - classe - erro adminissivel (para olhar se é eletrico ou mecanico)
                            String numGeral = nextLine[0].substring(0, 2);
                            String fabricante = nextLine[1];
                            String modelo = nextLine[2];
                            Double correnteNominal = Double.valueOf(Integer.parseInt(nextLine[3]));
                            int numElementos = Integer.parseInt(nextLine[4]);
                            int tensaoNominal = Integer.parseInt(nextLine[5]);
                            String RR = nextLine[6];
                            double KdKe = Double.parseDouble(nextLine[7]);
                            int fios = Integer.parseInt(nextLine[8]);
                            String classe = nextLine[9];
                            String tipoMedidorString = " ";
                            if (nextLine[10].toString().startsWith("4")) {
                                tipoMedidorString = "Mecânico";
                            } else {
                                tipoMedidorString = "Eletrônico";
                            }
                            String anoFabricacao = nextLine[0].substring(3, 4);
                            String porInmetro = "";
                            if (nextLine[11].toString().startsWith("")) {
                                porInmetro = "";
                            } else {
                                porInmetro = nextLine[11];
                            }




                            String resultado = crud.insereNovoMedidor(numGeral, fabricante, numElementos, modelo, correnteNominal,
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
