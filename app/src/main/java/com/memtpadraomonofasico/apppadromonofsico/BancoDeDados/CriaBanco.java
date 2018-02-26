package com.memtpadraomonofasico.apppadromonofsico.BancoDeDados;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by loraine.duarte on 03/02/2018.
 */

public class CriaBanco extends SQLiteOpenHelper {

    // for our logs
    public static final String TAG = "DatabaseHandler.java";


    // database name
    protected static final String DATABASE_NAME = "PadraoMonofasicoDataBase";


    private static final String NOME_BANCO = "banco.db";
    private static final int DATABASE_VERSION = 1;


    //tabela de avaliador
    public static final String TABELA_AVALIADOR = "avaliador";
    public static final String ID_AVALIADOR = "_id_avaliador";
    public static final String NOME_AVALIADOR = "avaliador_nome";
    public static final String MATRICULA = "avaliador_matricula";
    //tabela de medidor
    public static final String TABELA_MEDIDOR = "medidor";
    public static final String ID_MEDIDOR = "_id_medidor";
    public static final String INSTALACAO = "medidor_instalacao";
    public static final String NUM_SERIE = "medidor_num_serie";
    public static final String NUM_GERAL = "medidor_num_geral";
    public static final String FABRICANTE = "medidor_fabricante";
    public static final String NUM_ELEMENTOS = "medidor_num_elementos";
    public static final String MODELO = "medidor_modelo";
    public static final String CORRENTE_NOMINAL = "medidor_corrente_nominal";
    public static final String CLASSE = "medidor_classe";
    public static final String ANO_FABRICACAO = "medidor_ano_fabricacao";
    public static final String TENSAO_NOMINAL = "medidor_tensao_nominal";
    public static final String KDKE = "medidor_KdKe";
    public static final String RR = "medidor_RR";
    public static final String PORT_INMETRO = "medidor_port_inmetro";
    public static final String FIOS = "medidor_fios";
    public static final String TIPO_MEDIDOR = "medidor_tipo_medidor";


    public CriaBanco(Context context){
        super(context, NOME_BANCO,null,DATABASE_VERSION);
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
                + INSTALACAO + " integer, "
                + NUM_SERIE + " text, "
                + NUM_GERAL + " text, "
                + FABRICANTE + " text, "
                + NUM_ELEMENTOS + " integer, "
                + MODELO + " text, "
                + CORRENTE_NOMINAL + " integer, "
                + CLASSE + " text, "
                + ANO_FABRICACAO + " integer, "
                + TENSAO_NOMINAL + " integer, "
                + KDKE + " integer, "
                + RR + " integer, "
                + FIOS + " integer, "
                + PORT_INMETRO + " text, "
                + TIPO_MEDIDOR + " text "
                +" )";
        db.execSQL(sqlMedidor);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABELA_AVALIADOR);
        db.execSQL(" DROP TABLE IF EXISTS " + TABELA_MEDIDOR);
        onCreate(db);
    }

    public String[] SelectAllAvaliadores() {
        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT avaliador_matricula FROM " + TABELA_AVALIADOR;
            Cursor cursor = db.rawQuery(strSQL, null);
            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];
                    int i= 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String SelecionaAvaliador(String matricula) {
        try {
            String arrData = "";
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT avaliador_nome FROM " + TABELA_AVALIADOR + " WHERE avaliador_matricula = " + matricula ;
            Cursor cursor = db.rawQuery(strSQL, null);

            cursor.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[] SelectAllMedidores() {
        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT medidor_num_serie FROM " + TABELA_MEDIDOR;
            Cursor cursor = db.rawQuery(strSQL, null);
            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getCount()];
                    int i= 0;
                    do {
                        arrData[i] = cursor.getString(0);
                        i++;
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }
}
