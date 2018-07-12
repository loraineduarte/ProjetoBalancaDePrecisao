package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResultadosFinaisActivity extends AppCompatActivity {

    private final CriaBanco banco = new CriaBanco(this);
    private final BancoController crud = new BancoController(this);
    private EditText leituraRetirada;
    private EditText leituraCalibracao;
    private EditText leituraPosCalibracao;
    private String dataFormatada;
    private String horaFinalFormatada;
    private String horaInicialFormatada;
    private AdapterListagemFinal adapter;
    private List<String> mensagens;
    private Cursor cursorMensagem;
    private String informacoesComplementares ="";


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_finais);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();


        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy" , Locale.getDefault());
        Date data = new Date();
        dataFormatada = formataData.format(data);

        Date hora = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss" , Locale.getDefault());
        horaFinalFormatada = sdf.format(hora);
        horaInicialFormatada = Hawk.get("HoraInicial");

        EditText dataInicial = findViewById(R.id.DataInicial);
        dataInicial.setText(dataFormatada);
        EditText dataFinal = findViewById(R.id.DataFinal);
        dataFinal.setText(dataFormatada);

        EditText horaInicial = findViewById(R.id.HoraInicio);
        horaInicial.setText(horaInicialFormatada);
        EditText horaFinal = findViewById(R.id.HoraFim);
        horaFinal.setText(horaFinalFormatada);

        final EditText numInvolucro = findViewById(R.id.numInvolucro);


        ListView listaDeMensagens = findViewById(R.id.lista);
        mensagens = todasMensagens();
        adapter = new AdapterListagemFinal(mensagens, this);
        listaDeMensagens.setAdapter(adapter);

        @SuppressLint("WrongViewCast") Button next =  findViewById(R.id.NextFase9);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Hawk.delete("DataInicial");
                Hawk.delete("DataFinal");
                Hawk.delete("HoraInicial");
                Hawk.delete("HoraFinal");
                Hawk.delete("InformacoesComplementares");
                Hawk.put("InformacoesComplementares", informacoesComplementares);

                if (!(numInvolucro.getText().toString().equals(""))) {
                    Hawk.delete("NumeroInvolucro");
                    Hawk.put("NumeroInvolucro", String.valueOf(numInvolucro.getText()));
                }

                Hawk.put("DataInicial", dataFormatada);
                Hawk.put("DataFinal", dataFormatada);
                Hawk.put("HoraInicial", horaInicialFormatada);
                Hawk.put("HoraFinal", horaFinalFormatada);

                abrirConclusao();

            }
        });
    }

    private void abrirConclusao() {
        Intent intent = new Intent(this, ConclusaoActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        reloadAllData();

    }


    private List<String> todasMensagens() {
        List<String> av = new ArrayList<>();
        Cursor cursor = crud.pegaMensagemEspecifica("Informações Complementares");
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String corpoMensagem = cursor.getString(2);
            av.add(corpoMensagem);
        }
        return av;
    }


    private void reloadAllData() {

        mensagens = todasMensagens();
        adapter.updateItens(mensagens);
    }

    public void selecionarMensagem(View view) {
        int position = (int) view.getTag();
        String mensagem = mensagens.get(position);
        Log.d("MSG", mensagem);
        if (informacoesComplementares.contains(mensagem)) {
            informacoesComplementares.replace(mensagem, "");
        } else {
            informacoesComplementares = informacoesComplementares + "\n" + mensagem;
        }


    }


}
