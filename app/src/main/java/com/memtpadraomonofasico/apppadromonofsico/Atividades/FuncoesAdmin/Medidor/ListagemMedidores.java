package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Medidor;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.apppadromonofsico.R;

import java.util.ArrayList;
import java.util.List;

public class ListagemMedidores extends AppCompatActivity {

    private final CriaBanco banco = new CriaBanco(this);
    private final BancoController crud = new BancoController(this);
    private AdapterMedidor adapter;
    private List<Medidor> medidores;
    private Cursor cursorMedidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_medidores);

        ListView listaDeMedidores = findViewById(R.id.lista);
        medidores = todosMedidores();
        adapter = new AdapterMedidor(medidores, this);
        listaDeMedidores.setAdapter(adapter);


    }


    @Override
    protected void onRestart() {
        super.onRestart();
        reloadAllDataMedidor();

    }


    private List<Medidor> todosMedidores() {


        List<Medidor> av = new ArrayList<>();
        Cursor cursor = crud.pegaMedidores();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String numeroSerie = cursor.getString(1);
            String numeroGeral = cursor.getString(2);
            String instalacao = cursor.getString(3);
            String modelo = cursor.getString(4);
            String fabricante = cursor.getString(5);
            String tensaoNominal = cursor.getString(6);
            int correnteNominal = Integer.parseInt(cursor.getString(7));
            String tipoMedidor = cursor.getString(8);
            String kdKe = cursor.getString(9);
            String rr = cursor.getString(10);
            int numElementos = Integer.parseInt(cursor.getString(11));
            String anoFabricacao = cursor.getString(12);
            String classe = cursor.getString(13);
            int fios = Integer.parseInt(cursor.getString(14));
            String portariaInmetro = cursor.getString(15);

            Medidor medidor = new Medidor(numeroSerie, numeroGeral, instalacao, modelo, fabricante, tensaoNominal, correnteNominal, tipoMedidor, kdKe, rr, numElementos, anoFabricacao, classe, fios, portariaInmetro);
            av.add(medidor);
        }
        return av;
    }

    private void deletarMedidorNoBanco(String numeroSerie, View view) {

        String cursor = banco.DeletarMedidor(numeroSerie);
        Toast.makeText(getApplicationContext(), cursor, Toast.LENGTH_LONG).show();
        reloadAllDataMedidor();
    }

    private void reloadAllDataMedidor() {
        medidores = todosMedidores();
        adapter.updateItens(medidores);

    }


    public void editarMedidorr(View view) {

        int position = (int) view.getTag();
        Medidor avaliador = medidores.get(position);

        final BancoController crud = new BancoController(getBaseContext());
        String numeroSerie = avaliador.getNumeroSerie();
        String numeroGeral = avaliador.getNumeroGeral();
        Cursor cursor = crud.pegarMedidor(numeroSerie);

        Log.d("cursor", cursor.getString(0));

        String instalacao = cursor.getString(3);
        String modelo = cursor.getString(4);
        String fabricante = cursor.getString(5);
        String tensaoNominal = cursor.getString(6);
        int correnteNominal = Integer.parseInt(cursor.getString(7));
        String tipoMedidor = cursor.getString(8);
        String kdKe = cursor.getString(9);
        String rr = cursor.getString(10);
        int numElementos = Integer.parseInt(cursor.getString(11));
        String anoFabricacao = cursor.getString(12);
        String classe = cursor.getString(13);
        int fios = Integer.parseInt(cursor.getString(14));
        String portariaInmetro = cursor.getString(15);

        Intent intent = new Intent(this, EditarMedidorActivity.class);
        intent.putExtra("numeroSerie", numeroSerie);
        intent.putExtra("numeroGeral", numeroGeral);
        intent.putExtra("instalacao", instalacao);
        intent.putExtra("modelo", modelo);
        intent.putExtra("fabricante", fabricante);
        intent.putExtra("tensaoNominal", tensaoNominal);
        intent.putExtra("correnteNominal", correnteNominal);
        intent.putExtra("tipoMedidor", tipoMedidor);
        intent.putExtra("kdKe", kdKe);
        intent.putExtra("rr", rr);
        intent.putExtra("numElementos", numElementos);
        intent.putExtra("anoFabricacao", anoFabricacao);
        intent.putExtra("classe", classe);
        intent.putExtra("fios", fios);
        intent.putExtra("portariaInmetro", portariaInmetro);

        startActivity(intent);
    }

    public void deletarMedidorr(View view) {

        int position = (int) view.getTag();
        Medidor medidor = medidores.get(position);
        String numeroSerie = medidor.getNumeroSerie();
        String numeroGeral = medidor.getNumeroGeral();

        deletarMedidorNoBanco(numeroSerie, view);
    }

    public void editarMedidor(View view) {

        int position = (int) view.getTag();
        Medidor avaliador = medidores.get(position);

        final BancoController crud = new BancoController(getBaseContext());

        String numeroSerie = avaliador.getNumeroSerie();
        Cursor cursor = crud.pegarMedidor(numeroSerie);

        String instalacao = cursor.getString(2);
        String numeroGeral = avaliador.getNumeroGeral();
        String fabricante = cursor.getString(4);
        String numElementos = cursor.getString(5);
        String modelo = cursor.getString(6);
        String correnteNominal = (cursor.getString(7));
        String classe = cursor.getString(8);
        String rr = cursor.getString(9);
        String anoFabricacao = cursor.getString(10);
        String tensaoNominal = cursor.getString(11);
        String kdKe = cursor.getString(12);
        String portariaInmetro = cursor.getString(13);
        String fios = cursor.getString(14);
        String tipoMedidor = cursor.getString(15);

        Intent intent = new Intent(this, EditarMedidorActivity.class);
        intent.putExtra("numeroSerie", numeroSerie);
        intent.putExtra("numeroGeral", numeroGeral);
        intent.putExtra("instalacao", instalacao);
        intent.putExtra("modelo", modelo);
        intent.putExtra("fabricante", fabricante);
        intent.putExtra("tensaoNominal", tensaoNominal);
        intent.putExtra("correnteNominal", correnteNominal);
        intent.putExtra("tipoMedidor", tipoMedidor);
        intent.putExtra("kdKe", kdKe);
        intent.putExtra("rr", rr);
        intent.putExtra("numElementos", numElementos);
        intent.putExtra("anoFabricacao", anoFabricacao);
        intent.putExtra("classe", classe);
        intent.putExtra("fios", fios);
        intent.putExtra("portariaInmetro", portariaInmetro);

        startActivity(intent);
    }

    public void deletarMedidor(View view) {

        int position = (int) view.getTag();
        Medidor medidor = medidores.get(position);
        String numeroSerie = medidor.getNumeroSerie();
        String numeroGeral = medidor.getNumeroGeral();

        deletarMedidorNoBanco(numeroSerie, view);
    }
}
