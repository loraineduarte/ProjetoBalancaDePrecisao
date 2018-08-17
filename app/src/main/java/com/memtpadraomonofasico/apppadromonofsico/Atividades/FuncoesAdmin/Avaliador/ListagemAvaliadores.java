package com.memtpadraomonofasico.apppadromonofsico.Atividades.FuncoesAdmin.Avaliador;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.apppadromonofsico.R;

import java.util.ArrayList;
import java.util.List;

public class ListagemAvaliadores extends AppCompatActivity {

    private final CriaBanco banco = new CriaBanco(this);
    private final BancoController crud = new BancoController(this);
    private AdapterAvaliador adapter;
    private List<Avaliador> avaliadores;
    private Cursor cursorAvaliador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_avaliadores);

        ListView listaDeAvaliadores = findViewById(R.id.lista);
        avaliadores = todosAvaliadores();
        adapter = new AdapterAvaliador(avaliadores, this);
        listaDeAvaliadores.setAdapter(adapter);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        reloadAllData();
    }



    private List<Avaliador> todosAvaliadores() {

        List<Avaliador> av = new ArrayList<>();
        Cursor cursor = crud.pegaAvaliadores();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String nome = cursor.getString(1);
            String matricula = cursor.getString(2);
            Avaliador avaliador = new Avaliador(nome, matricula);
            av.add(avaliador);
        }
        return av;
    }

    private void deletarContatoNoBanco(String nome, String matricula, View view) {

        if (nome.equals("administrador") && (matricula.equals("admin"))) {
            Toast.makeText(getApplicationContext(), "Não é possível excluir o Administrador principal do sistema. ", Toast.LENGTH_LONG).show();


        } else {
            BancoController crud = new BancoController(getBaseContext());
            String cursor = crud.deletaAvaliador(nome, matricula);
            Toast.makeText(getApplicationContext(), cursor, Toast.LENGTH_LONG).show();
            reloadAllData();
        }

    }

    private void reloadAllData() {
        avaliadores = todosAvaliadores();
        adapter.updateItens(avaliadores);

    }


    public void editarContato(View view) {
        int position = (int) view.getTag();
        Avaliador avaliador = avaliadores.get(position);

        final BancoController crud = new BancoController(getBaseContext());
        String nome = avaliador.getNome();
        String matricula = avaliador.getMatricula();
        Cursor cursor = crud.pegarAvaliador(nome, matricula);
        String senha = cursor.getString(3);
        String tipoUsu = cursor.getString(4);

        if (nome.equals("administrador") && (matricula.equals("admin"))) {
            Toast.makeText(getApplicationContext(), "Não é possível editar o Administrador principal do sistema. ", Toast.LENGTH_LONG).show();

        } else {
            Intent intent = new Intent(this, EditarAvaliadorActivity.class);
            intent.putExtra("nome", nome);
            intent.putExtra("matricula", matricula);
            intent.putExtra("senha", senha);
            intent.putExtra("tipousu", tipoUsu);
            startActivity(intent);

        }

    }

    public void deletarContato(View view) {
        int position = (int) view.getTag();
        Avaliador avaliador = avaliadores.get(position);
        String nome = avaliador.getNome();
        String matricula = avaliador.getMatricula();
        deletarContatoNoBanco(nome, matricula, view);
    }

}
