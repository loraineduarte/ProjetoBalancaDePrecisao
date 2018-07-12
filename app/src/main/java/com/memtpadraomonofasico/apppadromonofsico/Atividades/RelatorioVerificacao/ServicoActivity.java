package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

public class ServicoActivity extends AppCompatActivity {

    private EditText numSerie, instalacao, numNotaServico, numInstalacao, NomeCliente, NumDocumentoCliente, Rua, Numero, Complemento, Bairro, CEP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servico);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();


        numSerie = findViewById(R.id.numSerie);
        instalacao = findViewById(R.id.Instalacao);
        numNotaServico = findViewById( R.id.NumNotaServico );
        numInstalacao = findViewById( R.id.NumInstalacao );
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

                Hawk.delete("NumeroSerieMedidor");
                Hawk.delete("InstalacaoMedidor");
                Hawk.delete("NumeroNotaServico");

                Hawk.delete("NumeroInstalacaoServico");
                Hawk.delete("NomeClienteServico");
                Hawk.delete("NumDocumentoCliente");
                Hawk.delete("RuaCliente");
                Hawk.delete("NumeroCliente");
                Hawk.delete("ComplementoCliente");
                Hawk.delete("BairroCliente");
                Hawk.delete("CepCliente");

                if ((numSerie.getText().length() == 0) || (instalacao.getText().length() == 0) || numNotaServico.getText().toString().isEmpty() || numInstalacao.getText().toString().isEmpty() ||
                        NomeCliente.getText().toString().isEmpty() || NumDocumentoCliente.getText().toString().isEmpty() ){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Campo em Branco! ", Toast.LENGTH_LONG).show();

                } else {

                    Hawk.put("NumeroSerieMedidor", String.valueOf(numSerie.getText()));
                    Hawk.put("InstalacaoMedidor", String.valueOf(instalacao.getText()));
                    Hawk.put("NumeroNotaServico",String.valueOf(numNotaServico.getText()));

                    Hawk.put("NumeroInstalacaoServico", String.valueOf(numInstalacao.getText()));
                    Hawk.put("NomeClienteServico", String.valueOf(NomeCliente.getText()));
                    Hawk.put("NumDocumentoCliente", String.valueOf(NumDocumentoCliente.getText()));
                    Hawk.put("RuaCliente", String.valueOf(Rua.getText()));
                    Hawk.put("NumeroCliente", String.valueOf(Numero.getText()));
                    Hawk.put("ComplementoCliente", String.valueOf(Complemento.getText()));
                    Hawk.put("BairroCliente", String.valueOf(Bairro.getText()));
                    Hawk.put("CepCliente", String.valueOf(CEP.getText()));

                    abrirMedidor();
                }
            }
        });
    }

    private void abrirMedidor() {

        Intent intent = new Intent(this, SelecionarMedidorActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {


        savedInstanceState.putCharSequence("NumeroSerieMedidor", String.valueOf(numSerie.getText()));
        savedInstanceState.putCharSequence("InstalacaoMedidor", String.valueOf(instalacao.getText()));
        savedInstanceState.putCharSequence("NumNotaServico", String.valueOf(numNotaServico.getText()));
        savedInstanceState.putCharSequence("NumInstalacao", String.valueOf(numInstalacao.getText()));
        savedInstanceState.putCharSequence("NomeCliente", String.valueOf(NomeCliente.getText()));
        savedInstanceState.putCharSequence("NumDocumentoCliente", String.valueOf(NumDocumentoCliente.getText()));
        savedInstanceState.putCharSequence("Rua", String.valueOf(Rua.getText()));
        savedInstanceState.putCharSequence("Numero", String.valueOf(Numero.getText()));
        savedInstanceState.putCharSequence("Complemento", String.valueOf(Complemento.getText()));
        savedInstanceState.putCharSequence("Bairro", String.valueOf(Bairro.getText()));
        savedInstanceState.putCharSequence("CEP", String.valueOf(CEP.getText()));

        super.onSaveInstanceState(savedInstanceState);
    }


}
