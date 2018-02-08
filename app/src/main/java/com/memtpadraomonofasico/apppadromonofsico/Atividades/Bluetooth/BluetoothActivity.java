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
import android.widget.Toast;
import static java.lang.System.out;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class BluetoothActivity extends AppCompatActivity {

    private static final String TAG = "Bluetooth";
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVERABLE_BT = 0;
    public static int SELECT_PAIRED_DEVICE = 2;
    public static int SELECT_DISCOVERED_DEVICE = 3;
    static TextView textSpace;
    ThreadConexao conexao;
    String enderecoDispositivo = "";


    public void discoverDevices(View view){
        Intent searchPairedDevicesIntent = new Intent(this, DiscoveredDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_DISCOVERED_DEVICE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            out.append("device not supported");
        }

        if (!mBluetoothAdapter.isEnabled()) {

            Log.d(TAG, "ATIVANDO BLUETOOTH");
            Intent enableBtIntent  = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            startActivityForResult(enableBtIntent, REQUEST_DISCOVERABLE_BT);
            enableBtIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1000);

        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_PAIRED_DEVICE || requestCode == SELECT_DISCOVERED_DEVICE) {
            if (resultCode == RESULT_OK) {

                Log.d(TAG,"Você selecionou " + data.getStringExtra("btDevName") + "\n" + data.getStringExtra("btDevAddress"));
                enderecoDispositivo = data.getStringExtra("btDevAddress");
                Log.d(TAG, String.valueOf(enderecoDispositivo));

                if (BluetoothAdapter.checkBluetoothAddress(String.valueOf(enderecoDispositivo))) {

                    conexao = new ThreadConexao(enderecoDispositivo);
                    conexao.start();

                    Toast.makeText(getApplicationContext(), "Conectado", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getApplicationContext(), "Endereço MAC do dispositivo Bluetooth remoto não é válido", Toast.LENGTH_SHORT).show();
                }

            } else {
                Log.d(TAG,"Nenhum dispositivo selecionado :(");
            }
        }
    }

}
