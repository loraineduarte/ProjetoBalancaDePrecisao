package com.memtpadraomonofasico.apppadromonofsico.BancoDeDados;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class CriaBanco extends SQLiteOpenHelper {

    //tabela de avaliador
    public static final String TABELA_AVALIADOR = "avaliador";
    public static final String ID_AVALIADOR = "_id_avaliador";
    public static final String NOME_AVALIADOR = "avaliador_nome";
    public static final String MATRICULA = "avaliador_matricula";
    public static final String SENHA = "avaliador_senha";
    public static final String ADMIN = "avaliador_admin";
    //tabela de medidor
    public static final String TABELA_MEDIDOR = "medidor";
    public static final String ID_MEDIDOR = "_id_medidor";
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
    //tabela de medidor
    public static final String TABELA_MENSAGENS = "mensagens";
    public static final String ID_MENSAGENS = "_id_mensagens";
    public static final String LOCAL_MENSAGEM = "local_mensagem";
    public static final String MENSAGEM = "mensagem";


    private static final String NOME_BANCO = "banco.db";


    private static final int DATABASE_VERSION = 2;
    public SQLiteDatabase db;
    BancoController banco;


    public CriaBanco(Context context){
        super(context, NOME_BANCO,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlMedidor = "CREATE TABLE IF NOT EXISTS " + TABELA_MEDIDOR + " (  "
                + ID_MEDIDOR + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + NUM_GERAL + " text, "
                + FABRICANTE + " text, "
                + NUM_ELEMENTOS + " integer, "
                + MODELO + " text, "
                + CORRENTE_NOMINAL + " double, "
                + CLASSE + " text, "
                + ANO_FABRICACAO + " integer, "
                + TENSAO_NOMINAL + " integer, "
                + KDKE + " double, "
                + RR + " integer, "
                + FIOS + " integer, "
                + PORT_INMETRO + " text, "
                + TIPO_MEDIDOR + " text "
                + " ) ";
        db.execSQL(sqlMedidor);

        String sqlAvaliador = "CREATE TABLE IF NOT EXISTS " + TABELA_AVALIADOR + " (  "
                + ID_AVALIADOR + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + NOME_AVALIADOR + " text, "
                + MATRICULA + " text, "
                + SENHA + " text, "
                + ADMIN + " boolean "
                + " )";
        db.execSQL(sqlAvaliador);


        String sqlMensagens = "CREATE TABLE IF NOT EXISTS " + TABELA_MENSAGENS + " (  "
                + ID_MENSAGENS + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + LOCAL_MENSAGEM + " text, "
                + MENSAGEM + " text "
                + " )";
        db.execSQL(sqlMensagens);


        final String Insert_Data_Admin = "INSERT or replace INTO avaliador VALUES( 1, 'administrador','12345','admin', 'true');";
        db.execSQL(Insert_Data_Admin);

        final String Insert_Data_Mensagens = "INSERT or replace INTO mensagens VALUES ( 1, 'Selos de Calibração','Base Perfurada')," +
                "( 2, 'Selos de Calibração','Dispositivo de selagem quebrado'), ( 3, 'Selos de Calibração','Selo ausente ou não padronizado'), " +
                "( 4, 'Selos de Calibração','Selo ausente ou violado reconstituído'), ( 5, 'Selos de Calibração','Selo ausente ou violado outro ')," +
                " ( 6, 'Selos de Calibração','Selo ausente'), ( 7, 'Selos de Calibração','Selo íntegro'), ( 8, 'Selos de Calibração','Selo não padronizado')," +
                "( 9, 'Selos de Calibração','Selo de policarbonato íntegro'), ( 10, 'Selos de Calibração','Selo PP íntegro'), ( 11, 'Selos de Calibração','Selo reconstituído - reapertado - colado')," +
                "( 12, 'Selos de Calibração','Selo SB íntegro'), ( 13, 'Selos de Calibração','Selo violado'), ( 14, 'Selos de Calibração','Tampa solidarizada sem selo'), " +
                //teste de registrador
                "( 15, 'Registrador','Registrador não registra corretamente'),( 16, 'Registrador','Registrador não registra consumo'), ( 17, 'Registrador','Registrador defeituoso'), " +
                "( 18, 'Registrador','Registrador travado, provocando atrito excessivo'), ( 19, 'Registrador','Não possibilita teste de registrador')," +
                "( 20, 'Registrador','Ponteiros desalinhados'), ( 21, 'Registrador','Ciclômetros desalinhados'), ( 22, 'Registrador','Display apagado')," +
                "(23, 'Registrador','Primeira engrenagem desacoplada'), ( 24, 'Registrador','Primeira engrenagem com acoplamento excessivo'), ( 25, 'Registrador','Engrenagem com acoplamento intermitente')," +
                "( 26, 'Registrador','Primeira engrenagem do registrador trocada'), ( 27, 'Registrador','Engrenagem com acoplamento intermitente'), ( 28, 'Registrador','Engrenagem limada')," +
                "( 29, 'Registrador','Rosca sem fim desgastada'), ( 30, 'Registrador','Registrador incorreto constante disco'), ( 31, 'Registrador','Suporte de fixação do registrador quebrado')," +
                "( 32, 'Registrador','THS/ Irrig-Erro programação constante e med com cemig')," +
                //circuito de potencial
                "(33, 'Circuito de potencial','Circuito Potencial Interrompido - Elo Retirado'), ( 34, 'Circuito de potencial','Circuito Potencial Interrompido - Elo Aberto/ Desapertado')," +
                " (35, 'Circuito de potencial','Circuito Potencial Interrompido - Elo com Cola'), (36, 'Circuito de potencial','Elo com Sinal de Carbonização')," +
                "(37, 'Circuito de potencial','Circuito Pot. Interrompido - Elo Retirado BLOCO'), (38, 'Circuito de potencial','Circuito Pot. Interrompido - Elo Aberto/ Desapertado BLOCO')," +
                "(39, 'Circuito de potencial','Bobina interrompida sob tensão induzida'), (40, 'Circuito de potencial','Bobina Interrompida sem causa detectada')," +
                "(41, 'Circuito de potencial','Circuito de potencial com a fiação cortada')," +
                //situações observados
                "(42, 'Situações Observadas','Bloco de Terminais Carbonizado'), (43, 'Situações Observadas','Bobina interrompida sem causa detectada'), " +
                "(44, 'Situações Observadas','Circuito eletrônico defeituoso'), (45, 'Situações Observadas','Componentes eletrônicos queimados')," +
                "(46, 'Situações Observadas','Elemento móvel descentralizado'), (47, 'Situações Observadas','LED apagado/ não emite pulso')," +
                "(48, 'Situações Observadas','LED aceso direto'), (49, 'Situações Observadas','LED disparado'), (50, 'Situações Observadas','LED intermitente')," +
                "(51, 'Situações Observadas','Mancais deslocados'), (52, 'Situações Observadas','Marcas nos parafusos dos mancais'), (53, 'Situações Observadas','Neutro carbonizado')," +
                "(54, 'Situações Observadas','Placa identificação amassada'), (55, 'Situações Observadas','Placa solta'), (56, 'Situações Observadas','Ranhuras no disco do elemento móvel')," +
                "(57, 'Situações Observadas','Sinais de carbonização'), (58, 'Situações Observadas','Sujeira interna - atrito')," +
                //informações complementares
                "(59, 'Informações Complementares','Sem tampa do bloco de terminais'), (60, 'Informações Complementares','Tampa Solidarizada')," +
                "(61, 'Informações Complementares','Tampa quebrada'), (62, 'Informações Complementares','Tampa quebrada no transporte'), (63, 'Informações Complementares','Selo rompido no laboratório')," +
                "(64, 'Informações Complementares','Terminais de corrente oxidados'), (65, 'Informações Complementares','Leitura divergente');";
        db.execSQL(Insert_Data_Mensagens);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABELA_AVALIADOR);
        db.execSQL(" DROP TABLE IF EXISTS " + TABELA_MEDIDOR);
        db.execSQL(" DROP TABLE IF EXISTS " + TABELA_MENSAGENS);
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
            String strSQL = "SELECT medidor_num_geral FROM " + TABELA_MEDIDOR;
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
        String[] campos = {NUM_GERAL, FABRICANTE, NUM_ELEMENTOS, MODELO, CORRENTE_NOMINAL, CLASSE, ANO_FABRICACAO, TENSAO_NOMINAL, KDKE,
                RR, FIOS, PORT_INMETRO, TIPO_MEDIDOR};

        cursor = db.query(TABELA_MEDIDOR, campos, NUM_GERAL + " = ?", selectionArgs, null, null, null, null);

        if(cursor!=null){
            if(cursor.moveToFirst()){
                arrData = new String[14];
                arrData[0] = cursor.getString(cursor.getColumnIndexOrThrow(NUM_GERAL));
                arrData[2] = cursor.getString(cursor.getColumnIndexOrThrow(MODELO));
                arrData[3] = cursor.getString(cursor.getColumnIndexOrThrow(FABRICANTE));
                arrData[4] = cursor.getString(cursor.getColumnIndexOrThrow(TENSAO_NOMINAL));
                arrData[5] = String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(CORRENTE_NOMINAL)));
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

    public String DeletarMedidor(String codigo) {


        int cursor;
        SQLiteDatabase db;
        db = this.getReadableDatabase();
        String[] selectionArgs = new String[1];
        selectionArgs[0] = codigo;
        cursor = db.delete(TABELA_MEDIDOR, NUM_GERAL + " = ?", selectionArgs);

        Log.d("ARRAY", String.valueOf(cursor));
        if (cursor == -1) {
            Log.d("Deletou", "Erro ao deletar avaliador");
            return "Erro ao deletar registro";
        } else {
            Log.d("Deletou", "Deletou avaliador");
            return " Registro deletado com sucesso";
        }
    }
}
