package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoVisual.InspecaoVisualActivity;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class SelecionarMedidorActivity extends AppCompatActivity {

    private static final String TAG = "Selecionar Medidor";
    final CriaBanco banco = new CriaBanco(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_medidor);

        @SuppressLint("WrongViewCast") Button next =  findViewById(R.id.NextFase3);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirInspecaoVisual();
            }
        });

        @SuppressLint("WrongViewCast") Button previous = findViewById(R.id.PreviousFase2);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirServicos();
            }
        });



        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaMedidores();
        Log.d(TAG, String.valueOf(cursor.getCount()));

        if (cursor.getCount() > 0) {

            final String[] myData = banco.SelectAllMedidores();
            final AutoCompleteTextView autoCom = (AutoCompleteTextView) findViewById(R.id.NumSerieMedidor);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, myData);
            autoCom.setAdapter(adapter);
        }


        final EditText numSerie = (EditText) findViewById( R.id.NumSerieMedidor );
        numSerie.setOnFocusChangeListener( new View.OnFocusChangeListener() {

            public void onFocusChange( View v, boolean hasFocus ) {
                if( hasFocus ) {
                    numSerie.setText( "", TextView.BufferType.EDITABLE );
                }
            }

        } );
    }



    private void abrirInspecaoVisual() {
        Log.d(TAG, "Opção de serviços");
        Intent intent = new Intent(this, InspecaoVisualActivity.class);
        startActivity(intent);
    }

    private void abrirServicos() {
        Log.d(TAG, "Opção de serviços");
        Intent intent = new Intent(this, ServicoActivity.class);
        startActivity(intent);
    }
}
