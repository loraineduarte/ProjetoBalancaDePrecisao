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

        resultado = db.insert(CriaBanco.TABELA_AVALIADOR, null, valores);
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
        String[] campos =  {banco.ID_AVALIADOR,banco.NOME_AVALIADOR,banco.MATRICULA};
        db = banco.getReadableDatabase();
        cursor = db.query(banco.TABELA_AVALIADOR, campos, null, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        Log.d("BANCO", String.valueOf(cursor));

        return cursor;

    }



    public String insereNovoMedidor(int medidor_instalacao, String medidor_num_serie , String medidor_num_geral ,String medidor_fabricante, int medidor_num_elementos, String medidor_modelo,
                                    int medidor_corrente_nominal, String medidor_classe, int medidor_RR, int medidor_ano_fabricacao,
                                    int medidor_tensao_nominal, int medidor_KdKe, String medidor_port_inmetro, int medidor_fios, String medidor_tipo_medidor){
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriaBanco.NUM_SERIE, medidor_num_serie);
        valores.put(CriaBanco.INSTALACAO, medidor_instalacao);
        valores.put(CriaBanco.NUM_GERAL, medidor_num_geral);
        valores.put(CriaBanco.FABRICANTE, medidor_fabricante);
        valores.put(CriaBanco.NUM_ELEMENTOS, medidor_num_elementos);
        valores.put(CriaBanco.MODELO, medidor_modelo);
        valores.put(CriaBanco.CORRENTE_NOMINAL, medidor_corrente_nominal);
        valores.put(CriaBanco.CLASSE, medidor_classe);
        valores.put(CriaBanco.RR, medidor_RR);
        valores.put(CriaBanco.ANO_FABRICACAO, medidor_ano_fabricacao);
        valores.put(CriaBanco.TENSAO_NOMINAL, medidor_tensao_nominal);
        valores.put(CriaBanco.KDKE, medidor_KdKe);
        valores.put(CriaBanco.PORT_INMETRO, medidor_port_inmetro);
        valores.put(CriaBanco.FIOS, medidor_fios);
        valores.put(CriaBanco.TIPO_MEDIDOR, medidor_tipo_medidor);

        resultado = db.insert(CriaBanco.TABELA_MEDIDOR, null, valores);
        db.close();

        if (resultado ==-1){
            Log.d("Inseriu", "Erro ao inserir medidor");
            return "Erro ao inserir registro";}
        else{
            Log.d("Inseriu", "Inseriu medidor");
            return " Registro Inserido com sucesso";
        }
    }

    public Cursor pegaMedidores(){
        Cursor cursor;
        String[] campos =  {banco.ID_MEDIDOR,banco.NUM_SERIE,banco.INSTALACAO, banco.NUM_GERAL, banco.FABRICANTE, banco.NUM_ELEMENTOS, banco.MODELO, banco.CORRENTE_NOMINAL,
                banco.CLASSE, banco.RR, banco.ANO_FABRICACAO, banco.TENSAO_NOMINAL, banco.KDKE, banco.PORT_INMETRO, banco.FIOS, banco.TIPO_MEDIDOR};
        db = banco.getReadableDatabase();
        cursor = db.query(banco.TABELA_MEDIDOR, campos, null, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        Log.d("BANCO", String.valueOf(cursor));

        return cursor;

    }

}
