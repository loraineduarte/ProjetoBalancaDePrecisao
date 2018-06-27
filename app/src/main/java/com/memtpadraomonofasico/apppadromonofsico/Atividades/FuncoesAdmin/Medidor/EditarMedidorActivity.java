package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Medidor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class EditarMedidorActivity extends AppCompatActivity {

    private EditText numeroSerie, numeroGeral, instalacao, modelo, fabricante, tensaoNominal, correnteNominal, tipoMedidor, kdKe, rr, numElementos, anoFabricacao, classe, fios, portariaInmetro;
    private String numeroSerieString, numeroGeralString, instalacaoString, modeloString, fabricanteString, tensaoNominalString, correnteNominalString, tipoMedidorString, kdKeString, rrString,
            numElementosString, anoFabricacaoString, classeString, fiosString, portariaInmetroString;
    private RadioButton radioButtonMecanico, radioButtonEletronico;
    private Button botaoCriarMedidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_medidor);

        final BancoController crud = new BancoController(getBaseContext());
        Intent it = getIntent();
        String numeroSerieAntigo = it.getStringExtra("numeroSerie");
        String numeroGeralAntigo = it.getStringExtra("numeroGeral");
        String instalacaoAntigo = it.getStringExtra("instalacao");
        String modeloAntigo = it.getStringExtra("modelo");
        String fabricanteAntigo = it.getStringExtra("fabricante");
        String tensaoNominalAntigo = it.getStringExtra("tensaoNominal");
        String correnteNominalAntigo = it.getStringExtra("correnteNominal");
        String tipoMedidorAntigo = it.getStringExtra("tipoMedidor");
        String kdKeAntigo = it.getStringExtra("kdKe");
        String rrAntigo = it.getStringExtra("rr");
        String numElementosAntigo = it.getStringExtra("numElementos");
        String anoFabricacaoAntigo = it.getStringExtra("anoFabricacao");
        String classeAntigo = it.getStringExtra("classe");
        String fiosAntigo = it.getStringExtra("fios");
        String portariaInmetroAntigo = it.getStringExtra("portariaInmetro");


        botaoCriarMedidor = findViewById(R.id.buttonSalvarMedidor);

        numeroSerie = findViewById(R.id.numSerie);
        numeroSerie.setText(numeroSerieAntigo);
        numeroGeral = findViewById(R.id.NumGeral);
        numeroGeral.setText(numeroGeralAntigo);
        instalacao = findViewById(R.id.Instalacao);
        instalacao.setText(instalacaoAntigo);
        modelo = findViewById(R.id.ModeloMedidor);
        modelo.setText(modeloAntigo);
        fabricante = findViewById(R.id.fabricanteMedidor);
        fabricante.setText(fabricanteAntigo);
        tensaoNominal = findViewById(R.id.TensaoNominal);
        tensaoNominal.setText(tensaoNominalAntigo);
        correnteNominal = findViewById(R.id.CorrenteNominal);
        correnteNominal.setText(correnteNominalAntigo.toString());
        kdKe = findViewById(R.id.KdKe);
        kdKe.setText(kdKeAntigo);
        rr = findViewById(R.id.RR);
        rr.setText(rrAntigo);
        numElementos = findViewById(R.id.NumElementos);
        numElementos.setText(numElementosAntigo);
        anoFabricacao = findViewById(R.id.AnoFabricacao);
        anoFabricacao.setText(anoFabricacaoAntigo);
        classe = findViewById(R.id.Classe);
        classe.setText(classeAntigo);
        fios = findViewById(R.id.Fios);
        fios.setText(fiosAntigo);
        portariaInmetro = findViewById(R.id.PorInmetro);
        portariaInmetro.setText(portariaInmetroAntigo);

        radioButtonMecanico = findViewById(R.id.radioButtonAvaliador);
        radioButtonEletronico = findViewById(R.id.radioButtonEletronico);
        if (tipoMedidorAntigo.equals("Mecânico")) {
            radioButtonMecanico.setChecked(true);
        } else {
            radioButtonEletronico.setChecked(true);
        }

        botaoCriarMedidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
