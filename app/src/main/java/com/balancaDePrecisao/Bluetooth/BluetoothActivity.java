package com.balancaDePrecisao.Bluetooth;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.balancaDePrecisao.MainActivity;
import com.balancaDePrecisao.R;

import java.util.Arrays;
import java.util.Set;

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
                    Bundle bundle = msg.getData();
                    byte[] data = bundle.getByteArray("data");
                    String dataString = new String(data != null ? data : new byte[0]);

                    switch (dataString) {
                        case "---N":
                            Log.d("BLUETOOTH", "Ocorreu um erro durante a conexão.");
                            break;
                        case "---S":

                            break;

                        default:


                            Log.d("DADOS TAMANHO", String.valueOf(dataString.length()));
                            Log.d("DADOS STRING", dataString);

                            if (data.length >= 1) {
                                dados = dataString;
                                if(dados!=null){
                                    MainActivity.escreverTela(dados + " g");
                                }

                            }

                            break;
                    }
                }
            };
        }
    };
    private TextView statusMessage;
    private TextView textSpace;
    private com.balancaDePrecisao.Bluetooth.ThreadConexao conexao;

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

    public static class PairedDevices extends ListActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            /*  Esse trecho não é essencial, mas dá um melhor visual à lista.
                Adiciona um título à lista de dispositivos pareados utilizando
            o layout text_header.xml.
            */
            ListView lv = getListView();
            LayoutInflater inflater = getLayoutInflater();
            View header = inflater.inflate(R.layout.text_header, lv, false);
            ((TextView) header.findViewById(R.id.textView)).setText("\nDispositivos pareados\n");
            lv.addHeaderView(header, null, false);

            /*  Usa o adaptador Bluetooth para obter uma lista de dispositivos pareados.
             */
            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

            /*  Cria um modelo para a lista e o adiciona à tela.
                Se houver dispositivos pareados, adiciona cada um à lista.
             */
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            setListAdapter(adapter);
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    adapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        }

        /*  Este método é executado quando o usuário seleciona um elemento da lista.
         */
        @Override
        protected void onListItemClick(ListView l, View v, int position, long id) {

            /*  Extrai nome e endereço a partir do conteúdo do elemento selecionado.
                Nota: position-1 é utilizado pois adicionamos um título à lista e o
            valor de position recebido pelo método é deslocado em uma unidade.
             */
            String item = (String) getListAdapter().getItem(position - 1);
            String devName = item.substring(0, item.indexOf("\n"));
            String devAddress = item.substring(item.indexOf("\n") + 1, item.length());

            /*  Utiliza um Intent para encapsular as informações de nome e endereço.
                Informa à Activity principal que tudo foi um sucesso!
                Finaliza e retorna à Activity principal.
             */
            Intent returnIntent = new Intent();
            returnIntent.putExtra("btDevName", devName);
            returnIntent.putExtra("btDevAddress", devAddress);
            setResult(RESULT_OK, returnIntent);
            finish();
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
}
