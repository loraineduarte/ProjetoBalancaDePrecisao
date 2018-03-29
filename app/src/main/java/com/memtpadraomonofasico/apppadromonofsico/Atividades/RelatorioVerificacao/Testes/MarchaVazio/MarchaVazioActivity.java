package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.MarchaVazio;

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
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.CircuitoPotencial.CircuitoPotencialActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

import java.sql.Time;

public class MarchaVazioActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static RadioButton aprovado;
    @SuppressLint("StaticFieldLeak")
    private static RadioButton naoRealizado;
    @SuppressLint("StaticFieldLeak")
    private static RadioButton reprovado;
    private String statusMarchaVazio;
    @SuppressLint("StaticFieldLeak")
    private static EditText tempoReprovado;
    private String tempoReprovadoMarchaVazio;
    private static String macAddress = "";
    BluetoothAdapter mBluetoothAdapter;

    private AlertDialog dialogMarchaVazio;

    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 4;

    @SuppressLint("StaticFieldLeak")
    public static TextView textMessage;
    ThreadConexao conexao;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcha_vazio);



        textMessage = findViewById(R.id.textView6);
        aprovado = findViewById(R.id.aprovado);
        naoRealizado = findViewById(R.id.naoRealizado);
        reprovado = findViewById(R.id.Reprovado);
        tempoReprovado = findViewById(R.id.TempoMarchaVazio);
        tempoReprovado.setEnabled(false);


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


        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase5);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Hawk.delete("statusMarchaVazio");
                Hawk.delete("tempoReprovadoMarchaVazio");


                if (aprovado.isChecked()) {
                    statusMarchaVazio = "Aprovado";
                    tempoReprovadoMarchaVazio = Time.valueOf("00:00:00").toString();

                } else if (naoRealizado.isChecked()) {
                    statusMarchaVazio = "Não Realizado";
                    tempoReprovadoMarchaVazio = Time.valueOf("00:00:00").toString();

                } else if (reprovado.isChecked()) {
                    statusMarchaVazio = "Reprovado";
                    tempoReprovado = (findViewById(R.id.TempoMarchaVazio));
                    tempoReprovadoMarchaVazio = " - Tempo: " + tempoReprovado.getText().toString();
                }

//                if ((!aprovado.isChecked()) && (!naoRealizado.isChecked()) && (!reprovado.isChecked())) {
//                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Não existe opção de status marcado. ", Toast.LENGTH_LONG).show();
//                }
//                if ((reprovado.isChecked())&& (tempoReprovadoMarchaVazio.equals("00:00:00"))) {
//                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Colocar o tempo de reprovação do teste ", Toast.LENGTH_LONG).show();
//
//                }else {
                Hawk.put("statusMarchaVazio", statusMarchaVazio);
                Hawk.put("tempoReprovadoMarchaVazio", tempoReprovadoMarchaVazio);

                mBluetoothAdapter.disable();
                if(conexao!=null){
                    conexao.interrupt();
                }

                abrirCircuitoPotencial();
//                }
            }
        });

    }

    private void abrirCircuitoPotencial() {
        Intent intent = new Intent(this, CircuitoPotencialActivity.class);
        startActivity(intent);
    }

    public void executarTeste(View view) {



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
        pacote[1] = ('M' & 0xFF);
        pacote[2] = (byte) (bytes[0] & 0xFF);
        pacote[3] = (byte) (bytes[1] & 0xFF);
        pacote[4] = (byte) (bytes[2] & 0xFF);
        pacote[5] = (byte) (bytes[3] & 0xFF);
        pacote[6] = (byte) (0 & 0xFF);
        pacote[7] = (byte) (0 & 0xFF);
        pacote[8] = (byte) (0 & 0xFF);
        pacote[9] = (byte) (60 & 0xFF);

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

    public static void selecionarStatus(final double a) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if ((a == 1) || (a == 0)) {
                    aprovado.setChecked(true);
                    reprovado.setEnabled(false);
                    naoRealizado.setEnabled(false);
                    tempoReprovado.setEnabled(false);

                }
                else if (a > 1) {
                    reprovado.setChecked(true);
                    tempoReprovado.setText("60 s");
                    aprovado.setEnabled(false);
                    naoRealizado.setEnabled(false);

                } else {
                    naoRealizado.setChecked(true);
                    aprovado.setEnabled(false);
                    reprovado.setEnabled(false);
                    tempoReprovado.setEnabled(false);


                }
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
                    dialogMarchaVazio.cancel();
                }
                conexao.start();
            } else {
                textMessage.setText("Nenhum dispositivo selecionado.");
            }
        }
    }

    public void onCheckboxClicked(View view) {

        tempoReprovado = (findViewById(R.id.TempoMarchaVazio));
        aprovado = findViewById(R.id.tampasolidarizada);
        naoRealizado = findViewById(R.id.sinaisCarbonizacao);
        reprovado = findViewById(R.id.Reprovado);

        switch (view.getId()) {
            case R.id.tampasolidarizada:
                naoRealizado.setChecked(false);
                reprovado.setChecked(false);
                tempoReprovado.setEnabled(false);
                break;

            case R.id.sinaisCarbonizacao:
                aprovado.setChecked(false);
                reprovado.setChecked(false);
                tempoReprovado.setEnabled(false);
                break;

            case R.id.Reprovado:
                aprovado.setChecked(false);
                naoRealizado.setChecked(false);
                tempoReprovado.setEnabled(true);
                break;

        }
    }


}
