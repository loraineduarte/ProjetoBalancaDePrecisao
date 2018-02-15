package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoVisual;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.SelecionarMedidorActivity;
import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador.RegistradorActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;

public class InspecaoVisualActivity extends AppCompatActivity {

    private static final String TAG = "Inspeção Visual";
    private RadioButton VioladosInpecao, AusentesInspecao, ReconstituidosInspecao, NaoPadronizadosInpecao, Reprovado;
    Intent observacao = new Intent();
    private static final int TIRAR_FOTO = 10207;
    private static final int REQUEST_OBS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspecao_visual);

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase4);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirRegistrador();
            }
        });

        @SuppressLint("WrongViewCast") Button previous = findViewById(R.id.PreviousFase3);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirMedidor();
            }
        });

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

        VioladosInpecao = findViewById(R.id.VioladosInpecao);
        AusentesInspecao = findViewById(R.id.AusentesInspecao);
        ReconstituidosInspecao = findViewById(R.id.ReconstituidosInspecao);
        NaoPadronizadosInpecao = findViewById(R.id.NaoPadronizadosInpecao);
        Reprovado = findViewById(R.id.ReprovadoInspecaoVisual);


        //clean the editText
        final EditText Selo1 = (EditText) findViewById(R.id.Selo1);
        Selo1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Selo1.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

        final EditText Selo2 = (EditText) findViewById(R.id.Selo2);
        Selo2.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Selo2.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

        final EditText Selo3 = (EditText) findViewById(R.id.Selo3);
        Selo3.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Selo3.setText("", TextView.BufferType.EDITABLE);
                }

            }

        });

        final EditText Selo4 = (EditText) findViewById(R.id.Selo4);
        Selo4.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Selo4.setText("", TextView.BufferType.EDITABLE);
                }

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

    private void abrirMedidor() {
        Log.d(TAG, "Selecionar Medidor");
        Intent intent = new Intent(this, SelecionarMedidorActivity.class);
        startActivity(intent);
    }

    private void abrirRegistrador() {

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
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");

                    if(bitmap!=null){
                        Toast.makeText(getBaseContext(), "A imagem foi capturada", Toast.LENGTH_SHORT);
                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
                        imageView.setImageBitmap(bitmap);
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
