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
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.ConexaoMarchaVazio.ConexaoBLE.SampleGattAttributes;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.ConexaoMarchaVazio.ConexãoBluetooth.ThreadConexaoPadraoBrasileiro;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.sql.Time;
import java.util.UUID;

@SuppressWarnings("ALL")
@TargetApi(21)
public class MarchaVazioActivity extends AppCompatActivity {

    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
    private final static String TAG = MarchaVazioActivity.class.getSimpleName();
    static final private UUID CCCD_ID = UUID.fromString("000002902-0000-1000-8000-00805f9b34fb");
    private static final String myUUID = "00001800-0000-1000-8000-00805f9b34fb"; //"00060001-F8CE-11E4-ABF4-0002A5D5C51B";// configuração universão do WH-BLE 102

//    public final static String ACTION_GATT_CONNECTED =
//            "com.nordicsemi.nrfUART.ACTION_GATT_CONNECTED";
//    public final static String ACTION_GATT_DISCONNECTED =
//            "com.nordicsemi.nrfUART.ACTION_GATT_DISCONNECTED";
//    public final static String ACTION_GATT_SERVICES_DISCOVERED =
//            "com.nordicsemi.nrfUART.ACTION_GATT_SERVICES_DISCOVERED";
//    public final static String ACTION_DATA_AVAILABLE =
//            "com.nordicsemi.nrfUART.ACTION_DATA_AVAILABLE";
//    public final static String EXTRA_DATA =
//            "com.nordicsemi.nrfUART.EXTRA_DATA";
//    public final static String DEVICE_DOES_NOT_SUPPORT_UART =
//            "com.nordicsemi.nrfUART.DEVICE_DOES_NOT_SUPPORT_UART";
//
//    private int mConnectionState = STATE_DISCONNECTED;
//
//    private static final int STATE_DISCONNECTED = 0;
//    private static final int STATE_CONNECTING = 1;
//    private static final int STATE_CONNECTED = 2;
//
//

//    private static final long SCAN_PERIOD = 10000;
//
//    List<BluetoothGattService> services;

//    private Handler mHandler;
//    private BluetoothLeScanner mLEScanner;
//    private ScanSettings settings;
//    private List<ScanFilter> filters;
//    private BluetoothGatt mGatt;
//    BluetoothGattCharacteristic characteristic;
//    boolean enabled;
//
//    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//            if (ACTION_GATT_CONNECTED.equals(action)) {
//                mConnected = true;
//                updateConnectionState(R.string.connected);
//                invalidateOptionsMenu();
//            } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
//                mConnected = false;
//                updateConnectionState(R.string.disconnected);
//                invalidateOptionsMenu();
//                clearUI();
//            } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
//                // Show all the supported services and characteristics on the
//                // user interface.
//                displayGattServices(mBluetoothLeService.getSupportedGattServices());
//            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
//                displayData(intent.getStringExtra(EXTRA_DATA));
//            }
//        }
//
//
//    };
//
//    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
//        @Override
//        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d("onLeScan", device.toString());
//                    connectToDevice(device);
//                }
//            });
//        }
//    };
//    //----classes de Scan
//    private ScanCallback mScanCallback = new ScanCallback() {
//        @Override
//        public void onScanResult(int callbackType, ScanResult result) {
//
//            Log.d("callbackType", String.valueOf(callbackType));
//            Log.d("result", result.toString());
//            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(result.getDevice().getAddress());
//            connectToDevice(device);
//
//        }
//
//        @Override
//        public void onBatchScanResults(List<ScanResult> results) {
//            for (ScanResult sr : results) {
//                Log.d("ScanResult - Results", sr.toString());
//            }
//        }
//
//        @Override
//        public void onScanFailed(int errorCode) {
//            Log.d("Scan Failed", "Error Code: " + errorCode);
//        }
//    };
//
//    //-----GATT conection
//    public final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
//
//        @Override
//        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
//            String intentAction;
//            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                intentAction = ACTION_GATT_CONNECTED;
//                mConnectionState = STATE_CONNECTED;
//                broadcastUpdate(intentAction);
//                Log.i(TAG, "Connected to GATT server.");
//                Log.i(TAG, "Attempting to start service discovery:" +
//                        mGatt.discoverServices());
//
//            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
//                intentAction = ACTION_GATT_DISCONNECTED;
//                mConnectionState = STATE_DISCONNECTED;
//                Log.i(TAG, "Disconnected from GATT server.");
//                broadcastUpdate(intentAction);
//            }
//
//        }
//
//        @Override
//        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
//            } else {
//                Log.w(TAG, "onServicesDiscovered received: " + status);
//            }
//        }
//
//        @Override
//        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) { //le os dados recebidos
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
//            }
//        }
//
//        @Override
//        // Characteristic notification
//        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
//            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
//        }
//    };
//


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

