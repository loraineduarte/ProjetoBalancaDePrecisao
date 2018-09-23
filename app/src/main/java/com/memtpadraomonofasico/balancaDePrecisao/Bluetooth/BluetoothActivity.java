package com.memtpadraomonofasico.balancaDePrecisao.Bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
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
import android.widget.Toast;

import com.memtpadraomonofasico.balancaDePrecisao.R;

import java.util.Arrays;
import java.util.zip.CRC32;

import static java.lang.System.out;

@SuppressWarnings({"ALL", "StringConcatenationInLoop"})
public class BluetoothActivity extends AppCompatActivity {

    private static final String TAG = "Bluetooth";

    private static final int ENABLE_BLUETOOTH = 1;
    private static final int SELECT_PAIRED_DEVICE = 2;
    private static final int SELECT_DISCOVERED_DEVICE = 3;
    private static final int REQUEST_ENABLE_BT = 4;
    private static final byte[] pacote = new byte[10];
    static String tipoTeste = "";
    static boolean finalDeTeste = false;
    private static String res;
    @SuppressLint("HandlerLeak")
    public final ThreadLocal<Handler> handler = new ThreadLocal<Handler>() {
        @Override
        protected Handler initialValue() {
            return new Handler() {
                @Override
                public void handleMessage(Message msg) {

                    String dados;
                    int cont = 0;
                    Bundle bundle = msg.getData();
                    byte[] data = bundle.getByteArray("data");
                    String dataString = new String(data != null ? data : new byte[0]);

                    Log.d("DADOS TAMANHO", String.valueOf(dataString.length()));
                    Log.d("DADOS STRING", dataString);


                    switch (dataString) {
                        case "---N":
                            Log.d("BLUETOOTH", "Ocorreu um erro durante a conexão.");
                            break;
                        case "---S":
                            Log.d("BLUETOOTH", "Conectado.");
                            break;

                        default:

                            double a = 0, b = 0;
                            String valorCrcMandado;
                            cont = cont + 1;
                            dados = dataString;

                            //funções padrao Brasileiro
                            if (dados.length() == 1) {
                                pacote[0] = (byte) (data[0] & 0xFF);
                            }

                            if ((dataString.startsWith("F"))) {
                                finalDeTeste = true;
                            }

                            if (dados.length() == 14) {
                                pacote[1] = (byte) (data[0] & 0xFF);
                                pacote[2] = (byte) (data[1] & 0xFF);
                                pacote[3] = (byte) (data[2] & 0xFF);
                                pacote[4] = (byte) (data[3] & 0xFF);
                                pacote[5] = (byte) (data[4] & 0xFF);
                                pacote[6] = (byte) (data[5] & 0xFF);
                                pacote[7] = (byte) (data[6] & 0xFF);
                                pacote[8] = (byte) (data[7] & 0xFF);
                                pacote[9] = (byte) (data[8] & 0xFF);
                                pacote[10] = (byte) (data[9] & 0xFF);
                                pacote[11] = (byte) (data[10] & 0xFF);
                                pacote[12] = (byte) (data[11] & 0xFF);
                                pacote[13] = (byte) (data[12] & 0xFF);
                                pacote[14] = (byte) (data[13] & 0xFF);

                                valorCrcMandado = String.valueOf((pacote[10]) * Math.pow(256, 3) + (pacote[11] & 0xFF) * Math.pow(256, 2) + (pacote[12] & 0xFF) * 256 + (pacote[13] & 0xFF));

                                CRC32 crc = new CRC32();
                                String pacoteDados = new String(data.length <= 9 ? data : new byte[0]);
                                crc.update(pacoteDados.getBytes()); //tem que pegar os bytes 0-9
                                String crcValorEncontrado = String.format("%08X", crc.getValue());
                                Log.d("CHECKSUM CALCULADO", crcValorEncontrado);
                                Log.d("CHECKSUM MANDADO", valorCrcMandado);

                                if (valorCrcMandado.equals(crcValorEncontrado)) {
                                    a = (pacote[2]) * Math.pow(256, 3) + (pacote[3] & 0xFF) * Math.pow(256, 2) + (pacote[4] & 0xFF) * 256 + (pacote[5] & 0xFF);
                                    b = (pacote[6] & 0xFF) * Math.pow(256, 3) + (pacote[7] & 0xFF) * Math.pow(256, 2) + (pacote[8] & 0xFF) * 256 + (pacote[9] & 0xFF);
                                    res = "  Tensão:   " + Float.toString((float) a / 1000) + " V   Corrente:  " + Float.toString((float) b / 1000) + " A \n";

                                } else {
                                    res = " ";
                                    Log.d("CHECKSUM", "Pacote Incorreto");
                                }

                            }

//                            if (dados.length() == 8) {
//                                pacote[1] = (byte) (data[0] & 0xFF);
//                                pacote[2] = (byte) (data[1] & 0xFF);
//                                pacote[3] = (byte) (data[2] & 0xFF);
//                                pacote[4] = (byte) (data[3] & 0xFF);
//                                pacote[5] = (byte) (data[4] & 0xFF);
//                                pacote[6] = (byte) (data[5] & 0xFF);
//                                pacote[7] = (byte) (data[6] & 0xFF);
//                                pacote[8] = (byte) (data[7] & 0xFF);
//
//                                Log.d("PACOTE", String.valueOf(pacote[0]));
//                                Log.d("PACOTE", String.valueOf(pacote[1]));
//                                Log.d("PACOTE", String.valueOf(pacote[2]));
//                                Log.d("PACOTE", String.valueOf(pacote[3]));
//                                Log.d("PACOTE", String.valueOf(pacote[4]));
//                                Log.d("PACOTE", String.valueOf(pacote[5]));
//                                Log.d("PACOTE", String.valueOf(pacote[6]));
//                                Log.d("PACOTE", String.valueOf(pacote[7]));
//                                Log.d("PACOTE", String.valueOf(pacote[8]));
//
//                                a = (pacote[2]) * Math.pow(256, 3) + (pacote[3] & 0xFF) * Math.pow(256, 2) + (pacote[4] & 0xFF) * 256 + (pacote[5] & 0xFF);
//                                res = " Tensão :   " + Float.toString((float) a / 1000);
//                            }



                            if (dataString.contains("M")) {


                            }



                            if (cont >= 2) {
                                res = "";
                            }

                            break;
                    }
                }
            };
        }
    };
    private TextView statusMessage;
    private TextView textSpace;
    private ThreadConexao conexao;

