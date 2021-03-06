package com.balancaDePrecisao.Banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by loraine.duarte on 29/09/2018.
 */

public class CriaBanco extends SQLiteOpenHelper {

    //tabela de avaliador
    public static final String TABELA_DADOS = "tabela_dados";
    public static final String ID_DADOS = "_id_dados";
    public static final String DADOS_DATA_HORA = "dados_dataHora";
    public static final String DADOS_PESO = "dados_peso";
    public static final String DADOS_DESCRICAO = "dados_descricao";

    private static final String NOME_BANCO = "banco.db";


    private static final int DATABASE_VERSION = 1;
    public SQLiteDatabase db;
    BancoController banco;


    public CriaBanco(Context context) {
        super(context, NOME_BANCO, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String sqlDados = "CREATE TABLE IF NOT EXISTS " + TABELA_DADOS + " (  "
                + ID_DADOS + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + DADOS_DATA_HORA + " text, "//TODO OLHAR PARA COLOCAR TIMESTAMP
                + DADOS_PESO + " text, "
                + DADOS_DESCRICAO + " text "
                + " )";
        db.execSQL(sqlDados);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 0:
                db.execSQL(" DROP TABLE IF EXISTS " + TABELA_DADOS);
                onCreate(db);
                break;
        }

    }



}

