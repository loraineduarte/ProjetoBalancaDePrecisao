package com.memtpadraomonofasico.balancaDePrecisao.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.memtpadraomonofasico.balancaDePrecisao.Bluetooth.PairedDevices;
import com.memtpadraomonofasico.balancaDePrecisao.Bluetooth.BluetoothServicoChamadaPelaAtividade;
import com.memtpadraomonofasico.balancaDePrecisao.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.sql.Time;
import java.util.zip.CRC32;

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
    private static BluetoothServicoChamadaPelaAtividade conexaoPadraoMKV;
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private BluetoothAdapter bluetoothAdapter = null;


    private boolean mConnected = false;


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
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

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

                conexaoPadraoMKV = new BluetoothServicoChamadaPelaAtividade(macAddress);
                conexaoPadraoMKV.start();

                if (conexaoPadraoMKV != null) {
                    mensagemNaTela.setText("Conexao finalizada com: " + data.getStringExtra("btDevName") + "\n Verifique o LED de conexão");
                }
            } else {
                mensagemNaTela.setText("Nenhum dispositivo selecionado.");
            }

        }
    }

    private void abrirInspecaoConformidade() {
//        Intent intent = new Intent(this, ExatidaoActivity.class);
//        startActivity(intent);
    }


    //-----------Funções Bluetooth
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
            if (conexaoPadraoMKV != null) {
                Toast.makeText(getApplicationContext(), "Dispositivo já conectado.", Toast.LENGTH_LONG).show();
            }
            Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
            startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);

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



    //-------------------- Funções Gerais da Atividade
    public void TesteMarchaEmVazio(View view) {

        if ((conexaoPadraoMKV == null)) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser iniciado/parado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            botaoTesteMarchaVazio = findViewById(R.id.buttonAplicarTensao);

            if (!testeMarchaVazioRodando) {
                testeMarchaVazioRodando = true;
                botaoTesteMarchaVazio.clearComposingText();
                botaoTesteMarchaVazio.setText("Cancelar Teste de Marcha em Vazio");
                if (modeloPadrao.startsWith("MKV")) {
                    Log.d("PADRAO", modeloPadrao);
                    marchaVazioPadrãoMKV();

                }
                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste Iniciado!");

            } else {
                testeMarchaVazioRodando = false;
                botaoTesteMarchaVazio.clearComposingText();
                botaoTesteMarchaVazio.setText("Iniciar Teste de Marcha em Vazio");
                if (modeloPadrao.startsWith("MKV")) {
                    Log.d("PADRAO", modeloPadrao);
                    pararTestesPadrãoMKV();

                }

                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste Cancelado!");
            }
        }
    }

    public void TesteFotoCelula(View view) {
        if ((conexaoPadraoMKV == null) ) {
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
                    fotoCelulaPadraoMKV();

                }

                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste de FotoCélula Iniciado!");

            } else {
                testeFotoCelulaRodando = false;
                botaoTesteFotoCelula.clearComposingText();
                botaoTesteFotoCelula.setText("Iniciar Teste de FotoCélula");

                if (modeloPadrao.startsWith("MKV")) {
                    Log.d("PADRAO", modeloPadrao);
                    pararTestesPadrãoMKV();

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


    //-------------------Testes Padrao Brasileiro
    //TODO - função de pedir o numero de série do padrao

    private void marchaVazioPadrãoMKV() {

        //TODO Função para pegar o numero de série do medidor para ser comparado nos próximos testes

        byte[] pacote = new byte[15];
        float kdMedidor = Float.parseFloat((String) Hawk.get("KdKeMedidor"));

        byte[] bytesKdKe = new byte[4];
        int valorMultiplicado = (int) (kdMedidor * 1e6);

        bytesKdKe[0] = (byte) (valorMultiplicado / (Math.pow(256, 3)));
        bytesKdKe[1] = (byte) ((valorMultiplicado - (bytesKdKe[0] * (Math.pow(256, 3)))) / Math.pow(256, 2));
        bytesKdKe[2] = (byte) ((valorMultiplicado - ((bytesKdKe[0] * (Math.pow(256, 3))) + (bytesKdKe[1] * (Math.pow(256, 2))))) / Math.pow(256, 1));
        bytesKdKe[3] = (byte) ((valorMultiplicado - ((bytesKdKe[0] * (Math.pow(256, 3))) + (bytesKdKe[1] * (Math.pow(256, 2))) + (bytesKdKe[2] * Math.pow(256, 1)))));

        pacote[0] = ('I' & 0xFF);
        pacote[1] = ('M' & 0xFF);
        pacote[2] = (byte) (bytesKdKe[0] & 0xFF);
        pacote[3] = (byte) (bytesKdKe[1] & 0xFF);
        pacote[4] = (byte) (bytesKdKe[2] & 0xFF);
        pacote[5] = (byte) (bytesKdKe[3] & 0xFF);

        String time = tempoReprovado.getText().toString();

        if (time.equals("")) {
            tempoReprovado.setText("1");
            Toast.makeText(getApplicationContext(), "Tempo de teste ajustado para 1 minuto... ", Toast.LENGTH_LONG).show();
            pacote[6] = (byte) (0 & 0xFF);
            pacote[7] = (byte) (0 & 0xFF);
            pacote[8] = (byte) (0 & 0xFF);
            pacote[9] = (byte) (60 & 0xFF);

        } else {
            byte[] bytesTempo = new byte[4];
            float tempo = (Float.parseFloat(tempoReprovado.getText().toString())) * 60;
            bytesTempo[0] = (byte) (tempo / (Math.pow(256, 3)));
            bytesTempo[1] = (byte) ((tempo - (bytesTempo[0] * (Math.pow(256, 3)))) / Math.pow(256, 2));
            bytesTempo[2] = (byte) ((tempo - ((bytesTempo[0] * (Math.pow(256, 3))) + (bytesTempo[1] * (Math.pow(256, 2))))) / Math.pow(256, 1));
            bytesTempo[3] = (byte) ((tempo - ((bytesTempo[0] * (Math.pow(256, 3))) + (bytesTempo[1] * (Math.pow(256, 2))) + (bytesTempo[2] * Math.pow(256, 1)))));

            pacote[6] = (byte) (bytesTempo[0] & 0xFF);
            pacote[7] = (byte) (bytesTempo[1] & 0xFF);
            pacote[8] = (byte) (bytesTempo[2] & 0xFF);
            pacote[9] = (byte) (bytesTempo[3] & 0xFF);

        }

        //Lógica nova para o CRC32
        String dataString = new String(pacote != null ? pacote : new byte[0]);
        Log.d("MANDADO PARA FAZER O CHECKSUM", dataString);
        CRC32 crc = new CRC32();
        crc.update(dataString.getBytes());
        int checkSumEncontrado = Integer.parseInt(String.format("%08X", crc.getValue()));
        Log.d("CHECKSUM", String.valueOf(checkSumEncontrado));

        byte[] bytesCheckSum = new byte[4];
        bytesCheckSum[0] = (byte) (checkSumEncontrado / (Math.pow(256, 3)));
        bytesCheckSum[1] = (byte) ((checkSumEncontrado - (bytesCheckSum[0] * (Math.pow(256, 3)))) / Math.pow(256, 2));
        bytesCheckSum[2] = (byte) ((checkSumEncontrado - ((bytesCheckSum[0] * (Math.pow(256, 3))) + (bytesCheckSum[1] * (Math.pow(256, 2))))) / Math.pow(256, 1));
        bytesCheckSum[3] = (byte) ((checkSumEncontrado - ((bytesCheckSum[0] * (Math.pow(256, 3))) + (bytesCheckSum[1] * (Math.pow(256, 2))) + (bytesCheckSum[2] * Math.pow(256, 1)))));

        pacote[10] = (byte) (bytesCheckSum[0] & 0xFF);
        pacote[11] = (byte) (bytesCheckSum[1] & 0xFF);
        pacote[12] = (byte) (bytesCheckSum[2] & 0xFF);
        pacote[13] = (byte) (bytesCheckSum[3] & 0xFF);
        pacote[14] = ('Z' & 0xFF);

        conexaoPadraoMKV.write(pacote);


    }

    public void fotoCelulaPadraoMKV() {

        //TODO Função para pegar o numero de série do medidor para ser comparado nos próximos testes
        if (conexaoPadraoMKV == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser inicializado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (conexaoPadraoMKV != null) {
                mensagemNaTela.setText("O teste de FotoCélula vai ser iniciado...");
            }

            byte[] pacote = new byte[15];
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

            //Lógica nova para o CRC32
            String dataString = new String(pacote != null ? pacote : new byte[0]);
            Log.d("MANDADO PARA FAZER O CHECKSUM", dataString);
            CRC32 crc = new CRC32();
            crc.update(dataString.getBytes());
            int checkSumEncontrado = Integer.parseInt(String.format("%08X", crc.getValue()));
            Log.d("CHECKSUM", String.valueOf(checkSumEncontrado));

            byte[] bytesCheckSum = new byte[4];
            bytesCheckSum[0] = (byte) (checkSumEncontrado / (Math.pow(256, 3)));
            bytesCheckSum[1] = (byte) ((checkSumEncontrado - (bytesCheckSum[0] * (Math.pow(256, 3)))) / Math.pow(256, 2));
            bytesCheckSum[2] = (byte) ((checkSumEncontrado - ((bytesCheckSum[0] * (Math.pow(256, 3))) + (bytesCheckSum[1] * (Math.pow(256, 2))))) / Math.pow(256, 1));
            bytesCheckSum[3] = (byte) ((checkSumEncontrado - ((bytesCheckSum[0] * (Math.pow(256, 3))) + (bytesCheckSum[1] * (Math.pow(256, 2))) + (bytesCheckSum[2] * Math.pow(256, 1)))));

            pacote[10] = (byte) (bytesCheckSum[0] & 0xFF);
            pacote[11] = (byte) (bytesCheckSum[1] & 0xFF);
            pacote[12] = (byte) (bytesCheckSum[2] & 0xFF);
            pacote[13] = (byte) (bytesCheckSum[3] & 0xFF);
            pacote[14] = ('Z' & 0xFF);

            conexaoPadraoMKV.write(pacote);
        }
    }

    private void pararTestesPadrãoMKV() {

        byte[] pacote = new byte[15];

        pacote[0] = ('P' & 0xFF);
        pacote[1] = (byte) (0 & 0xFF);
        pacote[2] = (byte) (0 & 0xFF);
        pacote[3] = (byte) (0 & 0xFF);
        pacote[4] = (byte) (0 & 0xFF);
        pacote[5] = (byte) (0 & 0xFF);
        pacote[6] = (byte) (0 & 0xFF);
        pacote[7] = (byte) (0 & 0xFF);
        pacote[8] = (byte) (0 & 0xFF);
        pacote[9] = (byte) (0 & 0xFF);

        //Lógica nova para o CRC32
        String dataString = new String(pacote != null ? pacote : new byte[0]);
        Log.d("MANDADO PARA FAZER O CHECKSUM", dataString);
        CRC32 crc = new CRC32();
        crc.update(dataString.getBytes());
        int checkSumEncontrado = Integer.parseInt(String.format("%08X", crc.getValue()));
        Log.d("CHECKSUM", String.valueOf(checkSumEncontrado));

        byte[] bytesCheckSum = new byte[4];
        bytesCheckSum[0] = (byte) (checkSumEncontrado / (Math.pow(256, 3)));
        bytesCheckSum[1] = (byte) ((checkSumEncontrado - (bytesCheckSum[0] * (Math.pow(256, 3)))) / Math.pow(256, 2));
        bytesCheckSum[2] = (byte) ((checkSumEncontrado - ((bytesCheckSum[0] * (Math.pow(256, 3))) + (bytesCheckSum[1] * (Math.pow(256, 2))))) / Math.pow(256, 1));
        bytesCheckSum[3] = (byte) ((checkSumEncontrado - ((bytesCheckSum[0] * (Math.pow(256, 3))) + (bytesCheckSum[1] * (Math.pow(256, 2))) + (bytesCheckSum[2] * Math.pow(256, 1)))));

        pacote[10] = (byte) (bytesCheckSum[0] & 0xFF);
        pacote[11] = (byte) (bytesCheckSum[1] & 0xFF);
        pacote[12] = (byte) (bytesCheckSum[2] & 0xFF);
        pacote[13] = (byte) (bytesCheckSum[3] & 0xFF);
        pacote[14] = ('Z' & 0xFF);

        conexaoPadraoMKV.write(pacote);


    }

    //----------------------Testes padrão chinês







}





