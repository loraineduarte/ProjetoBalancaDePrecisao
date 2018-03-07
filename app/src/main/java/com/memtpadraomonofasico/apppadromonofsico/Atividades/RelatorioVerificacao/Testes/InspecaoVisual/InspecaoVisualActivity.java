package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.InspecaoVisual;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao.Testes.Registrador.RegistradorActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

public class InspecaoVisualActivity extends AppCompatActivity {

    private static final String TAG = "Inspeção Visual";
    private RadioButton Reprovado, Aprovado;
    Intent observacao = new Intent();
    private static final int TIRAR_FOTO = 10207;
    private static final int REQUEST_OBS = 0;
    String selo1, selo2, selo3, status, observacaoInspecao;
    Bitmap fotoInspecao;
    Spinner opcoesReprovados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        Log.d("INSPEÇÃO VISUAL ", String.valueOf(Hawk.count()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspecao_visual);

        final EditText Selo1 = (EditText) findViewById(R.id.Selo1);
        selo1 = String.valueOf(Selo1.getText());

        final EditText Selo2 = (EditText) findViewById(R.id.Selo2);
        selo2 = String.valueOf(Selo2.getText());

        final EditText Selo3 = (EditText) findViewById(R.id.Selo3);
        selo3 = String.valueOf(Selo3.getText());

        Reprovado = findViewById(R.id.ReprovadoInspecaoVisual);
        Aprovado = findViewById(R.id.AprovadoInspecaoVisual);


        if (Aprovado.isChecked()){
            status = "Selos íntegros";

        }else if (Reprovado.isChecked()){
            status = "Reprovado";

        }

        opcoesReprovados = (Spinner) findViewById(R.id.spinner);
        opcoesReprovados.setEnabled(false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,  R.array.ReprovadoInspeçãoVisual, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
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

        observacaoInspecao = observacao.getDataString();

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
                Hawk.delete("Selo1");
                Hawk.delete("Selo2");
                Hawk.delete("Selo3");
                Hawk.delete("Status");
                Hawk.delete("FotoInspecaoVisual");
                Hawk.delete("ObservacaoInspecaoVisual");

                Hawk.put("Selo1",selo1);
                Hawk.put("Selo2", selo2);
                Hawk.put("Selo3", selo3);
                Hawk.put("Status", status);
                Hawk.put("FotoInspecaoVisual", fotoInspecao);
                Hawk.put("ObservacaoInspecaoVisual", observacaoInspecao);

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



        Log.d(TAG, "Teste de Registrador");
        Intent intent = new Intent(this, RegistradorActivity.class);
        startActivity(intent);
    }

    public void onCheckboxClicked(View view) {

        switch (view.getId()) {
            case R.id.AprovadoInspecaoVisual:
                Reprovado.setChecked(false);
                opcoesReprovados.setEnabled(false);


                break;

            case R.id.ReprovadoInspecaoVisual:
                Aprovado.setChecked(false);
                opcoesReprovados.setEnabled(true);

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
