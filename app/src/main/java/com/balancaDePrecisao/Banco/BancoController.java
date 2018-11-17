package com.balancaDePrecisao.Banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by loraine.duarte on 23/09/2018.
 */
public class BancoController {

    private SQLiteDatabase db;
    private final CriaBanco banco;

    public BancoController(Context context){
        banco = new CriaBanco(context);
    }


    public Cursor pegaDados() {

        String selectQuery = "SELECT * FROM "+ CriaBanco.TABELA_DADOS;

        db = banco.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();

        return cursor;
    }


    public String insere(Dado dado) {

        db = banco.getWritableDatabase();
        long resultado;
        ContentValues dados = new ContentValues();
        dados.put("dados_peso", dado.getPeso());
        dados.put("dados_dataHora", dado.getDataHora());
        dados.put("dados_descricao", dado.getDescricao());

        resultado = db.insert(CriaBanco.TABELA_DADOS, null, dados);
        db.close();

        if (resultado ==-1){
            Log.d("Inseriu", "Erro ao inserir medidor");
            return "Erro ao inserir registro";}
        else{
            Log.d("Inseriu", "Inseriu medidor");
            return " Registro Inserido com sucesso";
        }
    }
}
