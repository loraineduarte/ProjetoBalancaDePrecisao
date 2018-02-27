package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConclusaoActivity extends AppCompatActivity {

    RadioButton FuncionandoCorretamente, ComDefeito, MedidorIrregularidade, Reintegracao, garantia;
    String FuncionandoCorretamenteStatus, ComDefeitoStatus, MedidorIrregularidadeStatus, ReintegracaoStatus, garantiaStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusao);

        Log.d("CONCLUSAO", String.valueOf(Hawk.count()));
        Log.d("1", String.valueOf(Hawk.get("statusConformidade")));


        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.gerarRelatorio);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Hawk.delete("FuncionandoCorretamente");
                Hawk.delete("ComDefeito");
                Hawk.delete("MedidorIrregularidade");
                Hawk.delete("Reintegracao");
                Hawk.delete("garantia");

                FuncionandoCorretamente = findViewById(R.id.FuncionandoCorretamente);
                ComDefeito = findViewById(R.id.ComDefeito);
                MedidorIrregularidade = findViewById(R.id.MedidorIrregularidade);
                Reintegracao = findViewById(R.id.Reintegracao);
                garantia = findViewById(R.id.garantia);



                if(FuncionandoCorretamente.isChecked()){
                    FuncionandoCorretamenteStatus = "Medidor funcionando corretamente";
                    Hawk.put("FuncionandoCorretamente",FuncionandoCorretamenteStatus);

                }
                if (ComDefeito.isChecked()){
                    ComDefeitoStatus = "Medidor com defeito";
                    Hawk.put("ComDefeito",ComDefeitoStatus);

                }
                if (MedidorIrregularidade.isChecked()){
                    MedidorIrregularidadeStatus = "Medidor com irregularidade";
                    Hawk.put("MedidorIrregularidade",MedidorIrregularidadeStatus);

                }
                if (Reintegracao.isChecked()){
                    ReintegracaoStatus = "Reintegração";
                    Hawk.put("Reintegracao",ReintegracaoStatus);

                }
                if (garantia.isChecked()){
                    garantiaStatus = "Garantia";
                    Hawk.put("garantia",garantiaStatus);

                }

                gerarRelatorio();
            }
        });

    }

    private void gerarRelatorio() {

        Document document = new Document();
        try {

            File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "RelatorioDeVerificacao");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
                Log.i("Conclusao", "Pdf Directory created");
            }

            //Create time stamp
            Date date = new Date() ;
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(date);

            File myFile = new File(pdfFolder + timeStamp + ".pdf");

            OutputStream output = new FileOutputStream(myFile);


            //Step 2
            PdfWriter.getInstance(document, output);

            //Step 3
            document.open();

            //Step 4 Add content
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));
            document.add(new Paragraph(String.valueOf(Hawk.get("statusConformidade"))));





            //Step 5: Close the document
            document.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

        }
        catch(DocumentException de) {
            System.err.println(de.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        document.close();


    }

    public void onCheckboxClicked(View view) {

        FuncionandoCorretamente = findViewById(R.id.FuncionandoCorretamente);
        ComDefeito = findViewById(R.id.ComDefeito);
        MedidorIrregularidade = findViewById(R.id.MedidorIrregularidade);
        Reintegracao = findViewById(R.id.Reintegracao);
        garantia = findViewById(R.id.garantia);


        switch (view.getId()) {
            case R.id.FuncionandoCorretamente:
                ComDefeito.setChecked(false);
                MedidorIrregularidade.setChecked(false);
                Reintegracao.setChecked(false);
                garantia.setChecked(false);
                break;

            case R.id.ComDefeito:
                FuncionandoCorretamente.setChecked(false);
                MedidorIrregularidade.setChecked(false);
                Reintegracao.setChecked(false);
                garantia.setChecked(false);
                break;

            case R.id.MedidorIrregularidade:
                FuncionandoCorretamente.setChecked(false);
                ComDefeito.setChecked(false);
                Reintegracao.setChecked(false);
                garantia.setChecked(false);
                break;

            case R.id.Reintegracao:
                FuncionandoCorretamente.setChecked(false);
                ComDefeito.setChecked(false);
                MedidorIrregularidade.setChecked(false);
                garantia.setChecked(false);
                break;

            case R.id.garantia:
                FuncionandoCorretamente.setChecked(false);
                ComDefeito.setChecked(false);
                MedidorIrregularidade.setChecked(false);
                Reintegracao.setChecked(false);
                break;

        }
    }
}

