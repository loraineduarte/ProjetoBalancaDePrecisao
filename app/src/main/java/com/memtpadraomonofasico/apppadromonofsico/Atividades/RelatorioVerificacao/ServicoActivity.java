package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class ServicoActivity extends AppCompatActivity {

    private static final String TAG = "Serviço Activity";
    String numNotaServico, numInstalacao, nomeCliente, numDocumentoCliente, rua, numero, complemento, bairro, cep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.d("SERVIÇO", String.valueOf(Hawk.count()));


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servico);

        final EditText NumNotaServico = (EditText) findViewById( R.id.NumNotaServico );
        numNotaServico = String.valueOf(NumNotaServico.getText());
        NumNotaServico.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    NumNotaServico.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText NumInstalacao = (EditText) findViewById( R.id.NumInstalacao );
        numInstalacao = String.valueOf(NumInstalacao.getText());
        NumInstalacao.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    NumInstalacao.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText NomeCliente = (EditText) findViewById( R.id.NomeCliente );
        nomeCliente = String.valueOf(NomeCliente.getText());
        NomeCliente.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    NomeCliente.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText NumDocumentoCliente = (EditText) findViewById( R.id.NumDocumentoCliente );
        numDocumentoCliente = String.valueOf(NumDocumentoCliente.getText());
        NumDocumentoCliente.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    NumDocumentoCliente.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText Rua = (EditText) findViewById( R.id.Rua );
        rua = String.valueOf(Rua.getText());
        Rua.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    Rua.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText Numero = (EditText) findViewById( R.id.Numero );
        numero = String.valueOf(Numero.getText());
        Numero.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    Numero.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText Complemento = (EditText) findViewById( R.id.Complemento );
        complemento = String.valueOf(Complemento.getText());
        Complemento.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    Complemento.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );

        final EditText Bairro = (EditText) findViewById( R.id.Bairro );
        bairro = String.valueOf(Bairro.getText());
        Bairro.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    Bairro.setText( "", TextView.BufferType.EDITABLE );
                }
            }
        } );

        final EditText CEP = (EditText) findViewById( R.id.CEP );
        cep = String.valueOf(CEP.getText());
        CEP.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    CEP.setText( "", TextView.BufferType.EDITABLE );
                }
            }
        } );

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numNotaServico.isEmpty() || numInstalacao.isEmpty() || nomeCliente.isEmpty() || numDocumentoCliente.isEmpty() || rua.isEmpty() ||
                        numero.isEmpty() || complemento.isEmpty() || bairro.isEmpty() || cep.isEmpty() ){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Campo em Branco! ", Toast.LENGTH_LONG).show();
                } else {
                    abrirMedidor();
                }

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
