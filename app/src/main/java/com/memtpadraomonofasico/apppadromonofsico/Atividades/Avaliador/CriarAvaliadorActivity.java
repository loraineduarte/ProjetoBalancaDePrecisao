package com.memtpadraomonofasico.apppadromonofsico.Atividades.Avaliador;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class CriarAvaliadorActivity extends AppCompatActivity {

    private static final String TAG = "Criar Avaliador";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_avaliador);

        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaAvaliadores();

        Log.d(TAG, String.valueOf(cursor.getCount()));


        Button botaoCriarAvaliador = (Button)findViewById(R.id.buttonSalvarAvaliador);

        botaoCriarAvaliador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BancoController crud = new BancoController(getBaseContext());
                EditText nome = (EditText)findViewById(R.id.nomeAvaliador);
                EditText matricula = (EditText)findViewById(R.id.numeroMatriculaAvaliador);
                String nomeString = nome.getText().toString();
                String matriculaString = matricula.getText().toString();
                String resultado = crud.insereNovoAvaliador(nomeString,matriculaString);
                Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                finish();
            }
        });

        Button botaoLimparCampos = (Button)findViewById(R.id.buttonLimparCamposAvaliador);

        botaoLimparCampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nome = (EditText)findViewById(R.id.nomeAvaliador);
                EditText matricula = (EditText)findViewById(R.id.numeroMatriculaAvaliador);
                nome.clearComposingText();
                matricula.clearComposingText();
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
