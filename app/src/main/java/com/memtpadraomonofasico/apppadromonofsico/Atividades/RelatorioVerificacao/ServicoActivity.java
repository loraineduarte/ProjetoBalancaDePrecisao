package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class ServicoActivity extends AppCompatActivity {

    private static final String TAG = "Serviço Activity";
    String numNotaServico, numInstalacao, nomeCliente, numDocumentoCliente, rua, numero, complemento, bairro, cep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Hawk.delete("NumeroNotaServico");
        Hawk.delete("NumeroInstalacaoServico");
        Hawk.delete("NomeClienteServico");
        Hawk.delete("NumDocumentoCliente");
        Hawk.delete("RuaCliente");
        Hawk.delete("NumeroCliente");
        Hawk.delete("ComplementoCliente");
        Hawk.delete("BairroCliente");
        Hawk.delete("CepCliente");

        Log.d("SERVIÇO", String.valueOf(Hawk.count()));


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servico);

        final EditText NumNotaServico = (EditText) findViewById( R.id.NumNotaServico );
        numNotaServico = String.valueOf(NumNotaServico.getText());

        final EditText NumInstalacao = (EditText) findViewById( R.id.NumInstalacao );
        numInstalacao = String.valueOf(NumInstalacao.getText());

        final EditText NomeCliente = (EditText) findViewById( R.id.NomeCliente );
        nomeCliente = String.valueOf(NomeCliente.getText());

        final EditText NumDocumentoCliente = (EditText) findViewById( R.id.NumDocumentoCliente );
        numDocumentoCliente = String.valueOf(NumDocumentoCliente.getText());

        final EditText Rua = (EditText) findViewById( R.id.Rua );
        rua = String.valueOf(Rua.getText());

        final EditText Numero = (EditText) findViewById( R.id.Numero );
        numero = String.valueOf(Numero.getText());

        final EditText Complemento = (EditText) findViewById( R.id.Complemento );
        complemento = String.valueOf(Complemento.getText());

        final EditText Bairro = (EditText) findViewById( R.id.Bairro );
        bairro = String.valueOf(Bairro.getText());

        final EditText CEP = (EditText) findViewById( R.id.CEP );
        cep = String.valueOf(CEP.getText());

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(numNotaServico.equals("") || numInstalacao.equals("") || nomeCliente.equals("") || numDocumentoCliente.equals("") ){
//                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Campo em Branco! ", Toast.LENGTH_LONG).show();
//                } else {
                    abrirMedidor();
//                }

            }
        });
    }

    private void abrirMedidor() {

        Hawk.put("NumeroNotaServico",numNotaServico);
        Hawk.put("NumeroInstalacaoServico", numInstalacao);
        Hawk.put("NomeClienteServico", nomeCliente);
        Hawk.put("NumDocumentoCliente", numDocumentoCliente);
        Hawk.put("RuaCliente", rua);
        Hawk.put("NumeroCliente", numero);
        Hawk.put("ComplementoCliente", complemento);
        Hawk.put("BairroCliente", bairro);
        Hawk.put("CepCliente", cep);


        Intent intent = new Intent(this, SelecionarMedidorActivity.class);
        startActivity(intent);
    }

}
