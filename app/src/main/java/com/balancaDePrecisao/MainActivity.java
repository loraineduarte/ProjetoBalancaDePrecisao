package com.balancaDePrecisao;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.balancaDePrecisao.Bluetooth.BluetoothActivity;
import com.balancaDePrecisao.Bluetooth.PairedDevices;
import com.balancaDePrecisao.Bluetooth.ThreadConexao;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("HandlerLeak")
    private static final Handler handler = new Handler() {
    };

    private static final int ENABLE_BLUETOOTH = 1;
    private static final int REQUEST_ENABLE_BT = 4;
    private static final int SELECT_PAIRED_DEVICE = 2;
    private static final int SALVAR_PESO = 5;
    private com.balancaDePrecisao.Bluetooth.ThreadConexao conexao;

    private static Button botaoConectar, botaoSalvar, botaoHistorico, botaoTara;
    private static TextView textViewPesoBalanca, textViewSituacaoConexao;
    private static String macAddress;
    private BluetoothAdapter bluetoothAdapter = null;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        botaoConectar = findViewById(R.id.conexaoBluetooth);
        botaoSalvar = findViewById(R.id.salvarPeso);
        botaoHistorico = findViewById(R.id.historico);
        botaoTara = findViewById(R.id.tara);
        textViewPesoBalanca = findViewById(R.id.PesoDabalanca);
        textViewSituacaoConexao = findViewById(R.id.statusConexao);


    }





    public void MostrarHistorico(View view) {
        Intent intent = new Intent (this, HistoricoPesosActivity.class);
        startActivity(intent);
    }

    public void SalvarPeso(View view) {
        Intent intent = new Intent(this, SalvarPesoActivity.class);
        intent.putExtra("peso", textViewPesoBalanca.getText().toString());
        Log.d("PESO",textViewPesoBalanca.getText().toString() );
        startActivityForResult(intent, SALVAR_PESO);

    }

    public void LimparTara(View view) {

        byte[] pacote = new byte[1];
        pacote[0] = 0;
        conexao.write(pacote);
    }

    public void ConectarBluetooth(View view) {


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

    private void ativarBluetooth() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            textViewSituacaoConexao.setText("Bluetooth não está funcionando.");

        } else {
            textViewSituacaoConexao.setText("Bluetooth está funcionando.");
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                textViewSituacaoConexao.setText("Solicitando ativação do Bluetooth...");

            } else {
                textViewSituacaoConexao.setText("Bluetooth Ativado.");
            }
        }
    }

    private void conectarDispositivo() {


        if (conexao != null) {
            Toast.makeText(getApplicationContext(), "Dispositivo já conectado.", Toast.LENGTH_LONG).show();
        }
        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                textViewSituacaoConexao.setText("Bluetooth ativado.");

            } else {
                textViewSituacaoConexao.setText("Bluetooth não ativado.");
            }

        } else if (requestCode == SELECT_PAIRED_DEVICE ) {
            if (resultCode == RESULT_OK) {
                textViewSituacaoConexao.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n"
                        + data.getStringExtra("btDevAddress"));

                conexao = new ThreadConexao(data.getStringExtra("btDevAddress"));
                conexao.start();
            } else {
                textViewSituacaoConexao.setText("Nenhum dispositivo selecionado.");
            }
        }


    }

    public static void escreverTela(final String dados) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                textViewPesoBalanca.setText(dados);
            }
        });


    }


}
