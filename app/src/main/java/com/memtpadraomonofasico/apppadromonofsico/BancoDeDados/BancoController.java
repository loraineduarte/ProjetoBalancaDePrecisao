package com.memtpadraomonofasico.apppadromonofsico.BancoDeDados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BancoController {
    private final CriaBanco banco;
    private SQLiteDatabase db;

    public BancoController(Context context){
        banco = new CriaBanco(context);
    }

    public String insereNovoAvaliador(String nome, String matricula, String senha, boolean admin){
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriaBanco.NOME_AVALIADOR, nome);
        valores.put(CriaBanco.MATRICULA, matricula);
        valores.put(CriaBanco.SENHA, senha);
        valores.put(CriaBanco.ADMIN, admin);

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
        String[] campos =  {CriaBanco.ID_AVALIADOR, CriaBanco.NOME_AVALIADOR, CriaBanco.MATRICULA, CriaBanco.SENHA, CriaBanco.ADMIN };
        db = banco.getReadableDatabase();
        cursor = db.query(CriaBanco.TABELA_AVALIADOR, campos, null, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();

        return cursor;

    }


    public String insereNovoMedidor(String medidor_num_geral, String medidor_fabricante, int medidor_num_elementos, String medidor_modelo,
                                    Double medidor_corrente_nominal, String medidor_classe, String medidor_RR,
                                    int medidor_tensao_nominal, Double medidor_KdKe, String medidor_port_inmetro, int medidor_fios, String medidor_tipo_medidor){
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriaBanco.NUM_GERAL, medidor_num_geral);
        valores.put(CriaBanco.FABRICANTE, medidor_fabricante);
        valores.put(CriaBanco.NUM_ELEMENTOS, medidor_num_elementos);
        valores.put(CriaBanco.MODELO, medidor_modelo);
        valores.put(CriaBanco.CORRENTE_NOMINAL, medidor_corrente_nominal);
        valores.put(CriaBanco.CLASSE, medidor_classe);
        valores.put(CriaBanco.RR, medidor_RR);
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
        String[] campos = {CriaBanco.ID_MEDIDOR, CriaBanco.NUM_GERAL, CriaBanco.FABRICANTE, CriaBanco.NUM_ELEMENTOS, CriaBanco.MODELO, CriaBanco.CORRENTE_NOMINAL,
                CriaBanco.CLASSE, CriaBanco.RR, CriaBanco.TENSAO_NOMINAL, CriaBanco.KDKE, CriaBanco.PORT_INMETRO, CriaBanco.FIOS, CriaBanco.TIPO_MEDIDOR};
        db = banco.getReadableDatabase();
        cursor = db.query(CriaBanco.TABELA_MEDIDOR, campos, null, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();

        return cursor;

    }

    public Cursor validaLogin(String matricula, String senha) {

        Cursor cursor;
        String[] campos =  {CriaBanco.ID_AVALIADOR, CriaBanco.NOME_AVALIADOR, CriaBanco.MATRICULA, CriaBanco.SENHA, CriaBanco.ADMIN };
        String avaliador =  CriaBanco.MATRICULA +" = ? AND " +  CriaBanco.SENHA + " = ? " ;
        String[] avaliadorArgs =  {matricula, senha};
        db = banco.getReadableDatabase();
        cursor = db.query(CriaBanco.TABELA_AVALIADOR, campos, avaliador, avaliadorArgs, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();

        return cursor;
    }

    public String pegaTipoUsuario(String email, String senha) {

        Cursor cursor;
        String[] campos =  {CriaBanco.ADMIN };
        String avaliador =  CriaBanco.MATRICULA +" = ? AND " +  CriaBanco.SENHA + " = ? " ;
        String[] avaliadorArgs =  {email, senha};
        db = banco.getReadableDatabase();
        cursor = db.query(CriaBanco.TABELA_AVALIADOR, campos, avaliador, avaliadorArgs, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();

        String valor = cursor.getString(cursor.getColumnIndexOrThrow("avaliador_admin"));
        cursor.close();
        return valor;
    }

    public String deletaAvaliador(String nome, String matricula) {
        db = banco.getWritableDatabase();
        long resultado;
        String avaliador = CriaBanco.NOME_AVALIADOR + " = ? AND " + CriaBanco.MATRICULA + " = ? ";
        String[] avaliadorArgs = {nome, matricula};
        resultado = db.delete(CriaBanco.TABELA_AVALIADOR, avaliador, avaliadorArgs);
        db.close();

        if (resultado == -1) {
            Log.d("Deletou", "Erro ao deletar avaliador");
            return "Erro ao deletar registro";
        } else {
            Log.d("Deletou", "Deletou avaliador");
            return " Registro deletado com sucesso";
        }
    }

    public String updateAvaliador(String nomeNovoString, String matriculaNovoString, String senhaNovaString, boolean bNovo, String nomeAntigo, String matriculaAntigo) {

        db = banco.getWritableDatabase();
        long resultado;
        String avaliador = CriaBanco.NOME_AVALIADOR + " = ? AND " + CriaBanco.MATRICULA + " = ? ";
        String[] avaliadorArgs = {nomeAntigo, matriculaAntigo};
        resultado = db.delete(CriaBanco.TABELA_AVALIADOR, avaliador, avaliadorArgs);
        db.close();

        if (resultado == -1) {
            Log.d("Inseriu", "Erro ao deletar avaliador");
        } else {
            Log.d("Inseriu", "Deletou avaliador");
        }

        ContentValues valores;
        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriaBanco.NOME_AVALIADOR, nomeNovoString);
        valores.put(CriaBanco.MATRICULA, matriculaNovoString);
        valores.put(CriaBanco.SENHA, senhaNovaString);
        valores.put(CriaBanco.ADMIN, bNovo);

        resultado = db.insert(CriaBanco.TABELA_AVALIADOR, null, valores);
        db.close();


        if (resultado == -1) {
            Log.d("atualizou", "Erro ao atualizar avaliador");
            return "Erro ao atualizar registro";
        } else {
            Log.d("atualizou", "Atualizou avaliador");
            return " Registro atualizado com sucesso";
        }
    }

    public Cursor pegarAvaliador(String nome, String matricula) {

        Cursor cursor;
        String[] campos = {CriaBanco.ID_AVALIADOR, CriaBanco.NOME_AVALIADOR, CriaBanco.MATRICULA, CriaBanco.SENHA, CriaBanco.ADMIN};
        String avaliador = CriaBanco.NOME_AVALIADOR + " = ? AND " + CriaBanco.MATRICULA + " = ? ";
        String[] avaliadorArgs = {nome, matricula};
        db = banco.getReadableDatabase();
        cursor = db.query(CriaBanco.TABELA_AVALIADOR, campos, avaliador, avaliadorArgs, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();

        return cursor;
    }


    public Cursor pegarMedidor(String numeroGeral) {

        Cursor cursor;
        String[] campos = {CriaBanco.FABRICANTE, CriaBanco.NUM_ELEMENTOS, CriaBanco.MODELO, CriaBanco.CORRENTE_NOMINAL,
                CriaBanco.CLASSE, CriaBanco.RR, CriaBanco.TENSAO_NOMINAL, CriaBanco.KDKE, CriaBanco.PORT_INMETRO, CriaBanco.FIOS, CriaBanco.TIPO_MEDIDOR};
        String medidor = CriaBanco.NUM_GERAL + " = ? ";
        String[] avaliadorArgs = {numeroGeral};
        db = banco.getReadableDatabase();
        cursor = db.query(CriaBanco.TABELA_MEDIDOR, campos, medidor, avaliadorArgs, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        db.close();

        return cursor;
    }

    public Cursor pegaMensagens() {

        Cursor cursor;
        String[] campos = {CriaBanco.ID_MENSAGENS, CriaBanco.LOCAL_MENSAGEM, CriaBanco.MENSAGEM};
        db = banco.getReadableDatabase();
        cursor = db.query(CriaBanco.TABELA_MENSAGENS, campos, null, null, null, null, CriaBanco.LOCAL_MENSAGEM, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();

        return cursor;
    }

    public String insereMensagem(String mensagemString, String tabelaEscolhida) {

        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriaBanco.LOCAL_MENSAGEM, tabelaEscolhida);
        valores.put(CriaBanco.MENSAGEM, mensagemString);

        resultado = db.insert(CriaBanco.TABELA_MENSAGENS, null, valores);
        db.close();

        if (resultado == -1) {
            Log.d("Inseriu", "Erro ao inserir mensagem");
            return "Erro ao inserir registro";
        } else {
            Log.d("Inseriu", "Inseriu mensagem");
            return " Registro Inserido com sucesso";
        }
    }

    public String deletaMensagem(String mensagem, String tabela) {

        db = banco.getWritableDatabase();
        long resultado;
        String msg = CriaBanco.MENSAGEM + " = ? AND " + CriaBanco.LOCAL_MENSAGEM + " = ? ";
        String[] Args = {mensagem, tabela};
        resultado = db.delete(CriaBanco.TABELA_MENSAGENS, msg, Args);
        db.close();

        if (resultado == -1) {
            Log.d("Deletou", "Erro ao deletar avaliador");
            return "Erro ao deletar registro";
        } else {
            Log.d("Deletou", "Deletou avaliador");
            return " Registro deletado com sucesso";
        }
    }

    public String updateMensagem(String msgAntigaString, String tabelaAntigaString, String msgString, String tabelaString) {


        db = banco.getWritableDatabase();
        long resultado;
        String avaliador = CriaBanco.MENSAGEM + " = ? AND " + CriaBanco.LOCAL_MENSAGEM + " = ? ";
        String[] avaliadorArgs = {msgAntigaString, tabelaAntigaString};
        resultado = db.delete(CriaBanco.TABELA_MENSAGENS, avaliador, avaliadorArgs);
        db.close();

        if (resultado == -1) {
            Log.d("Inseriu", "Erro ao deletar avaliador");
        } else {
            Log.d("Inseriu", "Deletou avaliador");
        }

        ContentValues valores;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriaBanco.MENSAGEM, msgString);
        valores.put(CriaBanco.LOCAL_MENSAGEM, tabelaString);

        resultado = db.insert(CriaBanco.TABELA_MENSAGENS, null, valores);
        db.close();

        if (resultado == -1) {
            Log.d("atualizou", "Erro ao atualizar mensagem");
            return "Erro ao atualizar registro";
        } else {
            Log.d("atualizou", "Atualizou mensagem");
            return " Registro atualizado com sucesso";
        }
    }

    public Cursor pegaMensagemEspecifica(String tabela) {

        Cursor cursor;
        String[] campos = {CriaBanco.ID_MENSAGENS, CriaBanco.LOCAL_MENSAGEM, CriaBanco.MENSAGEM};
        String msg = CriaBanco.LOCAL_MENSAGEM + " = ? ";
        String[] Args = {tabela};
        db = banco.getReadableDatabase();
        cursor = db.query(CriaBanco.TABELA_MENSAGENS, campos, msg, Args, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();

        return cursor;
    }


    public String updateMedidor(String numeroGeralAntigo, String numeroGeralString, String modeloString,
                                String fabricanteString, String tensaoNominalString, Double correnteNominalString, String tipoMedidorString, String kdKeString, String rrString,
                                String numElementosString, String anoFabricacaoString, String classeString, String fiosString, String portariaInmetroString) {

        db = banco.getWritableDatabase();
        long resultado;
        String avaliador = CriaBanco.NUM_GERAL + " = ? ";
        String[] avaliadorArgs = {numeroGeralAntigo};
        resultado = db.delete(CriaBanco.TABELA_MEDIDOR, avaliador, avaliadorArgs);
        db.close();

        if ((resultado == -1) || (resultado == 0)) {
            Log.d("Deletou", "Erro ao deletar medidor");
        } else {
            Log.d("Deletou", "Deletou medidor");
        }

        ContentValues valores;
        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(CriaBanco.NUM_GERAL, numeroGeralString);
        valores.put(CriaBanco.FABRICANTE, fabricanteString);
        valores.put(CriaBanco.NUM_ELEMENTOS, numElementosString);
        valores.put(CriaBanco.MODELO, modeloString);
        valores.put(CriaBanco.CORRENTE_NOMINAL, correnteNominalString);
        valores.put(CriaBanco.CLASSE, classeString);
        valores.put(CriaBanco.RR, rrString);
        // valores.put(CriaBanco.ANO_FABRICACAO, anoFabricacaoString);
        valores.put(CriaBanco.TENSAO_NOMINAL, tensaoNominalString);
        valores.put(CriaBanco.KDKE, kdKeString);
        valores.put(CriaBanco.PORT_INMETRO, portariaInmetroString);
        valores.put(CriaBanco.FIOS, fiosString);
        valores.put(CriaBanco.TIPO_MEDIDOR, tipoMedidorString);

        resultado = db.insert(CriaBanco.TABELA_MEDIDOR, null, valores);
        db.close();

        if (resultado == -1) {
            Log.d("Inseriu", "Erro ao inserir medidor");
            return "Erro ao inserir registro";
        } else {
            Log.d("Inseriu", "Inseriu medidor");
            return " Registro Inserido com sucesso";
        }
    }
}

