package com.balancaDePrecisao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.balancaDePrecisao.Banco.Dado;
import com.balancaDePrecisao.Banco.DadoDAO;

public class SalvarPesoActivity extends AppCompatActivity {

    static TextView pesoDaBalança, dataHoraMedicao, descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salvar_peso);
    }


    @Override
    protected void onResume() {
        pesoDaBalança = findViewById(R.id.pesoDaBalança);
        dataHoraMedicao = findViewById(R.id.dataHoraMedicao);
        descricao = findViewById(R.id.descricao);
        super.onResume();
    }

    public void limparDados(View view) {
        pesoDaBalança.clearComposingText();
        dataHoraMedicao.clearComposingText();
        descricao.clearComposingText();
    }

    public void SalvarMedicao(View view) {

        Dado dado = new Dado(pesoDaBalança.getText().toString(), dataHoraMedicao.getText().toString(), descricao.getText().toString());
        DadoDAO dao = new DadoDAO(this);
        if (dado.getPeso() != null) {
            dao.altera(dado);
        } else {
            dao.insere(dado);
        }
        dao.close();

        Toast.makeText(SalvarPesoActivity.this, "Dado salvo!", Toast.LENGTH_SHORT).show();


    }
}
