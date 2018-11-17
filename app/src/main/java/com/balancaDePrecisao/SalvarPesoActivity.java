package com.balancaDePrecisao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.balancaDePrecisao.Banco.BancoController;
import com.balancaDePrecisao.Banco.Dado;
import com.balancaDePrecisao.Banco.CriaBanco;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SalvarPesoActivity extends AppCompatActivity {

    static TextView pesoDaBalança, dataHoraMedicao, descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salvar_peso);

        pesoDaBalança = findViewById(R.id.pesoDaBalança);
        dataHoraMedicao = findViewById(R.id.dataHoraMedicao);
        descricao = findViewById(R.id.descricao);

        Intent intent = getIntent();
        String peso = (intent.getStringExtra("peso"));
        pesoDaBalança.setText(peso);
        pesoDaBalança.setEnabled(false);

        Date dataHoraAtual = new Date();
        String data = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
        String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);
        dataHoraMedicao.setText(data +" - "+ hora);
        dataHoraMedicao.setEnabled(false);


    }


    @Override
    protected void onResume() {

        super.onResume();
    }

    public void limparDados(View view) {
        pesoDaBalança.clearComposingText();
        dataHoraMedicao.clearComposingText();
        descricao.clearComposingText();
    }

    public void SalvarMedicao(View view) {

        Dado dado = new Dado(pesoDaBalança.getText().toString(), dataHoraMedicao.getText().toString(), descricao.getText().toString());
        BancoController dao = new BancoController(this);
        String result = dao.insere(dado);

        Toast.makeText(SalvarPesoActivity.this, result, Toast.LENGTH_SHORT).show();

        finish();


    }
}
