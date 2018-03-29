package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoConformidade;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.Bluetooth.PairedDevices;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.Bluetooth.ThreadConexao;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.SituacoesObservadasActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class InspecaoConformidadeActivity extends AppCompatActivity {

    private RadioButton Aprovado, NaoPossibilitaTeste, VariacaoLeitura, Reprovado;
    private String statusConformidade;
    private static EditText cargaNominalErro;
    private static EditText cargaPequenaErro;
    private static String macAddress = "";
    BluetoothAdapter mBluetoothAdapter;
    private AlertDialog dialogConformidade;

    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 4;

    @SuppressLint("StaticFieldLeak")
    public static TextView textMessage;
    ThreadConexao conexao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspecao_conformidade);



        textMessage = findViewById(R.id.textView6);
        cargaNominalErro =  findViewById(R.id.CargaNominalErro);
        cargaPequenaErro = findViewById(R.id.CargaPequenaErro);
        Aprovado = findViewById(R.id.tampasolidarizada);
        NaoPossibilitaTeste = findViewById(R.id.sinaisCarbonizacao);
        VariacaoLeitura = findViewById(R.id.VariacaoLeitura);
        Reprovado = findViewById(R.id.Reprovado);

        // verificando ativação do bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            textMessage.setText("Bluetooth não está funcionando.");
        } else {
            textMessage.setText("Bluetooth está funcionando.");
            if (!mBluetoothAdapter.isEnabled()) {
                Log.d("Bluetooth", "ATIVANDO BLUETOOTH");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                textMessage.setText("Solicitando ativação do Bluetooth...");
            } else {
                textMessage.setText("Bluetooth Ativado.");
            }



        }
        // Fim - verificando ativação do bluetooth

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase7);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Hawk.delete("CargaNominalErroConformidade");
                Hawk.delete("CargaPequenaErroConformidade");
                Hawk.delete("statusConformidade");



                if(Aprovado.isChecked()){
                    statusConformidade = "Aprovado";

                } if (NaoPossibilitaTeste.isChecked()){
                    statusConformidade = "Não Possibilita Teste";

                }if (VariacaoLeitura.isChecked()){
                    statusConformidade = "Variação de Leitura";

                } if (Reprovado.isChecked()){
                    statusConformidade = "Reprovado";
                }

//                if ((!Aprovado.isChecked()) && (!NaoPossibilitaTeste.isChecked()) && (!VariacaoLeitura.isChecked()) && (!Reprovado.isChecked()))
//                {
//                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Não existe opção de status marcado. ", Toast.LENGTH_LONG).show();
//
//                } else{
                    Hawk.put("CargaNominalErroConformidade",String.valueOf(cargaNominalErro.getText()));
                    Hawk.put("CargaPequenaErroConformidade",String.valueOf(cargaPequenaErro.getText()));
                    Hawk.put("statusConformidade",statusConformidade);

                mBluetoothAdapter.disable();
                if(conexao!=null){
                    conexao.interrupt();
                }

                    abrirSituacoesObservadas();
