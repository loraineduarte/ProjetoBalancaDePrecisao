package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Avaliador;

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
public class CriarAvaliadorActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    String path;
    String curFileName;
    private EditText nome, matricula, senha, tipoUsuario;
    private String nomeString, matriculaString, senhaString, tipoUsuarioString = "";
    private RadioButton radioButtonAvaliador, radioButtonEletronico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_avaliador);

        final BancoController crud = new BancoController(getBaseContext());
        Button botaoCriarAvaliador = findViewById(R.id.buttonSalvarAvaliador);
        radioButtonAvaliador = findViewById(R.id.radioButtonAvaliador);
        radioButtonEletronico = findViewById(R.id.radioButtonEletronico);

        Button botaoImportarDoExcel = findViewById(R.id.ImportarExcel);

        botaoImportarDoExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importarExcel();
            }
        });

        botaoCriarAvaliador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = findViewById(R.id.nomeAvaliador);
                matricula = findViewById(R.id.numeroMatriculaAvaliador);
                senha = findViewById(R.id.senhaAvaliador);

                nomeString = nome.getText().toString();
                matriculaString = matricula.getText().toString();
                senhaString = senha.getText().toString();

                if (radioButtonAvaliador.isChecked()) {
                    tipoUsuarioString = "false";

                } else if (radioButtonEletronico.isChecked()) {
                    tipoUsuarioString = "true";
                }


                if (nomeString.equals("") || matriculaString.equals("") || senhaString.equals("") || tipoUsuarioString.equals("")) {
                    Toast.makeText(getApplicationContext(), "Campos em branco! ", Toast.LENGTH_LONG).show();
                } else {
                    String resultado = crud.insereNovoAvaliador(nomeString, matriculaString, senhaString, Boolean.parseBoolean(tipoUsuarioString));
                    Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

        Button botaoLimparCampos = findViewById(R.id.buttonLimparCamposAvaliador);

        botaoLimparCampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = findViewById(R.id.nomeAvaliador);
                matricula = findViewById(R.id.numeroMatriculaAvaliador);
                nome.getText().clear();
                matricula.getText().clear();
            }
        });

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


    public void onCheckboxClicked(View view) {

        switch (view.getId()) {
            case R.id.radioButtonEletronico:
                radioButtonAvaliador.setChecked(false);
                break;

            case R.id.radioButtonAvaliador:
                radioButtonEletronico.setChecked(false);
                break;

        }
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

                try{
                    File csvfile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/avaliador.csv");
                    CSVReader reader = new CSVReader(new FileReader(csvfile));
                    String [] nextLine;
                    while ((nextLine = reader.readNext()) != null) {
                        boolean tipo = false;
                        String nome= nextLine[0];
                        String matricula = nextLine[1];
                        String senha = nextLine[2];
                        String tipoUsuario = nextLine[3];
                        if(tipoUsuario.startsWith("adm")){
                            tipo=true;
                        } else if(tipoUsuario.startsWith("ava")){
                            tipo=false;
                        }
                        String resultado = crud.insereNovoAvaliador(nome, matricula, senha, tipo);
                        Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                        finish();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "Erro ao abrir o arquivo", Toast.LENGTH_SHORT).show();
                }


            }
        }
    }
   
}
