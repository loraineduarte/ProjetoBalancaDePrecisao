package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.MarchaVazio.MarchaVazioActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class RegistradorActivity extends AppCompatActivity {


    private static final int TIRAR_FOTO_ANTES = 10207;
    private static final int TIRAR_FOTO_DEPOIS = 10208;
    private static final int REQUEST_OBS = 0;
    Intent observacao = new Intent();
    RadioButton aprovado, naoPossibilitaTeste, reprovado;
    Bitmap fotoAntesRegistrador, fotoDepoisRegistrador, fotoResized1, fotoResized2;
    String status, observacaoRegistrador;
    Spinner opcoesReprovados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("INSPEÇÃO VISUAL ", String.valueOf(Hawk.count()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrador);

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

        opcoesReprovados = findViewById(R.id.RegistradorSpinner);
        opcoesReprovados.setEnabled(false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,  R.array.ReprovadoRegistrador, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcoesReprovados.setAdapter(adapter);
        opcoesReprovados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                status = parent.getItemAtPosition(position).toString();
                Log.d("SELECIONADO", status);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        observacaoRegistrador = observacao.getDataString();

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

                observacaoRegistrador = observacao.getDataString();
                if(status.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Status não selecionado!", Toast.LENGTH_LONG).show();

                }if((fotoResized1==null) || (fotoResized2==null)){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Fotos não tiradas!", Toast.LENGTH_LONG).show();

                } else {

                    Hawk.put("FotoPreTesteRegistrador",fotoResized1);
                    Hawk.put("FotoPosTesteRegistrador", fotoResized2);
                    Hawk.put("statusRegistrador", status);
                    Hawk.put("ObservaçãoRegistrador", observacaoRegistrador);

                    abrirMarchaVazio();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //trazer as observações que o usuário adicionou
        //salvar a foto que foi tirada

        if (requestCode == TIRAR_FOTO_ANTES) {
            if (resultCode == RESULT_OK) {

                if (data != null) {
                    Toast.makeText(getBaseContext(), "A imagem foi capturada", Toast.LENGTH_SHORT);
                    Bundle bundle = data.getExtras();
                    fotoAntesRegistrador = (Bitmap) bundle.get("data");
                    fotoResized1 = Bitmap.createScaledBitmap(fotoAntesRegistrador, 100, 120,false);

                    if (fotoResized1 != null) {

                        ImageView imageView = findViewById(R.id.FotoAntes);
                        imageView.setImageBitmap(fotoResized1);
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
                    fotoResized2 = Bitmap.createScaledBitmap(fotoDepoisRegistrador, 100, 120,false);

                    if (fotoResized2 != null) {

                        ImageView imageView = findViewById(R.id.FotoDepois);
                        imageView.setImageBitmap(fotoResized2);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
