package com.memtpadraomonofasico.apppadromonofsico.Atividades.Avaliador;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.R;

/**
 *
 */
public class CriarAvaliadorActivity extends AppCompatActivity {

    private EditText nome;
    private EditText matricula;
    private String nomeString;
    private String matriculaString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_avaliador);

        final BancoController crud = new BancoController(getBaseContext());
        Button botaoCriarAvaliador = findViewById(R.id.buttonSalvarAvaliador);

        botaoCriarAvaliador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = findViewById(R.id.nomeAvaliador);
                matricula = findViewById(R.id.numeroMatriculaAvaliador);
                nomeString = nome.getText().toString();
                matriculaString = matricula.getText().toString();

                if(nomeString.equals("")|| matriculaString.equals("")){
                    Toast.makeText(getApplicationContext(), "Campos em branco! ", Toast.LENGTH_LONG).show();
                }
                else {
                    String resultado = crud.insereNovoAvaliador(nomeString,matriculaString);
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

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putCharSequence("nome", String.valueOf(nome.getText()));
        savedInstanceState.putCharSequence("matricula", String.valueOf(matricula.getText()));

        super.onSaveInstanceState(savedInstanceState);
    }


}
