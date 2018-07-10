package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 */
@SuppressWarnings("ALL")
public class ConclusaoActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST = 10;
    private static final int ABRIR_PDF = 3;
    private RadioButton FuncionandoCorretamente;
    private RadioButton ComDefeito;
    private RadioButton MedidorIrregularidade;
    private String conclusão = "";

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusao);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();


        FuncionandoCorretamente = findViewById(R.id.FuncionandoCorretamente);
        ComDefeito = findViewById(R.id.ComDefeito);
        MedidorIrregularidade = findViewById(R.id.MedidorIrregularidade);


        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.gerarRelatorio);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Hawk.delete("Conclusao");

                FuncionandoCorretamente = findViewById(R.id.FuncionandoCorretamente);
                ComDefeito = findViewById(R.id.ComDefeito);
                MedidorIrregularidade = findViewById(R.id.MedidorIrregularidade);


                if (FuncionandoCorretamente.isChecked()) {
                    conclusão = "Medidor funcionando corretamente";

                }
                if (ComDefeito.isChecked()) {
                    conclusão = "Medidor com defeito";

                }
                if (MedidorIrregularidade.isChecked()) {
                    conclusão = "Medidor com irregularidade";

                }

                if ((!MedidorIrregularidade.isChecked()) && (!ComDefeito.isChecked()) && (!FuncionandoCorretamente.isChecked())) {
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Não existe opção de conclusão marcado. ", Toast.LENGTH_LONG).show();

                } else {

                    Hawk.put("Conclusao", conclusão);
                    gerarRelatorio();

                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    gerarRelatorio();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void gerarRelatorio() {


        Document document = new Document();
        try {

            String numServico =  Hawk.get("NumeroNotaServico");
            File folder = new File(Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOCUMENTS)  + "/Relatórios_Padrao/Relatorio_"+numServico );

            if (!folder.exists()) {
                folder.mkdirs();

            }

            Bitmap fotoPreRegistrador = Hawk.get("FotoPreTesteRegistrador");
            Bitmap fotoPosRegistrador = Hawk.get("FotoPosTesteRegistrador");
            Bitmap fotoInspecao = Hawk.get("FotoInspecaoVisual");

            //salvando pdf na pasta
            File myFile = new File(folder,numServico + ".pdf");
            OutputStream output = new FileOutputStream(myFile);

            //salvando foto pre registrador na pasta
            ByteArrayOutputStream streamfotoPre = new ByteArrayOutputStream();
            if(fotoPreRegistrador != null){
                fotoPreRegistrador.compress(Bitmap.CompressFormat.PNG, 100, streamfotoPre);

                byte[] bytes = streamfotoPre.toByteArray();;
                String nomeArquivo = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOCUMENTS)  + "/Relatórios_Padrao/FotoPreRegistrador_"+numServico + ".png";

                FileOutputStream fos1 = new FileOutputStream(nomeArquivo);
                fos1.write(bytes);
            }



            //salvando foto pos registrador na pasta
            ByteArrayOutputStream streamfotoPos = new ByteArrayOutputStream();
            if(fotoPosRegistrador != null){
                fotoPosRegistrador.compress(Bitmap.CompressFormat.PNG, 100, streamfotoPos);

                byte[] bytes = streamfotoPos.toByteArray();;
                String nomeArquivo = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOCUMENTS)  + "/Relatórios_Padrao/FotoPosRegistrador_"+numServico + ".png";

                FileOutputStream fos1 = new FileOutputStream(nomeArquivo);
                fos1.write(bytes);
            }

            //salvando foto inspeçao visual na pasta
            ByteArrayOutputStream streamfotoInpecao = new ByteArrayOutputStream();
            if(fotoInspecao!=null){
                Log.d("FEZDIRETORIO", "done");
                fotoInspecao.compress(Bitmap.CompressFormat.PNG, 100, streamfotoInpecao);
                byte[] bytes = streamfotoInpecao.toByteArray();
                File foto = new File(folder,"FotoInspecaoVisual_"+numServico + ".png");
                OutputStream output1 = new FileOutputStream(foto);
                output1.write(bytes);

            }


            //Step 2
            PdfWriter writer = PdfWriter.getInstance(document, output);

            //Step 3
            document.open();

            //Step 4 Add content
            Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
            Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
            Font smallNormal = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);

            document.addTitle("Relatório de Verificação");
            document.addSubject("Avaliação Técnica");


            //--------------------------------------------CABEÇALHO
            Paragraph preface = new Paragraph();
            addEmptyLine(preface, 1);
            //add imagem

            addEmptyLine(preface, 1);
            preface.add(new Paragraph("Relatório de Verificação", catFont));
            preface.setAlignment(Element.ALIGN_CENTER);

            //-----------------------------------------------DADOS GERAIS
            addEmptyLine(preface, 1);
            Paragraph dadosGeraisPara = new Paragraph();
            dadosGeraisPara.setAlignment(Element.ALIGN_JUSTIFIED);

            //Tabela com Dados Gerais
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            PdfPCell c1 = new PdfPCell(new Phrase("Dados do Processo ", subFont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase(" ", smallBold));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);


            Phrase p = new Phrase("Nome do Avaliador : ", smallNormal);
            p.add(new Phrase((String) Hawk.get("NomeAvaliador"), smallNormal));
            c1 = new PdfPCell(p);
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);

            p = new Phrase("Matrícula : ", smallNormal);
            p.add(new Chunk((String) Hawk.get("MatriculaAvaliador"), smallNormal));
            c1 = new PdfPCell(p);
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);

            p = new Phrase("Solicitação da Verificação : ", smallNormal);
            p.add(new Chunk((String) Hawk.get("TipoSolicitação"), smallNormal));

            if (!(String.valueOf(Hawk.get("TOINumero")).equals("null"))) {
                p.add(new Chunk(" - " + Hawk.get("TOINumero"), smallNormal));
            }
            c1 = new PdfPCell(p);
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase(" ", smallNormal));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);

            dadosGeraisPara.add(table);


            //--------------------------------SERVIÇO
            addEmptyLine(dadosGeraisPara, 1);
            Paragraph ServicoPara = new Paragraph();
            ServicoPara.setAlignment(Element.ALIGN_JUSTIFIED);

            //tabela com dados do serviço
            PdfPTable tableServico = new PdfPTable(2);
            tableServico.setWidthPercentage(100);

            p = new Phrase("Serviço ", subFont);
            PdfPCell servicoItem = new PdfPCell(p);
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            servicoItem = new PdfPCell(new Phrase(" ", subFont));
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            p = new Phrase("Nº da nota de Serviço: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("NumeroNotaServico"), smallNormal));
            servicoItem = new PdfPCell(p);
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            p = new Phrase("Nº Invólucro: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("NumeroInvolucro"), smallNormal));
            servicoItem = new PdfPCell(p);
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            p = new Phrase("Nº da Instalação: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("NumeroInstalacaoServico"), smallNormal));
            servicoItem = new PdfPCell(p);
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            p = new Phrase("Nome do Cliente: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("NomeClienteServico"), smallNormal));
            servicoItem = new PdfPCell(p);
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            p = new Phrase("Nº de Documento do Cliente: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("NumDocumentoCliente"), smallNormal));
            servicoItem = new PdfPCell(p);
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            p = new Phrase("Nº de Série: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("NumeroSerieMedidor"), smallNormal));
            servicoItem = new PdfPCell(p);
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            p = new Phrase("Instalação: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("InstalacaoMedidor"), smallNormal));
            servicoItem = new PdfPCell(p);
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            if ((String.valueOf(Hawk.get("RuaCliente")).isEmpty()) || (String.valueOf(Hawk.get("NumeroCliente")).isEmpty()) || (String.valueOf(Hawk.get("BairroCliente")).isEmpty())) {

                servicoItem = new PdfPCell(new Phrase(" ", subFont));
                servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                servicoItem.setBorder(PdfPCell.NO_BORDER);
                tableServico.addCell(servicoItem);

            } else {
                p = new Phrase("Endereço: ", smallNormal);
                if (String.valueOf(Hawk.get("ComplementoCliente")).isEmpty()) {
                    p.add(new Chunk((Hawk.get("RuaCliente")) + " " + (Hawk.get("NumeroCliente")) + ",  " + (Hawk.get("BairroCliente")) + " - " + (Hawk.get("CepCliente")), smallNormal));
                } else {
                    p.add(new Chunk((Hawk.get("RuaCliente")) + " " + (Hawk.get("NumeroCliente")) + " - " + (Hawk.get("ComplementoCliente")) + ", " + (Hawk.get("BairroCliente")) + " - " + (Hawk.get("CepCliente")), smallNormal));
                }
                servicoItem = new PdfPCell(p);
                servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                servicoItem.setBorder(PdfPCell.NO_BORDER);
                tableServico.addCell(servicoItem);
            }


            p = new Phrase("Data-Hora Início: ", smallNormal);
            p.add(new Chunk((Hawk.get("DataFinal")) + " " + (Hawk.get("HoraInicial")), smallNormal));
            servicoItem = new PdfPCell(p);
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            p = new Phrase("Data-Hora Fim : ", smallNormal);
            p.add(new Chunk((Hawk.get("DataFinal")) + " " + (Hawk.get("HoraFinal")), smallNormal));
            servicoItem = new PdfPCell(p);
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            ServicoPara.add(tableServico);

            //---------------------------------MEDIDOR
            addEmptyLine(ServicoPara, 1);
            Paragraph sessaoMedidor = new Paragraph();

            //tabela com dados do serviço
            PdfPTable tabelaMedidor = new PdfPTable(3);
            tabelaMedidor.setWidthPercentage(100);

            PdfPCell medidorItem = new PdfPCell(new Phrase("Identificação do Medidor  ", subFont));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase(" ", smallNormal));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase(" ", smallNormal));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);



            p = new Phrase("Nº Geral:  ", smallNormal);
            p.add(new Chunk((String) Hawk.get("NumeroGeralMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);



            p = new Phrase("Modelo: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("ModeloMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Fabricante: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("FaricanteMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Tensão Nominal (V): ", smallNormal);
            p.add(new Chunk((String) Hawk.get("TensaoNominalMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Corrente Nominal (A): ", smallNormal);
            p.add(new Chunk((String) Hawk.get("CorrenteNominalMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Tipo: ", smallNormal);
            if (Hawk.get("TipoMedidor").toString().equals("")) {
                p.add(new Chunk(" ", smallNormal));
                medidorItem = new PdfPCell(p);
                medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                medidorItem.setBorder(PdfPCell.NO_BORDER);
                tabelaMedidor.addCell(medidorItem);

            } else {
                p.add(new Chunk((String) Hawk.get("TipoMedidor"), smallNormal));
                medidorItem = new PdfPCell(p);
                medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                medidorItem.setBorder(PdfPCell.NO_BORDER);
                tabelaMedidor.addCell(medidorItem);
            }


            p = new Phrase("Kd/Ke: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("KdKeMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("RR: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("rrMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Nº Elementos: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("NumElementosMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Ano de Fabricação: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("AnoFabricacaoMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Classe: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("ClasseMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Fios: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("FiosMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Portaria Inmetro: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("PortariaInmetroMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Leitura Pré Calibração: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("leituraPreTeste"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Leitura Pós Calibração: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("leituraPosTeste"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase(" ", smallNormal);
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            sessaoMedidor.add(tabelaMedidor);

            //--------------------------------------------REAULTADOS DOS ENSAIOS
            addEmptyLine(sessaoMedidor, 3);
            Paragraph resultadoEnsaios = new Paragraph();
            resultadoEnsaios.add(new Paragraph("Resultados do Ensaios", catFont));
            resultadoEnsaios.setAlignment(Element.ALIGN_CENTER);


            //--------------------------------INSPEÇÃO VISUAL
            addEmptyLine(resultadoEnsaios, 1);
            Paragraph inspecaoVisual = new Paragraph();

            //tabela com dados do serviço
            PdfPTable tabelaInspecaoVisual = new PdfPTable(3);
            tabelaInspecaoVisual.setWidthPercentage(100);
            tabelaInspecaoVisual.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell inspecaoVisualItem = new PdfPCell(new Phrase("Inspeção Visual ", subFont));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisualItem = new PdfPCell(new Phrase(" ", smallBold));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisualItem = new PdfPCell(new Phrase(" ", smallBold));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            p = new Phrase("Status: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("Status"), smallNormal));
            inspecaoVisualItem = new PdfPCell(p);
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisualItem = new PdfPCell(new Phrase(" ", smallBold));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisualItem = new PdfPCell(new Phrase(" ", smallBold));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            p = new Phrase("Selo nº1:  ", smallNormal);
            p.add(new Chunk((String) Hawk.get("Selo1"), smallNormal));
            inspecaoVisualItem = new PdfPCell(p);
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            p = new Phrase("Selo nº2:  ", smallNormal);
            p.add(new Chunk((String) Hawk.get("Selo2"), smallNormal));
            inspecaoVisualItem = new PdfPCell(p);
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            p = new Phrase("Selo nº3:  ", smallNormal);
            p.add(new Chunk((String) Hawk.get("Selo3"), smallNormal));
            inspecaoVisualItem = new PdfPCell(p);
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            if (String.valueOf(Hawk.get("ObservacaoInspecaoVisual")).equals("null")) {

                inspecaoVisualItem = new PdfPCell(new Phrase(" ", smallBold));
                inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
                tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            } else {

                p = new Phrase("Observação:  ", smallNormal);
                p.add(new Chunk((String) Hawk.get("ObservacaoInspecaoVisual"), smallNormal));
                inspecaoVisualItem = new PdfPCell(p);
                inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
                tabelaInspecaoVisual.addCell(inspecaoVisualItem);
            }


            inspecaoVisualItem = new PdfPCell(new Phrase(" ", smallBold));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisualItem = new PdfPCell(new Phrase(" ", smallBold));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisual.add(tabelaInspecaoVisual);

            //--------------------------------REGISTRADOR/MOSTRADOR + MARCHA EM VAZIO
            addEmptyLine(inspecaoVisual, 1);
            Paragraph registrador = new Paragraph();
            registrador.setAlignment(Element.ALIGN_JUSTIFIED);

            //tabela com dados do serviço
            PdfPTable tabelaRegistrador = new PdfPTable(2);
            tabelaRegistrador.setWidthPercentage(100);
            tabelaRegistrador.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell MostradorItem = new PdfPCell(new Phrase("Registrador/Mostrador  ", subFont));
            MostradorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            MostradorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaRegistrador.addCell(MostradorItem);

            MostradorItem = new PdfPCell(new Phrase("Marcha em Vazio  ", subFont));
            MostradorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            MostradorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaRegistrador.addCell(MostradorItem);


            p = new Phrase("Status: ", smallNormal); //registrador
            p.add(new Chunk((String) Hawk.get("statusRegistrador"), smallNormal));
            MostradorItem = new PdfPCell(p);
            MostradorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            MostradorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaRegistrador.addCell(MostradorItem);

            p = new Phrase("Status: ", smallNormal); //marcha em vazio
            if (String.valueOf(Hawk.get("ObservaçãoRegistrador")).equals("null")) {
                p.add(new Chunk((String) " ", smallNormal));
            } else {
                p.add(new Chunk((String) Hawk.get("statusMarchaVazio"), smallNormal));
            }

            if (!(String.valueOf(Hawk.get("tempoReprovadoMarchaVazio")).equals("00:00:00"))) {
                p.add(new Chunk((String) Hawk.get("tempoReprovadoMarchaVazio"), smallNormal));
            }
            MostradorItem = new PdfPCell(p);
            MostradorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            MostradorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaRegistrador.addCell(MostradorItem);

            if (String.valueOf(Hawk.get("ObservaçãoRegistrador")).equals("null")) {
                MostradorItem = new PdfPCell(new Phrase(" ", smallBold));
                MostradorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                MostradorItem.setBorder(PdfPCell.NO_BORDER);
                tabelaRegistrador.addCell(MostradorItem);

            } else {
                p = new Phrase("Observação:  ", smallNormal);
                p.add(new Chunk((String) Hawk.get("ObservaçãoRegistrador"), smallNormal));
                MostradorItem = new PdfPCell(p);
                MostradorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                MostradorItem.setBorder(PdfPCell.NO_BORDER);
                tabelaRegistrador.addCell(MostradorItem);
            }
            p = new Phrase(" ", smallNormal);
            MostradorItem = new PdfPCell(p);
            MostradorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            MostradorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaRegistrador.addCell(MostradorItem);

            registrador.add(tabelaRegistrador);


            //--------------------------------CIRCUITO POTENCIAL / ELO DE CALIBRAÇÃO + CONFORMIDADE/ CONDIÇÕES DE CARGA
            addEmptyLine(registrador, 1);
            Paragraph circuitoPotencial = new Paragraph();
            circuitoPotencial.setAlignment(Element.ALIGN_JUSTIFIED);

            //tabela com dados do serviço
            PdfPTable tabelaCircuitoPotecial = new PdfPTable(2);
            tabelaCircuitoPotecial.setWidthPercentage(100);
            tabelaCircuitoPotecial.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell ciecuitoPotencialItem = new PdfPCell(new Phrase("Circuito de Potencial/ Elo de Calibração ", subFont));
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            ciecuitoPotencialItem = new PdfPCell(new Phrase("Conformidade/ Condições de Carga ", subFont));
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            p = new Phrase("Status: ", smallNormal); //Circuito de Potencial/ Elo de Calibração:
            p.add(new Chunk((String) Hawk.get("statusCircuitoPotencial"), smallNormal));
            ciecuitoPotencialItem = new PdfPCell(p);
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            p = new Phrase("Status: ", smallNormal); //"Conformidade/ Condições de Carga:
            p.add(new Chunk((String) Hawk.get("statusConformidade"), smallNormal));
            ciecuitoPotencialItem = new PdfPCell(p);
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            ciecuitoPotencialItem = new PdfPCell(new Phrase(" ", smallBold)); //Circuito de Potencial/ Elo de Calibração:
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            p = new Phrase("Carga Nominal Erro(%): ", smallNormal); //"Conformidade/ Condições de Carga:
            p.add(new Chunk((String) Hawk.get("CargaNominalErroConformidade"), smallNormal));
            ciecuitoPotencialItem = new PdfPCell(p);
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            ciecuitoPotencialItem = new PdfPCell(new Phrase(" ", smallBold)); //Circuito de Potencial/ Elo de Calibração:
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            p = new Phrase("Carga Pequena Erro(%): ", smallNormal); //"Conformidade/ Condições de Carga:
            p.add(new Chunk((String) Hawk.get("CargaPequenaErroConformidade"), smallNormal));
            ciecuitoPotencialItem = new PdfPCell(p);
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            circuitoPotencial.add(tabelaCircuitoPotecial);

            //--------------------------------------------SITUAÇÕES OBSERVADAS
            addEmptyLine(circuitoPotencial, 2);
            Paragraph situacaoObservada = new Paragraph();
            situacaoObservada.add(new Paragraph("Situações Observadas", catFont));
            situacaoObservada.setAlignment(Element.ALIGN_CENTER);

            PdfPTable tabelaSituacoesObservadas = new PdfPTable(1);
            tabelaSituacoesObservadas.setWidthPercentage(100);
            tabelaSituacoesObservadas.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell situacoesObservadasItem = new PdfPCell(new Phrase((String) Hawk.get("SituacoesObservadas"), smallNormal));
            situacoesObservadasItem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            situacoesObservadasItem.setBorder(PdfPCell.NO_BORDER);
            tabelaSituacoesObservadas.addCell(situacoesObservadasItem);

            situacaoObservada.add(tabelaSituacoesObservadas);

            //--------------------------------------------INFORMAÇÕES COMPLEMENTARES
            addEmptyLine(situacaoObservada, 2);
            Paragraph informacoesComplementares = new Paragraph();
            informacoesComplementares.add(new Paragraph("Informações Complementares ", catFont));
            informacoesComplementares.setAlignment(Element.ALIGN_CENTER);

            PdfPTable tabelaInformacoesComplementares = new PdfPTable(1);
            tabelaInformacoesComplementares.setWidthPercentage(100);
            tabelaInformacoesComplementares.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell informacoesComplementaresItem = new PdfPCell(new Phrase((String) Hawk.get("InformacoesComplementares"), smallNormal));
            informacoesComplementaresItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            informacoesComplementaresItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInformacoesComplementares.addCell(informacoesComplementaresItem);


            informacoesComplementares.add(tabelaInformacoesComplementares);

            //--------------------------------------------CONCLUSAO
            addEmptyLine(informacoesComplementares, 2);
            Paragraph conclusao = new Paragraph();
            conclusao.add(new Paragraph("Conclusão: ", catFont));
            conclusao.setAlignment(Element.ALIGN_CENTER);

            PdfPTable tabelaConclusao = new PdfPTable(1);
            tabelaConclusao.setWidthPercentage(100);
            tabelaConclusao.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell conclusaoItem = new PdfPCell(new Phrase((String) Hawk.get("Conclusao"), smallNormal));
            conclusaoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            conclusaoItem.setBorder(PdfPCell.NO_BORDER);
            tabelaConclusao.addCell(conclusaoItem);

            conclusao.add(tabelaConclusao);

            //--------------------------------------------ANEXOS - FOTOS
            addEmptyLine(conclusao, 3);
            Paragraph anexos = new Paragraph();
            PdfPCell anexoItem = null;

            if((fotoPreRegistrador == null) && (fotoInspecao == null) && (fotoPosRegistrador == null)){

                anexos.add(new Paragraph("Anexos: ", catFont));
                anexos.setAlignment(Element.ALIGN_CENTER);

                PdfPTable tabelaAnexos = new PdfPTable(1);
                tabelaAnexos.setWidthPercentage(100);
                tabelaAnexos.setHorizontalAlignment(Element.ALIGN_LEFT);

                anexoItem = new PdfPCell(new Phrase("Não existe nenhum arquivo de anexo ", smallNormal));
                anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                anexoItem.setBorder(PdfPCell.NO_BORDER);
                tabelaAnexos.addCell(anexoItem);

                anexos.add(tabelaAnexos);

            } else{

                anexos.add(new Paragraph("Anexos: ", catFont));
                anexos.setAlignment(Element.ALIGN_CENTER);

                PdfPTable tabelaAnexos = new PdfPTable(2);
                tabelaAnexos.setWidthPercentage(100);
                tabelaAnexos.setHorizontalAlignment(Element.ALIGN_LEFT);

                ByteArrayOutputStream streampreregistrador = new ByteArrayOutputStream();
                if(fotoPreRegistrador!=null){
                    fotoPreRegistrador.compress(Bitmap.CompressFormat.PNG, 100, streampreregistrador);
                    Image imagePreregistrador = Image.getInstance(streampreregistrador.toByteArray());
                    anexoItem = new PdfPCell(imagePreregistrador);
                    anexoItem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
                    anexoItem.setBorder(PdfPCell.NO_BORDER);
                    tabelaAnexos.addCell(anexoItem);

                }



                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if(fotoInspecao!=null){
                    fotoInspecao.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image image = Image.getInstance(stream.toByteArray());
                    anexoItem = new PdfPCell(image);
                    anexoItem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
                    anexoItem.setBorder(PdfPCell.NO_BORDER);
                    tabelaAnexos.addCell(anexoItem);

                }

                if(fotoPreRegistrador!=null){
                    anexoItem = new PdfPCell(new Phrase("Teste de Registrador - Foto Pré Teste ", subFont));
                    anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                    anexoItem.setBorder(PdfPCell.NO_BORDER);
                    tabelaAnexos.addCell(anexoItem);
                } else {
                    anexoItem = new PdfPCell(new Phrase(" ", subFont));
                    anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                    anexoItem.setBorder(PdfPCell.NO_BORDER);
                    tabelaAnexos.addCell(anexoItem);
                }


                anexoItem = new PdfPCell(new Phrase("Inspeção Visual", subFont));
                anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                anexoItem.setBorder(PdfPCell.NO_BORDER);
                tabelaAnexos.addCell(anexoItem);

                anexoItem = new PdfPCell(new Phrase(" ", subFont));
                anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                anexoItem.setBorder(PdfPCell.NO_BORDER);
                tabelaAnexos.addCell(anexoItem);

                anexoItem = new PdfPCell(new Phrase(" ", subFont));
                anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                anexoItem.setBorder(PdfPCell.NO_BORDER);
                tabelaAnexos.addCell(anexoItem);


                ByteArrayOutputStream streamPosregistrador = new ByteArrayOutputStream();
                if(fotoPosRegistrador!=null){
                    fotoPosRegistrador.compress(Bitmap.CompressFormat.PNG, 100, streamPosregistrador);
                    Image imagePosregistrador = Image.getInstance(streamPosregistrador.toByteArray());
                    anexoItem = new PdfPCell(imagePosregistrador);
                    anexoItem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
                    anexoItem.setBorder(PdfPCell.NO_BORDER);
                    tabelaAnexos.addCell(anexoItem);

                }



                anexoItem = new PdfPCell(new Phrase(" ", subFont));
                anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                anexoItem.setBorder(PdfPCell.NO_BORDER);
                tabelaAnexos.addCell(anexoItem);

                if(fotoPosRegistrador!=null){
                    anexoItem = new PdfPCell(new Phrase("Teste de Registrador - Foto Pós Teste ", subFont));
                    anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                    anexoItem.setBorder(PdfPCell.NO_BORDER);
                    tabelaAnexos.addCell(anexoItem);
                } else {
                    anexoItem = new PdfPCell(new Phrase(" ", subFont));
                    anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                    anexoItem.setBorder(PdfPCell.NO_BORDER);
                    tabelaAnexos.addCell(anexoItem);
                }


                anexoItem = new PdfPCell(new Phrase(" ", subFont));
                anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
                anexoItem.setBorder(PdfPCell.NO_BORDER);
                tabelaAnexos.addCell(anexoItem);

                anexos.add(tabelaAnexos);
            }



            //--------------------------------------------ASSINATURA DOS RESPONSÁVEIS
            addEmptyLine(anexos, 6);
            Paragraph assinatura = new Paragraph();


            PdfPTable tabelaAssinaturas = new PdfPTable(2);
            tabelaAssinaturas.setWidthPercentage(100);
            tabelaAssinaturas.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell AssinaturaItem = null;
            AssinaturaItem = new PdfPCell(new Phrase("________________________________________ ", subFont));
            AssinaturaItem.setHorizontalAlignment(Element.ALIGN_CENTER);
            AssinaturaItem.setBorder(PdfPCell.NO_BORDER);
            tabelaAssinaturas.addCell(AssinaturaItem);

            AssinaturaItem = new PdfPCell(new Phrase("________________________________________ ", subFont));
            AssinaturaItem.setHorizontalAlignment(Element.ALIGN_CENTER);
            AssinaturaItem.setBorder(PdfPCell.NO_BORDER);
            tabelaAssinaturas.addCell(AssinaturaItem);

            Log.d("GERENTE", (String) Hawk.get("GerenteAvaliador"));
            AssinaturaItem = new PdfPCell(new Phrase((String) Hawk.get("GerenteAvaliador"), subFont));
            AssinaturaItem.setHorizontalAlignment(Element.ALIGN_CENTER);
            AssinaturaItem.setBorder(PdfPCell.NO_BORDER);
            tabelaAssinaturas.addCell(AssinaturaItem);

            AssinaturaItem = new PdfPCell(new Phrase((String) Hawk.get("NomeAvaliador"), subFont));
            AssinaturaItem.setHorizontalAlignment(Element.ALIGN_CENTER);
            AssinaturaItem.setBorder(PdfPCell.NO_BORDER);
            tabelaAssinaturas.addCell(AssinaturaItem);


            assinatura.add(tabelaAssinaturas);


//----------------------------------------------------------------------------------
            document.add(preface);
            document.add(dadosGeraisPara);
            document.add(ServicoPara);
            document.add(sessaoMedidor);
            document.add(resultadoEnsaios);
            document.add(inspecaoVisual);
            document.add(registrador);
            document.add(circuitoPotencial);
            document.add(situacaoObservada);
            document.add(informacoesComplementares);
            document.add(conclusao);
            document.add(anexos);
            document.add(assinatura);


            document.close();



            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivityForResult(intent, ABRIR_PDF);





        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param view
     */
    public void onCheckboxClicked(View view) {

        FuncionandoCorretamente = findViewById(R.id.FuncionandoCorretamente);
        ComDefeito = findViewById(R.id.ComDefeito);
        MedidorIrregularidade = findViewById(R.id.MedidorIrregularidade);


        switch (view.getId()) {
            case R.id.FuncionandoCorretamente:
                ComDefeito.setChecked(false);
                MedidorIrregularidade.setChecked(false);
                break;

            case R.id.ComDefeito:
                FuncionandoCorretamente.setChecked(false);
                MedidorIrregularidade.setChecked(false);
                break;

            case R.id.MedidorIrregularidade:
                FuncionandoCorretamente.setChecked(false);
                ComDefeito.setChecked(false);
                break;



        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ABRIR_PDF) {
            if (resultCode == RESULT_OK) {

            } else {
                Toast.makeText(getBaseContext(), "Verifique as permissões do aplicativo para abrir o relatório.", Toast.LENGTH_SHORT);
            }
        }
    }

}

