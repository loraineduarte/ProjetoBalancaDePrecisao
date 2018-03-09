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
    static Intent observacao = new Intent();
    private static final int TIRAR_FOTO = 10207;
    private static final int REQUEST_OBS = 0;
    String status, statusReprovado;
    static String observacaoInspecao = null;
    Bitmap fotoInspecao;
    Spinner opcoesReprovados;
    EditText Selo1, Selo2, Selo3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("INSPEÇÃO VISUAL ", String.valueOf(Hawk.count()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspecao_visual);

        Selo1 =  findViewById(R.id.Selo1);
        Selo2 =  findViewById(R.id.Selo2);
        Selo3 =  findViewById(R.id.Selo3);
        Reprovado = findViewById(R.id.ReprovadoInspecaoVisual);
        Aprovado = findViewById(R.id.AprovadoInspecaoVisual);

        opcoesReprovados =  findViewById(R.id.spinner);
        opcoesReprovados.setEnabled(false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,  R.array.ReprovadoInspeçãoVisual, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcoesReprovados.setAdapter(adapter);
        opcoesReprovados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                statusReprovado = parent.getItemAtPosition(position).toString();
                Log.d("SELECIONADO", statusReprovado);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

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

        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.NextFase4);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Selo1 =  findViewById(R.id.Selo1);
                Selo2 =  findViewById(R.id.Selo2);
                Selo3 =  findViewById(R.id.Selo3);
                Reprovado = findViewById(R.id.ReprovadoInspecaoVisual);
                Aprovado = findViewById(R.id.AprovadoInspecaoVisual);


                if (Aprovado.isChecked()){
                    status = "Selos íntegros";

                }else if (Reprovado.isChecked()){
                    status = "Reprovado";

                }

                observacaoInspecao = observacao.getDataString();
//                Log.d("STATUS", observacaoInspecao);
//                Log.d("STATUS", observacao.getDataString());

                Hawk.delete("Selo1");
                Hawk.delete("Selo2");
                Hawk.delete("Selo3");
                Hawk.delete("Status");
                Hawk.delete("FotoInspecaoVisual");
                Hawk.delete("ObservacaoInspecaoVisual");

                if(!statusReprovado.isEmpty()){
                    status = status +" - "+ statusReprovado;
                }
                if((String.valueOf(Selo1.getText()).isEmpty())||(String.valueOf(Selo2.getText()).isEmpty())|| (String.valueOf(Selo3.getText()).isEmpty())){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Completar os selos da inspeção visual! ", Toast.LENGTH_LONG).show();
                }
//                if(status==null){
//                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Status não selecionado!", Toast.LENGTH_LONG).show();
//                }
                if(fotoInspecao==null){
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Foto de inspeção não tirada!", Toast.LENGTH_LONG).show();

                } else {

                    Hawk.put("Selo1",String.valueOf(Selo1.getText()));
                    Hawk.put("Selo2", String.valueOf(Selo2.getText()));
                    Hawk.put("Selo3", String.valueOf(Selo3.getText()));
                    Hawk.put("Status", status);
                    Hawk.put("FotoInspecaoVisual", fotoInspecao);
                    Hawk.put("ObservacaoInspecaoVisual", observacao.getDataString());

                    abrirRegistrador();
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

        if(resultCode== RESULT_OK){ //add observação
            observacao = data;
            observacaoInspecao = observacao.getDataString();
        }
    }

}
