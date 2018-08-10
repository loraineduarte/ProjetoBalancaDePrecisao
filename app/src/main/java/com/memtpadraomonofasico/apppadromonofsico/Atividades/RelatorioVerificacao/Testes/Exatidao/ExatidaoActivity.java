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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;


@SuppressWarnings("ALL")
public class ExatidaoActivity extends AppCompatActivity {

    private static final int ENABLE_BLUETOOTH = 1;
    private static final int SELECT_PAIRED_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 4;
    @SuppressLint("HandlerLeak")
    private static final Handler handlerInspecaoConformidade = new Handler(Looper.getMainLooper()) {
    };
    static int hours, minutes, seconds;
    @SuppressLint("StaticFieldLeak")
    private static EditText cargaNominalErro, cargaPequenaErro;
    @SuppressLint("StaticFieldLeak")
    private static TextView textMessageInspecaoConformidade;
    @SuppressLint("StaticFieldLeak")
    private static EditText quantidadePulsos;
    @SuppressLint("StaticFieldLeak")
    private static Chronometer cronometroPequeno;
    @SuppressLint("StaticFieldLeak")
    private static Chronometer cronometroNominal;
    boolean testeCargaNominalComecou = false;
    boolean testeCargaPequenaComecou = false;
    boolean testeFCComecou = false;
    double tempoTeste;
    long tempoInicio, tempoCorrendo;
    Instant iInicial;
    @SuppressLint("WrongViewCast")
    private
    Button conectar;
    private RadioButton Aprovado, NaoPossibilitaTeste, VariacaoLeitura, Reprovado;
    private String statusConformidade;
    private BluetoothAdapter mBluetoothAdapter;
    private ThreadConexao conexao;
    private Button testeNominal, testePequeno, teste;

    public void escreverTelaCargaPequena(final String res) {

        new Thread(new Runnable() {
            public void run() {
                if (res.startsWith("T")) {

                    cronometroPequeno.stop(); // stop a chronometer
                    cronometroPequeno.setText("00:00");
                }
            }
        }).start();

        handlerInspecaoConformidade.post(new Runnable() {
            @Override
            public void run() {

                cargaPequenaErro.setEnabled(true);
                if (!(cargaPequenaErro == null)) {


                    if (res.startsWith("T")) {

                        cronometroPequeno.setVisibility(View.INVISIBLE);
                        cronometroPequeno.setEnabled(false);//stop(); // stop a chronometer
                        cronometroPequeno.setText("00:00");

                        textMessageInspecaoConformidade.clearComposingText();
                        textMessageInspecaoConformidade.setText(res);

                        testeCargaNominalComecou = false;
                        testeNominal.clearComposingText();
                        testeNominal.setText("Iniciar Teste de Carga Nominal");
                        testeCargaPequenaComecou = false;
                        testePequeno.clearComposingText();
                        testePequeno.setText("Iniciar Teste de Carga Pequena");

                    }

                    cargaPequenaErro.clearComposingText();
                    cargaPequenaErro.setText(res);
                }

            }
        });

    }

    public void escreverTelaCargaNominal(final String res) {


        handlerInspecaoConformidade.post(new Runnable() {
            @Override
            public void run() {

                cargaNominalErro.setEnabled(true);
                if (!(cargaNominalErro == null)) {
                    if (res.startsWith("T")) {

                        cronometroNominal.setVisibility(View.INVISIBLE);
                        cronometroNominal.setEnabled(false);// stop(); // stop a chronometer
                        cronometroNominal.setText("00:00");


                        textMessageInspecaoConformidade.clearComposingText();
                        textMessageInspecaoConformidade.setText(res);
                        testeCargaNominalComecou = false;
                        testeNominal.clearComposingText();
                        testeNominal.setText("Iniciar Teste de Carga Nominal");
                        testeCargaPequenaComecou = false;
                        testePequeno.clearComposingText();
                        testePequeno.setText("Iniciar Teste de Carga Pequena");

                    }

                    cargaNominalErro.clearComposingText();
                    cargaNominalErro.setText(res);

                }
            }
        });

        new Thread(new Runnable() {
            public void run() {
                if (res.startsWith("T")) {

                    cronometroNominal.stop(); // stop a chronometer
                    cronometroNominal.setText("00:00");
                }
            }
        }).start();

    }

