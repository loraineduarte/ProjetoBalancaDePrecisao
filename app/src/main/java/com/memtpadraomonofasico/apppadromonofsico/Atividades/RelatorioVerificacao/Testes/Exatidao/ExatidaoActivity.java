package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Exatidao;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

@SuppressWarnings("ALL")
public class ExatidaoActivity extends AppCompatActivity {

    private static final int ENABLE_BLUETOOTH = 1;
    private static final int SELECT_PAIRED_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 4;
    @SuppressLint("HandlerLeak")
    private static final Handler handlerInspecaoConformidade = new Handler() {
    };
    @SuppressLint("StaticFieldLeak")
    private static EditText cargaNominalErro, cargaPequenaErro;
    @SuppressLint("StaticFieldLeak")
    private static TextView textMessageInspecaoConformidade;
    @SuppressLint("StaticFieldLeak")
    private static EditText quantidadePulsos;
    boolean testeCargaNominalComecou = false;
    boolean testeCargaPequenaComecou = false;
    boolean testeFCComecou = false;
    @SuppressLint("WrongViewCast")
    private
    Button conectar;
    private RadioButton Aprovado, NaoPossibilitaTeste, VariacaoLeitura, Reprovado;
    private String statusConformidade;
    private BluetoothAdapter mBluetoothAdapter;
    private ThreadConexao conexao;
    private Button testeNominal, testePequeno, teste;

    public void escreverTelaCargaPequena(final String res) {

        handlerInspecaoConformidade.post(new Runnable() {
            @Override
            public void run() {

                cargaPequenaErro.setEnabled(true);
                if (!(cargaPequenaErro == null)) {
                    if (res.startsWith("T")) {
                        textMessageInspecaoConformidade.clearComposingText();
                        textMessageInspecaoConformidade.setText("Teste Concluído!");

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
                        textMessageInspecaoConformidade.clearComposingText();
                        textMessageInspecaoConformidade.setText("Teste Concluído!");


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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspecao_conformidade);

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

        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser iniciado/parado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (!testeCargaNominalComecou) {
                testeCargaNominalComecou = true;
                testeNominal.clearComposingText();
                testeNominal.setText("Cancelar Teste de Carga Nominal");
                aplicarCargaNominal(view);
                textMessageInspecaoConformidade.clearComposingText();
                textMessageInspecaoConformidade.setText("Teste sendo iniciado...");

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

        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser iniciado/parado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (!testeCargaPequenaComecou) {
                testeCargaPequenaComecou = true;
                testePequeno.clearComposingText();
                testePequeno.setText("Cancelar Teste de Carga Pequena");
                aplicarCargaPequena(view);
                textMessageInspecaoConformidade.clearComposingText();
                textMessageInspecaoConformidade.setText("Teste sendo iniciado...");

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


    }

    public void escreverTelaInspecaoConformidade(final String res) {
        handlerInspecaoConformidade.post(new Runnable() {
            @Override
            public void run() {
                if (!(textMessageInspecaoConformidade == null)) {
                    if (res.startsWith("T")) {
                        textMessageInspecaoConformidade.clearComposingText();
                        textMessageInspecaoConformidade.setText("Teste Concluído!");

                        if(testePequeno!=null){
                            testeCargaPequenaComecou = false;
                            testePequeno.clearComposingText();
                            testePequeno.setText("Iniciar Teste de Carga Pequena");
                        }
                       if(testeNominal!=null){
                           testeCargaNominalComecou = false;
                           testeNominal.clearComposingText();
                           testeNominal.setText("Iniciar Teste de Carga Nominal");
                       }


                    } else {
                        textMessageInspecaoConformidade.clearComposingText();
                        textMessageInspecaoConformidade.setText(res);
                    }

                }
            }
        });

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


        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser inicializado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {
            if (conexao.isAlive()) {
                textMessageInspecaoConformidade.setText("Conectado!");
            }

            byte[] pacote = new byte[10];
            float kdMedidor = Float.parseFloat((String) Hawk.get("KdKeMedidor"));

            byte[] bytes = new byte[4];
            int valorMultiplicado = (int) (kdMedidor * 1000000);

            bytes[0] = (byte) (valorMultiplicado / (Math.pow(256, 3)));
            bytes[1] = (byte) ((valorMultiplicado - (bytes[0] * (Math.pow(256, 3)))) / Math.pow(256, 2));
            bytes[2] = (byte) ((valorMultiplicado - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))))) / Math.pow(256, 1));
            bytes[3] = (byte) ((valorMultiplicado - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))) + (bytes[2] * Math.pow(256, 1)))));

