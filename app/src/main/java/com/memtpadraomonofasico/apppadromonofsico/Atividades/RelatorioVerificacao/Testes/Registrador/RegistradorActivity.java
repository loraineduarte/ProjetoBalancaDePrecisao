package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.MarchaVazio.MarchaVazioActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class RegistradorActivity extends AppCompatActivity {


    private static final int TIRAR_FOTO_ANTES = 10207;
    private static final int TIRAR_FOTO_DEPOIS = 10208;
    private static final int REQUEST_OBS = 0;
    Intent observacao = new Intent();
    RadioButton aprovado, naoPossibilitaTeste, reprovado, naoRegistraConsumo, naoRegistraCorretamente, displayApagado;
    Bitmap fotoAntesRegistrador, fotoDepoisRegistrador;
    String status, observacaoRegistrador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Hawk.delete("FotoPreTesteRegistrador");
        Hawk.delete("FotoPosTesteRegistrador");
        Hawk.delete("statusRegistrador");
        Hawk.delete("ObservaçãoRegistrador");
        Log.d("INSPEÇÃO VISUAL ", String.valueOf(Hawk.count()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrador);

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
                aprovado = findViewById(R.id.AprovadoMarchaVazio);
                naoPossibilitaTeste = findViewById(R.id.NaoRealizado);
                reprovado = findViewById(R.id.Reprovado);
                naoRegistraConsumo = findViewById(R.id.NaoregistraConsumo);
                naoRegistraCorretamente = findViewById(R.id.NaoRegistraCorretamente);
                displayApagado = findViewById(R.id.displayApagado);

                if(aprovado.isChecked()){
                    status = "Aprovado";

                } else if (naoPossibilitaTeste.isChecked()){
                    status = "Não Possibilita Testes";

                } else if (reprovado.isChecked()){
                    status = "Reprovado";

                } else if (naoRegistraConsumo.isChecked()){
                    status = "Não Registra Consumo";

                } else if (naoRegistraCorretamente.isChecked()){
                    status = "Não Registra Corretamente";

                } else if (displayApagado.isChecked()){
                    status = "Display Apagado";

                }

                observacaoRegistrador = observacao.getDataString();

                abrirMarchaVazio();
            }
        });


    }



    private void abrirMarchaVazio() {

        Hawk.put("FotoPreTesteRegistrador",fotoAntesRegistrador);
        Hawk.put("FotoPosTesteRegistrador", fotoDepoisRegistrador);
        Hawk.put("statusRegistrador", status);
        Hawk.put("ObservaçãoRegistrador", observacaoRegistrador);

        Intent intent = new Intent(this, MarchaVazioActivity.class);
        startActivity(intent);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //trazer as observações que o usuário adicionou
        //salvar a foto que foi tirada

        if (requestCode == TIRAR_FOTO_ANTES) {
            if (resultCode == RESULT_OK) {

                if (data != null) {
                    Toast.makeText(getBaseContext(), "A imagem foi capturada", Toast.LENGTH_SHORT);
                    Bundle bundle = data.getExtras();
                    fotoAntesRegistrador = (Bitmap) bundle.get("data");

                    if (fotoAntesRegistrador != null) {

                        ImageView imageView = (ImageView) findViewById(R.id.FotoAntes);
                        imageView.setImageBitmap(fotoAntesRegistrador);
                    } else {

                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getBaseContext(), "A captura foi cancelada",
                            Toast.LENGTH_SHORT);
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
                    fotoDepoisRegistrador = (Bitmap) bundle.get("data");

                    if (fotoDepoisRegistrador != null) {

                        ImageView imageView = (ImageView) findViewById(R.id.FotoDepois);
                        imageView.setImageBitmap(fotoDepoisRegistrador);
                    } else {

                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getBaseContext(), "A captura foi cancelada",
                            Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getBaseContext(), "A câmera foi fechada",
                            Toast.LENGTH_SHORT);
                }
            }
        }
        if(requestCode== REQUEST_OBS){

            if (resultCode == RESULT_OK) { //add observação
                observacao = data;
                Log.d("Registrador", String.valueOf(observacao));
            }
        }
    }
}