    public static void setAuth(Context contexto, String a) {
        Toast.makeText(contexto, a, Toast.LENGTH_SHORT).show();

    }

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
        } else {
            statusMessage.setText("Bluetooth está funcionando.");
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                statusMessage.setText("Solicitando ativação do Bluetooth...");
            } else {
                statusMessage.setText("Bluetooth Ativado.");
            }
        }
        // Fim - verificando ativação do bluetooth

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                statusMessage.setText("Bluetooth ativado.");
            } else {
                statusMessage.setText("Bluetooth não ativado.");
            }
        } else if (requestCode == SELECT_PAIRED_DEVICE || requestCode == SELECT_DISCOVERED_DEVICE) {
            if (resultCode == RESULT_OK) {
                statusMessage.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n"
                        + data.getStringExtra("btDevAddress"));

                conexao = new ThreadConexao(data.getStringExtra("btDevAddress"));
                conexao.start();
            } else {
                statusMessage.setText("Nenhum dispositivo selecionado.");
            }
        }
    }

    /**
     * @param view
     */
    public void searchPairedDevices(View view) {
        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }

    public void discoverDevices(View view) {
        Intent searchPairedDevicesIntent = new Intent(this, DiscoveredDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_DISCOVERED_DEVICE);
    }

    public void waitConnection(View view) {
        conexao = new ThreadConexao();
        conexao.start();
    }

    public void stopConnection() {
        conexao.interrupt();
    }

    public void enableVisibility(View view) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
        startActivity(discoverableIntent);
    }

    public void sendMessage(View view) {
        EditText messageBox = findViewById(R.id.editText_MessageBox);
        String messageBoxString = messageBox.getText().toString();

        byte[] pacote = new byte[10];

        if (messageBox.getText().toString().equalsIgnoreCase("i")) {
            pacote[0] = ('I' & 0xFF);
            pacote[1] = ('B' & 0xFF);
            pacote[2] = (byte) (0 & 0xFF);
            pacote[3] = (byte) (0 & 0xFF);
            pacote[4] = (byte) (90 & 0xFF);
            pacote[5] = (byte) (175 & 0xFF);
            pacote[6] = (byte) (0 & 0xFF);
            pacote[7] = (byte) (10 & 0xFF);
            pacote[8] = (byte) (0 & 0xFF);
            pacote[9] = (byte) (0 & 0xFF);
        } else if (messageBox.getText().toString().equalsIgnoreCase("c")) {
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
        }

        statusMessage.setText(Arrays.toString(pacote));
        conexao.write(pacote);
    }

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
