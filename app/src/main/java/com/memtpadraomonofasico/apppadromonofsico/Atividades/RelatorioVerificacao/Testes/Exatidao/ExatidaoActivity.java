package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Exatidao;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador.RegistradorActivity;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.PairedDevices;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.ThreadConexao;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.text.DecimalFormat;
import java.util.zip.CRC32;


@SuppressWarnings("ALL")
public class ExatidaoActivity extends AppCompatActivity {


    private static final int ENABLE_BLUETOOTH = 1;
    private static final int SELECT_PAIRED_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 4;
    @SuppressLint("HandlerLeak")
    private static final Handler handler = new Handler(Looper.getMainLooper()) {
    };
    private static String modeloPadraoMonofasico;
    @SuppressLint("StaticFieldLeak")
    private static EditText erroCargaNominal, erroCargaPequeno, quantidadePulsos;
    @SuppressLint("StaticFieldLeak")
    private static TextView mensagemNaTela;
    @SuppressLint("StaticFieldLeak")
    private static Chronometer cronometroTesteCargaPequena, cronometroTesteCargaNominal;
    @SuppressLint("WrongViewCast")
    private static Button botaoConectar, botaoTesteNominal, botaoTestePequeno, botaoTesteFotoCelula;
    boolean testeCargaNominalRodando = false;
    boolean testeCargaPequenaRodando = false;
    boolean testeFotoCelulaRodando = false;
    double tempoEstimadoTeste;
    private RadioButton Aprovado, NaoPossibilitaTeste, VariacaoLeitura, Reprovado;
    private String statusTestesExatidao;
    private BluetoothAdapter bluetoothAdapter;
    private ThreadConexao conexaoMKV;

    public void escreverTelaCargaPequena(final String res) {

        new Thread(new Runnable() {
            public void run() {
                if (res.startsWith("T")) {

                    cronometroTesteCargaPequena.stop(); // stop a chronometer
                    cronometroTesteCargaPequena.setText("00:00");
                }
            }
        }).start();

        handler.post(new Runnable() {
            @Override
            public void run() {

                erroCargaPequeno.setEnabled(true);
                if (!(erroCargaPequeno == null)) {


                    if (res.startsWith("T")) {

                        cronometroTesteCargaPequena.setVisibility(View.INVISIBLE);
                        cronometroTesteCargaPequena.setEnabled(false);//stop(); // stop a chronometer
                        cronometroTesteCargaPequena.setText("00:00");
                        mensagemNaTela.clearComposingText();
                        mensagemNaTela.setText(res);
                        testeCargaNominalRodando = false;
                        botaoTesteNominal.clearComposingText();
                        botaoTesteNominal.setText("Iniciar Teste de Carga Nominal");
                        testeCargaPequenaRodando = false;
                        botaoTestePequeno.clearComposingText();
                        botaoTestePequeno.setText("Iniciar Teste de Carga Pequena");
                    }

                    erroCargaPequeno.clearComposingText();
                    erroCargaPequeno.setText(res);
                }

            }
        });

    }

