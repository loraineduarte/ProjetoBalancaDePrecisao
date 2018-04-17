package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoConformidade;

import android.annotation.SuppressLint;
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

import com.memtpadraomonofasico.apppadromonofsico.Atividades.Bluetooth.PairedDevices;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.Bluetooth.ThreadConexao;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.SituacoesObservadasActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

public class InspecaoConformidadeActivity extends AppCompatActivity {

    private RadioButton Aprovado, NaoPossibilitaTeste, VariacaoLeitura, Reprovado;
    private String statusConformidade;
    @SuppressLint("StaticFieldLeak")
    private static EditText cargaNominalErro, cargaPequenaErro;
    private BluetoothAdapter mBluetoothAdapter;

    private static final int ENABLE_BLUETOOTH = 1;
    private static final int SELECT_PAIRED_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 4;

    @SuppressLint("StaticFieldLeak")
    private static TextView textMessageInspecaoConformidade;
    private ThreadConexao conexao;

    @SuppressLint("WrongViewCast") Button  conectar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspecao_conformidade);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();

        textMessageInspecaoConformidade = findViewById(R.id.textView7);
        textMessageInspecaoConformidade.setText("  ");
        cargaNominalErro =  findViewById(R.id.CargaNominalErro);
        cargaPequenaErro = findViewById(R.id.CargaPequenaErro);
        Aprovado = findViewById(R.id.tampasolidarizada);
        NaoPossibilitaTeste = findViewById(R.id.sinaisCarbonizacao);
        VariacaoLeitura = findViewById(R.id.VariacaoLeitura);
        Reprovado = findViewById(R.id.Reprovado);
        conectar = findViewById(R.id.buttonConectarDispositivo);

        ativarBluetooth();

        conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ativarBluetooth();
                    new Thread().sleep(4500);
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


                if(Aprovado.isChecked()){
                    statusConformidade = "Aprovado";

                } if (NaoPossibilitaTeste.isChecked()){
                    statusConformidade = "Não Possibilita Teste";

                }if (VariacaoLeitura.isChecked()){
                    statusConformidade = "Variação de Leitura";

                } if (Reprovado.isChecked()){
                    statusConformidade = "Reprovado";
                }

                if ((!Aprovado.isChecked()) && (!NaoPossibilitaTeste.isChecked()) && (!VariacaoLeitura.isChecked()) && (!Reprovado.isChecked()))
                {
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Não existe opção de status marcado. ", Toast.LENGTH_LONG).show();

                } else{
                    Hawk.put("CargaNominalErroConformidade",String.valueOf(cargaNominalErro.getText()));
                    Hawk.put("CargaPequenaErroConformidade",String.valueOf(cargaPequenaErro.getText()));
                    Hawk.put("statusConformidade",statusConformidade);

                    if(conexao!= null){
                        conexao.interrupt();
                    }
                    mBluetoothAdapter.disable();


                    abrirSituacoesObservadas();
                }
            }
        });
    }

    private void ativarBluetooth() {
        // verificando ativação do bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            textMessageInspecaoConformidade.setText("Bluetooth não está funcionando.");
        } else {
            textMessageInspecaoConformidade.setText("Bluetooth está funcionando.");
            if (!mBluetoothAdapter.isEnabled()) {
                Log.d("Bluetooth", "ATIVANDO BLUETOOTH");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                textMessageInspecaoConformidade.setText("Solicitando ativação do Bluetooth...");
            } else {
                textMessageInspecaoConformidade.setText("Bluetooth Ativado.");
            }

        }
        // Fim - verificando ativação do bluetooth

    }

    public void aplicarCargaNominal(View view) {

        if(conexao == null){
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

            pacote[0] = ('I' & 0xFF);
            pacote[1] = ('N' & 0xFF);
            pacote[2] = (byte) (bytes[0] & 0xFF);
            pacote[3] = (byte) (bytes[1] & 0xFF);
            pacote[4] = (byte) (bytes[2] & 0xFF);
            pacote[5] = (byte) (bytes[3] & 0xFF);
            pacote[6] = (byte) (0 & 0xFF);
            pacote[7] = (byte) (5 & 0xFF);
            pacote[8] = (byte) (0 & 0xFF);
            pacote[9] = (byte) (0 & 0xFF);

            conexao.write(pacote);
        }




    }

    public void aplicarCargaPequena(View view) {

        if(conexao == null){
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

            pacote[0] = ('I' & 0xFF);
            pacote[1] = ('B' & 0xFF);
            pacote[2] = (byte) (bytes[0] & 0xFF);
            pacote[3] = (byte) (bytes[1] & 0xFF);
            pacote[4] = (byte) (bytes[2] & 0xFF);
            pacote[5] = (byte) (bytes[3] & 0xFF);
            pacote[6] = (byte) (0 & 0xFF);
            pacote[7] = (byte) (5 & 0xFF);
            pacote[8] = (byte) (0 & 0xFF);
            pacote[9] = (byte) (0 & 0xFF);

            conexao.write(pacote);
        }


    }

    @SuppressLint("HandlerLeak")
    private static final Handler handlerInspecaoConformidade = new Handler() {
    };

    public static void escreverTelaInspecaoConformidade(final String res) {
        handlerInspecaoConformidade.post(new Runnable() {
            @Override
            public void run() {

                if(!(textMessageInspecaoConformidade==null)){
                    if(res.startsWith("F")){
                        textMessageInspecaoConformidade.clearComposingText();
                        textMessageInspecaoConformidade.setText("Teste Concluído!");

                    } else {
                        textMessageInspecaoConformidade.clearComposingText();
                        textMessageInspecaoConformidade.setText(res);
                    }

                }

            }
        });

    }

    public static void escreverTelaCargaNominal(final String res) {

        handlerInspecaoConformidade.post(new Runnable() {
            @Override
            public void run() {
                if(!(cargaNominalErro==null)){
                    if(res.startsWith("F")){
                        cargaNominalErro.clearComposingText();
                        cargaNominalErro.setText("Teste Concluído!");

                    } else {
                        cargaNominalErro.clearComposingText();
                        cargaNominalErro.setText(res);
                    }

                }

            }
        });

    }

    public static void escreverTelaCargaPequena(final String res) {

        handlerInspecaoConformidade.post(new Runnable() {
            @Override
            public void run() {
                if(!(cargaPequenaErro==null)) {
                    if (res.startsWith("F")) {
                        cargaPequenaErro.clearComposingText();
                        cargaPequenaErro.setText("Teste Concluído!");

                    } else {
                        cargaPequenaErro.clearComposingText();
                        cargaPequenaErro.setText(res);
                    }
                }

            }
        });

    }

    public void conectarDispositivo(View view) {
        ativarBluetooth();

        if(conexao != null){
            Toast.makeText(getApplicationContext(), "Dispositivo já conectado.", Toast.LENGTH_LONG).show();

        } else {
            Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
            startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);

        }
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
                if(conexao.isAlive()){
                    textMessageInspecaoConformidade.setText("Conexao sendo finalizada com:" + data.getStringExtra("btDevName") + "\n" + data.getStringExtra("btDevAddress"));
                }
            } else {
                textMessageInspecaoConformidade.setText("Nenhum dispositivo selecionado.");
            }
        }
    }

    private void abrirSituacoesObservadas() {
        Intent intent = new Intent(this, SituacoesObservadasActivity.class);
        startActivity(intent);
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

}

