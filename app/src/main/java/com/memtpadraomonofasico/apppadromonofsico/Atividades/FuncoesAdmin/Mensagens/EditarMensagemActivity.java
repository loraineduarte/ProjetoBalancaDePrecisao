package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Mensagens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class EditarMensagemActivity extends AppCompatActivity {

    private EditText msg, tabela;
    private String msgString, tabelaString;
    private String msgAntigoString, tabelaAntigoString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_mensagem);
        final BancoController crud = new BancoController(getBaseContext());
        Intent it = getIntent();
        msgAntigoString = it.getStringExtra("mensagem");
        tabelaAntigoString = it.getStringExtra("tabela");

        Button botaoCriarAvaliador = findViewById(R.id.buttonSalvarAvaliador);

        msg = findViewById(R.id.nomeAvaliador);
        msg.setText(msgAntigoString);
        tabela = findViewById(R.id.numeroMatriculaAvaliador);
        tabela.setText(tabelaAntigoString);
        tabela.setEnabled(false);

        botaoCriarAvaliador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                msgString = msg.getText().toString();
                tabelaString = tabela.getText().toString();

                if (msgString.equals("")) {
                    Toast.makeText(getApplicationContext(), "Campos em branco! ", Toast.LENGTH_LONG).show();
                } else {
                    String resultado = crud.updateMensagem(msgAntigoString, tabelaAntigoString, msgString, tabelaString);
                    Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();

                    finish();
                }

            }
        });
    }
}
