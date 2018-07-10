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

            String numeroGeral = cursor.getString(1);
            String modelo = cursor.getString(2);
            String fabricante = cursor.getString(3);
            String tensaoNominal = cursor.getString(4);
            int correnteNominal = Integer.parseInt(cursor.getString(5));
            String tipoMedidor = cursor.getString(6);
            String kdKe = cursor.getString(7);
            String rr = cursor.getString(8);
            int numElementos = Integer.parseInt(cursor.getString(9));
            String anoFabricacao = cursor.getString(10);
            String classe = cursor.getString(11);
            int fios = Integer.parseInt(cursor.getString(12));
            String portariaInmetro = cursor.getString(13);

            Medidor medidor = new Medidor(numeroGeral, modelo, fabricante, tensaoNominal, correnteNominal, tipoMedidor, kdKe, rr, numElementos, anoFabricacao, classe, fios, portariaInmetro);
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

        String numeroGeral = avaliador.getNumeroGeral();
        Cursor cursor = crud.pegarMedidor(numeroGeral);

        Log.d("cursor", cursor.getString(0));

        String fabricante = cursor.getString(0);
        String numElementos = cursor.getString(1);
        String modelo = cursor.getString(2);
        String correnteNominal = (cursor.getString(3));
        String classe = cursor.getString(4);
        String rr = cursor.getString(5);
        String anoFabricacao = cursor.getString(6);
        String tensaoNominal = cursor.getString(7);
        String kdKe = cursor.getString(8);
        String portariaInmetro = cursor.getString(9);
        String fios = cursor.getString(10);
        String tipoMedidor = cursor.getString(11);

        Intent intent = new Intent(this, EditarMedidorActivity.class);

        intent.putExtra("numeroGeral", numeroGeral);
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
        String numeroGeral = medidor.getNumeroGeral();

        deletarMedidorNoBanco(numeroGeral, view);
    }

    public void editarMedidor(View view) {

        int position = (int) view.getTag();
        Medidor avaliador = medidores.get(position);

        final BancoController crud = new BancoController(getBaseContext());
        String numeroGeral = avaliador.getNumeroGeral();
        Cursor cursor = crud.pegarMedidor(numeroGeral);

        String fabricante = cursor.getString(0);
        String numElementos = cursor.getString(1);
        String modelo = cursor.getString(2);
        String correnteNominal = (cursor.getString(3));
        String classe = cursor.getString(4);
        String rr = cursor.getString(5);
        String anoFabricacao = cursor.getString(6);
        String tensaoNominal = cursor.getString(7);
        String kdKe = cursor.getString(8);
        String portariaInmetro = cursor.getString(9);
        String fios = cursor.getString(10);
        String tipoMedidor = cursor.getString(11);

        Intent intent = new Intent(this, EditarMedidorActivity.class);
        intent.putExtra("numeroGeral", numeroGeral);
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
        String numeroGeral = medidor.getNumeroGeral();

        deletarMedidorNoBanco(numeroGeral, view);
    }
}