    //para o padrao chines
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
                Log.d("MAC ACRESS", macAddress);

                Intent intent = new Intent(this, DeviceControlActivity.class);
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, data.getStringExtra("btDevName"));
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, data.getStringExtra("btDevAddress"));

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
                executarTestePadrãoBrasileiro();
                textMessage.clearComposingText();
                textMessage.setText("Teste Iniciado!");

            } else {
                testeComecou = false;
                teste.clearComposingText();
                teste.setText("Iniciar Teste");
                pararTestePadrãoBrasileiro();
                textMessage.clearComposingText();
                textMessage.setText("Teste Cancelado!");
            }
        }
    }

    public void TesteFotoCelula(View view) {

        confereVersaoPadraoChines();


        if (conexaoBrasileiro == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser iniciado/parado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            teste = findViewById(R.id.buttonTesteFotoCelula);

            if (!testeFCComecou) {
                testeFCComecou = true;
                teste.clearComposingText();
                teste.setText("Cancelar Teste de FotoCélula");
                fazerTesteFotoCelulaPadraoBrasileiro();
                textMessage.clearComposingText();
                textMessage.setText("Teste de FotoCélula Iniciado!");

            } else {
                testeFCComecou = false;
                teste.clearComposingText();
                teste.setText("Iniciar Teste de FotoCélula");
                //olhar qual o padrão
                pararTestePadrãoBrasileiro();
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

    //Funções Padrão Brasileiro
    private void conectarDispositivo() {

//        if (conexaoBrasileiro != null) {
//            Toast.makeText(getApplicationContext(), "Dispositivo já conectado.", Toast.LENGTH_LONG).show();
//        }
//
//        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
//        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);

        Intent searchPairedDevicesIntent = new Intent(this, DeviceScanActivity.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_BLE_DEVICE);


    }

    private void executarTestePadrãoBrasileiro() {

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

    private void pararTestePadrãoBrasileiro() {

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

    public void fazerTesteFotoCelulaPadraoBrasileiro() {
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

    //Funções Padrão Chines
//    private void scanLeDevice(final boolean enable) {
//        if (enable) {
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (Build.VERSION.SDK_INT < 21) {
//                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                    } else {
//                        mLEScanner.stopScan(mScanCallback);
//
//                    }
//                }
//            }, SCAN_PERIOD);
//            if (Build.VERSION.SDK_INT < 21) {
//                mBluetoothAdapter.startLeScan(mLeScanCallback);
//            } else {
//                mLEScanner.startScan(filters, settings, mScanCallback);
//            }
//        } else {
//            if (Build.VERSION.SDK_INT < 21) {
//                mBluetoothAdapter.stopLeScan(mLeScanCallback);
//            } else {
//                mLEScanner.stopScan(mScanCallback);
//            }
//        }
//    }
//
//    private void broadcastUpdate(final String action) {
//        final Intent intent = new Intent(action);
//        sendBroadcast(intent);
//    }
//
//    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
//        final Intent intent = new Intent(action);
//
//        // This is special handling for the Heart Rate Measurement profile. Data
//        // parsing is carried out as per profile specifications.
//        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
//            int flag = characteristic.getProperties();
//            int format = -1;
//            if ((flag & 0x01) != 0) {
//                format = BluetoothGattCharacteristic.FORMAT_UINT16;
//                Log.d(TAG, "Heart rate format UINT16.");
//            } else {
//                format = BluetoothGattCharacteristic.FORMAT_UINT8;
//                Log.d(TAG, "Heart rate format UINT8.");
//            }
//            final int heartRate = characteristic.getIntValue(format, 1);
//            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
//            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
//        } else {
//            // For all other profiles, writes the data formatted in HEX.
//            final byte[] data = characteristic.getValue();
//            if (data != null && data.length > 0) {
//                final StringBuilder stringBuilder = new StringBuilder(data.length);
//                for(byte byteChar : data)
//                    stringBuilder.append(String.format("%02X ", byteChar));
//                intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
//                        stringBuilder.toString());
//            }
//        }
//        sendBroadcast(intent);
//    }
//
//    //-----encontrar caracteristicas de serviços
//    private void findCharacteristic(BluetoothGattService service) {
//
//        if (service.getUuid().toString().equalsIgnoreCase(myUUID)) {
//            for (BluetoothGattCharacteristic serviceCharacteristic : service.getCharacteristics()) {
//                if (serviceCharacteristic.getUuid().toString().equalsIgnoreCase(myUUID)) {
//                    characteristic = serviceCharacteristic;
//                }
//            }
//        }
//    }
//
//    private boolean refreshDeviceCache(BluetoothGatt gatt) {
//        try {
//            BluetoothGatt localBluetoothGatt = gatt;
//            Method localMethod = localBluetoothGatt.getClass().getMethod("refresh", new Class[0]);
//            if (localMethod != null) {
//                boolean bool = ((Boolean) localMethod.invoke(localBluetoothGatt, new Object[0])).booleanValue();
//                return bool;
//            }
//        } catch (Exception localException) {
//            Log.e("REFRESHING CACHE", "An exception occured while refreshing device");
//        }
//        return false;
//    }
//
//    public void connectToDevice(BluetoothDevice device) {
//
//        if (mGatt == null) {
//            if (device.getName() == null) {
//                scanLeDevice(true);// will stop after first device detection
//
//            } else {
//                if (device.getName().startsWith("WH")) {
//
//                    mGatt = device.connectGatt(MarchaVazioActivity.this, false, gattCallback);
//                    textMessage.setText("Conexao finalizada com: " + device.getName() + "\n");
//                    refreshDeviceCache(mGatt);
//                    scanLeDevice(false);// will stop after first device detection
//
//                    Log.d("CONEXAO", String.valueOf(mGatt.getDevice().getAddress()));
//
//
//                } else {
//                    Log.d("DISPOSITIVO ACAHADO:", device.getName() + "\n" + device.getAddress());
//                    scanLeDevice(true);// will stop after first device detection
//                }
//            }
//        }
//    }
//
//    private void processData(byte[] value) {
//
//        String dados;
//        byte[] data = value;
//        String dataString = new String(data != null ? data : new byte[0]);
//        dados = dataString;
//        Log.d("DADOS", dados);
//        Log.d("DADOS TAMANHO", String.valueOf(dados.length()));
//        Log.d("DADOS STRING", dataString);
//
//    }

    private void confereVersaoPadraoChines() {

        int value = 5;

//        if (mBluetoothAdapter == null || mGatt == null) {
//            Log.d("CONEFERE", "BluetoothAdapter not initialized");
//            return;
//        }
//
//        //lista todos os serviços com a conexão
//        List<BluetoothGattService> mCustomService2 = mGatt.getServices();
//        for (BluetoothGattService servico : mCustomService2) {
//            Log.d("SERVIÇO ACHADO", servico.getUuid().toString());
//        }
//
//
//
//        BluetoothGattService mCustomService = mGatt.getService(UUID.fromString("00001800-0000-1000-8000-00805f9b34fb"));
//        if (mCustomService == null) {
//            Log.d("CONEFERE", "Custom BLE Service not found");
//            return;
//        }
//        /*get the read characteristic from the service*/
//        Log.d("CARACTERISTICA 1", String.valueOf(mGatt.getService(UUID.fromString("00001800-0000-1000-8000-00805f9b34fb")).getUuid()));
//        Log.d("CARACTERISTICA 2", String.valueOf(mCustomService.getCharacteristic(UUID.fromString("00001800-0000-1000-8000-00805f9b34fb")))); //aqui vem null
//        BluetoothGattCharacteristic mWriteCharacteristic = mCustomService.getCharacteristic(UUID.fromString("00001800-0000-1000-8000-00805f9b34fb"));
//       // Log.d("CARACTERISTICA ", mWriteCharacteristic.toString());
//        Log.d("VALUE", String.valueOf(value));
//        Log.d("FORMAT UNIT8", String.valueOf(BluetoothGattCharacteristic.FORMAT_UINT8));
//        mWriteCharacteristic.setValue(value, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
//
//        if (mGatt.writeCharacteristic(mWriteCharacteristic) == false) {
//            Log.d("CONEFERE", "Failed to write characteristic");
//        }
    }


}





