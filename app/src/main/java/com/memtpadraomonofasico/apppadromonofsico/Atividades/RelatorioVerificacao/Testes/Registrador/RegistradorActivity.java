package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.SituacoesObservadasActivity;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.PairedDevices;
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.ThreadConexaoRegistrador;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class RegistradorActivity extends AppCompatActivity {

    private static final String TAG = "Bluetooth";
    private static final int ENABLE_BLUETOOTH = 1;
    private static final int SELECT_PAIRED_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 4;
    private static final int TIRAR_FOTO_ANTES = 10207;
    private static final int TIRAR_FOTO_DEPOIS = 10208;
    private static final int REQUEST_OBS = 1000;
    @SuppressLint("HandlerLeak")
    private static final Handler handler = new Handler() { };
    @SuppressLint("StaticFieldLeak")
    private static TextView textMessage;
    private static TextView calibracaoPreTeste, calibracaoPosTeste;
    private static ThreadConexaoRegistrador conexao;
    @SuppressLint("StaticFieldLeak")
    private static Chronometer cronometro;
    List<String> av = new ArrayList<>();
    long tempoInicio;
    double tempoTeste;
    @SuppressLint("WrongViewCast")
    private Button fotoDepois;
    @SuppressLint("WrongViewCast")
    private Button fotoAntes;
    @SuppressLint("WrongViewCast")
    private Button conectar, estadoTeste, testeFotoCelula;
    private BluetoothAdapter mBluetoothAdapter = null;
    private RadioButton aprovado, naoPossibilitaTeste, reprovado;
    private Bitmap fotoResized1, fotoResized2;
    private Bitmap fotoDepoisRegistrador, fotoAntesRegistrador;
    private String status, observacaoRegistrador = " ", leituraPreTeste, leituraPosTeste;
    private Spinner opcoesReprovados;
    private boolean teste = false;
    private boolean testeFC = false;

    public void escreverTela(final String res, final double tensao, final double corrente) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (textMessage != null) {
                    if (res.startsWith("T")) {

                        cronometro.stop(); // stop a chronometer
                        cronometro.setText("00:00");

                        textMessage.clearComposingText();
                        textMessage.setText("Teste Concluído!");

                        if (conexao != null) {
                            conexao.interrupt();
                        }

                    } else {
                        tempoTeste = ((((1100) / (tensao * corrente))) * 60);
                        DecimalFormat df = new DecimalFormat("0.##");
                        String dx = df.format(tempoTeste);

                        textMessage.clearComposingText();
                        textMessage.setText(res + " \n Estimado " + dx + " minuto(s) ");
                    }
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrador);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();

        aprovado = findViewById(R.id.tampasolidarizada);
        naoPossibilitaTeste = findViewById(R.id.sinaisCarbonizacao);
        reprovado = findViewById(R.id.Reprovado);
        textMessage = findViewById(R.id.textView6);
        fotoAntes = findViewById(R.id.buttonFotoAntes);
        fotoDepois = findViewById(R.id.buttonFotoDepois);
        fotoDepois.setEnabled(false);
        conectar = findViewById(R.id.buttonConectarDispositivo);
        estadoTeste = findViewById(R.id.executarTeste);
        calibracaoPreTeste = findViewById(R.id.LeituraRetirada);
        calibracaoPosTeste = findViewById(R.id.leituraPosCalibracao);

        opcoesReprovados = findViewById(R.id.RegistradorSpinner);
        opcoesReprovados.setEnabled(false);
        av = todasMensagens();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, av);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcoesReprovados.setAdapter(adapter);
        opcoesReprovados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cronometro = new Chronometer(this);
        cronometro = findViewById(R.id.Cronometro);
        cronometro.setVisibility(View.INVISIBLE);
        ativarBluetooth();


        @SuppressLint("WrongViewCast") Button addObs = findViewById(R.id.addObservacao);
        addObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAddObs();
            }
        });

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

        fotoAntes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFotoAntes();
            }
        });

        fotoDepois.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFotoDepois();
            }
        });

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase4);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Hawk.delete("FotoPreTesteRegistrador");
                Hawk.delete("FotoPosTesteRegistrador");
                Hawk.delete("statusRegistrador");
                Hawk.delete("ObservaçãoRegistrador");
                Hawk.delete("leituraPreTeste");
                Hawk.delete("leituraPosTeste");


                if (aprovado.isChecked()) {
                    status = "Aprovado";

                } else if (naoPossibilitaTeste.isChecked()) {
                    status = "Não Possibilita Testes";

                } else if (reprovado.isChecked()) {
                    status = "Reprovado";
                }

                leituraPreTeste = calibracaoPreTeste.getText().toString();
                leituraPosTeste = calibracaoPosTeste.getText().toString();

                if (status.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Status não selecionado!", Toast.LENGTH_LONG).show();

                }
                else {
                    if ((leituraPreTeste.isEmpty())&& (leituraPosTeste.isEmpty())) {
                        Toast.makeText(getApplicationContext(), "Sessão incompleta - Faltam as leituras de calibração!", Toast.LENGTH_LONG).show();

                    }
                    else {
                        if (((status.equals("Aprovado"))) && ((fotoAntesRegistrador == null))) {
                            Toast.makeText(getApplicationContext(), "Sessão incompleta - Fotos não tiradas!", Toast.LENGTH_LONG).show();

                        } else {
                            Hawk.put("FotoPreTesteRegistrador", fotoAntesRegistrador);
                            Hawk.put("FotoPosTesteRegistrador", fotoDepoisRegistrador);
                            Hawk.put("statusRegistrador", status);
                            Hawk.put("ObservaçãoRegistrador", observacaoRegistrador);
                            Hawk.put("leituraPreTeste", leituraPreTeste);
                            Hawk.put("leituraPosTeste", leituraPosTeste);

                            mBluetoothAdapter.disable();
                            conexao = null;
                            abrirSituacoesObservadas();

                        }
                    }
                }

            }
        });
    }

    private List<String> todasMensagens() {

        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaMensagemEspecifica("Registrador");
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String corpoMensagem = cursor.getString(2);
            av.add(corpoMensagem);
        }
        return av;
    }

    private void abrirSituacoesObservadas() {
        Intent intent = new Intent(this, SituacoesObservadasActivity.class);
        startActivity(intent);
    }

    private void ativarBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            textMessage.setText("Bluetooth não está funcionando.");

        } else {
            textMessage.setText("Bluetooth está funcionando.");

            if (!mBluetoothAdapter.isEnabled()) {
                Log.d(TAG, "ATIVANDO BLUETOOTH");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                textMessage.setText("Solicitando ativação do Bluetooth...");

            } else {
                textMessage.setText("Bluetooth Ativado.");

            }
        }
    }

    public void mudarEstadoTeste(View view) {

        float kdMedidor = Float.parseFloat((String) Hawk.get("KdKeMedidor"));
        float FP = 1;

        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser iniciado/parado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (!teste) {
                teste = true;
                estadoTeste.clearComposingText();
                estadoTeste.setText("Cancelar Teste ");
                executarTeste(view);
                // tempoTeste = (float) (((1100) / (120 * 35 * FP)) );
                textMessage.clearComposingText();
                //Estimativa: " + tempoTeste + "minutos para finalizar o teste. "
                textMessage.setText("Teste sendo iniciado...");

            } else {
                teste = false;
                estadoTeste.clearComposingText();
                estadoTeste.setText("Iniciar Teste ");
                pararTeste(view);
                textMessage.clearComposingText();
                textMessage.setText("Teste Cancelado!");
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

        cronometro.stop();


    }

    public void executarTeste(View view) {

        cronometro.setVisibility(View.VISIBLE);
        cronometro.setBase(SystemClock.elapsedRealtime());
        cronometro.start(); // start a chronometer

        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser inicializado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        }
        if (fotoAntesRegistrador == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser inicializado sem a foto pré-teste.", Toast.LENGTH_LONG).show();

        } else {

            if (conexao != null) {
                textMessage.setText("O teste vai ser iniciado...");
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
            fotoDepois.setEnabled(true);

        }
    }

    private void abrirAddObs() {

        Intent intent = new Intent(this, ObservacaoRegistradorActivity.class);
        startActivityForResult(intent, REQUEST_OBS);
    }

    private void tirarFotoAntes() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TIRAR_FOTO_ANTES);
    }

    private void tirarFotoDepois() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TIRAR_FOTO_DEPOIS);
    }

    private void conectarDispositivo(View view) {

        if (conexao != null) {
            Toast.makeText(getApplicationContext(), "Dispositivo já conectado.", Toast.LENGTH_LONG).show();

        }

        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }

    @SuppressLint("SetTextI18n")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == TIRAR_FOTO_ANTES) {
            if (resultCode == RESULT_OK) {

                if (data != null) {
                    Toast.makeText(getBaseContext(), "A imagem foi capturada", Toast.LENGTH_SHORT);
                    Bundle bundle = data.getExtras();
                    assert bundle != null;
                    fotoAntesRegistrador = (Bitmap) bundle.get("data");

                    if (fotoAntesRegistrador != null) {
                        ImageView imageView = findViewById(R.id.FotoAntes);
                        imageView.setImageBitmap(fotoAntesRegistrador);
                    }

                } else {
                    Toast.makeText(getBaseContext(), "A câmera foi fechada",
                            Toast.LENGTH_SHORT);
                }
            }
        }

        if (requestCode == TIRAR_FOTO_DEPOIS) {
            if (resultCode == RESULT_OK) {

                if (data != null) {
                    Toast.makeText(getBaseContext(), "A imagem foi capturada", Toast.LENGTH_SHORT);
                    Bundle bundle = data.getExtras();
                    assert bundle != null;
                    fotoDepoisRegistrador = (Bitmap) bundle.get("data");

                    if (fotoDepoisRegistrador != null) {

                        ImageView imageView = findViewById(R.id.FotoDepois);
                        imageView.setImageBitmap(fotoDepoisRegistrador);
                    }

                } else {
                    Toast.makeText(getBaseContext(), "A câmera foi fechada", Toast.LENGTH_SHORT);
                }
            }
        }
        if (requestCode == REQUEST_OBS) {
            if (resultCode == RESULT_OK) {
                observacaoRegistrador = data.getStringExtra("RESULT_STRING");
            }
        }

        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                textMessage.setText("Bluetooth ativado.");
            } else {
                textMessage.setText("Bluetooth não ativado.");
            }

        } else if (requestCode == SELECT_PAIRED_DEVICE) {
            if (resultCode == RESULT_OK) {


                assert data != null;
                textMessage.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n" + data.getStringExtra("btDevAddress"));
                String macAddress = data.getStringExtra("btDevAddress");

                conexao = new ThreadConexaoRegistrador(macAddress);
                conexao.start();

                if (conexao.isAlive()) {
                    textMessage.setText("Conexao finalizada com: " + data.getStringExtra("btDevName") + "\n Verifique o LED de conexão ");
                }

            } else {
                textMessage.setText("Nenhum dispositivo selecionado.");
            }
        }
    }

    public void onCheckboxClicked(View view) {

        switch (view.getId()) {
            case R.id.tampasolidarizada:
                naoPossibilitaTeste.setChecked(false);
                reprovado.setChecked(false);
                opcoesReprovados.setEnabled(false);
                break;

            case R.id.sinaisCarbonizacao:
                aprovado.setChecked(false);
                reprovado.setChecked(false);
                opcoesReprovados.setEnabled(true);
                break;

            case R.id.Reprovado:
                aprovado.setChecked(false);
                naoPossibilitaTeste.setChecked(false);
                opcoesReprovados.setEnabled(true);

                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putParcelable("FotoPreTesteRegistrador", fotoResized1);
        savedInstanceState.putParcelable("FotoPosTesteRegistrador", fotoResized2);
        savedInstanceState.putCharSequence("statusRegistrador", status);
        savedInstanceState.putCharSequence("ObservaçãoRegistrador", observacaoRegistrador);
        savedInstanceState.putCharSequence("leituraPreTeste", leituraPreTeste);
        savedInstanceState.putCharSequence("leituraPosTeste", leituraPosTeste);


        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        savedInstanceState.putParcelable("FotoPreTesteRegistrador", fotoResized1);
        savedInstanceState.putParcelable("FotoPosTesteRegistrador", fotoResized2);
        savedInstanceState.putCharSequence("statusRegistrador", status);
        savedInstanceState.putCharSequence("ObservaçãoRegistrador", observacaoRegistrador);
        savedInstanceState.putCharSequence("leituraPreTeste", leituraPreTeste);
        savedInstanceState.putCharSequence("leituraPosTeste", leituraPosTeste);


    }

    public void testeFotoCelula(View view) {

        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste da FotoCélula não pode ser iniciado/parado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {
            testeFotoCelula = findViewById(R.id.buttonTesteFotoCelula);

            if (!testeFC) {
                testeFC = true;
                testeFotoCelula.clearComposingText();
                testeFotoCelula.setText("Cancelar Teste da FotoCélula");
                testeFoto(view);
                textMessage.clearComposingText();
                textMessage.setText("Teste da FotoCélula sendo iniciado...");

            } else {
                testeFC = false;
                testeFotoCelula.clearComposingText();
                testeFotoCelula.setText("Iniciar Teste da FotoCélula");
                pararTeste(view);
                textMessage.clearComposingText();
                textMessage.setText("Teste da FotoCélula Cancelado!");
            }
        }
    }

    private void testeFoto(View view) {


        if (conexao == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser inicializado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (conexao != null) {
                textMessage.setText("O teste de FotoCélula vai ser iniciado...");
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

