package com.memtpadraomonofasico.apppadromonofsico.BancoDeDados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by loraine.duarte on 03/02/2018.
 */

public class CriaBanco extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "banco.db";
    public static final String TABELA = "avaliador";
    public static final String ID = "_id";
    public static final String NOME_AVALIADOR = "avaliador_nome";
    public static final String MATRICULA = "avaliador_matricula";
    private static final int VERSAO = 1;

    public CriaBanco(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS "+TABELA+" (  "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + NOME_AVALIADOR + " text, "
                + MATRICULA + " text "
                +" )";
        db.execSQL(sql);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABELA);
        onCreate(db);
    }
}
