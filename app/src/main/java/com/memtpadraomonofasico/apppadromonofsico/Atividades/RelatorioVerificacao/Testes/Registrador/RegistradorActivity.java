package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.Bluetooth.PairedDevices;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.Bluetooth.ThreadConexao;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.CircuitoPotencial.CircuitoPotencialActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.util.Objects;

public class RegistradorActivity extends AppCompatActivity {

    private static final String TAG = "Bluetooth";
    private static final int ENABLE_BLUETOOTH = 1;
    private static final int SELECT_PAIRED_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 4;
    private static final int TIRAR_FOTO_ANTES = 10207;
    private static final int TIRAR_FOTO_DEPOIS = 10208;
    private static final int REQUEST_OBS = 1000;
    @SuppressLint("StaticFieldLeak")
    private static TextView textMessage;
    private ThreadConexao conexao;
    private RadioButton aprovado, naoPossibilitaTeste, reprovado;
    private Bitmap fotoResized1, fotoResized2;
    private String status, observacaoRegistrador = " ";
    private Spinner opcoesReprovados;
    @SuppressLint("WrongViewCast") Button fotoDepois, fotoAntes, conectar;
    BluetoothAdapter mBluetoothAdapter = null;

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


        opcoesReprovados = findViewById(R.id.RegistradorSpinner);
        opcoesReprovados.setEnabled(false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ReprovadoRegistrador, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcoesReprovados.setAdapter(adapter);
        opcoesReprovados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ativarBluetooth();


        @SuppressLint("WrongViewCast") Button addObs = findViewById(R.id.addObservacao);
        addObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAddObs();
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



                if (aprovado.isChecked()) {
                    status = "Aprovado";

                } else if (naoPossibilitaTeste.isChecked()) {
                    status = "Não Possibilita Testes";

                } else if (reprovado.isChecked()) {
                    status = "Reprovado";

                }

                if (status.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Status não selecionado!", Toast.LENGTH_LONG).show();

                }
                if (((status.equals("Aprovado"))||(status.equals("Reprovado"))) && ((fotoResized1 == null) || (fotoResized2 == null))){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Fotos não tiradas!", Toast.LENGTH_LONG).show();

                } else {


                    Hawk.put("FotoPreTesteRegistrador", fotoResized1);
                    Hawk.put("FotoPosTesteRegistrador", fotoResized2);
                    Hawk.put("statusRegistrador", status);
                    Hawk.put("ObservaçãoRegistrador", observacaoRegistrador);


                    if(conexao != null){
                        conexao.interrupt();
                    }
                    mBluetoothAdapter.disable();
                    abrirCircuitoPotencial();
                }
            }
        });
    }

    private void ativarBluetooth() {
        // verificando ativação do bluetooth
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
        // Fim - verificando ativação do bluetooth
    }

    public void executarTeste(View view) {

        if(conexao == null){
            Toast.makeText(getApplicationContext(), "O teste não pode ser inicializado, favor conectar com o padrão.", Toast.LENGTH_LONG).show();

        } if (fotoResized1 == null) {
            Toast.makeText(getApplicationContext(), "O teste não pode ser inicializado sem a foto pré-teste.", Toast.LENGTH_LONG).show();

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

    @SuppressLint("HandlerLeak")
    private static final Handler handler = new Handler() {

    };

    public static void escreverTela(final String res) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if(res.startsWith("T")){
                    textMessage.clearComposingText();
                    textMessage.setText("Teste Concluído!");

                } else {
                    textMessage.clearComposingText();
                    textMessage.setText(res);
                }

            }
        });

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

    public void conectarDispositivo(View view) {
        ativarBluetooth();

        if(conexao != null){
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
                    Bitmap fotoAntesRegistrador = (Bitmap) bundle.get("data");
                    fotoResized1 = Bitmap.createScaledBitmap(Objects.requireNonNull(fotoAntesRegistrador), 100, 120, false);

                    if (fotoResized1 != null) {
                        ImageView imageView = findViewById(R.id.FotoAntes);
                        imageView.setImageBitmap(fotoResized1);
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
                    Bitmap fotoDepoisRegistrador = (Bitmap) bundle.get("data");
                    fotoResized2 = Bitmap.createScaledBitmap(Objects.requireNonNull(fotoDepoisRegistrador), 100, 120, false);

                    if (fotoResized2 != null) {

                        ImageView imageView = findViewById(R.id.FotoDepois);
                        imageView.setImageBitmap(fotoResized2);
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

                conexao = new ThreadConexao(macAddress);
                conexao.start();

                if(conexao.isAlive()){
                    textMessage.setText("Conectado com: " + data.getStringExtra("btDevName") + "\n" + data.getStringExtra("btDevAddress"));
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
                opcoesReprovados.setEnabled(false);
                break;

            case R.id.Reprovado:
                aprovado.setChecked(false);
                naoPossibilitaTeste.setChecked(false);
                opcoesReprovados.setEnabled(true);

                break;
        }
    }

    private void abrirCircuitoPotencial() {
        Intent intent = new Intent(this, CircuitoPotencialActivity.class);
        startActivity(intent);
    }
}

