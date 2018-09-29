package com.balancaDePrecisao.Banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by loraine.duarte on 23/09/2018.
 */
public class BancoController {
    private final DadoDAO banco;
    private SQLiteDatabase db;

    public BancoController(Context context) {
        banco = new DadoDAO(context);
    }
}
