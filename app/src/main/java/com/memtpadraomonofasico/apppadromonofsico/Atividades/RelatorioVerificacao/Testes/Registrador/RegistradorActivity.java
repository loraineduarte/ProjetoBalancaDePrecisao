package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.MarchaVazio.MarchaVazioActivity;

import com.orhanobut.hawk.Hawk;

import java.util.Objects;

public class RegistradorActivity extends AppCompatActivity {

    private static final String TAG = "Bluetooth";
    private static String macAddress = "";

    public static int ENABLE_BLUETOOTH = 1;
    public static int SELECT_PAIRED_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 4;

    private static final int TIRAR_FOTO_ANTES = 10207;
    private static final int TIRAR_FOTO_DEPOIS = 10208;

    Intent observacao = new Intent();
    private AlertDialog dialogRegistrador;
    static TextView textMessage;

    ThreadConexao conexao;

    private static final int REQUEST_OBS = 1000;
    private RadioButton aprovado;
    private RadioButton naoPossibilitaTeste;
    private RadioButton reprovado;
    private Bitmap fotoResized1;
    private Bitmap fotoResized2;
    private String status;
    private String observacaoRegistrador = " ";
    private Spinner opcoesReprovados;
    private Spinner dispositivos;

    private static String dados;
    private static String res;

