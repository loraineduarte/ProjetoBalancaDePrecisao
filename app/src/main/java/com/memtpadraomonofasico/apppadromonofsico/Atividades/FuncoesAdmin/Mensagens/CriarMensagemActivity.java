package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Mensagens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class CriarMensagemActivity extends AppCompatActivity {

    private Spinner opcoesSpinner;
    private String tabelaEscolhida, mensagemString;
    private EditText mensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_mensagem);

        final BancoController crud = new BancoController(getBaseContext());
        Button botaoCriarAvaliador = findViewById(R.id.buttonSalvarAvaliador);

        opcoesSpinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.TabelaMensagens, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcoesSpinner.setAdapter(adapter);
        opcoesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tabelaEscolhida = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        botaoCriarAvaliador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensagem = findViewById(R.id.nomeAvaliador);
                mensagemString = mensagem.getText().toString();

                if (mensagemString.equals("")) {
                    Toast.makeText(getApplicationContext(), "Campos em branco! ", Toast.LENGTH_LONG).show();
                } else {
                    String resultado = crud.insereMensagem(mensagemString, tabelaEscolhida);
                    Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

        Button botaoLimparCampos = findViewById(R.id.buttonLimparCamposAvaliador);

        botaoLimparCampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensagem = findViewById(R.id.nomeAvaliador);
                mensagem.getText().clear();
            }
        });

    }
}
