package com.memtpadraomonofasico.apppadromonofsico.Atividades.Bluetooth;

import android.Manifest;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * Created by loraine.duarte on 05/02/2018.
 */

public class DiscoveredDevices extends ListActivity {

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        setListAdapter(arrayAdapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { // Pede permissao de localizaçao ao usuario.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        }

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();  //  Usa o adaptador Bluetooth padrão para iniciar o processo de descoberta.
        btAdapter.startDiscovery();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND); //Cria um filtro que captura o momento em que um dispositivo é descoberto.
        registerReceiver(receiver, filter); //Registra o filtro e define um receptor para o evento de descoberta.
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        String item = (String) getListAdapter().getItem(position-1);
        String devName = item.substring(0, item.indexOf("\n"));
        String devAddress = item.substring(item.indexOf("\n")+1, item.length());

        Intent returnIntent = new Intent();
        Log.d("selecionando ", devAddress);
        returnIntent.putExtra("btDevName", devName);
        returnIntent.putExtra("btDevAddress", devAddress);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        /*  Este método é executado sempre que um novo dispositivo for descoberto.
         */
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d("Device", "Achou um device para conectar");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                arrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }



}