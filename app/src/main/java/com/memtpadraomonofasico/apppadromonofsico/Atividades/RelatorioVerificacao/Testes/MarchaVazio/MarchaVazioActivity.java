package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.MarchaVazio;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Exatidao.ExatidaoActivity;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.ConexaoMarchaVazio.ConexaoBLE.DeviceControlActivity;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.ConexaoMarchaVazio.ConexaoBLE.DeviceScanActivity;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.ConexaoMarchaVazio.ConexãoBluetooth.ThreadConexaoPadraoBrasileiro;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.sql.Time;
import java.util.UUID;

@SuppressWarnings("ALL")
@TargetApi(21)
public class MarchaVazioActivity extends AppCompatActivity {

    private final static String TAG = MarchaVazioActivity.class.getSimpleName();
    static final private UUID CCCD_ID = UUID.fromString("000002902-0000-1000-8000-00805f9b34fb");
    private static final String myUUID = "00001800-0000-1000-8000-00805f9b34fb";

    private static final int ENABLE_BLUETOOTH = 1;
    private static final int SELECT_PAIRED_DEVICE = 2;
    private static final int SELECT_BLE_DEVICE = 3;
    private static final int REQUEST_ENABLE_BT = 4;

    @SuppressLint("HandlerLeak")
    private static final Handler handler = new Handler() {
    };
    private static final long SCAN_PERIOD = 10000;  // Stops scanning after 10 seconds.
    @SuppressLint("StaticFieldLeak")
    private static RadioButton aprovado, naoRealizado, reprovado;
    @SuppressLint("StaticFieldLeak")
    private static EditText tempoReprovado;
    @SuppressLint("StaticFieldLeak")
    private static TextView textMessage;
    private static Runnable handlerTask;
    long tempoInicio;
    String macAddress = null;
    private BluetoothAdapter mBluetoothAdapter;
    private String statusMarchaVazio, tempoReprovadoMarchaVazio;
    private ThreadConexaoPadraoBrasileiro conexaoBrasileiro;
    @SuppressLint("WrongViewCast")
    private Button conectar, teste;
    private boolean testeComecou = false;
    private boolean testeFCComecou = false;


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
        conectar = findViewById(R.id.buttonConectarDispositivo);
        teste = findViewById(R.id.buttonAplicarTensao);

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

