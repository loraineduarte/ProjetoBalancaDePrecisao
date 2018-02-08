package com.memtpadraomonofasico.apppadromonofsico.Atividades.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by loraine.duarte on 05/02/2018.
 */
public class ThreadConexao extends Thread {

    BluetoothSocket btSocket = null;
    BluetoothServerSocket btServerSocket = null;
    InputStream input = null;
    OutputStream output = null;
    String btDevAddress = null;
    String myUUID = "00001101-0000-1000-8000-00805F9B34FB";
    boolean server;
    boolean running = false;

    /*  Este construtor prepara o dispositivo para atuar como servidor.
     */
    public ThreadConexao() {
        this.server = true;
    }

    public ThreadConexao(String btDevAddress) {
        this.server = false;
        this.btDevAddress = btDevAddress;
    }


    public void run() {


        this.running = true;
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(this.server) //Servidor.
        {
            try {

                btServerSocket = btAdapter.listenUsingRfcommWithServiceRecord("Super Bluetooth", UUID.fromString(myUUID));
                btSocket = btServerSocket.accept();

                if(btSocket != null) {
                    btServerSocket.close();
                }

            } catch (IOException e) {

                e.printStackTrace();
                toMainActivity("---N".getBytes());
            }
        }
        else // Cliente.
        {
            try
            {
                BluetoothDevice btDevice = btAdapter.getRemoteDevice(btDevAddress);
                btSocket = btDevice.createRfcommSocketToServiceRecord(UUID.fromString(myUUID));
                btAdapter.cancelDiscovery();

                if (btSocket != null) {
                    btSocket.connect();
                    toMainActivity("---S".getBytes());
                }

            } catch (IOException e) {
                e.printStackTrace();

                try {
                    Log.i("Error","Trying fallback...");
                    BluetoothDevice btDevice = btAdapter.getRemoteDevice(btDevAddress);
                    btSocket =(BluetoothSocket) btDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(btDevice,1);
                    btSocket.connect();
                    toMainActivity("---S".getBytes());
                    Log.i("Thread Connection","Connected");
                } catch (Exception e2) {
                    Log.e("Thread Connection","Couldn't establish Bluetooth connection!");
                    toMainActivity("---N".getBytes());
                }
                toMainActivity("---N".getBytes());
            }

        }

        if(btSocket.isConnected())
        {

            try {

                input = btSocket.getInputStream();
                output = btSocket.getOutputStream();

                byte[] buffer = new byte[1024];
                int bytes;

                while(running) {
                    bytes = input.read(buffer);
                    toMainActivity(Arrays.copyOfRange(buffer, 0, bytes));
                }

            } catch (IOException e) {
                e.printStackTrace();
                toMainActivity("---W".getBytes());
            }

        }
    }

    public boolean getConectado(){
        if (btSocket.isConnected()) {
            return true;
        }
        return false;
    }

    private void toMainActivity(byte[] data) {

        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putByteArray("data", data);
        message.setData(bundle);
        BluetoothActivity.handler.sendMessage(message);

    }

    public void write(byte[] data) {

        if(output != null) {
            try {

                output.write(data);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            toMainActivity("---N".getBytes());
        }
    }

    public void cancel() {

        try {
            if (this.server)
            {
                btServerSocket.close();
            }
            else
            {
                btSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        running = false;
    }

}
