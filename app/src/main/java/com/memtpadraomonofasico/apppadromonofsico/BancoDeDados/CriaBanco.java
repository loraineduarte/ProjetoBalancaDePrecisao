package com.memtpadraomonofasico.apppadromonofsico.BancoDeDados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by loraine.duarte on 03/02/2018.
 */

public class CriaBanco extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "banco.db";
    private static final int VERSAO = 1;
    //tabela de avaliador
    public static final String TABELA_AVALIADOR = "avaliador";
    public static final String ID_AVALIADOR = "_id_avaliador";
    public static final String NOME_AVALIADOR = "avaliador_nome";
    public static final String MATRICULA = "avaliador_matricula";
    //tabela de medidor
    public static final String TABELA_MEDIDOR = "medidor";
    public static final String ID_MEDIDOR = "_id_medidor";
    public static final String NUM_SERIE = "medidor_num_serie";
    public static final String FABRICANTE = "medidor_fabricante";
    public static final String NUM_ELEMENTOS = "medidor_num_elementos";
    public static final String MODELO = "medidor_modelo";
    public static final String CORRENTE_NOMINAL = "medidor_corrente_nominal";
    public static final String CLASSE = "medidor_classe";
    public static final String ANO_FABRICACAO = "medidor_ano_fabricacao";
    public static final String TENSAO_NOMINAL = "medidor_tensao_nominal";
    public static final String KDKE = "medidor_KdKe";
    public static final String PORT_INMETRO = "medidor_port_inmetro";


    public CriaBanco(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlAvaliador = "CREATE TABLE IF NOT EXISTS "+TABELA_AVALIADOR+" (  "
                + ID_AVALIADOR + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + NOME_AVALIADOR + " text, "
                + MATRICULA + " text "
                +" )";
        db.execSQL(sqlAvaliador);

        String sqlMedidor = "CREATE TABLE IF NOT EXISTS "+TABELA_MEDIDOR+" (  "
                + ID_MEDIDOR + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + TABELA_MEDIDOR + " text, "
                + ID_MEDIDOR + " text, "
                + NUM_SERIE + " text, "
                + FABRICANTE + " text, "
                + NUM_ELEMENTOS + " text, "
                + MODELO + " text, "
                + CORRENTE_NOMINAL + " text, "
                + CLASSE + " text, "
                + ANO_FABRICACAO + " text, "
                + TENSAO_NOMINAL + " text, "
                + KDKE + " text, "
                + PORT_INMETRO + " text "
                +" )";
        db.execSQL(sqlMedidor);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABELA_AVALIADOR);
        db.execSQL(" DROP TABLE IF EXISTS " + TABELA_MEDIDOR);
        onCreate(db);
    }
}