            byte[] bytesPulso = new byte[4];
            int pulsos = Integer.parseInt(quantidadePulsos.getText().toString());

            if (pulsos == 0) {
                bytesPulso[0] = 0;
                bytesPulso[1] = 5;
                bytesPulso[2] = 0;
                bytesPulso[3] = 0;
            } else {
                bytesPulso[0] = (byte) (pulsos / (Math.pow(256, 3)));
                bytesPulso[1] = (byte) ((pulsos - (bytes[0] * (Math.pow(256, 3)))) / Math.pow(256, 2));
                bytesPulso[2] = (byte) ((pulsos - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))))) / Math.pow(256, 1));
                bytesPulso[3] = (byte) ((pulsos - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))) + (bytes[2] * Math.pow(256, 1)))));
            }

            pacote[0] = ('I' & 0xFF);
            pacote[1] = ('B' & 0xFF);
            pacote[2] = (byte) (bytes[0] & 0xFF);
            pacote[3] = (byte) (bytes[1] & 0xFF);
            pacote[4] = (byte) (bytes[2] & 0xFF);
            pacote[5] = (byte) (bytes[3] & 0xFF);
            pacote[6] = (byte) (bytesPulso[0] & 0xFF);
            pacote[7] = (byte) (bytesPulso[1] & 0xFF);
            pacote[8] = (byte) (bytesPulso[2] & 0xFF);
            pacote[9] = (byte) (bytesPulso[3] & 0xFF);

            conexao.write(pacote);
        }


    }

    public void aplicarCargaPequena(View view) {


        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser inicializado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            byte[] pacote = new byte[10];

            //pegando valores do medidor

            float kdMedidor = Float.parseFloat((String) Hawk.get("KdKeMedidor"));

            byte[] bytes = new byte[4];
            int valorMultiplicado = (int) (kdMedidor * 1000000);

            bytes[0] = (byte) (valorMultiplicado / (Math.pow(256, 3)));
            bytes[1] = (byte) ((valorMultiplicado - (bytes[0] * (Math.pow(256, 3)))) / Math.pow(256, 2));
            bytes[2] = (byte) ((valorMultiplicado - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))))) / Math.pow(256, 1));
            bytes[3] = (byte) ((valorMultiplicado - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))) + (bytes[2] * Math.pow(256, 1)))));

            byte[] bytesPulso = new byte[4];
            int pulsos = Integer.parseInt(quantidadePulsos.getText().toString());

            if (pulsos == 0) {
                bytesPulso[0] = 0;
                bytesPulso[1] = 5;
                bytesPulso[2] = 0;
                bytesPulso[3] = 0;
            } else {
                bytesPulso[0] = (byte) (pulsos / (Math.pow(256, 3)));
                bytesPulso[1] = (byte) ((pulsos - (bytes[0] * (Math.pow(256, 3)))) / Math.pow(256, 2));
                bytesPulso[2] = (byte) ((pulsos - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))))) / Math.pow(256, 1));
                bytesPulso[3] = (byte) ((pulsos - ((bytes[0] * (Math.pow(256, 3))) + (bytes[1] * (Math.pow(256, 2))) + (bytes[2] * Math.pow(256, 1)))));
            }



            pacote[0] = ('I' & 0xFF);
            pacote[1] = ('B' & 0xFF);
            pacote[2] = (byte) (bytes[0] & 0xFF);
            pacote[3] = (byte) (bytes[1] & 0xFF);
            pacote[4] = (byte) (bytes[2] & 0xFF);
            pacote[5] = (byte) (bytes[3] & 0xFF);
            pacote[6] = (byte) (bytesPulso[0] & 0xFF);
            pacote[7] = (byte) (bytesPulso[1] & 0xFF);
            pacote[8] = (byte) (bytesPulso[2] & 0xFF);
            pacote[9] = (byte) (bytesPulso[3] & 0xFF);

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
}