    private static byte[] pacote = new  byte[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Hawk.delete("FotoPreTesteRegistrador");
        Hawk.delete("FotoPosTesteRegistrador");
        Hawk.delete("statusRegistrador");
        Hawk.delete("ObservaçãoRegistrador");
        Log.d("INSPEÇÃO VISUAL ", String.valueOf(Hawk.count()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrador);
        textMessage = (TextView) findViewById(R.id.textView6);
        aprovado = findViewById(R.id.tampasolidarizada);
        naoPossibilitaTeste = findViewById(R.id.sinaisCarbonizacao);
        reprovado = findViewById(R.id.Reprovado);

        if(aprovado.isChecked()){
            status = "Aprovado";

        } else if (naoPossibilitaTeste.isChecked()){
            status = "Não Possibilita Testes";

        } else if (reprovado.isChecked()){
            status = "Reprovado";

        }

        observacaoRegistrador = observacao.getDataString();

        opcoesReprovados = (Spinner) findViewById(R.id.RegistradorSpinner);

        opcoesReprovados.setEnabled(false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,  R.array.ReprovadoRegistrador, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcoesReprovados.setAdapter(adapter);
        opcoesReprovados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                status = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        observacaoRegistrador = observacao.getDataString();

        // Início - verificando ativação do bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            textMessage.setText("Bluetooth não está funcionando.");
        }
        else{
            textMessage.setText("Bluetooth está funcionando.");
            if (!mBluetoothAdapter.isEnabled()) {
                Log.d(TAG, "ATIVANDO BLUETOOTH");
                Intent enableBtIntent  = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                textMessage.setText("Solicitando ativação do Bluetooth...");
            }
            else{
                textMessage.setText("Bluetooth Ativado.");
            }
        }
        // Fim - verificando ativação do bluetooth

        @SuppressLint("WrongViewCast") Button addObs = findViewById(R.id.addObservacao);
        addObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAddObs();
            }
        });

        @SuppressLint("WrongViewCast") Button fotoAntes = findViewById(R.id.buttonFotoAntes);
        fotoAntes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFotoAntes();
            }
        });

        @SuppressLint("WrongViewCast") Button fotoDepois = findViewById(R.id.buttonFotoDepois);
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

                aprovado = findViewById(R.id.tampasolidarizada);
                naoPossibilitaTeste = findViewById(R.id.sinaisCarbonizacao);
                reprovado = findViewById(R.id.Reprovado);


                if(aprovado.isChecked()){
                    status = "Aprovado";

                } else if (naoPossibilitaTeste.isChecked()){
                    status = "Não Possibilita Testes";

                } else if (reprovado.isChecked()){
                    status = "Reprovado";

                }
                if(status.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Status não selecionado!", Toast.LENGTH_LONG).show();

                }if((fotoResized1==null) || (fotoResized2==null)){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Fotos não tiradas!", Toast.LENGTH_LONG).show();

                } else {

                    Log.d("OBSERVACAO", observacaoRegistrador);

                    Hawk.put("FotoPreTesteRegistrador",fotoResized1);
                    Hawk.put("FotoPosTesteRegistrador", fotoResized2);
                    Hawk.put("statusRegistrador", status);
                    Hawk.put("ObservaçãoRegistrador", observacaoRegistrador);

                    abrirMarchaVazio();
                }
            }
        });
    }

    public void executarTeste(View view){

        if (conexao.isAlive()){
            textMessage.setText(".. Conectado ..");
        }

        byte[] pacote = new byte[10];

        pacote[0] = ('I' & 0xFF);
        pacote[1] = ('B' & 0xFF);
        pacote[2] = (byte)(0 & 0xFF);
        pacote[3] = (byte)(0 & 0xFF);
        pacote[4] = (byte)(90 & 0xFF);
        pacote[5] = (byte)(175 & 0xFF);
        pacote[6] = (byte)(0 & 0xFF);
        pacote[7] = (byte)(10 & 0xFF);
        pacote[8] = (byte)(0 & 0xFF);
        pacote[9] = (byte)(0 & 0xFF);

        conexao.write(pacote);
    }

    public void turnOfDialogFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        RegistradorDialogFragment rdf = (RegistradorDialogFragment) getSupportFragmentManager().findFragmentByTag("dialog");
        if(rdf != null){
            rdf.dismiss();
            ft.remove(rdf);
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

    public void conectarDispositivo(View view){
        Intent searchPairedDevicesIntent = new Intent(this, PairedDevices.class);
        startActivityForResult(searchPairedDevicesIntent, SELECT_PAIRED_DEVICE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == TIRAR_FOTO_ANTES) {
            if (resultCode == RESULT_OK) {

                if (data != null) {
                    Toast.makeText(getBaseContext(), "A imagem foi capturada", Toast.LENGTH_SHORT);
                    Bundle bundle = data.getExtras();
                    assert bundle != null;
                    Bitmap fotoAntesRegistrador = (Bitmap) bundle.get("data");
                    fotoResized1 = Bitmap.createScaledBitmap(Objects.requireNonNull(fotoAntesRegistrador), 100, 120,false);

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
                    fotoResized2 = Bitmap.createScaledBitmap(Objects.requireNonNull(fotoDepoisRegistrador), 100, 120,false);

                    if (fotoResized2 != null) {

                        ImageView imageView = findViewById(R.id.FotoDepois);
                        imageView.setImageBitmap(fotoResized2);
                    }

                }  else {
                    Toast.makeText(getBaseContext(), "A câmera foi fechada", Toast.LENGTH_SHORT);
                }
            }
        }
        if(requestCode== REQUEST_OBS){
            if (resultCode == RESULT_OK) {
                observacaoRegistrador = data.getStringExtra("RESULT_STRING");
            }
        }

        if(requestCode == ENABLE_BLUETOOTH) {
            if(resultCode == RESULT_OK) {
                textMessage.setText("Bluetooth ativado.");
            }
            else {
                textMessage.setText("Bluetooth não ativado.");
            }
        }
        else if(requestCode == SELECT_PAIRED_DEVICE) {
            if(resultCode == RESULT_OK) {
                textMessage.setText("Você selecionou " + data.getStringExtra("btDevName") + "\n"
                        + data.getStringExtra("btDevAddress"));
                macAddress = data.getStringExtra("btDevAddress");

                conexao = new ThreadConexao(macAddress);

                if (conexao.isAlive()){
                    conexao.cancel();
                }
                conexao.start();
            }
            else {
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

    private void abrirMarchaVazio() {
        Intent intent = new Intent(this, MarchaVazioActivity.class);
        startActivity(intent);
    }
}