                conectarDispositivo();
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
                } else {
                    if ((reprovado.isChecked()) && (tempoReprovadoMarchaVazio.equals("00:00:00"))) {
                        Toast.makeText(getApplicationContext(), "Sessão incompleta - Colocar o tempo de reprovação do teste ", Toast.LENGTH_LONG).show();

                    } else {
                        if ((naoRealizado.isChecked())) {
                            Hawk.put("statusMarchaVazio", statusMarchaVazio);
                            Hawk.put("tempoReprovadoMarchaVazio", tempoReprovadoMarchaVazio);

                            if (conexaoBrasileiro != null) {
                                conexaoBrasileiro.interrupt();
                            }
                            mBluetoothAdapter.disable();
                            conexaoBrasileiro = null;
                            abrirInspecaoConformidade();

                        } else {
                            Hawk.put("statusMarchaVazio", statusMarchaVazio);
                            Hawk.put("tempoReprovadoMarchaVazio", tempoReprovadoMarchaVazio);

                            if (conexaoBrasileiro != null) {
                                conexaoBrasileiro.interrupt();
                            }
                            mBluetoothAdapter.disable();
                            abrirInspecaoConformidade();
                        }
                    }
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                Log.d("MAC ACRESS", macAddress);

                conexaoBrasileiro = new ThreadConexaoPadraoBrasileiro(macAddress);
                conexaoBrasileiro.start();

                if (conexaoBrasileiro != null) {
                    textMessage.setText("Conexao finalizada com: " + data.getStringExtra("btDevName") + "\n Verifique o LED de conexão");
                }
            } else {
                textMessage.setText("Nenhum dispositivo selecionado.");
            }

        } else if (requestCode == SELECT_BLE_DEVICE) {

            if (resultCode == RESULT_OK) {
                textMessage.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n" + data.getStringExtra("btDevAddress"));
                macAddress = data.getStringExtra("btDevAddress");
                String name = data.getStringExtra("btDevName");
                Log.d("MAC ACRESS", macAddress);
                Log.d("NAME", name);

                final Intent intentConection = new Intent(this, DeviceControlActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(DeviceControlActivity.EXTRAS_DEVICE_NAME, name);
                bundle.putString(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, macAddress);
                intentConection.putExtras(bundle);
                startActivity(intentConection);

            } else {
                textMessage.setText("Nenhum dispositivo selecionado.");
            }
        }
    }

    private void abrirInspecaoConformidade() {
        Intent intent = new Intent(this, ExatidaoActivity.class);
        startActivity(intent);
    }

    //Funções Bluetooth
    private void ativarBluetooth() {

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

    private void conectarDispositivo() {

        //TODO conferir se é padrão brasileiro ou chines - integrção

//        if (conexaoBrasileiro != null) {
//            Toast.makeText(getApplicationContext(), "Dispositivo já conectado.", Toast.LENGTH_LONG).show();
//        }
//
//        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
//        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);

        Intent searchPairedDevicesIntent = new Intent(this, DeviceScanActivity.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_BLE_DEVICE);

    }


    //Funções Gerais da Atividade
    public void TesteMarchaEmVazio(View view) {

        if (conexaoBrasileiro == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser iniciado/parado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {
            teste = findViewById(R.id.buttonAplicarTensao);

            if (!testeComecou) {
                testeComecou = true;
                teste.clearComposingText();
                teste.setText("Cancelar Teste");
                //TODO conferir se é padrão brasileiro ou chines - if
                marchaVazioPadrãoBrasileiro();
                textMessage.clearComposingText();
                textMessage.setText("Teste Iniciado!");

            } else {
                testeComecou = false;
                teste.clearComposingText();
                teste.setText("Iniciar Teste");
                //TODO conferir se é padrão brasileiro ou chines - if
                pararTestesPadrãoBrasileiro();
                textMessage.clearComposingText();
                textMessage.setText("Teste Cancelado!");
            }
        }
    }

    public void TesteFotoCelula(View view) {
        if (conexaoBrasileiro == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser iniciado/parado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            teste = findViewById(R.id.buttonTesteFotoCelula);

            if (!testeFCComecou) {
                testeFCComecou = true;
                teste.clearComposingText();
                teste.setText("Cancelar Teste de FotoCélula");
                //TODO conferir se é padrão brasileiro ou chines - if
                fotoCelulaPadraoBrasileiro();
                textMessage.clearComposingText();
                textMessage.setText("Teste de FotoCélula Iniciado!");

            } else {
                testeFCComecou = false;
                teste.clearComposingText();
                teste.setText("Iniciar Teste de FotoCélula");
                //TODO conferir se é padrão brasileiro ou chines - if
                pararTestesPadrãoBrasileiro();
                textMessage.clearComposingText();
                textMessage.setText("Teste de FotoCélula Cancelado!");
            }
        }
    }

    public void escreverTela(final String res) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!(textMessage == null)) {
                    if (res.startsWith("T")) {
                        textMessage.clearComposingText();
                        textMessage.setText("Teste Concluído!");
                        testeComecou = false;
                        teste.clearComposingText();
                        teste.setText("Iniciar Teste");

                    } else {
                        textMessage.clearComposingText();
                        textMessage.setText(res);
                    }
                }
            }
        });
    }

    public void selecionarStatus(final double a) {

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

                        } else if (a > 1) {
                            reprovado.setChecked(true);

                        } else {
                            naoRealizado.setChecked(true);
                        }
                    }
                });
            }
        }).start();
    }


    //Conferir versões dos padrões
    private void confereNumSeriePadraoChines() {

        byte[] pacote = new byte[12];

        //Header - AA -------------- OK
        pacote[0] = (byte) 0xAA;
        Log.d("HEADER", String.valueOf(pacote[0]));

        //Length - 0007 (2 bytes)(passar para hexadecimal) -------OK
        int lenght1 = 0x00;
        int lenght2 = 0x07;
        Log.d("Length", String.valueOf(lenght1));
        Log.d("Length", String.valueOf(lenght2));
        pacote[2] = (byte) (lenght1);
        pacote[3] = (byte) (lenght2);

        //Header Check - XX (passar para hexadecimal)(fazer XOR de frames anteriores) ---TESTAR
        pacote[4] = (byte) (pacote[0] ^ pacote[1] ^ pacote[2]);
        Log.d("Header Check", String.valueOf(pacote[4]));

        //Category ID - 0010 (2 bytes)(passar para hexadecimal) ---TESTAR
        int category1 = 0x0010;

        byte[] bytes1 = new byte[2];
        bytes1[0] = (byte) (category1 / (Math.pow(256, 1)));
        bytes1[1] = (byte) (category1 - (bytes1[0] * (Math.pow(256, 1))));
        Log.d("Category ID", String.valueOf(bytes1[0]));
        Log.d("Category ID", String.valueOf(bytes1[1]));

        pacote[5] = (byte) bytes1[0];
        pacote[6] = (byte) bytes1[1];

        //Command ID - 00 (1 byte)------------OK
        pacote[7] = (byte) (0x00);
        Log.d("Command ID", String.valueOf(pacote[7]));

        //Function Adress - 00 (1 byte) (Sempre 0)-----------OK
        pacote[8] = (byte) (0x00);
        Log.d("Function Adress", String.valueOf(pacote[8]));

        //Data - 00 ------------------------OK
        pacote[9] = (byte) (0x00);
        Log.d("Data", String.valueOf(pacote[9]));

        //Pack Check - XX (fazer XOR de todos os pacotes anteriores) ---TESTAR
        pacote[10] = (byte) (pacote[0] ^ pacote[1] ^ pacote[2] ^ pacote[3] ^ pacote[4] ^ pacote[5] ^ pacote[6] ^ pacote[7] ^ pacote[8] ^ pacote[9]);
        Log.d("Pack Check ", String.valueOf(pacote[10]));

        //End - 55 ------------------------ OK
        pacote[11] = (byte) (0x55);
        Log.d("End", String.valueOf(pacote[11]));


        Log.d("PACOTE", String.valueOf(pacote));

        //  // BluetoothGattService service = mGatt.getService(UUID.fromString(myUUID));//serviços vem vazio
        //   findCharacteristic(service);

        //CARACTERISTIC: []
        //CARACTERISTIC: null

        // Log.d("CARACTERISTIC", String.valueOf(characteristic));

//        characteristic.setValue(pacote);
//        Log.d("CARACTERISTIC", String.valueOf(characteristic.getValue()));
//        boolean successfullyWritten = mGatt.writeCharacteristic(characteristic);
    }

    //Testes Padrao Brasileiro
    private void marchaVazioPadrãoBrasileiro() {

        //TODO Pegar o numero de série do medidor para ser comparado nos próximos testes
        tempoInicio = System.currentTimeMillis();
        byte[] pacote = new byte[10];
        float kdMedidor = Float.parseFloat((String) Hawk.get("KdKeMedidor"));

        byte[] bytes = new byte[8];
        int valorMultiplicado = (int) (kdMedidor * 1e6);

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

        String time = tempoReprovado.getText().toString();

        if (time.equals("")) {
            tempoReprovado.setText("1");
            Toast.makeText(getApplicationContext(), "Tempo de teste ajustado para 1 minuto... ", Toast.LENGTH_LONG).show();
            pacote[6] = (byte) (0 & 0xFF);
            pacote[7] = (byte) (0 & 0xFF);
            pacote[8] = (byte) (0 & 0xFF);
            pacote[9] = (byte) (60 & 0xFF);

        } else {
            float tempo = (Float.parseFloat(tempoReprovado.getText().toString())) * 60;
            bytes[4] = (byte) (tempo / (Math.pow(256, 3)));
            bytes[5] = (byte) ((tempo - (bytes[4] * (Math.pow(256, 3)))) / Math.pow(256, 2));
            bytes[6] = (byte) ((tempo - ((bytes[4] * (Math.pow(256, 3))) + (bytes[5] * (Math.pow(256, 2))))) / Math.pow(256, 1));
            bytes[7] = (byte) ((tempo - ((bytes[4] * (Math.pow(256, 3))) + (bytes[5] * (Math.pow(256, 2))) + (bytes[6] * Math.pow(256, 1)))));

            pacote[6] = (byte) (bytes[4] & 0xFF);
            pacote[7] = (byte) (bytes[5] & 0xFF);
            pacote[8] = (byte) (bytes[6] & 0xFF);
            pacote[9] = (byte) (bytes[7] & 0xFF);

        }

        conexaoBrasileiro.write(pacote);


    }

    public void fotoCelulaPadraoBrasileiro() {

        //TODO Pegar o numero de série do medidor para ser comparado nos próximos testes
        if (conexaoBrasileiro == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser inicializado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (conexaoBrasileiro != null) {
                textMessage.setText("O teste de FotoCélula vai ser iniciado...");
            }

            byte[] pacote = new byte[10];
            float kdMedidor = Float.parseFloat((String) Hawk.get("KdKeMedidor"));
            byte[] bytes = new byte[4];
            int valorMultiplicado = (int) (kdMedidor * 1000000);

            bytes[0] = (byte) (valorMultiplicado / (Math.pow(256, 3)));
            bytes[1] = (byte) ((valorMultiplicado - (bytes[0] * (Math.pow(256, 3)))) / Math.pow(256, 2));
            bytes[2] = (byte) ((valorMultiplicado - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))))) / Math.pow(256, 1));
            bytes[3] = (byte) ((valorMultiplicado - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))) + (bytes[2] * Math.pow(256, 1)))));

            pacote[0] = ('I' & 0xFF);
            pacote[1] = ('R' & 0xFF);
            pacote[2] = (byte) (bytes[0] & 0xFF);
            pacote[3] = (byte) (bytes[1] & 0xFF);
            pacote[4] = (byte) (bytes[2] & 0xFF);
            pacote[5] = (byte) (bytes[3] & 0xFF);
            pacote[6] = (byte) (0 & 0xFF);
            pacote[7] = (byte) (0 & 0xFF);
            pacote[8] = (byte) (3 & 0xFF);
            pacote[9] = (byte) (232 & 0xFF);

            conexaoBrasileiro.write(pacote);
        }
    }

    private void pararTestesPadrãoBrasileiro() {

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

        conexaoBrasileiro.write(pacote);


    }

    //Testes padrão chinês
    //TODO função para teste de marcha em vaxio

    //TODO função para teste de foto celula (registrador)

    //TODO função de cancelar testes




}





