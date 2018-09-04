package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.MarchaVazio;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Exatidao.ExatidaoActivity;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.ConexaoMarchaVazio.ConexaoBLE.BluetoothLeService;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.ConexaoMarchaVazio.ConexaoBLE.DeviceScanActivity;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.ConexaoMarchaVazio.ConexaoBLE.SampleGattAttributes;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.ConexaoMarchaVazio.ConexãoBluetooth.ThreadConexaoPadraoBrasileiro;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ALL")
@TargetApi(21)
public class MarchaVazioActivity extends AppCompatActivity {

    private final static String TAG = MarchaVazioActivity.class.getSimpleName();

    private static final int ENABLE_BLUETOOTH = 1;
    private static final int SELECT_PAIRED_DEVICE = 2;
    private static final int SELECT_BLE_DEVICE = 3;
    private static final int REQUEST_ENABLE_BT = 4;
    private static final int CONEXAO_BLE_FEITA = 5;
    @SuppressLint("HandlerLeak")
    private static final Handler handler = new Handler() {
    };
    private static final long SCAN_PERIOD = 10000;  // Stops scanning after 10 seconds.
    @SuppressLint("StaticFieldLeak")
    private static RadioButton aprovado, naoRealizado, reprovado;
    @SuppressLint("StaticFieldLeak")
    private static EditText tempoReprovado;
    @SuppressLint("StaticFieldLeak")
    private static TextView mensagemNaTela;
    private static Runnable handlerTask;
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    String macAddress = null;
    private BluetoothAdapter bluetoothAdapter;
    private String statusMarchaVazio, tempoReprovadoMarchaVazio;
    private ThreadConexaoPadraoBrasileiro conexaoBrasileiro;
    @SuppressLint("WrongViewCast")
    private Button conectar, testeMarchaVazio, testeFotoCelula;
    private boolean testeMarchaVazioRodando = false;
    private boolean testeFotoCelulaRodando = false;
    private ArrayList<BluetoothGattCharacteristic> servicos1 = new ArrayList<>();
    private ArrayList<BluetoothGattCharacteristic> servicos2 = new ArrayList<>();
    private ArrayList<BluetoothGattCharacteristic> servicos3 = new ArrayList<>();
    private BluetoothLeService conexaoBLEPadraoChines = new BluetoothLeService();
    private final ServiceConnection mServiceConnection = new ServiceConnection() {   // Code to manage Service lifecycle.

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            conexaoBLEPadraoChines = ((BluetoothLeService.LocalBinder) service).getService();
            if (conexaoBLEPadraoChines.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }

            conexaoBLEPadraoChines.connect(macAddress);  // Automatically connects to the device upon successful start-up initialization.
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            conexaoBLEPadraoChines = null;
        }
    };
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<>();
    // If a given GATT characteristic is selected, check for supported features.  This sample demonstrates 'Read' and 'Notify' features.
    private final ExpandableListView.OnChildClickListener servicesListClickListner = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

            if (mGattCharacteristics != null) {

                final BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(groupPosition).get(childPosition);
                final int charaProp = characteristic.getProperties();

                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                    // If there is an active notification on a characteristic, clear
                    // it first so it doesn't update the data field on the user interface.
                    if (mNotifyCharacteristic != null) {
                        conexaoBLEPadraoChines.setCharacteristicNotification(mNotifyCharacteristic, false);
                        mNotifyCharacteristic = null;
                    }
                    conexaoBLEPadraoChines.readCharacteristic(characteristic);
                }
                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {

                    mNotifyCharacteristic = characteristic;
                    conexaoBLEPadraoChines.setCharacteristicNotification(characteristic, true);
                }

                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {

                    mNotifyCharacteristic = characteristic;
                    conexaoBLEPadraoChines.setCharacteristicNotification(characteristic, true);
                }
                return true;
            }
            return false;
        }
    };
    private boolean mConnected = false;
    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read  or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);


            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                clearUI();

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(conexaoBLEPadraoChines.getSupportedGattServices());

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcha_vazio);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();

        mensagemNaTela = findViewById(R.id.textView5);
        mensagemNaTela.setText("");
        aprovado = findViewById(R.id.aprovado);
        aprovado.setEnabled(false);
        naoRealizado = findViewById(R.id.naoRealizado);
        reprovado = findViewById(R.id.Reprovado);
        reprovado.setEnabled(false);
        tempoReprovado = findViewById(R.id.TempoMarchaVazio);
        conectar = findViewById(R.id.buttonConectarDispositivo);
        testeMarchaVazio = findViewById(R.id.buttonAplicarTensao);
        testeFotoCelula = findViewById(R.id.buttonTesteFotoCelula);

        ativarBluetooth();

        conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (bluetoothAdapter == null) {
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
                            bluetoothAdapter.disable();
                            conexaoBrasileiro = null;
                            abrirInspecaoConformidade();

                        } else {
                            Hawk.put("statusMarchaVazio", statusMarchaVazio);
                            Hawk.put("tempoReprovadoMarchaVazio", tempoReprovadoMarchaVazio);

                            if (conexaoBrasileiro != null) {
                                conexaoBrasileiro.interrupt();
                            }
                            bluetoothAdapter.disable();
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
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (conexaoBLEPadraoChines != null) {
            final boolean result = conexaoBLEPadraoChines.connect(macAddress);
            Log.d(TAG, "Connect request result=" + result);
        }

        conexaoBLEPadraoChines.connect(macAddress);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        conexaoBLEPadraoChines = null;
    }

    //Funções bluetooth Padrao Chinês

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                mensagemNaTela.setText("Bluetooth ativado.");

            } else {
                mensagemNaTela.setText("Bluetooth não ativado.");
            }

        } else if (requestCode == SELECT_PAIRED_DEVICE) {

            if (resultCode == RESULT_OK) {
                mensagemNaTela.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n" + data.getStringExtra("btDevAddress"));
                macAddress = data.getStringExtra("btDevAddress");
                // Log.d("MAC ACRESS", macAddress);

                conexaoBrasileiro = new ThreadConexaoPadraoBrasileiro(macAddress);
                conexaoBrasileiro.start();

                if (conexaoBrasileiro != null) {
                    mensagemNaTela.setText("Conexao finalizada com: " + data.getStringExtra("btDevName") + "\n Verifique o LED de conexão");
                }
            } else {
                mensagemNaTela.setText("Nenhum dispositivo selecionado.");
            }

        } else if (requestCode == SELECT_BLE_DEVICE) {

            if (resultCode == RESULT_OK) {
                mensagemNaTela.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n" + data.getStringExtra("btDevAddress"));
                macAddress = data.getStringExtra("btDevAddress");

                Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
                bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                conexaoBLEPadraoChines.connect(macAddress);

            } else {
                mensagemNaTela.setText("Nenhum dispositivo selecionado.");
            }

        } else if (requestCode == BIND_AUTO_CREATE) {

            if (resultCode == RESULT_OK) {

                servicos1 = data.getParcelableArrayListExtra("lista1");
                Log.d("MGATTCHARACTERISTIC", String.valueOf(servicos1));
                Log.d("1", String.valueOf(servicos1.size()));

                servicos2 = data.getParcelableArrayListExtra("lista2");
                Log.d("MGATTCHARACTERISTIC", String.valueOf(servicos2.size()));
                Log.d("2", String.valueOf(servicos2.size()));

                servicos3 = data.getParcelableArrayListExtra(("lista3"));
                Log.d("MGATTCHARACTERISTIC", String.valueOf(servicos3.size()));
                Log.d("3", String.valueOf(servicos3.size()));


            } else {
                mensagemNaTela.setText("Não conseguiu ser realiza a conexão. Veja as permissões do aplicativo em seu aparelho e habilite a localização.");
            }
        }
    }

    private void abrirInspecaoConformidade() {
        Intent intent = new Intent(this, ExatidaoActivity.class);
        startActivity(intent);
    }

    //Funções Bluetooth
    private void ativarBluetooth() {

        if (bluetoothAdapter == null) {
            mensagemNaTela.setText("Bluetooth não está funcionando.");

        } else {
            mensagemNaTela.setText("Bluetooth está funcionando.");
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                mensagemNaTela.setText("Solicitando ativação do Bluetooth...");

            } else {
                mensagemNaTela.setText("Bluetooth Ativado.");
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

    private void clearUI() {
        mensagemNaTela.setText(R.string.no_data);

    }

    private void updateConnectionState(final int resourceId) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mensagemNaTela.setText(resourceId); //Erro aqui
            }
        });
    }

    private void displayData(String data) {
        if (data != null) {
            mensagemNaTela.setText(data);
        }
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {

        if (gattServices == null) return;
        String uuid;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<>();
        mGattCharacteristics = new ArrayList<>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {

            HashMap<String, String> currentServiceData = new HashMap<>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);
            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<>();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);

            }

            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        servicos1 = mGattCharacteristics.get(0);
        servicos2 = mGattCharacteristics.get(1);
        servicos3 = mGattCharacteristics.get(2);
        Log.d("MGATTCHARACTERISTIC", String.valueOf(mGattCharacteristics.size()));
        Log.d("1", String.valueOf(servicos1.size()));
        Log.d("2", String.valueOf(servicos2.size()));
        Log.d("3", String.valueOf(servicos3.size()));

    }


    //Funções Gerais da Atividade
    public void TesteMarchaEmVazio(View view) {

        if ((conexaoBrasileiro == null) && (conexaoBLEPadraoChines == null)) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser iniciado/parado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {
            testeMarchaVazio = findViewById(R.id.buttonAplicarTensao);

            if (!testeMarchaVazioRodando) {
                testeMarchaVazioRodando = true;
                testeMarchaVazio.clearComposingText();
                testeMarchaVazio.setText("Cancelar Teste");
                //TODO conferir se é padrão brasileiro ou chines - if
                marchaVazioPadrãoBrasileiro();
                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste Iniciado!");

            } else {
                testeMarchaVazioRodando = false;
                testeMarchaVazio.clearComposingText();
                testeMarchaVazio.setText("Iniciar Teste");
                //TODO conferir se é padrão brasileiro ou chines - if
                pararTestesPadrãoBrasileiro();
                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste Cancelado!");
            }
        }
    }

    public void TesteFotoCelula(View view) {
        if ((conexaoBrasileiro == null) && (conexaoBLEPadraoChines == null)) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser iniciado/parado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();
            return;

        } else {

            confereNumSeriePadraoChines();
            testeFotoCelula = findViewById(R.id.buttonTesteFotoCelula);

            if (!testeFotoCelulaRodando) {
                testeFotoCelulaRodando = true;
                testeFotoCelula.clearComposingText();
                testeFotoCelula.setText("Cancelar Teste de FotoCélula");

                //TODO conferir se é padrão brasileiro ou chines - if
                fotoCelulaPadraoBrasileiro();
                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste de FotoCélula Iniciado!");

            } else {
                testeFotoCelulaRodando = false;
                testeFotoCelula.clearComposingText();
                testeFotoCelula.setText("Iniciar Teste de FotoCélula");
                //TODO conferir se é padrão brasileiro ou chines - if
                pararTestesPadrãoBrasileiro();
                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste de FotoCélula Cancelado!");
            }
        }
    }

    public void escreverTela(final String res) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!(mensagemNaTela == null)) {
                    if (res.startsWith("T")) {
                        mensagemNaTela.clearComposingText();
                        mensagemNaTela.setText("Teste Concluído!");
                        testeMarchaVazioRodando = false;
                        testeFotoCelulaRodando = false;
                        testeMarchaVazio.clearComposingText();
                        testeMarchaVazio.setText("Iniciar Teste");
                        testeFotoCelula.clearComposingText();
                        testeFotoCelula.setText("Iniciar Teste");

                    } else {
                        mensagemNaTela.clearComposingText();
                        mensagemNaTela.setText(res);
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

//        BluetoothGattCharacteristic caracteristica = servicos3.get(0);
//        Log.d("CARACTERISTIC", String.valueOf(servicos3.get(0).getUuid()));
//        caracteristica.setValue(pacote);
//        Log.d("CARACTERISTIC", String.valueOf(caracteristica.getValue()));
//        conexaoBLEPadraoChines.writeCharacteristic(caracteristica);

        boolean success = conexaoBLEPadraoChines.writeCharacteristic(pacote);

        Log.d(TAG, String.valueOf(success));


    }


    //Testes Padrao Brasileiro
    private void marchaVazioPadrãoBrasileiro() {

        //TODO Pegar o numero de série do medidor para ser comparado nos próximos testes

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
                mensagemNaTela.setText("O teste de FotoCélula vai ser iniciado...");
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