    public void escreverStatusTestesExatidao(final String res) {
        handlerInspecaoConformidade.post(new Runnable() {
            @Override
            public void run() {
                if (!(textMessageInspecaoConformidade == null)) {
                    if (res.startsWith("T")) {
                        textMessageInspecaoConformidade.clearComposingText();
                        textMessageInspecaoConformidade.setText(res);

                        if (testePequeno != null) {
                            testeCargaPequenaComecou = false;
                            testePequeno.clearComposingText();
                            testePequeno.setText("Iniciar Teste de Carga Pequena");
                        }
                        if (testeNominal != null) {
                            testeCargaNominalComecou = false;
                            testeNominal.clearComposingText();
                            testeNominal.setText("Iniciar Teste de Carga Nominal");
                        }


                    } else {

//                        long total = System.currentTimeMillis();
//                        tempoCorrendo = ((((total - tempoInicio)%1000)%3600) %60) ;
//                        long hours, minutes, seconds;
//                        hours = tempoCorrendo / 3600;
//                        tempoCorrendo = tempoCorrendo - (hours * 3600);
//                        minutes = tempoCorrendo / 60;
//                        tempoCorrendo = tempoCorrendo - (minutes * 60);
//                        seconds = tempoCorrendo;
//                        Log.d("TEMPO", ( minutes + " minute(s) " + seconds + " second(s)"));
//                        Log.d("TEMPO CORRIDO", String.valueOf(tempoCorrendo));


                        textMessageInspecaoConformidade.clearComposingText();
                        textMessageInspecaoConformidade.setText(res);
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

        textMessageInspecaoConformidade = findViewById(R.id.textView7);
        textMessageInspecaoConformidade.setText("  ");
        cargaNominalErro = findViewById(R.id.CargaNominalErro);
        cargaNominalErro.setEnabled(false);
        cargaPequenaErro = findViewById(R.id.CargaPequenaErro);
        cargaPequenaErro.setEnabled(false);
        Aprovado = findViewById(R.id.tampasolidarizada);
        NaoPossibilitaTeste = findViewById(R.id.sinaisCarbonizacao);
        VariacaoLeitura = findViewById(R.id.VariacaoLeitura);
        Reprovado = findViewById(R.id.Reprovado);
        conectar = findViewById(R.id.buttonConectarDispositivo);
        testeNominal = findViewById(R.id.button2);
        testePequeno = findViewById(R.id.button3);
        quantidadePulsos = findViewById(R.id.QuantidadePulsos);


        cronometroPequeno = new Chronometer(this);
        cronometroPequeno = findViewById(R.id.CronometroPequeno);
        cronometroPequeno.setVisibility(View.INVISIBLE);

        cronometroNominal = new Chronometer(this);
        cronometroNominal = (Chronometer) findViewById(R.id.CronometroNominal); // initiate a chronometer
        cronometroNominal.setVisibility(View.INVISIBLE);

        ativarBluetooth();

        conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (mBluetoothAdapter == null) {
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
                Hawk.delete("statusConformidade");


                if (Aprovado.isChecked()) {
                    statusConformidade = "Aprovado";

                }
                if (NaoPossibilitaTeste.isChecked()) {
                    statusConformidade = "Não Possibilita Teste";

                }
                if (VariacaoLeitura.isChecked()) {
                    statusConformidade = "Variação de Leitura";

                }
                if (Reprovado.isChecked()) {
                    statusConformidade = "Reprovado";
                }

                if ((!Aprovado.isChecked()) && (!NaoPossibilitaTeste.isChecked()) && (!VariacaoLeitura.isChecked()) && (!Reprovado.isChecked())) {
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Não existe opção de status marcado. ", Toast.LENGTH_LONG).show();

                } else {
                    if ((NaoPossibilitaTeste.isChecked())) {
                        Hawk.put("CargaNominalErroConformidade", String.valueOf(cargaNominalErro.getText()));
                        Hawk.put("CargaPequenaErroConformidade", String.valueOf(cargaPequenaErro.getText()));
                        Hawk.put("statusConformidade", statusConformidade);

                        if (conexao != null) {
                            conexao.interrupt();
                        }
                        mBluetoothAdapter.disable();
                        conexao = null;
                        abrirRegistrador();

                    } else {
                        Hawk.put("CargaNominalErroConformidade", String.valueOf(cargaNominalErro.getText()));
                        Hawk.put("CargaPequenaErroConformidade", String.valueOf(cargaPequenaErro.getText()));
                        Hawk.put("statusConformidade", statusConformidade);

                        if (conexao != null) {
                            conexao.interrupt();
                        }
                        mBluetoothAdapter.disable();
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

    public void mudarEstadoTesteCargaNominal(View view) {

        cronometroNominal.setVisibility(View.VISIBLE);
        cronometroNominal.setBase(SystemClock.elapsedRealtime());
        cronometroNominal.start(); // start a chronometer

        int pulsos = 5;
        if (quantidadePulsos.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "O teste vai ser realizado com 5 pulsos!", Toast.LENGTH_LONG).show();
            pulsos = 5;
        } else {
            pulsos = Integer.parseInt(quantidadePulsos.getText().toString());
        }

        float tensao = Float.parseFloat((String) Hawk.get("TensaoNominalMedidor"));
        float corrente = Float.parseFloat((String) Hawk.get("CorrenteNominalMedidor"));
        float kdMedidor = Float.parseFloat((String) Hawk.get("KdKeMedidor"));
        float FP = 1;

        tempoTeste = ((((3600 * pulsos * kdMedidor) / (tensao * corrente * FP))) / 10);
        DecimalFormat df = new DecimalFormat("0.##");
        String dx = df.format(tempoTeste);

        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser iniciado/parado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (!testeCargaNominalComecou) {
                testeCargaNominalComecou = true;
                testeNominal.clearComposingText();
                testeNominal.setText("Cancelar Teste de Carga Nominal");
                aplicarCargaNominal(view);
                textMessageInspecaoConformidade.clearComposingText();
                textMessageInspecaoConformidade.setText("Teste sendo iniciado...  \n " +
                        "Estimativa: " + dx + " minuto(s)");
                tempoTeste = 0;

            } else {
                testeCargaNominalComecou = false;
                testeNominal.clearComposingText();
                testeNominal.setText("Iniciar Teste de Carga Nominal");
                pararTeste(view);
                textMessageInspecaoConformidade.clearComposingText();
                textMessageInspecaoConformidade.setText("Teste Cancelado!");
            }
        }
    }

    public void mudarEstadoTesteCargaPequena(View view) {

        cronometroPequeno.setVisibility(View.VISIBLE);
        cronometroPequeno.setBase(SystemClock.elapsedRealtime());
        cronometroPequeno.start(); // start a chronometer

        int pulsos = 5;
        if (quantidadePulsos.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "O teste vai ser realizado com 5 pulsos!", Toast.LENGTH_LONG).show();
            pulsos = 5;
        } else {
            pulsos = Integer.parseInt(quantidadePulsos.getText().toString());
        }
        float tensao = Float.parseFloat((String) Hawk.get("TensaoNominalMedidor"));
        float corrente = Float.parseFloat((String) Hawk.get("CorrenteNominalMedidor"));
        float kdMedidor = Float.parseFloat((String) Hawk.get("KdKeMedidor"));
        float FP = 1;

        tempoTeste = ((((3600 * pulsos * kdMedidor) / (tensao * corrente * FP))) / 10);

        DecimalFormat df = new DecimalFormat("0.##");
        String dx = df.format(tempoTeste);

        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser iniciado/parado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (!testeCargaPequenaComecou) {
                testeCargaPequenaComecou = true;
                testePequeno.clearComposingText();
                testePequeno.setText("Cancelar Teste de Carga Pequena");
                aplicarCargaPequena(view);
                textMessageInspecaoConformidade.clearComposingText();
                textMessageInspecaoConformidade.setText("Teste sendo iniciado... \n" +
                        "Estimativa: " + dx + " minuto(s) ");
                tempoTeste = 0;

            } else {
                testeCargaPequenaComecou = false;
                testePequeno.clearComposingText();
                testePequeno.setText("Iniciar Teste de Carga Pequena");
                pararTeste(view);
                textMessageInspecaoConformidade.clearComposingText();
                textMessageInspecaoConformidade.setText("Teste Cancelado!");
            }
        }
    }

    private void pararTeste(View view) {

        byte[] pacote = new byte[10];

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

        conexao.write(pacote);


        cronometroPequeno.stop(); // stop a chronometer
        cronometroPequeno.setText("00:00");

        cronometroNominal.stop(); // stop a chronometer
        cronometroNominal.setText("00:00");


    }

    private void ativarBluetooth() {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            textMessageInspecaoConformidade.setText("Bluetooth não está funcionando.");

        } else {
            textMessageInspecaoConformidade.setText("Bluetooth está funcionando.");

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                textMessageInspecaoConformidade.setText("Solicitando ativação do Bluetooth...");

            } else {
                textMessageInspecaoConformidade.setText("Bluetooth Ativado.");

            }

        }

    }

    public void aplicarCargaNominal(View view) {


        tempoInicio = System.currentTimeMillis();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String today = formatter.format(tempoInicio);
        Log.d("TEMPO DO INICIO", String.valueOf(today));


        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser inicializado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {
            if (conexao.isAlive()) {
                textMessageInspecaoConformidade.setText("Conectado!");
            }

            byte[] pacote = new byte[10];
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
                Toast.makeText(getApplicationContext(), "O teste foi configurado para 5 pulsos", Toast.LENGTH_LONG).show();
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

            conexao.write(pacote);
        }


    }

    public void aplicarCargaPequena(View view) {

//
//        tempoInicio = System.currentTimeMillis();
//        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//        String today = formatter.format(tempoInicio);
//        Log.d("TEMPO DO INICIO", String.valueOf(today));

        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser inicializado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            byte[] pacote = new byte[10];
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
                Toast.makeText(getApplicationContext(), "O teste foi configurado para 5 pulsos", Toast.LENGTH_LONG).show();
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




            conexao.write(pacote);
        }


    }

    private void conectarDispositivo(View view) {

        if (conexao != null) {
            Toast.makeText(getApplicationContext(), "Dispositivo já conectado.", Toast.LENGTH_LONG).show();

        }
        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                textMessageInspecaoConformidade.setText("Bluetooth ativado.");
            } else {
                textMessageInspecaoConformidade.setText("Bluetooth não ativado.");
            }
        } else if (requestCode == SELECT_PAIRED_DEVICE) {
            if (resultCode == RESULT_OK) {
                textMessageInspecaoConformidade.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n" + data.getStringExtra("btDevAddress"));
                String macAddress = data.getStringExtra("btDevAddress");

                conexao = new ThreadConexao(macAddress);
                conexao.start();
                if (conexao.isAlive()) {
                    textMessageInspecaoConformidade.setText("Conexao finalizada com:" + data.getStringExtra("btDevName") + "\n Verifique o LED de conexão");
                }
            } else {
                textMessageInspecaoConformidade.setText("Nenhum dispositivo selecionado.");
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

    public void testeFotoCelula(View view) {

        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste da FotoCélula não pode ser iniciado/parado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {
            teste = findViewById(R.id.buttonTesteFotoCelula);

            if (!testeFCComecou) {
                testeFCComecou = true;
                teste.clearComposingText();
                teste.setText("Cancelar Teste da FotoCélula");
                testeFoto(view);
                textMessageInspecaoConformidade.clearComposingText();
                textMessageInspecaoConformidade.setText("Teste da FotoCélula sendo iniciado...");

            } else {
                testeFCComecou = false;
                teste.clearComposingText();
                teste.setText("Iniciar Teste da FotoCélula");
                pararTeste(view);
                textMessageInspecaoConformidade.clearComposingText();
                textMessageInspecaoConformidade.setText("Teste da FotoCélula Cancelado!");
            }
        }
    }

    private void testeFoto(View view) {

        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser inicializado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (conexao != null) {
                textMessageInspecaoConformidade.setText("O teste de FotoCélula vai ser iniciado...");
            }

            byte[] pacote = new byte[10];
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

            conexao.write(pacote);
        }
    }

    public void pararCronometro(String res) {
        if (res.startsWith("T")) {
            cronometroNominal.setVisibility(View.INVISIBLE);
            cronometroNominal.setEnabled(false);// stop(); // stop a chronometer
            cronometroNominal.setText("00:00");
        }
    }
}

