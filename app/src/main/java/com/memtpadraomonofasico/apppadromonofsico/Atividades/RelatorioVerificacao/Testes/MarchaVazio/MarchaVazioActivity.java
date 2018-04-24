package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.MarchaVazio;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.Bluetooth.PairedDevices;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.Bluetooth.ThreadConexaoMarchaVazio;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoConformidade.InspecaoConformidadeActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.sql.Time;

public class MarchaVazioActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static RadioButton aprovado, naoRealizado, reprovado;
    private String statusMarchaVazio, tempoReprovadoMarchaVazio;

    @SuppressLint("StaticFieldLeak")
    private static EditText tempoReprovado;
    private static final int ENABLE_BLUETOOTH = 1;
    private static final int SELECT_PAIRED_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 4;

    @SuppressLint("StaticFieldLeak")
    private static TextView textMessage;
    private ThreadConexaoMarchaVazio conexao;

    @SuppressLint("WrongViewCast")
    Button conectar;
    BluetoothAdapter mBluetoothAdapter = null;
    private static Runnable handlerTask;

    @SuppressLint("HandlerLeak")
    private static final Handler handler = new Handler() {};
    boolean testeComecou = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcha_vazio);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();

        textMessage = findViewById(R.id.textView5);
        textMessage.setText("");
        aprovado = findViewById(R.id.aprovado);
        aprovado.setEnabled(false);
        naoRealizado = findViewById(R.id.naoRealizado);
        reprovado = findViewById(R.id.Reprovado);
        reprovado.setEnabled(false);
        tempoReprovado = findViewById(R.id.TempoMarchaVazio);
        tempoReprovado.setEnabled(false);
        conectar = findViewById(R.id.buttonConectarDispositivo);

        ativarBluetooth();

        conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (mBluetoothAdapter == null) {
                        ativarBluetooth();
                        new Thread().sleep(4500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                conectarDispositivo(view);
            }
        });


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

                if ((!aprovado.isChecked()) && (!naoRealizado.isChecked()) && (!reprovado.isChecked())) {
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Não existe opção de status marcado. ", Toast.LENGTH_LONG).show();
                }
                if ((reprovado.isChecked()) && (tempoReprovadoMarchaVazio.equals("00:00:00"))) {
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Colocar o tempo de reprovação do teste ", Toast.LENGTH_LONG).show();

                }
                if ((naoRealizado.isChecked())) {
                    Hawk.put("statusMarchaVazio", statusMarchaVazio);
                    Hawk.put("tempoReprovadoMarchaVazio", tempoReprovadoMarchaVazio);

                    if (conexao != null) {
                        conexao.interrupt();
                    }
                    mBluetoothAdapter.disable();
                    abrirInspecaoConformidade();

                } else {
                    Hawk.put("statusMarchaVazio", statusMarchaVazio);
                    Hawk.put("tempoReprovadoMarchaVazio", tempoReprovadoMarchaVazio);

                    if (conexao != null) {
                        conexao.interrupt();
                    }
                    mBluetoothAdapter.disable();
                    abrirInspecaoConformidade();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    private void ativarBluetooth() {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            textMessage.setText("Bluetooth não está funcionando.");

        } else {
            textMessage.setText("Bluetooth está funcionando.");
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                textMessage.setText("Solicitando ativação do Bluetooth...");

            } else {
                textMessage.setText("Bluetooth Ativado.");
            }
        }
    }


    private void abrirInspecaoConformidade() {

        Intent intent = new Intent(this, InspecaoConformidadeActivity.class);
        startActivity(intent);
    }

    public void mudarEstadoTeste(View view) {

        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser iniciado/parado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (testeComecou == false) {
                testeComecou = true;
                executarTeste(view);
                textMessage.clearComposingText();
                textMessage.setText("Teste Iniciado!");

            } else {
                testeComecou = false;
                pararTeste(view);
                textMessage.clearComposingText();
                textMessage.setText("Teste Cancelado!");
            }
        }
    }

    public void executarTeste(View view) {


        byte[] pacote = new byte[10];
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

    public void pararTeste(View view) {

        byte[] pacote = new byte[10];

        pacote[0] = ('C' & 0xFF);
        pacote[1] = (byte) (0 & 0xFF);
        pacote[2] = (byte) (0 & 0xFF);
        pacote[3] = (byte) (0 & 0xFF);
        pacote[4] = (byte) (0 & 0xFF);
        pacote[5] = (byte) (0 & 0xFF);
        pacote[6] = (byte) (0 & 0xFF);
        pacote[7] = (byte) (0 & 0xFF);
        pacote[8] = (byte) (0 & 0xFF);
        pacote[9] = (byte) (0 & 0xFF);

        conexao.write(pacote);

    }


    public static void escreverTelaMarchaVazio(final String res) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!(textMessage == null)) {
                    if (res.startsWith("T")) {
                        textMessage.clearComposingText();
                        textMessage.setText("Teste Concluído!");
                        textMessage.setText(res);


                    } else {
                        textMessage.clearComposingText();
                        textMessage.setText(res);
                    }

                }
            }
        });
    }

    public static void selecionarStatus(final double a) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        aprovado.setEnabled(true);
                        reprovado.setEnabled(true);
                        naoRealizado.setEnabled(true);

                        if ((a == 1) || (a == 0)) {
                            aprovado.setChecked(true);
                            reprovado.setEnabled(false);
                            naoRealizado.setEnabled(false);
                            tempoReprovado.setEnabled(false);

                        } else if (a > 1) {
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
        }).start();


    }

    public void conectarDispositivo(View view) {

        if (conexao != null) {
            Toast.makeText(getApplicationContext(), "Dispositivo já conectado.", Toast.LENGTH_LONG).show();
        }

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
                String macAddress = data.getStringExtra("btDevAddress");

                conexao = new ThreadConexaoMarchaVazio(macAddress);
                conexao.start();

                if (conexao != null) {
                    textMessage.setText("Conexao sendo finalizada com: " + data.getStringExtra("btDevName") + "\n" + data.getStringExtra("btDevAddress"));
                }

            } else {
                textMessage.setText("Nenhum dispositivo selecionado.");
            }
        }
    }
    
}
