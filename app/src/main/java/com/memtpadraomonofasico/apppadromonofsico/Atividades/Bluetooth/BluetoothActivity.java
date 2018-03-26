package com.memtpadraomonofasico.apppadromonofsico.Atividades.Bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.MarchaVazio.MarchaVazioActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador.RegistradorActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;

import java.util.Arrays;

import static java.lang.System.out;

@SuppressWarnings({"ALL", "StringConcatenationInLoop"})
public class BluetoothActivity extends AppCompatActivity {

    private static final String TAG = "Bluetooth";

    private static final int ENABLE_BLUETOOTH = 1;
    private static final int SELECT_PAIRED_DEVICE = 2;
    private static final int SELECT_DISCOVERED_DEVICE = 3;
    private static final int REQUEST_ENABLE_BT = 4;

    private static String res;
    static String tipoTeste = "";

    private static TextView statusMessage;
    private static TextView textSpace;

    private static final byte[] pacote = new  byte[10];

    private ThreadConexao conexao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        statusMessage = findViewById(R.id.statusMessage);
        textSpace = findViewById(R.id.textSpace);

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

    /**
     * @param view
     */
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
        EditText messageBox = findViewById(R.id.editText_MessageBox);
        String messageBoxString = messageBox.getText().toString();

        byte[] pacote = new byte[10];

        if (messageBox.getText().toString().equalsIgnoreCase("i")){
            pacote[0] = ('I' & 0xFF);
            pacote[1] = ('B' & 0xFF);
            pacote[2] = (byte)(0 & 0xFF);
            pacote[3] = (byte)(0 & 0xFF);
            pacote[4] = (byte)(90 & 0xFF);
            pacote[5] = (byte)(175 & 0xFF);
            pacote[6] = (byte)(0 & 0xFF);
            pacote[7] = (byte)(10 & 0xFF);
            pacote[8] = (byte)(0 & 0xFF);
            pacote[9] = (byte)(0 & 0xFF);
        }
        else if(messageBox.getText().toString().equalsIgnoreCase("c")){
            pacote[0] = ('C' & 0xFF);
            pacote[1] = (byte)(0 & 0xFF);
            pacote[2] = (byte)(0 & 0xFF);
            pacote[3] = (byte)(0 & 0xFF);
            pacote[4] = (byte)(0 & 0xFF);
            pacote[5] = (byte)(0 & 0xFF);
            pacote[6] = (byte)(0 & 0xFF);
            pacote[7] = (byte)(0 & 0xFF);
            pacote[8] = (byte)(0 & 0xFF);
            pacote[9] = (byte)(0 & 0xFF);
        }

