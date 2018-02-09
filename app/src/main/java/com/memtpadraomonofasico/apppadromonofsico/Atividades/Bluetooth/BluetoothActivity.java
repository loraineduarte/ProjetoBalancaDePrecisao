package com.memtpadraomonofasico.apppadromonofsico.Atividades.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import static java.lang.System.out;
import com.memtpadraomonofasico.apppadromonofsico.R;
import android.widget.EditText;

public class BluetoothActivity extends AppCompatActivity {

    private static final String TAG = "Bluetooth";

    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;
    private static final int REQUEST_ENABLE_BT = 4;

    static TextView statusMessage;
    static TextView textSpace;

    ThreadConexao conexao;
    String enderecoDispositivo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        statusMessage = (TextView) findViewById(R.id.statusMessage);
        textSpace = (TextView) findViewById(R.id.textSpace);

        // Início - verificando ativação do bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            statusMessage.setText("Bluetooth não está funcionando.");
            out.append("device not supported");
        }
        else{
            statusMessage.setText("Bluetooth está funcionando.");
            if (!mBluetoothAdapter.isEnabled()) {
                Log.d(TAG, "ATIVANDO BLUETOOTH");
                Intent enableBtIntent  = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                statusMessage.setText("Solicitando ativação do Bluetooth...");
                //startActivityForResult(enableBtIntent, REQUEST_DISCOVERABLE_BT);
                //enableBtIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1000);
            }
            else{
                statusMessage.setText("Bluetooth Ativado.");
            }
        }
        // Fim - verificando ativação do bluetooth

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ENABLE_BLUETOOTH) {
            if(resultCode == RESULT_OK) {
                statusMessage.setText("Bluetooth ativado.");
            }
            else {
                statusMessage.setText("Bluetooth não ativado.");
            }
        }
        else if(requestCode == SELECT_PAIRED_DEVICE || requestCode == SELECT_DISCOVERED_DEVICE) {
            if(resultCode == RESULT_OK) {
                statusMessage.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n"
                        + data.getStringExtra("btDevAddress"));

                conexao = new ThreadConexao(data.getStringExtra("btDevAddress"));
                conexao.start();
            }
            else {
                statusMessage.setText("Nenhum dispositivo selecionado.");
            }
        }
    }

    public void searchPairedDevices(View view){
        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }

    public void discoverDevices(View view){
        Intent searchPairedDevicesIntent = new Intent(this, DiscoveredDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_DISCOVERED_DEVICE);
    }

    public void waitConnection(View view){
        conexao = new ThreadConexao();
        conexao.start();
    }

    public void enableVisibility(View view){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
        startActivity(discoverableIntent);
    }

    public void sendMessage(View view){
        EditText messageBox = (EditText) findViewById(R.id.editText_MessageBox);
        String messageBoxString = messageBox.getText().toString();
        byte[] data =  messageBoxString.getBytes();
        conexao.write(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String dataString= new String(data);
            Log.d("DataString", dataString);

            if(dataString.equals("---N"))

                Log.d(TAG,"Ocorreu um erro durante a conexão D:");
            else if(dataString.equals("---S"))
                Log.d(TAG,"Conectado :D");
            else {

                textSpace.setText(new String(data));
            }
        }
    };

}
