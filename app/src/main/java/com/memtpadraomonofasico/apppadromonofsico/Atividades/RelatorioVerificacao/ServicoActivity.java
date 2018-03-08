package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class ServicoActivity extends AppCompatActivity {

    String numNotaServico, NumInvolucro, numInstalacao, nomeCliente, numDocumentoCliente, rua, numero, complemento, bairro, cep;
    EditText NumNotaServico, numInvolucro, NumInstalacao, NomeCliente, NumDocumentoCliente, Rua, Numero, Complemento, Bairro, CEP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("SERVIÇO", String.valueOf(Hawk.count()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servico);

        NumNotaServico = findViewById( R.id.NumNotaServico );
        numInvolucro = findViewById( R.id.numInvolucro );
        NumInstalacao = findViewById( R.id.NumInstalacao );
        NomeCliente = findViewById( R.id.NomeCliente );
        NumDocumentoCliente = findViewById( R.id.NumDocumentoCliente );
        Rua = findViewById( R.id.Rua );
        Numero = findViewById( R.id.Numero );
        Complemento = findViewById( R.id.Complemento );
        Bairro = findViewById( R.id.Bairro );
        CEP = findViewById( R.id.CEP );


        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                numNotaServico = String.valueOf(NumNotaServico.getText());
                NumInvolucro = String.valueOf(numInvolucro.getText());
                numInstalacao = String.valueOf(NumInstalacao.getText());
                nomeCliente = String.valueOf(NomeCliente.getText());
                numDocumentoCliente = String.valueOf(NumDocumentoCliente.getText());
                rua = String.valueOf(Rua.getText());
                numero = String.valueOf(Numero.getText());
                complemento = String.valueOf(Complemento.getText());
                bairro = String.valueOf(Bairro.getText());
                cep = String.valueOf(CEP.getText());

                if(numNotaServico.isEmpty() || NumInvolucro.isEmpty()|| numInstalacao.isEmpty() || nomeCliente.isEmpty() || numDocumentoCliente.isEmpty() ){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Campo em Branco! ", Toast.LENGTH_LONG).show();
                } else {

                Hawk.delete("NumeroNotaServico");
                Hawk.delete("NumeroInvolucro");
                Hawk.delete("NumeroInstalacaoServico");
                Hawk.delete("NomeClienteServico");
                Hawk.delete("NumDocumentoCliente");
                Hawk.delete("RuaCliente");
                Hawk.delete("NumeroCliente");
                Hawk.delete("ComplementoCliente");
                Hawk.delete("BairroCliente");
                Hawk.delete("CepCliente");

                abrirMedidor();
                }

            }
        });
    }

    private void abrirMedidor() {



        Hawk.put("NumeroNotaServico",numNotaServico);
        Hawk.put("NumeroInvolucro",NumInvolucro);
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
