package com.memtpadraomonofasico.apppadromonofsico.BancoDeDados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by loraine.duarte on 03/02/2018.
 */

public class BancoController {
    private SQLiteDatabase db;
    private CriaBanco banco;

    public BancoController(Context context){
        banco = new CriaBanco(context);
    }

    public String insereNovoAvaliador(String nome, String matricula){
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriaBanco.NOME_AVALIADOR, nome);
        valores.put(CriaBanco.MATRICULA, matricula);

        resultado = db.insert(CriaBanco.TABELA, null, valores);
        db.close();

        if (resultado ==-1){
            Log.d("Inseriu", "Erro ao inserir avaliador");
            return "Erro ao inserir registro";}
        else{
            Log.d("Inseriu", "Inseriu avaliador");
            return " Registro Inserido com sucesso";
        }
    }

    public Cursor pegaAvaliadores(){
        Cursor cursor;
        String[] campos =  {banco.ID,banco.NOME_AVALIADOR,banco.MATRICULA};
        db = banco.getReadableDatabase();
        cursor = db.query(banco.TABELA, campos, null, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        Log.d("BANCO", String.valueOf(cursor));

        return cursor;


    }
}
