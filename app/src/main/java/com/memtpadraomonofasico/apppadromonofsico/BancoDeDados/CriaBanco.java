package com.memtpadraomonofasico.apppadromonofsico.BancoDeDados;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CriaBanco extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "banco.db";
    private static final int DATABASE_VERSION = 2;

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
                + NUM_SERIE + " text, "
                + INSTALACAO + " integer, "
                + NUM_GERAL + " text, "
                + FABRICANTE + " text, "
                + NUM_ELEMENTOS + " integer, "
                + MODELO + " text, "
                + CORRENTE_NOMINAL + " integer, "
                + CLASSE + " text, "
                + ANO_FABRICACAO + " integer, "
                + TENSAO_NOMINAL + " integer, "
                + KDKE + " double, "
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

            assert cursor != null;
            cursor.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[] SelecionaAvaliador(String matricula) {
        try {
            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT avaliador_nome FROM " + TABELA_AVALIADOR + " WHERE avaliador_matricula = " + matricula ;
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
            assert cursor != null;
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

            assert cursor != null;
            cursor.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }
    }

    public String[] SelecionaMedidor(String codigo) {

        String arrData[] = null;
        Cursor cursor;
        SQLiteDatabase db;
         db = this.getReadableDatabase();
        String[] selectionArgs = new String[1];
        selectionArgs[0] = codigo;
        String[] campos =  {INSTALACAO, NUM_GERAL, FABRICANTE, NUM_ELEMENTOS, MODELO, CORRENTE_NOMINAL, CLASSE, ANO_FABRICACAO, TENSAO_NOMINAL, KDKE,
                RR, FIOS, PORT_INMETRO, TIPO_MEDIDOR};

        cursor = db.query(TABELA_MEDIDOR , campos, NUM_SERIE + " = ?", selectionArgs, null, null, null, null);

        if(cursor!=null){
            if(cursor.moveToFirst()){
                arrData = new String[14];
                arrData[0] = cursor.getString(cursor.getColumnIndexOrThrow(NUM_GERAL));
                arrData[1] = cursor.getString(cursor.getColumnIndexOrThrow(INSTALACAO));
                arrData[2] = cursor.getString(cursor.getColumnIndexOrThrow(MODELO));
                arrData[3] = cursor.getString(cursor.getColumnIndexOrThrow(FABRICANTE));
                arrData[4] = cursor.getString(cursor.getColumnIndexOrThrow(TENSAO_NOMINAL));
                arrData[5] = cursor.getString(cursor.getColumnIndexOrThrow(CORRENTE_NOMINAL));
                arrData[6] = cursor.getString(cursor.getColumnIndexOrThrow(TIPO_MEDIDOR));
                arrData[7] = String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(KDKE)));
                arrData[8] = cursor.getString(cursor.getColumnIndexOrThrow(RR));
                arrData[9] = cursor.getString(cursor.getColumnIndexOrThrow(NUM_ELEMENTOS));
                arrData[10] = cursor.getString(cursor.getColumnIndexOrThrow(ANO_FABRICACAO));
                arrData[11] = cursor.getString(cursor.getColumnIndexOrThrow(CLASSE));
                arrData[12] = cursor.getString(cursor.getColumnIndexOrThrow(FIOS));
                arrData[13] = cursor.getString(cursor.getColumnIndexOrThrow(PORT_INMETRO));

            }
        }
        assert cursor != null;
        cursor.close();
        return arrData;
    }
}