        statusMessage.setText(Arrays.toString(pacote));
        conexao.write(pacote);
    }

    @SuppressLint("HandlerLeak")
    public static final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            String dados;
            int cont = 0;
            Bundle bundle = msg.getData();
            byte[] data = bundle.getByteArray("data");
            String dataString= new String(data != null ? data : new byte[0]);



            switch (dataString) {
                case "---N":
                    RegistradorActivity.escreverTela("Ocorreu um erro durante a conexão.");
                    MarchaVazioActivity.escreverTela("Ocorreu um erro durante a conexão.");
                    break;
                case "---S":
                    RegistradorActivity.escreverTela("Conectado.");
                    MarchaVazioActivity.escreverTela("Conectado.");
                    break;

                default:
                    cont = cont + 1;
                    dados = dataString;
                    //byte[] dado0255 = new byte[10];
                    //int contar = 0;

                    //for (byte dado :data) {
                    //    dado0255[contar] = (byte) (dado & 0xFF);
                    //    contar = contar + 1;
                    //}

                    //int a = dado0255[2] << 24 | dado0255[3] << 16 | dado0255[4] << 8 | dado0255[5];
                    //int b = dado0255[6] << 24 | dado0255[7] << 16 | dado0255[8] << 8 | dado0255[9];
                    //dados = dados+Integer.toString((dado0255[0] & 0xFF))+", "+Integer.toString((dado0255[1] & 0xFF))+"||||"+Integer.toString((dado0255[2] & 0xFF))+", "+Integer.toString((dado0255[3] & 0xFF))+", "+Integer.toString((dado0255[4] & 0xFF))+", "+Integer.toString((dado0255[5] & 0xFF))+"||||"+Integer.toString((dado0255[6] & 0xFF))+", "+Integer.toString((dado0255[7] & 0xFF))+", "+Integer.toString((dado0255[8] & 0xFF))+", "+Integer.toString((dado0255[9] & 0xFF));
                    //String teste = new String(dado0255);
                    //dados = dados+Integer.toString(bundle.getByteArray("data").length)+ " : ";
                    //textSpace.setText(dados);
                    double a = 0;
                    double b = 0;
                    res = res + Integer.toString(data.length) + "\n";
                    if (dados.length() == 1) {
                        pacote[0] = (byte) (data[0] & 0xFF);
                    }

                    Log.d("RECEBIDO", dataString);



                    if((dataString.startsWith("D"))){
                        tipoTeste="";
                    }

                    if(dataString.startsWith("R")){
                        tipoTeste = tipoTeste + dataString;

                        if (dados.length() == 9) {
                            pacote[1] = (byte) (data[0] & 0xFF);
                            pacote[2] = (byte) (data[1] & 0xFF);
                            pacote[3] = (byte) (data[2] & 0xFF);
                            pacote[4] = (byte) (data[3] & 0xFF);
                            pacote[5] = (byte) (data[4] & 0xFF);
                            pacote[6] = (byte) (data[5] & 0xFF);
                            pacote[7] = (byte) (data[6] & 0xFF);
                            pacote[8] = (byte) (data[7] & 0xFF);
                            pacote[9] = (byte) (data[8] & 0xFF);
                            a = (pacote[2]) * Math.pow(256, 3) + (pacote[3] & 0xFF) * Math.pow(256, 2) + (pacote[4] & 0xFF) * 256 + (pacote[5] & 0xFF);
                            b = (pacote[6] & 0xFF) * Math.pow(256, 3) + (pacote[7] & 0xFF) * Math.pow(256, 2) + (pacote[8] & 0xFF) * 256 + (pacote[9] & 0xFF);
                        }
                        res = "  Tensão:   " + Integer.toString((int) a) + " uV   Corrente:  " + Integer.toString((int) b) + " uA \n";

                        // Integer.toString((pacote[0] & 0xFF)) + ", " + Integer.toString((pacote[1] & 0xFF)) +
//                    for (byte d : pacote) {
//                        res = res + "[" + Integer.toString((d & 0xFF)) + "]";
//                    }
//                    res = res + "\n";

                      RegistradorActivity.escreverTela("Recebendo dados do padrão \n" + res);

                    }

                    if(dataString.startsWith("M")){
                        tipoTeste = tipoTeste + dataString;

                        if (dados.length() == 9) {
                            pacote[1] = (byte) (data[0] & 0xFF);
                            pacote[2] = (byte) (data[1] & 0xFF);
                            pacote[3] = (byte) (data[2] & 0xFF);
                            pacote[4] = (byte) (data[3] & 0xFF);
                            pacote[5] = (byte) (data[4] & 0xFF);
                            pacote[6] = (byte) (data[5] & 0xFF);
                            pacote[7] = (byte) (data[6] & 0xFF);
                            pacote[8] = (byte) (data[7] & 0xFF);
                            pacote[9] = (byte) (data[8] & 0xFF);

                            a = (pacote[2]) * Math.pow(256, 3) + (pacote[3] & 0xFF) * Math.pow(256, 2) + (pacote[4] & 0xFF) * 256 + (pacote[5] & 0xFF);
                            b = (pacote[6] & 0xFF) * Math.pow(256, 3) + (pacote[7] & 0xFF) * Math.pow(256, 2) + (pacote[8] & 0xFF) * 256 + (pacote[9] & 0xFF);
                        }
                        res = "  Tensão:   " + Integer.toString((int) a) + " uV   Corrente:  " + Integer.toString((int) b) + " uA \n";
                        MarchaVazioActivity.escreverTela("Recebendo dados do padrão \n" + res);

                    }

                    if(dataString.contains("F")){
                        tipoTeste = tipoTeste + dataString;

                        if(tipoTeste.contains("R")){
                            RegistradorActivity.escreverTela("Teste sendo finalizado ... \n" + res);

                        } if(tipoTeste.contains("M")){
                            a = ((pacote[2]) * 256) + (pacote[3] & 0xFF);
                            res = "  Número de pulsos :   " + Integer.toString((int) a) + "\n";
                            MarchaVazioActivity.escreverTela("Teste sendo finalizado ... \n" + res);
                            MarchaVazioActivity.selecionarStatus(a);


                        }
                        break;
                    }




                    if (cont >= 2) {
                        res = "";
                    }
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

}
