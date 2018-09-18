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
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.BLE.DeviceScanActivity;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.BLE.SampleGattAttributes;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.PairedDevices;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.Testes.ConexaoMarchaVazio.BluetoothServicoPadraoMKV;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.Testes.ConexaoMarchaVazio.BluetoothServicoPadraoMSC;
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
    private static final long SCAN_PERIOD = 10000;  // Stops scanning after 10 seconds.
    @SuppressLint("HandlerLeak")
    private static final Handler handler = new Handler() {
    };
    @SuppressLint("StaticFieldLeak")
    private static RadioButton aprovado, naoRealizado, reprovado;
    @SuppressLint("StaticFieldLeak")
    private static EditText tempoReprovado;
    @SuppressLint("StaticFieldLeak")
    private static TextView mensagemNaTela;
    @SuppressLint("WrongViewCast")
    private static Button botaoConectar, botaoTesteMarchaVazio, botaoTesteFotoCelula;
    private static boolean testeMarchaVazioRodando = false;
    private static boolean testeFotoCelulaRodando = false;
    private static String modeloPadrao, macAddress, statusMarchaVazio, tempoReprovadoMarchaVazio;
    private static BluetoothServicoPadraoMKV conexaoPadraoMKV;
    private static BluetoothServicoPadraoMSC conexaoPadraoMSC = new BluetoothServicoPadraoMSC();
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            conexaoPadraoMSC = ((BluetoothServicoPadraoMSC.LocalBinder) service).getService();
            if (conexaoPadraoMSC.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }

            conexaoPadraoMSC.connect(macAddress);  // Automatically connects to the device upon successful start-up initialization.
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            conexaoPadraoMSC = null;
        }
    };
    private BluetoothAdapter bluetoothAdapter = null;
    private ArrayList<BluetoothGattCharacteristic> servicos1 = new ArrayList<>();
    private ArrayList<BluetoothGattCharacteristic> servicos2 = new ArrayList<>();
    private ArrayList<BluetoothGattCharacteristic> servicos3 = new ArrayList<>();
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<>();

    private final ExpandableListView.OnChildClickListener servicesListClickListner = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

            if (mGattCharacteristics != null) {

                final BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(groupPosition).get(childPosition);
                final int charaProp = characteristic.getProperties();

                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {

                    if (mNotifyCharacteristic != null) {
                        conexaoPadraoMSC.setCharacteristicNotification(mNotifyCharacteristic, false);
                        mNotifyCharacteristic = null;
                    }
                    conexaoPadraoMSC.readCharacteristic(characteristic);
                }
                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {

                    mNotifyCharacteristic = characteristic;
                    conexaoPadraoMSC.setCharacteristicNotification(characteristic, true);
                }

                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {

                    mNotifyCharacteristic = characteristic;
                    conexaoPadraoMSC.setCharacteristicNotification(characteristic, true);
                }
                return true;
            }
            return false;
        }
    };

    private boolean mConnected = false;

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();
            if (BluetoothServicoPadraoMSC.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);


            } else if (BluetoothServicoPadraoMSC.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                clearUI();

            } else if (BluetoothServicoPadraoMSC.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                mostraServicosGatt(conexaoPadraoMSC.getSupportedGattServices());

            } else if (BluetoothServicoPadraoMSC.ACTION_DATA_AVAILABLE.equals(action)) {
                mostraDados(intent.getStringExtra(BluetoothServicoPadraoMSC.EXTRA_DATA));

            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothServicoPadraoMSC.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothServicoPadraoMSC.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothServicoPadraoMSC.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothServicoPadraoMSC.ACTION_DATA_AVAILABLE);
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
        botaoConectar = findViewById(R.id.buttonConectarDispositivo);
        botaoTesteMarchaVazio = findViewById(R.id.buttonAplicarTensao);
        botaoTesteFotoCelula = findViewById(R.id.buttonTesteFotoCelula);

        modeloPadrao = Hawk.get("ModeloPadrao");
        Log.d("PADRAO", modeloPadrao);
        ativarBluetooth();

        botaoConectar.setOnClickListener(new View.OnClickListener() {
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

                            if (conexaoPadraoMKV != null) {
                                conexaoPadraoMKV.interrupt();
                            }
                            bluetoothAdapter.disable();
                            conexaoPadraoMKV = null;
                            abrirInspecaoConformidade();

                        } else {
                            Hawk.put("statusMarchaVazio", statusMarchaVazio);
                            Hawk.put("tempoReprovadoMarchaVazio", tempoReprovadoMarchaVazio);

                            if (conexaoPadraoMKV != null) {
                                conexaoPadraoMKV.interrupt();
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

        if (modeloPadrao.startsWith("MKV")) {


        } else if (modeloPadrao.startsWith("MSC")) {
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
            if (conexaoPadraoMSC != null) {
                final boolean result = conexaoPadraoMSC.connect(macAddress);
                Log.d(TAG, "Connect request result=" + result);
            }

            conexaoPadraoMSC.connect(macAddress);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (modeloPadrao.startsWith("MKV")) {


        } else if (modeloPadrao.startsWith("MSC")) {
            unregisterReceiver(mGattUpdateReceiver);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (modeloPadrao.startsWith("MKV")) {


        } else if (modeloPadrao.startsWith("MSC")) {
            unbindService(mServiceConnection);
            conexaoPadraoMSC = null;
        }

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

                conexaoPadraoMKV = new BluetoothServicoPadraoMKV(macAddress);
                conexaoPadraoMKV.start();

                if (conexaoPadraoMKV != null) {
                    mensagemNaTela.setText("Conexao finalizada com: " + data.getStringExtra("btDevName") + "\n Verifique o LED de conexão");
                }
            } else {
                mensagemNaTela.setText("Nenhum dispositivo selecionado.");
            }

        } else if (requestCode == SELECT_BLE_DEVICE) {

            if (resultCode == RESULT_OK) {

                mensagemNaTela.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n" + data.getStringExtra("btDevAddress"));
                macAddress = data.getStringExtra("btDevAddress");

                Intent gattServiceIntent = new Intent(this, BluetoothServicoPadraoMSC.class);
                bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                conexaoPadraoMSC.connect(macAddress);

            } else {
                mensagemNaTela.setText("Nenhum dispositivo selecionado.");
            }

        } else if (requestCode == BIND_AUTO_CREATE) {
            if (resultCode == RESULT_OK) {

                servicos1 = data.getParcelableArrayListExtra("lista1");
                servicos2 = data.getParcelableArrayListExtra("lista2");
                servicos3 = data.getParcelableArrayListExtra(("lista3"));

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

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

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

        if (modeloPadrao.startsWith("MKV")) {
            Log.d("ENTROU MKV", "ENTROU MKV");
            Log.d("PADRAO", modeloPadrao);

            if (conexaoPadraoMKV != null) {
                Toast.makeText(getApplicationContext(), "Dispositivo já conectado.", Toast.LENGTH_LONG).show();
            }
            Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
            startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);

        }
        if (modeloPadrao.startsWith("MSC")) {
            if (conexaoPadraoMSC != null) {
                Toast.makeText(getApplicationContext(), "Dispositivo já conectado.", Toast.LENGTH_LONG).show();
            }
            Intent searchPairedDevicesIntent = new Intent(this, DeviceScanActivity.class);
            startActivityForResult(searchPairedDevicesIntent, SELECT_BLE_DEVICE);
        }

    }

    private void clearUI() {
        mensagemNaTela.setText(R.string.no_data);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mensagemNaTela.setText(resourceId);
            }
        });
    }

    private void mostraDados(String data) {
        if (data != null) {

            Log.d("RECEBIDO STRING", data.toString());
            Log.d("RECEBIDO TAMANHO", String.valueOf(data.length()));
            String mensagemCortada = data.substring(27);  //pacote vem com tamanho 22)

            String[] pacote = new String[9];

            Log.d("RECEBIDO TAMANHO", String.valueOf(mensagemCortada.length()));
            pacote[0] = (mensagemCortada.substring(0, 2));
            pacote[1] = (mensagemCortada.substring(2, 4));
            pacote[2] = (mensagemCortada.substring(4, 6));
            pacote[3] = (mensagemCortada.substring(6, 11));
            pacote[4] = (mensagemCortada.substring(12, 13));
            pacote[5] = (mensagemCortada.substring(14, 15));
            pacote[6] = (mensagemCortada.substring(16, 17));
            pacote[7] = (mensagemCortada.substring(18, 19));
            pacote[8] = (mensagemCortada.substring(20, 22));

//            String category = pacote[2].substring(0,3);
//            String commandID = pacote[2].substring(4,5);
//            String functionAdress = pacote[2].substring(6,7);
//            String equipmentID = pacote[2].substring(8,10);


            Log.d("RECEBIDO CHAR", String.valueOf(pacote[0]));
            Log.d("RECEBIDO CHAR", String.valueOf(pacote[1]));
            Log.d("RECEBIDO CHAR", String.valueOf(pacote[2]));
            Log.d("RECEBIDO CHAR", String.valueOf(pacote[3]));
            Log.d("RECEBIDO CHAR", String.valueOf(pacote[4]));
            Log.d("RECEBIDO CHAR", String.valueOf(pacote[5]));
            Log.d("RECEBIDO CHAR", String.valueOf(pacote[6]));
            Log.d("RECEBIDO CHAR", String.valueOf(pacote[7]));
            Log.d("RECEBIDO CHAR", String.valueOf(pacote[8]));


//            Log.d("CATEGORY", String.valueOf(category));
//            Log.d("COMMAND ID", String.valueOf(commandID));
//            Log.d("FUNCTION ADRESS", String.valueOf(functionAdress));
//            Log.d("EQUIPMENT ID", String.valueOf(equipmentID));
//            pacote[3] = (byte) (letras[3] & 0xFF);
//            pacote[4] = (byte) (letras[4] & 0xFF);
//            pacote[5] = (byte) (letras[5] & 0xFF);
//            pacote[6] = (byte) (letras[6] & 0xFF);
//            pacote[7] = (byte) (letras[7] & 0xFF);
//            pacote[8] = (byte) (letras[8] & 0xFF);
//            pacote[9] = (byte) (letras[9] & 0xFF);
//            pacote[10] = (byte) (letras[10]);
//            pacote[11] = (byte) (letras[11] & 0xFF);
//            pacote[12] = (byte) (letras[12] & 0xFF);
//            pacote[13] = (byte) (letras[13] & 0xFF);
//            pacote[14] = (byte) (letras[14] & 0xFF);
//            pacote[15] = (byte) (letras[15] & 0xFF);
//            pacote[16] = (byte) (letras[16] & 0xFF);
//            pacote[17] = (byte) (letras[17] & 0xFF);
//            pacote[18] = (byte) (letras[18] & 0xFF);
//            pacote[19] = (byte) (letras[19] & 0xFF);
//            pacote[20] = (byte) (letras[20]);
//            pacote[21] = (byte) (letras[21] & 0xFF);
//            pacote[22] = (byte) (letras[22] & 0xFF);
//            pacote[23] = (byte) (letras[23] & 0xFF);
//            pacote[24] = (byte) (letras[24] & 0xFF);
//            pacote[25] = (byte) (letras[25]);
//            pacote[26] = (byte) (letras[26]);
//            pacote[27] = (byte) (letras[27]);
//            pacote[28] = (byte) (letras[28]);
//            pacote[29] = (byte) (letras[29]);
//            pacote[30] = (byte) (letras[30]);
//            pacote[31] = (byte) (letras[31]);
//            pacote[32] = (byte) (letras[32]);
//            pacote[33] = (byte) (letras[33]);
//            pacote[34] = (byte) (letras[34]);
//            pacote[35] = (byte) (letras[35]);
//            pacote[36] = (byte) (letras[36]);
//            pacote[37] = (byte) (letras[37]);
//            pacote[38] = (byte) (letras[38]);
//            pacote[39] = (byte) (letras[39]);
//            pacote[40] = (byte) (letras[40]);
//            pacote[41] = (byte) (letras[41]);
//            pacote[42] = (byte) (letras[42]);
//            pacote[43] = (byte) (letras[43]);
//            pacote[44] = (byte) (letras[44]);
//            pacote[45] = (byte) (letras[45]);
//            pacote[46] = (byte) (letras[46]);
//            pacote[47] = (byte) (letras[47]);
//            pacote[48] = (byte) (letras[48]);


//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[0]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[1]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[2]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[3]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[4]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[5]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[6]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[7]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[8]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[9]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[10]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[11]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[12]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[13]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[14]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[15]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[16]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[17]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[18]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[19]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[20]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[21]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[22]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[23]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[24]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[25]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[26]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[27]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[28]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[29]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[30]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[31]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[32]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[33]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[34]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[35]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[36]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[37]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[38]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[39]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[40]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[41]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[42]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[43]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[44]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[45]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[46]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[47]));
//            Log.d("RECEBIDO CHAR", String.valueOf(pacote[48]));


            mensagemNaTela.setText(data.toString());

        }
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void mostraServicosGatt(List<BluetoothGattService> gattServices) {

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

    }


    //Funções Gerais da Atividade
    public void TesteMarchaEmVazio(View view) {

        if ((conexaoPadraoMKV == null) && (conexaoPadraoMSC == null)) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser iniciado/parado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            botaoTesteMarchaVazio = findViewById(R.id.buttonAplicarTensao);

            if (!testeMarchaVazioRodando) {
                testeMarchaVazioRodando = true;
                botaoTesteMarchaVazio.clearComposingText();
                botaoTesteMarchaVazio.setText("Cancelar Teste de Marcha em Vazio");
                if (modeloPadrao.startsWith("MKV")) {
                    Log.d("PADRAO", modeloPadrao);
                    marchaVazioPadrãoBrasileiro();

                } else if (modeloPadrao.startsWith("MSC")) {
                    //TODO - função para teste de marcha a vazio para o padrão chines
                }
                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste Iniciado!");

            } else {
                testeMarchaVazioRodando = false;
                botaoTesteMarchaVazio.clearComposingText();
                botaoTesteMarchaVazio.setText("Iniciar Teste de Marcha em Vazio");
                if (modeloPadrao.startsWith("MKV")) {
                    Log.d("PADRAO", modeloPadrao);
                    pararTestesPadrãoBrasileiro();

                } else if (modeloPadrao.startsWith("MSC")) {
                    //TODO - função para parar teste no padrão chinês

                }

                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste Cancelado!");
            }
        }
    }

    public void TesteFotoCelula(View view) {
        if ((conexaoPadraoMKV == null) && (conexaoPadraoMSC == null)) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser iniciado/parado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();
            return;

        } else {

            botaoTesteFotoCelula = findViewById(R.id.buttonTesteFotoCelula);

            if (!testeFotoCelulaRodando) {
                testeFotoCelulaRodando = true;
                botaoTesteFotoCelula.clearComposingText();
                botaoTesteFotoCelula.setText("Cancelar Teste de FotoCélula");

                if (modeloPadrao.startsWith("MKV")) {
                    Log.d("PADRAO", modeloPadrao);
                    fotoCelulaPadraoBrasileiro();

                } else if (modeloPadrao.startsWith("MSC")) {
                    //TODO - função teste para foto celula padrão chinês

                }

                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste de FotoCélula Iniciado!");

            } else {
                testeFotoCelulaRodando = false;
                botaoTesteFotoCelula.clearComposingText();
                botaoTesteFotoCelula.setText("Iniciar Teste de FotoCélula");

                if (modeloPadrao.startsWith("MKV")) {
                    Log.d("PADRAO", modeloPadrao);
                    pararTestesPadrãoBrasileiro();

                } else if (modeloPadrao.startsWith("MSC")) {
                    //TODO - função para teste padrão chinês

                }
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

                        botaoTesteMarchaVazio.clearComposingText();
                        botaoTesteMarchaVazio.setText("Iniciar Teste de Marcha em Vazio");
                        botaoTesteFotoCelula.clearComposingText();
                        botaoTesteFotoCelula.setText("Iniciar Teste de FotoCelula");

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
    private void confereNumSeriePadraoMSC() {

        byte[] pacote = new byte[12];

        //Header - AA -------------- OK
        pacote[0] = (byte) 0xAA;
        Log.d("HEADER", String.valueOf(pacote[0]));

        //Length - 0007 (2 bytes)(passar para hexadecimal)
        int lenght1 = 0x00;
        int lenght2 = 0x07;
        pacote[2] = (byte) (lenght1);
        pacote[3] = (byte) (lenght2);
        Log.d("Length", String.valueOf(lenght1));
        Log.d("Length", String.valueOf(lenght2));

        //Header Check - XX (passar para hexadecimal)(fazer XOR de frames anteriores)
        pacote[4] = (byte) (pacote[0] ^ pacote[1] ^ pacote[2]);
        Log.d("Header Check", String.valueOf(pacote[4]));

        //Category ID - 0010 (2 bytes)(passar para hexadecimal)
        int category1 = 0x0010;
        byte[] bytes1 = new byte[2];
        bytes1[0] = (byte) (category1 / (Math.pow(256, 1)));
        bytes1[1] = (byte) (category1 - (bytes1[0] * (Math.pow(256, 1))));
        Log.d("Category ID", String.valueOf(bytes1[0]));
        Log.d("Category ID", String.valueOf(bytes1[1]));
        pacote[5] = (byte) bytes1[0];
        pacote[6] = (byte) bytes1[1];

        //Command ID - 02 (1 byte)
        pacote[7] = (byte) (0x02);
        Log.d("Command ID", String.valueOf(pacote[7]));

        //Function Adress - 00 (1 byte) (Sempre 0)
        pacote[8] = (byte) (0x00);
        Log.d("Function Adress", String.valueOf(pacote[8]));

        //Data - 00
        pacote[9] = (byte) (0x00);
        Log.d("Data", String.valueOf(pacote[9]));

        //Pack Check - XX (fazer XOR de todos os pacotes anteriores)
        pacote[10] = (byte) (pacote[0] ^ pacote[1] ^ pacote[2] ^ pacote[3] ^ pacote[4] ^ pacote[5] ^ pacote[6] ^ pacote[7] ^ pacote[8] ^ pacote[9]);
        Log.d("Pack Check ", String.valueOf(pacote[10]));

        //End - 55 ------------------------ OK
        pacote[11] = (byte) (0x55);
        Log.d("End", String.valueOf(pacote[11]));


        Log.d("PACOTE", String.valueOf(pacote));

        boolean success = conexaoPadraoMSC.writeCharacteristic(pacote);
        Log.d(TAG, String.valueOf(success));

    }


    //Testes Padrao Brasileiro
    private void marchaVazioPadrãoBrasileiro() {

        //TODO Função para pegar o numero de série do medidor para ser comparado nos próximos testes

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

        conexaoPadraoMKV.write(pacote);


    }

    public void fotoCelulaPadraoBrasileiro() {

        //TODO Função para pegar o numero de série do medidor para ser comparado nos próximos testes
        if (conexaoPadraoMKV == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser inicializado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (conexaoPadraoMKV != null) {
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

            conexaoPadraoMKV.write(pacote);
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

        conexaoPadraoMKV.write(pacote);


    }

    //Testes padrão chinês
    //TODO função para teste de marcha em vaxio

    //TODO função para teste de foto celula (registrador)

    //TODO função de cancelar testes




}