//                }
            }
        });
    }

    public void aplicarCargaNominal(View view) {

        if (conexao.isAlive()) {
            textMessage.setText(".. Conectado ..");
        }

        byte[] pacote = new byte[10];

        //pegando valores do medidor

        float kdMedidor = Float.parseFloat((String) Hawk.get("KdKeMedidor"));

        byte[] bytes = new byte[4];
        int valorMultiplicado = (int) (kdMedidor * 1000000);

        bytes[0] = (byte) (valorMultiplicado / (Math.pow(256, 3)));
        bytes[1] = (byte) ((valorMultiplicado - (bytes[0] * (Math.pow(256, 3)))) / Math.pow(256, 2));
        bytes[2] = (byte) ((valorMultiplicado - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))))) / Math.pow(256, 1));
        bytes[3] = (byte) ((valorMultiplicado - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))) + (bytes[2] * Math.pow(256, 1)))));

        pacote[0] = ('I' & 0xFF);
        pacote[1] = ('N' & 0xFF);
        pacote[2] = (byte) (bytes[0] & 0xFF);
        pacote[3] = (byte) (bytes[1] & 0xFF);
        pacote[4] = (byte) (bytes[2] & 0xFF);
        pacote[5] = (byte) (bytes[3] & 0xFF);
        pacote[6] = (byte) (0 & 0xFF);
        pacote[7] = (byte) (5 & 0xFF);
        pacote[8] = (byte) (0 & 0xFF);
        pacote[9] = (byte) (0 & 0xFF);

        conexao.write(pacote);
    }

    public void aplicarCargaPequena(View view) {

        if (conexao.isAlive()) {
            textMessage.setText(".. Conectado ..");
        }

        byte[] pacote = new byte[10];

        //pegando valores do medidor

        float kdMedidor = Float.parseFloat((String) Hawk.get("KdKeMedidor"));

        byte[] bytes = new byte[4];
        int valorMultiplicado = (int) (kdMedidor * 1000000);

        bytes[0] = (byte) (valorMultiplicado / (Math.pow(256, 3)));
        bytes[1] = (byte) ((valorMultiplicado - (bytes[0] * (Math.pow(256, 3)))) / Math.pow(256, 2));
        bytes[2] = (byte) ((valorMultiplicado - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))))) / Math.pow(256, 1));
        bytes[3] = (byte) ((valorMultiplicado - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))) + (bytes[2] * Math.pow(256, 1)))));

        pacote[0] = ('I' & 0xFF);
        pacote[1] = ('B' & 0xFF);
        pacote[2] = (byte) (bytes[0] & 0xFF);
        pacote[3] = (byte) (bytes[1] & 0xFF);
        pacote[4] = (byte) (bytes[2] & 0xFF);
        pacote[5] = (byte) (bytes[3] & 0xFF);
        pacote[6] = (byte) (0 & 0xFF);
        pacote[7] = (byte) (5 & 0xFF);
        pacote[8] = (byte) (0 & 0xFF);
        pacote[9] = (byte) (0 & 0xFF);

        conexao.write(pacote);
    }

    @SuppressLint("HandlerLeak")
    public static final Handler handler = new Handler() {
    };

    public static void escreverTela(final String res) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                textMessage.clearComposingText();
                textMessage.setText(res);
            }
        });

    }

    public static void escreverTelaCargaNominal(final String res) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                cargaNominalErro.clearComposingText();
                cargaNominalErro.setText(res);
            }
        });

    }

    public static void escreverTelaCargaPequena(final String res) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                cargaPequenaErro.clearComposingText();
                cargaPequenaErro.setText(res);
            }
        });

    }

    public void conectarDispositivo(View view) {
        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                textMessage.setText("Bluetooth ativado.");
            } else {
                textMessage.setText("Bluetooth não ativado.");
            }
        } else if (requestCode == SELECT_PAIRED_DEVICE) {
            if (resultCode == RESULT_OK) {
                textMessage.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n" + data.getStringExtra("btDevAddress"));
                macAddress = data.getStringExtra("btDevAddress");

                conexao = new ThreadConexao(macAddress);

                if (conexao.isAlive()) {
                    dialogConformidade.cancel();
                }
                conexao.start();
            } else {
                textMessage.setText("Nenhum dispositivo selecionado.");
            }
        }
    }

    private void abrirSituacoesObservadas() {
        Intent intent = new Intent(this, SituacoesObservadasActivity.class);
        startActivity(intent);
    }


    public void onCheckboxClicked(View view) {

        Aprovado = findViewById(R.id.tampasolidarizada);
        NaoPossibilitaTeste = findViewById(R.id.sinaisCarbonizacao);
        VariacaoLeitura = findViewById(R.id.VariacaoLeitura);
        Reprovado = findViewById(R.id.Reprovado);

        switch (view.getId()) {
            case R.id.tampasolidarizada:
                NaoPossibilitaTeste.setChecked(false);
                VariacaoLeitura.setChecked(false);
                Reprovado.setChecked(false);
                break;

            case R.id.sinaisCarbonizacao:
                Aprovado.setChecked(false);
                VariacaoLeitura.setChecked(false);
                Reprovado.setChecked(false);
                break;

            case R.id.VariacaoLeitura:
                Aprovado.setChecked(false);
                NaoPossibilitaTeste.setChecked(false);
                Reprovado.setChecked(false);
                break;

            case R.id.Reprovado:
                Aprovado.setChecked(false);
                NaoPossibilitaTeste.setChecked(false);
                VariacaoLeitura.setChecked(false);
                break;

        }
    }

}

