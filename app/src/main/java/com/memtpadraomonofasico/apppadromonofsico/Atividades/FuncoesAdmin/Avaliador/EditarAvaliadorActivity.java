package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Avaliador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class EditarAvaliadorActivity extends AppCompatActivity {

    private EditText nome, matricula, senha, tipoUsuario;
    private String nomeString, matriculaString, senhaString, tipoUsuarioString = "";
    private String nomeAntigoString, matriculaAntigoString, senhaAntigoString, tipoUsuarioAntigoString = "";
    private RadioButton radioButtonAvaliador, radioButtonEletronico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_avaliador);
        final BancoController crud = new BancoController(getBaseContext());

        Intent it = getIntent();
        nomeAntigoString = it.getStringExtra("nome");
        matriculaAntigoString = it.getStringExtra("matricula");
        senhaAntigoString = it.getStringExtra("senha");
        tipoUsuarioAntigoString = it.getStringExtra("tipousu");

        Button botaoCriarAvaliador = findViewById(R.id.buttonSalvarAvaliador);

        nome = findViewById(R.id.nomeAvaliador);
        nome.setText(nomeAntigoString);
        matricula = findViewById(R.id.numeroMatriculaAvaliador);
        matricula.setText(matriculaAntigoString);
        senha = findViewById(R.id.senhaAvaliador);
        senha.setText(senhaAntigoString);

        radioButtonAvaliador = findViewById(R.id.radioButtonAvaliador);
        radioButtonEletronico = findViewById(R.id.radioButtonEletronico);
        if (tipoUsuarioAntigoString.equals("true")) {
            radioButtonEletronico.setChecked(true);
        } else {
            radioButtonAvaliador.setChecked(true);
        }

        botaoCriarAvaliador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                    String resultado = crud.updateAvaliador(nomeString, matriculaString, senhaString, Boolean.parseBoolean(tipoUsuarioString), nomeAntigoString, matriculaAntigoString);
                    Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();

                    finish();
                }

            }
        });
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
}
