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
import com.memtpadraomonofasico.apppadromonofsico.Bluetooth.Testes.ConexaoRegistrador.ThreadConexaoRegistrador;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

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
    private static TextView mensagemNaTela;
    private static TextView calibracaoPreTeste, calibracaoPosTeste;
    private static ThreadConexaoRegistrador conexaoPadraoMKV;
    @SuppressLint("StaticFieldLeak")
    private static Chronometer cronometroTesteRegistrador;
    private static boolean testeRegistradorRodando = false;
    private static boolean testeFotoCelulaRodando = false;
    List<String> arrayTodasMensagensErro = new ArrayList<>();
    double tempoEstimadoTeste;
    @SuppressLint("WrongViewCast")
    private Button fotoDepois;
    @SuppressLint("WrongViewCast")
    private Button fotoAntes;
    @SuppressLint("WrongViewCast")
    private Button botaoConectar, botaoTesteRegistrador, botaoTesteFotoCelula;
    private BluetoothAdapter bluetoothAdapter = null;
    private RadioButton aprovado, naoPossibilitaTeste, reprovado;
    private Bitmap fotoDepoisRegistrador, fotoAntesRegistrador;
    private String statusTesteRegistrador, observacaoRegistrador = " ", leituraCalibradorPreTeste, leituraCalibradorPosTeste;
    private Spinner opcoesReprovados;

    public void escreverTela(final String res, final double tensao, final double corrente) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mensagemNaTela != null) {
                    if (res.startsWith("T")) {

                        cronometroTesteRegistrador.stop(); // stop a chronometer
                        cronometroTesteRegistrador.setText("00:00");

                        mensagemNaTela.clearComposingText();
                        mensagemNaTela.setText("Teste Concluído!");

                        if (conexaoPadraoMKV != null) {
                            conexaoPadraoMKV.interrupt();
                        }

                    } else {
                        tempoEstimadoTeste = ((((1100) / (tensao * corrente))) * 60);
                        DecimalFormat df = new DecimalFormat("0.##");
                        String dx = df.format(tempoEstimadoTeste);

                        mensagemNaTela.clearComposingText();
                        mensagemNaTela.setText(res + " \n Estimado " + dx + " minuto(s) ");
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
        mensagemNaTela = findViewById(R.id.textView6);
        fotoAntes = findViewById(R.id.buttonFotoAntes);
        fotoDepois = findViewById(R.id.buttonFotoDepois);
        fotoDepois.setEnabled(false);
        botaoConectar = findViewById(R.id.buttonConectarDispositivo);
        botaoTesteRegistrador = findViewById(R.id.executarTeste);
        calibracaoPreTeste = findViewById(R.id.LeituraRetirada);
        calibracaoPosTeste = findViewById(R.id.leituraPosCalibracao);
        opcoesReprovados = findViewById(R.id.RegistradorSpinner);
        opcoesReprovados.setEnabled(false);

        arrayTodasMensagensErro = buscarTodasMensagensDeErro();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayTodasMensagensErro);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcoesReprovados.setAdapter(adapter);
        opcoesReprovados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusTesteRegistrador = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cronometroTesteRegistrador = new Chronometer(this);
        cronometroTesteRegistrador = findViewById(R.id.Cronometro);
        cronometroTesteRegistrador.setVisibility(View.INVISIBLE);
        ativarBluetooth();


        @SuppressLint("WrongViewCast") Button addObs = findViewById(R.id.addObservacao);
        addObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAddObs();
            }
        });

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
                Hawk.delete("leituraCalibradorPreTeste");
                Hawk.delete("leituraCalibradorPosTeste");


                if (aprovado.isChecked()) {
                    statusTesteRegistrador = "Aprovado";

                } else if (naoPossibilitaTeste.isChecked()) {
                    statusTesteRegistrador = "Não Possibilita Testes";

                } else if (reprovado.isChecked()) {
                    statusTesteRegistrador = "Reprovado";
                }

                leituraCalibradorPreTeste = calibracaoPreTeste.getText().toString();
                leituraCalibradorPosTeste = calibracaoPosTeste.getText().toString();

                if (statusTesteRegistrador.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Status não selecionado!", Toast.LENGTH_LONG).show();

                }
                else {
                    if ((leituraCalibradorPreTeste.isEmpty()) && (leituraCalibradorPosTeste.isEmpty())) {
                        Toast.makeText(getApplicationContext(), "Sessão incompleta - Faltam as leituras de calibração!", Toast.LENGTH_LONG).show();

                    }
                    else {
                        if (((statusTesteRegistrador.equals("Aprovado"))) && ((fotoAntesRegistrador == null))) {
                            Toast.makeText(getApplicationContext(), "Sessão incompleta - Fotos não tiradas!", Toast.LENGTH_LONG).show();

                        } else {
                            Hawk.put("FotoPreTesteRegistrador", fotoAntesRegistrador);
                            Hawk.put("FotoPosTesteRegistrador", fotoDepoisRegistrador);
                            Hawk.put("statusRegistrador", statusTesteRegistrador);
                            Hawk.put("ObservaçãoRegistrador", observacaoRegistrador);
                            Hawk.put("leituraCalibradorPreTeste", leituraCalibradorPreTeste);
                            Hawk.put("leituraCalibradorPosTeste", leituraCalibradorPosTeste);

                            bluetoothAdapter.disable();
                            conexaoPadraoMKV = null;
                            abrirSituacoesObservadas();

                        }
                    }
                }

            }
        });
    }

    private List<String> buscarTodasMensagensDeErro() {

        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.pegaMensagemEspecifica("Registrador");
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String corpoMensagem = cursor.getString(2);
            arrayTodasMensagensErro.add(corpoMensagem);
        }
        return arrayTodasMensagensErro;
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

    private void abrirSituacoesObservadas() {
        Intent intent = new Intent(this, SituacoesObservadasActivity.class);
        startActivity(intent);
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
                mensagemNaTela.setText("Bluetooth ativado.");
            } else {
                mensagemNaTela.setText("Bluetooth não ativado.");
            }

        } else if (requestCode == SELECT_PAIRED_DEVICE) {
            if (resultCode == RESULT_OK) {


                assert data != null;
                mensagemNaTela.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n" + data.getStringExtra("btDevAddress"));
                String macAddress = data.getStringExtra("btDevAddress");

                conexaoPadraoMKV = new ThreadConexaoRegistrador(macAddress);
                conexaoPadraoMKV.start();

                if (conexaoPadraoMKV.isAlive()) {
                    mensagemNaTela.setText("Conexao finalizada com: " + data.getStringExtra("btDevName") + "\n Verifique o LED de conexão ");
                }

            } else {
                mensagemNaTela.setText("Nenhum dispositivo selecionado.");
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

        savedInstanceState.putParcelable("FotoPreTesteRegistrador", fotoAntesRegistrador);
        savedInstanceState.putParcelable("FotoPosTesteRegistrador", fotoDepoisRegistrador);
        savedInstanceState.putCharSequence("statusRegistrador", statusTesteRegistrador);
        savedInstanceState.putCharSequence("ObservaçãoRegistrador", observacaoRegistrador);
        savedInstanceState.putCharSequence("leituraCalibradorPreTeste", leituraCalibradorPreTeste);
        savedInstanceState.putCharSequence("leituraCalibradorPosTeste", leituraCalibradorPosTeste);


        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        savedInstanceState.putParcelable("FotoPreTesteRegistrador", fotoAntesRegistrador);
        savedInstanceState.putParcelable("FotoPosTesteRegistrador", fotoDepoisRegistrador);
        savedInstanceState.putCharSequence("statusRegistrador", statusTesteRegistrador);
        savedInstanceState.putCharSequence("ObservaçãoRegistrador", observacaoRegistrador);
        savedInstanceState.putCharSequence("leituraCalibradorPreTeste", leituraCalibradorPreTeste);
        savedInstanceState.putCharSequence("leituraCalibradorPosTeste", leituraCalibradorPosTeste);


    }


    //-------------Funções Bluetooth
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

        //TODO - olhar o tipo de padrão para realizar a conexão

        if (conexaoPadraoMKV != null) {
            Toast.makeText(getApplicationContext(), "Dispositivo já conectado.", Toast.LENGTH_LONG).show();

        }

        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }


    //-------------Testes
    public void testeRegistrador(View view) {

        if (conexaoPadraoMKV == null) {
            Toast.makeText(getApplicationContext(), "O testeRegistradorRodando não pode ser iniciado/parado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (!testeRegistradorRodando) {
                testeRegistradorRodando = true;
                botaoTesteRegistrador.clearComposingText();
                botaoTesteRegistrador.setText("Cancelar Teste ");
                //TODO conferir se é padrão chinês ou brasileiro
                registradorPadraoMKV(view);
                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste sendo iniciado...");

            } else {
                testeRegistradorRodando = false;
                botaoTesteRegistrador.clearComposingText();
                botaoTesteRegistrador.setText("Iniciar Teste de Registrador");
                //TODO conferir se é padrão chinês ou brasileiro
                pararTestePadraoMKV(view);
                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste Cancelado!");
            }
        }
    }

    public void testeFotoCelula(View view) {

        if (conexaoPadraoMKV == null) {
            Toast.makeText(getApplicationContext(), "O testeRegistradorRodando da FotoCélula não pode ser iniciado/parado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {
            botaoTesteFotoCelula = findViewById(R.id.buttonTesteFotoCelula);

            if (!testeFotoCelulaRodando) {
                testeFotoCelulaRodando = true;
                botaoTesteFotoCelula.clearComposingText();
                botaoTesteFotoCelula.setText("Cancelar Teste da FotoCélula");
                //TODO - conferir se é padrao brasileiro ou chines
                fotoCelulaPadraoMKV(view);
                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste da FotoCélula sendo iniciado...");

            } else {
                testeFotoCelulaRodando = false;
                botaoTesteFotoCelula.clearComposingText();
                botaoTesteFotoCelula.setText("Iniciar Teste da FotoCélula");
                //TODO - conferir se é padrao brasileiro ou chines
                pararTestePadraoMKV(view);
                mensagemNaTela.clearComposingText();
                mensagemNaTela.setText("Teste da FotoCélula Cancelado!");
            }
        }
    }


    //--------------Padrão MKV

    //TODO - função de pedir o numero de série do padrao
    private void pararTestePadraoMKV(View view) {

        byte[] pacote = new byte[15];

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
        cronometroTesteRegistrador.stop();

    }

    public void registradorPadraoMKV(View view) {

        //TODO - função para pedir número de série do padrão
        //TODO - comparar com o número de série mandado pelo botaoTesteFotoCelula anterior

        cronometroTesteRegistrador.setVisibility(View.VISIBLE);
        cronometroTesteRegistrador.setBase(SystemClock.elapsedRealtime());
        cronometroTesteRegistrador.start(); // start a chronometer

        if (conexaoPadraoMKV == null) {
            Toast.makeText(getApplicationContext(), "O testeRegistradorRodando não pode ser inicializado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();

        }
        if (fotoAntesRegistrador == null) {
            Toast.makeText(getApplicationContext(), "O testeRegistradorRodando não pode ser inicializado sem a foto pré-testeRegistradorRodando.", Toast.LENGTH_LONG).show();

        } else {

            if (conexaoPadraoMKV != null) {
                mensagemNaTela.setText("O teste de Registrador vai ser iniciado...");
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
            fotoDepois.setEnabled(true);

        }
    }

    private void fotoCelulaPadraoMKV(View view) {

        //TODO - função para pedir número de série do padrão
        //TODO - comparar com o número de série mandado pelo botaoTesteFotoCelula anterior
        if (conexaoPadraoMKV == null) {
            Toast.makeText(getApplicationContext(), "O testeRegistradorRodando não pode ser inicializado, favor botaoConectar com o padrão.", Toast.LENGTH_LONG).show();

        } else {

            if (conexaoPadraoMKV != null) {
                mensagemNaTela.setText("O testeRegistradorRodando de FotoCélula vai ser iniciado...");
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

    //-----------------PadraoChines
    //TODO - Função de teste de foto celula
    //TODO - função de teste de registrador
    //TODO - função de parar teste
    //TODO - função de pedir o numero de série do padrao
}