    public void escreverTelaCargaNominal(final String res) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                erroCargaNominal.setEnabled(true);
                if (!(erroCargaNominal == null)) {
                    if (res.startsWith("T")) {
                        cronometroTesteCargaNominal.setVisibility(View.INVISIBLE);
                        cronometroTesteCargaNominal.setEnabled(false);// stop(); // stop a chronometer
                        cronometroTesteCargaNominal.setText("00:00");
                        mensagemNaTela.clearComposingText();
                        mensagemNaTela.setText(res);
                        testeCargaNominalRodando = false;
                        botaoTesteNominal.clearComposingText();
                        botaoTesteNominal.setText("Iniciar Teste de Carga Nominal");
                        testeCargaPequenaRodando = false;
                        botaoTestePequeno.clearComposingText();
                        botaoTestePequeno.setText("Iniciar Teste de Carga Pequena");

                    }

                    erroCargaNominal.clearComposingText();
                    erroCargaNominal.setText(res);

                }
            }
        });

        new Thread(new Runnable() {
            public void run() {
                if (res.startsWith("T")) {

                    cronometroTesteCargaNominal.stop(); // stop a chronometer
                    cronometroTesteCargaNominal.setText("00:00");
                }
            }
        }).start();

    }

    public void escreverNaTela(final String res) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!(mensagemNaTela == null)) {
                    if (res.startsWith("T")) {
                        mensagemNaTela.clearComposingText();
                        mensagemNaTela.setText(res);

                        if (botaoTestePequeno != null) {
                            testeCargaPequenaRodando = false;
                            botaoTestePequeno.clearComposingText();
                            botaoTestePequeno.setText("Iniciar Teste de Carga Pequena");
                        }
                        if (botaoTesteNominal != null) {
                            testeCargaNominalRodando = false;
                            botaoTesteNominal.clearComposingText();
                            botaoTesteNominal.setText("Iniciar Teste de Carga Nominal");
                        }
                    } else {
                        mensagemNaTela.clearComposingText();
                        mensagemNaTela.setText(res);
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exatidao);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();

        mensagemNaTela = findViewById(R.id.textView7);
        mensagemNaTela.setText("  ");
        erroCargaNominal = findViewById(R.id.CargaNominalErro);
        erroCargaNominal.setEnabled(false);
        erroCargaPequeno = findViewById(R.id.CargaPequenaErro);
        erroCargaPequeno.setEnabled(false);
        Aprovado = findViewById(R.id.tampasolidarizada);
        NaoPossibilitaTeste = findViewById(R.id.sinaisCarbonizacao);
        VariacaoLeitura = findViewById(R.id.VariacaoLeitura);
        Reprovado = findViewById(R.id.Reprovado);
        botaoConectar = findViewById(R.id.buttonConectarDispositivo);
        botaoTesteNominal = findViewById(R.id.button2);
        botaoTestePequeno = findViewById(R.id.button3);
        quantidadePulsos = findViewById(R.id.QuantidadePulsos);

        cronometroTesteCargaPequena = new Chronometer(this);
        cronometroTesteCargaPequena = findViewById(R.id.CronometroPequeno);
        cronometroTesteCargaPequena.setVisibility(View.INVISIBLE);

        cronometroTesteCargaNominal = new Chronometer(this);
        cronometroTesteCargaNominal = (Chronometer) findViewById(R.id.CronometroNominal); // initiate a chronometer
        cronometroTesteCargaNominal.setVisibility(View.INVISIBLE);

        modeloPadraoMonofasico = Hawk.get("ModeloPadrao");
        Log.d("PADRAO", modeloPadraoMonofasico);
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
                conectarDispositivo(view);
            }
        });


        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase7);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Hawk.delete("CargaNominalErroConformidade");
                Hawk.delete("CargaPequenaErroConformidade");
                Hawk.delete("statusTestesExatidao");


                if (Aprovado.isChecked()) {
                    statusTestesExatidao = "Aprovado";

                }
                if (NaoPossibilitaTeste.isChecked()) {
                    statusTestesExatidao = "Não Possibilita Teste";

                }
                if (VariacaoLeitura.isChecked()) {
                    statusTestesExatidao = "Variação de Leitura";

                }
                if (Reprovado.isChecked()) {
                    statusTestesExatidao = "Reprovado";
                }

                if ((!Aprovado.isChecked()) && (!NaoPossibilitaTeste.isChecked()) && (!VariacaoLeitura.isChecked()) && (!Reprovado.isChecked())) {
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Não existe opção de status marcado. ", Toast.LENGTH_LONG).show();

                } else {
                    if ((NaoPossibilitaTeste.isChecked())) {
                        Hawk.put("CargaNominalErroConformidade", String.valueOf(erroCargaNominal.getText()));
                        Hawk.put("CargaPequenaErroConformidade", String.valueOf(erroCargaPequeno.getText()));
                        Hawk.put("statusTestesExatidao", statusTestesExatidao);

                        if (conexaoMKV != null) {
                            conexaoMKV.interrupt();
                        }
                        bluetoothAdapter.disable();
                        conexaoMKV = null;
                        abrirRegistrador();

                    } else {
                        Hawk.put("CargaNominalErroConformidade", String.valueOf(erroCargaNominal.getText()));
                        Hawk.put("CargaPequenaErroConformidade", String.valueOf(erroCargaPequeno.getText()));
                        Hawk.put("statusTestesExatidao", statusTestesExatidao);

                        if (conexaoMKV != null) {
                            conexaoMKV.interrupt();
                        }
                        bluetoothAdapter.disable();
                        abrirRegistrador();
                    }
                }
            }
        });
    }

    private void abrirRegistrador() {
        Intent intent = new Intent(this, RegistradorActivity.class);
        startActivity(intent);
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
                String macAddress = data.getStringExtra("btDevAddress");

                conexaoMKV = new ThreadConexao(macAddress);
                conexaoMKV.start();
                if (conexaoMKV.isAlive()) {
                    mensagemNaTela.setText("Conexao finalizada com:" + data.getStringExtra("btDevName") + "\n Verifique o LED de conexão");
                }
            } else {
                mensagemNaTela.setText("Nenhum dispositivo selecionado.");
            }
        }
    }

    public void onCheckboxClicked(View view) {

        Aprovado = findViewById(R.id.tampasolidarizada);
        NaoPossibilitaTeste = findViewById(R.id.sinaisCarbonizacao);
        VariacaoLeitura = findViewById(R.id.VariacaoLeitura);
        Reprovado = findViewById(R.id.Reprovado);

        switch (view.getId()) {
            case R.id.tampasolidarizada:
                NaoPossibilitaTeste.setChecked(false);
                VariacaoLeitura.setChecked(false);
                Reprovado.setChecked(false);
                break;

            case R.id.sinaisCarbonizacao:
                Aprovado.setChecked(false);
                VariacaoLeitura.setChecked(false);
                Reprovado.setChecked(false);
                break;

            case R.id.VariacaoLeitura:
                Aprovado.setChecked(false);
                NaoPossibilitaTeste.setChecked(false);
                Reprovado.setChecked(false);
                break;

            case R.id.Reprovado:
                Aprovado.setChecked(false);
                NaoPossibilitaTeste.setChecked(false);
                VariacaoLeitura.setChecked(false);
                break;

        }
    }

    public void pararCronometro(String res) {
        if (res.startsWith("T")) {
            cronometroTesteCargaNominal.setVisibility(View.INVISIBLE);
            cronometroTesteCargaNominal.setEnabled(false);// stop(); // stop a chronometer
            cronometroTesteCargaNominal.setText("00:00");
        }
    }

    //----------------Bluetooth
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

    private void conectarDispositivo(View view) {

        if (modeloPadraoMonofasico.startsWith("MKV")) {
            if (conexaoMKV != null) {
                Toast.makeText(getApplicationContext(), "Dispositivo já conectado.", Toast.LENGTH_LONG).show();

            }
            Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
            startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);

        } else if (modeloPadraoMonofasico.startsWith("Chin")) {
            //TODO - Fazer conexaoc om padrao chinês
        }


    }


    //------------------Testes
    public void testeCargaNominal(View view) {

        cronometroTesteCargaNominal.setVisibility(View.VISIBLE);
        cronometroTesteCargaNominal.setBase(SystemClock.elapsedRealtime());
        cronometroTesteCargaNominal.start(); // start a chronometer

        int pulsos = 5;

        if (quantidadePulsos.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "O botaoTesteFotoCelula vai ser realizado com 5 pulsos!", Toast.LENGTH_LONG).show();
            pulsos = 5;

        } else {
            pulsos = Integer.parseInt(quantidadePulsos.getText().toString());
        }

        float tensao = Float.parseFloat((String) Hawk.get("TensaoNominalMedidor"));
        float corrente = Float.parseFloat((String) Hawk.get("CorrenteNominalMedidor"));
        float kdMedidor = Float.parseFloat((String) Hawk.get("KdKeMedidor"));
        float FP = 1;

        tempoEstimadoTeste = ((((3600 * pulsos * kdMedidor) / (tensao * corrente * FP))) / 10);
        DecimalFormat df = new DecimalFormat("0.##");
        String dx = df.format(tempoEstimadoTeste);

        if (conexaoMKV == null) {
            Toast.makeText(getApplicationContext(), "O botaoTesteFotoCelula não pode ser iniciado/parado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (!testeCargaNominalRodando) {
                testeCargaNominalRodando = true;
                botaoTesteNominal.clearComposingText();
                botaoTesteNominal.setText("Cancelar Teste de Carga Nominal");
                //TODO - conferir modelo do padrão (se é chines ou Brasileiro)
                cargaNominalPadraoMKV(view);
                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste sendo iniciado...  \n " +
                        "Estimativa: " + dx + " minuto(s)");
                tempoEstimadoTeste = 0;

            } else {
                testeCargaNominalRodando = false;
                botaoTesteNominal.clearComposingText();
                botaoTesteNominal.setText("Iniciar Teste de Carga Nominal");
                //TODO - conferir modelo do padrão (se é chines ou Brasileiro)
                pararTestePadraoMKV(view);
                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste Cancelado!");
            }
        }
    }

    public void testeCargaPequena(View view) {

        cronometroTesteCargaPequena.setVisibility(View.VISIBLE);
        cronometroTesteCargaPequena.setBase(SystemClock.elapsedRealtime());
        cronometroTesteCargaPequena.start(); // start a chronometer

        int pulsos = 5;
        if (quantidadePulsos.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "O botaoTesteFotoCelula vai ser realizado com 5 pulsos!", Toast.LENGTH_LONG).show();
            pulsos = 5;
        } else {
            pulsos = Integer.parseInt(quantidadePulsos.getText().toString());
        }
        float tensao = Float.parseFloat((String) Hawk.get("TensaoNominalMedidor"));
        float corrente = Float.parseFloat((String) Hawk.get("CorrenteNominalMedidor"));
        float kdMedidor = Float.parseFloat((String) Hawk.get("KdKeMedidor"));
        float FP = 1;

        tempoEstimadoTeste = ((((3600 * pulsos * kdMedidor) / (tensao * corrente * FP))) / 10);

        DecimalFormat df = new DecimalFormat("0.##");
        String dx = df.format(tempoEstimadoTeste);

        if (conexaoMKV == null) {
            Toast.makeText(getApplicationContext(), "O botaoTesteFotoCelula não pode ser iniciado/parado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (!testeCargaPequenaRodando) {
                testeCargaPequenaRodando = true;
                botaoTestePequeno.clearComposingText();
                botaoTestePequeno.setText("Cancelar Teste de Carga Pequena");
                //TODO - conferir modelo do padrão (se é chines ou Brasileiro)
                cargaPequenaPadraoMKV(view);
                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste sendo iniciado... \n" +
                        "Estimativa: " + dx + " minuto(s) ");
                tempoEstimadoTeste = 0;

            } else {
                testeCargaPequenaRodando = false;
                botaoTestePequeno.clearComposingText();
                botaoTestePequeno.setText("Iniciar Teste de Carga Pequena");
                //TODO - conferir modelo do padrão (se é chines ou Brasileiro)
                pararTestePadraoMKV(view);
                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste Cancelado!");
            }
        }
    }

    public void testeFotoCelula(View view) {

        if (conexaoMKV == null) {
            Toast.makeText(getApplicationContext(), "O botaoTesteFotoCelula da FotoCélula não pode ser iniciado/parado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {
            botaoTesteFotoCelula = findViewById(R.id.buttonTesteFotoCelula);

            if (!testeFotoCelulaRodando) {
                testeFotoCelulaRodando = true;
                botaoTesteFotoCelula.clearComposingText();
                botaoTesteFotoCelula.setText("Cancelar Teste da FotoCélula");

                if (modeloPadraoMonofasico.startsWith("MKV")) {
                    fotoCelulaPadraoMKV(view);

                } else if (modeloPadraoMonofasico.startsWith("Chin")) {
                    //TODO - Fazer função de botaoTesteFotoCelula de foto célula para o padrão chinês
                }

                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste da FotoCélula sendo iniciado...");

            } else {
                testeFotoCelulaRodando = false;
                botaoTesteFotoCelula.clearComposingText();
                botaoTesteFotoCelula.setText("Iniciar Teste da FotoCélula");

                if (modeloPadraoMonofasico.startsWith("MKV")) {
                    pararTestePadraoMKV(view);

                } else if (modeloPadraoMonofasico.startsWith("Chin")) {
                    //TODO - Fazer função de para botaoTesteFotoCelula para o padrão chinês
                }

                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste da FotoCélula Cancelado!");
            }
        }
    }

    //--------------Funções Padrao MKV
    //TODO - Fazer função para conferir número de série do padrão MKV
    private void pararTestePadraoMKV(View view) {

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

        conexaoMKV.write(pacote);

        cronometroTesteCargaPequena.stop();
        cronometroTesteCargaPequena.setText("00:00");
        cronometroTesteCargaNominal.stop();
        cronometroTesteCargaNominal.setText("00:00");


    }

    public void cargaNominalPadraoMKV(View view) {

        //TODO - função para pedir número de série do padrão
        //TODO - comparar com o número de série mandado pelo botaoTesteFotoCelula anterior

        if (conexaoMKV == null) {
            Toast.makeText(getApplicationContext(), "O botaoTesteFotoCelula não pode ser inicializado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {
            if (conexaoMKV.isAlive()) {
                mensagemNaTela.setText("Conectado!");
            }

            byte[] pacote = new byte[15];
            float kdMedidor = Float.parseFloat((String) Hawk.get("KdKeMedidor"));

            byte[] bytes = new byte[4];
            float valorMultiplicado = (float) (kdMedidor * 1000000);
            bytes[0] = (byte) (valorMultiplicado / (Math.pow(256, 3)));
            bytes[1] = (byte) ((valorMultiplicado - (bytes[0] * (Math.pow(256, 3)))) / Math.pow(256, 2));
            bytes[2] = (byte) ((valorMultiplicado - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))))) / Math.pow(256, 1));
            bytes[3] = (byte) ((valorMultiplicado - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))) + (bytes[2] * Math.pow(256, 1)))));

            pacote[0] = ('I' & 0xFF);
            pacote[1] = ('N' & 0xFF);
            pacote[2] = (byte) (bytes[0] & 0xFF);
            pacote[3] = (byte) (bytes[1] & 0xFF);
            pacote[4] = (byte) (bytes[2] & 0xFF);
            pacote[5] = (byte) (bytes[3] & 0xFF);


            if (quantidadePulsos.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "O botaoTesteFotoCelula foi configurado para 5 pulsos", Toast.LENGTH_LONG).show();
                quantidadePulsos.setText("5");
                pacote[6] = (byte) (0 & 0xFF);
                pacote[7] = (byte) (5 & 0xFF);
                pacote[8] = (byte) (0 & 0xFF);
                pacote[9] = (byte) (0 & 0xFF);


            } else {
                float pulsos = Float.parseFloat((quantidadePulsos.getText().toString()));
                bytes[0] = (byte) (pulsos / (Math.pow(256, 1)));
                bytes[1] = (byte) (pulsos - (bytes[0] * (Math.pow(256, 1))));
                bytes[2] = 0;
                bytes[3] = 0;

                pacote[6] = (byte) (bytes[0] & 0xFF);
                pacote[7] = (byte) (bytes[1] & 0xFF);
                pacote[8] = (byte) (bytes[2] & 0xFF);
                pacote[9] = (byte) (bytes[3] & 0xFF);
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

            conexaoMKV.write(pacote);
        }


    }

    public void cargaPequenaPadraoMKV(View view) {

        //TODO - função para pedir número de série do padrão
        //TODO - comparar com o número de série mandado pelo botaoTesteFotoCelula anterior

        if (conexaoMKV == null) {
            Toast.makeText(getApplicationContext(), "O botaoTesteFotoCelula não pode ser inicializado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            byte[] pacote = new byte[15];
            float kdMedidor = Float.parseFloat((String) Hawk.get("KdKeMedidor"));

            byte[] bytes = new byte[4];
            float valorMultiplicado = (float) (kdMedidor * 1000000);
            bytes[0] = (byte) (valorMultiplicado / (Math.pow(256, 3)));
            bytes[1] = (byte) ((valorMultiplicado - (bytes[0] * (Math.pow(256, 3)))) / Math.pow(256, 2));
            bytes[2] = (byte) ((valorMultiplicado - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))))) / Math.pow(256, 1));
            bytes[3] = (byte) ((valorMultiplicado - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))) + (bytes[2] * Math.pow(256, 1)))));

            pacote[0] = ('I' & 0xFF);
            pacote[1] = ('B' & 0xFF);
            pacote[2] = (byte) (bytes[0] & 0xFF);
            pacote[3] = (byte) (bytes[1] & 0xFF);
            pacote[4] = (byte) (bytes[2] & 0xFF);
            pacote[5] = (byte) (bytes[3] & 0xFF);


            if (quantidadePulsos.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "O botaoTesteFotoCelula foi configurado para 5 pulsos", Toast.LENGTH_LONG).show();
                quantidadePulsos.setText("5");
                pacote[6] = (byte) (0 & 0xFF);
                pacote[7] = (byte) (5 & 0xFF);
                pacote[8] = (byte) (0 & 0xFF);
                pacote[9] = (byte) (0 & 0xFF);


            } else {
                float pulsos = Float.parseFloat((quantidadePulsos.getText().toString()));
                bytes[0] = (byte) (pulsos / (Math.pow(256, 1)));
                bytes[1] = (byte) (pulsos - (bytes[0] * (Math.pow(256, 1))));
                bytes[2] = 0;
                bytes[3] = 0;

                pacote[6] = (byte) (bytes[0] & 0xFF);
                pacote[7] = (byte) (bytes[1] & 0xFF);
                pacote[8] = (byte) (bytes[2] & 0xFF);
                pacote[9] = (byte) (bytes[3] & 0xFF);
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

            conexaoMKV.write(pacote);
        }


    }

    private void fotoCelulaPadraoMKV(View view) {

        //TODO - função para pedir número de série do padrão
        //TODO - comparar com o número de série mandado pelo botaoTesteFotoCelula anterior

        if (conexaoMKV == null) {
            Toast.makeText(getApplicationContext(), "O botaoTesteFotoCelula não pode ser inicializado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (conexaoMKV != null) {
                mensagemNaTela.setText("O botaoTesteFotoCelula de FotoCélula vai ser iniciado...");
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

            conexaoMKV.write(pacote);
        }
    }


    //--------------Funções Padrão Chinês
    //TODO - Funçao para pegar número de série do Padrão
    //TODO - Função para teste de Foto Célula do padrão
    //TODO - Função para teste de Carga Nominal
    //TODO - Função para teste de Carga Pequena
    //TODO - Função para para testes

}

