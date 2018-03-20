package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.Bluetooth.ThreadConexao;
import com.memtpadraomonofasico.apppadromonofsico.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by max on 01/03/18.
 */

public class RegistradorDialogFragment extends DialogFragment {
    private String[] disp = new String[]{"Dispositivo1", "Dispositivo2", "Dispositivo3", "Dispositivo4"};
    private Spinner sp;
    private static final String TAG = "BuscaBluetooth";
    private ArrayAdapter arrayAdapter;

    private final static int REQUEST_ENABLE_BT = 1;
    private List<BluetoothDevice> listaDispositivos = new ArrayList();

    static TextView tvStatus;
    ThreadConexao conexao;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.i("Script", "onCreate");
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        Log.i("Script", "onCreaate");
        View view = inflater.inflate(R.layout.registrador_dialog_fragment, container);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);

        /* Início - Botão de sair.*/
        Button btnSair = (Button) view.findViewById(R.id.btnSair);
        btnSair.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                ((RegistradorActivity) getActivity()).turnOfDialogFragment();
            }
        });
        /* Fim - Botão de sair.*/

        /* Início - Botão conectar.*/
        Button btnTesteRegistrador = (Button) view.findViewById(R.id.btnTesteRegistrador);
        btnTesteRegistrador.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                //String macAddress = sp.getSelectedItem().toString().split("\n")[1];
                //tvStatus.setText("1-"+macAddress);
                //conexao = new ThreadConexao(macAddress);
                //tvStatus.setText("2-"+macAddress+" "+conexao.getState()+" "+conexao.isAlive());
                //conexao.start();
                //tvStatus.setText("3-"+macAddress+" "+conexao.getState()+" "+conexao.isAlive());
                ((RegistradorActivity) getActivity()).turnOfDialogFragment();
            }
        });
        /* Fim - Botão conectar.*/

        /* Início - Botão Cancelar Teste.*/
        Button btnCancelarTeste = (Button) view.findViewById(R.id.btnCancelarTeste);
        btnCancelarTeste.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                ((RegistradorActivity) getActivity()).turnOfDialogFragment();
            }
        });
        /* Fim - Botão Cancelar Teste.*/

        /* Início - Spinner dos dispositivos bluetooth encontrados.*/
        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Bluetooth bluetooth = new Bluetooth();

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                adapter.add(device.getName() + "\n" + device.getAddress());
            }
        }

        sp = (Spinner) view.findViewById(R.id.dispositivos);
        sp.setAdapter(adapter);
        /* Fim - Spinner dos dispositivos bluetooth encontrados.*/

        return(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.i("Script", "onactivityCreated");
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        Log.i("Script", "onactivityCreated");
    }

    @Override
    public void onCancel(DialogInterface dialog){
        super.onCancel(dialog);
        Log.i("Script", "onCancel");
    }
    /*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreateDialog(savedInstanceState);
        Log.i("Script", "onCreateDialog");

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity())
                .setTitle("DialogForm")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Ok pressed", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        return(alert.show());
    }
    */
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.i("Script", "onDestroyView");
    }
    @Override
    public void onDetach(){
        super.onDetach();
        Log.i("Script", "onDetach");
    }
    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        Log.i("Script", "onDismiss");
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.i("Script", "onSaveInstanceState");
    }
    @Override
    public void onStart(){
        super.onStart();
        Log.i("Script", "onStart");
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.i("Script", "onStop");
    }
}
