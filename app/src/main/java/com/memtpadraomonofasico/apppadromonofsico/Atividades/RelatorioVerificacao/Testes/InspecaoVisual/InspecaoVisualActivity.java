package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoVisual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador.RegistradorActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class InspecaoVisualActivity extends AppCompatActivity {

    private static final String TAG = "Inspeção Visual";
    private RadioButton VioladosInpecao, AusentesInspecao, ReconstituidosInspecao, NaoPadronizadosInpecao, Reprovado, Aprovado;
    Intent observacao = new Intent();
    private static final int TIRAR_FOTO = 10207;
    private static final int REQUEST_OBS = 0;
    String selo1, selo2, selo3, selo4, status, observacaoInspecao;
    Bitmap fotoInspecao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Hawk.delete("Selo1");
        Hawk.delete("Selo2");
        Hawk.delete("Selo3");
        Hawk.delete("Selo4");
        Hawk.delete("Status");
        Hawk.delete("FotoInspecaoVisual");
        Hawk.delete("ObservacaoInspecaoVisual");

        Log.d("INSPEÇÃO VISUAL ", String.valueOf(Hawk.count()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspecao_visual);

        //clean the editText
        final EditText Selo1 = (EditText) findViewById(R.id.Selo1);
        selo1 = String.valueOf(Selo1.getText());
        Selo1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Selo1.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

        final EditText Selo2 = (EditText) findViewById(R.id.Selo2);
        selo2 = String.valueOf(Selo2.getText());
        Selo2.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Selo2.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

        final EditText Selo3 = (EditText) findViewById(R.id.Selo3);
        selo3 = String.valueOf(Selo3.getText());
        Selo3.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Selo3.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

        final EditText Selo4 = (EditText) findViewById(R.id.Selo4);
        selo4 = String.valueOf(Selo4.getText());
        Selo4.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Selo4.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

        VioladosInpecao = findViewById(R.id.VioladosInpecao);
        AusentesInspecao = findViewById(R.id.AusentesInspecao);
        ReconstituidosInspecao = findViewById(R.id.ReconstituidosInspecao);
        NaoPadronizadosInpecao = findViewById(R.id.NaoPadronizadosInpecao);
        Reprovado = findViewById(R.id.ReprovadoInspecaoVisual);
        Aprovado = findViewById(R.id.AprovadoInspecaoVisual);

        if(VioladosInpecao.isChecked()){
            status = "Violados";

        } else if (AusentesInspecao.isChecked()){
            status = "Ausentes";

        } else if (ReconstituidosInspecao.isChecked()){
            status = "Reconstituidos";

        } else if (NaoPadronizadosInpecao.isChecked()){
            status = "Não Padronizados";

        } else if (Reprovado.isChecked()){
            status = "Reprovado";

        } else if (Aprovado.isChecked()){
            status = "Aprovado";

        }

        observacaoInspecao = observacao.getDataString();
         //fotoInspecao


        @SuppressLint("WrongViewCast") Button addObs = findViewById(R.id.addObservacao);
        addObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAddObs();
            }
        });

        @SuppressLint("WrongViewCast") Button foto = findViewById(R.id.tirarFotoInspecao);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarFoto();
            }
        });

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase4);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirRegistrador();
            }
        });

    }

    private void tirarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TIRAR_FOTO);
    }

    private void abrirAddObs() {
        Log.d(TAG, "Adicionar Observação - Inspeção Visual ");
        Intent intent = new Intent(this, ObservacaoInspecaoVisualActivity.class);
        startActivityForResult(intent, REQUEST_OBS);
    }


    private void abrirRegistrador() {

        Hawk.put("Selo1",selo1);
        Hawk.put("Selo2", selo2);
        Hawk.put("Selo3", selo3);
        Hawk.put("Selo4", selo4);
        Hawk.put("Status", status);
        Hawk.put("FotoInspecaoVisual", fotoInspecao);
        Hawk.put("ObservacaoInspecaoVisual", observacaoInspecao);

        Log.d(TAG, "Teste de Registrador");
        Intent intent = new Intent(this, RegistradorActivity.class);
        startActivity(intent);
    }

    public void onCheckboxClicked(View view) {

        switch (view.getId()) {
            case R.id.AprovadoInspecaoVisual:
                Reprovado.setChecked(false);
                VioladosInpecao.setEnabled(false);
                AusentesInspecao.setEnabled(false);
                ReconstituidosInspecao.setEnabled(false);
                NaoPadronizadosInpecao.setEnabled(false);

                break;

            case R.id.ReprovadoInspecaoVisual:
                Aprovado.setChecked(false);
                VioladosInpecao.setEnabled(true);
                AusentesInspecao.setEnabled(true);
                ReconstituidosInspecao.setEnabled(true);
                NaoPadronizadosInpecao.setEnabled(true);
                break;

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //trazer as observações que o usuário adicionou
        //salvar a foto que foi tirada

        if (requestCode == TIRAR_FOTO) {
            if (resultCode == RESULT_OK) {

                if(data != null) {
                    Toast.makeText(getBaseContext(), "A imagem foi capturada", Toast.LENGTH_SHORT);
                    Bundle bundle = data.getExtras();
                    fotoInspecao = (Bitmap) bundle.get("data");

                    if(fotoInspecao!=null){

                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        imageView.setImageBitmap(fotoInspecao);

                    }
                    else{

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

        Log.d(TAG, String.valueOf(data));
        if(resultCode== RESULT_OK){ //add observação
            observacao = data;
            Log.d(TAG, String.valueOf(observacao));
        }
    }




}
