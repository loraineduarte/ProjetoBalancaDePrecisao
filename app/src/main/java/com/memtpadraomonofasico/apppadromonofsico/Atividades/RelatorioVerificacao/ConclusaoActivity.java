package com.memtpadraomonofasico.apppadromonofsico.Atividades.RelatorioVerificacao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 */
@SuppressWarnings("ALL")
public class ConclusaoActivity extends AppCompatActivity {

    private RadioButton FuncionandoCorretamente;
    private RadioButton ComDefeito;
    private RadioButton MedidorIrregularidade;
    private RadioButton Reintegracao;
    private RadioButton garantia;
    private String conclusão = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusao);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();


        FuncionandoCorretamente = findViewById(R.id.FuncionandoCorretamente);
        ComDefeito = findViewById(R.id.ComDefeito);
        MedidorIrregularidade = findViewById(R.id.MedidorIrregularidade);
        Reintegracao = findViewById(R.id.Reintegracao);
        garantia = findViewById(R.id.garantia);


        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.gerarRelatorio);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Hawk.delete("Conclusao");

                FuncionandoCorretamente = findViewById(R.id.FuncionandoCorretamente);
                ComDefeito = findViewById(R.id.ComDefeito);
                MedidorIrregularidade = findViewById(R.id.MedidorIrregularidade);
                Reintegracao = findViewById(R.id.Reintegracao);
                garantia = findViewById(R.id.garantia);


                if (FuncionandoCorretamente.isChecked()) {
                    conclusão = "Medidor funcionando corretamente";

                }
                if (ComDefeito.isChecked()) {
                    conclusão = "Medidor com defeito";

                }
                if (MedidorIrregularidade.isChecked()) {
                    conclusão = "Medidor com irregularidade";

                }
                if (Reintegracao.isChecked()) {
                    conclusão = "Reintegração";

                }
                if (garantia.isChecked()) {
                    conclusão = "Garantia";

                }

                if ((!garantia.isChecked()) && (!Reintegracao.isChecked()) && (!MedidorIrregularidade.isChecked()) && (!ComDefeito.isChecked()) && (!FuncionandoCorretamente.isChecked())) {
                    Toast.makeText(getApplicationContext(), "Sessão incompleta - Não existe opção de conclusão marcado. ", Toast.LENGTH_LONG).show();

                } else {

                    Hawk.put("Conclusao", conclusão);

                    gerarRelatorio();
                }

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
            }

            //Create time stamp
            Date date = new Date();
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(date);
            File myFile = new File(pdfFolder + timeStamp + ".pdf");
            OutputStream output = new FileOutputStream(myFile);

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

            p = new Phrase("Nº de Série: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("NumeroSerieMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Nº Geral:  ", smallNormal);
            p.add(new Chunk((String) Hawk.get("NumeroGeralMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Instalação: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("InstalacaoMedidor"), smallNormal));
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
            p.add(new Chunk((String) Hawk.get("TipoMedidor"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

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

            p = new Phrase("Leitura de Retirada: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("LeituraRetirada"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Leitura de Calibração: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("LeitursCalibracao"), smallNormal));
            medidorItem = new PdfPCell(p);
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            p = new Phrase("Leitura Pós Calibração: ", smallNormal);
            p.add(new Chunk((String) Hawk.get("LeituraPosCalibracao"), smallNormal));
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
            p.add(new Chunk((String) Hawk.get("statusMarchaVazio"), smallNormal));
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
            anexos.add(new Paragraph("Anexos: ", catFont));
            anexos.setAlignment(Element.ALIGN_CENTER);

            PdfPTable tabelaAnexos = new PdfPTable(2);
            tabelaAnexos.setWidthPercentage(100);
            tabelaAnexos.setHorizontalAlignment(Element.ALIGN_LEFT);

           
            Bitmap fotoPreRegistrador = Hawk.get("FotoPreTesteRegistrador");
            ByteArrayOutputStream streampreregistrador = new ByteArrayOutputStream();
            PdfPCell anexoItem = null;
            if(fotoPreRegistrador!=null){
                fotoPreRegistrador.compress(Bitmap.CompressFormat.PNG, 100, streampreregistrador);
                Image imagePreregistrador = Image.getInstance(streampreregistrador.toByteArray());
                anexoItem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
                anexoItem.setBorder(PdfPCell.NO_BORDER);
                tabelaAnexos.addCell(anexoItem);
                anexoItem = new PdfPCell(imagePreregistrador);
            }

           

            

            Bitmap fotoInspecao = Hawk.get("FotoInspecaoVisual");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            fotoInspecao.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());

            anexoItem = new PdfPCell(image);
            anexoItem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
            anexoItem.setBorder(PdfPCell.NO_BORDER);
            tabelaAnexos.addCell(anexoItem);

            anexoItem = new PdfPCell(new Phrase("Teste de Registrador - Foto Pré Teste ", subFont));
            anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            anexoItem.setBorder(PdfPCell.NO_BORDER);
            tabelaAnexos.addCell(anexoItem);

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

            Bitmap fotoPosRegistrador = Hawk.get("FotoPosTesteRegistrador");
            ByteArrayOutputStream streamPosregistrador = new ByteArrayOutputStream();
            fotoPosRegistrador.compress(Bitmap.CompressFormat.PNG, 100, streamPosregistrador);
            Image imagePosregistrador = Image.getInstance(streamPosregistrador.toByteArray());

            anexoItem = new PdfPCell(imagePosregistrador);
            anexoItem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
            anexoItem.setBorder(PdfPCell.NO_BORDER);
            tabelaAnexos.addCell(anexoItem);

            anexoItem = new PdfPCell(new Phrase(" ", subFont));
            anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            anexoItem.setBorder(PdfPCell.NO_BORDER);
            tabelaAnexos.addCell(anexoItem);

            anexoItem = new PdfPCell(new Phrase("Teste de Registrador - Foto Pós Teste ", subFont));
            anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            anexoItem.setBorder(PdfPCell.NO_BORDER);
            tabelaAnexos.addCell(anexoItem);

            anexoItem = new PdfPCell(new Phrase(" ", subFont));
            anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            anexoItem.setBorder(PdfPCell.NO_BORDER);
            tabelaAnexos.addCell(anexoItem);

            anexos.add(tabelaAnexos);

            //--------------------------------------------ASSINATURA DOS RESPONSÁVEIS
//            addEmptyLine(informacoesComplementares, 1);
//            Paragraph assinatura = new Paragraph();
//            conclusao.add(new Paragraph("Conclusão: ", catFont));
//            conclusao.setAlignment(Element.ALIGN_CENTER);
//
//            PdfPTable tabelaConclusao = new PdfPTable(1);
//            tabelaConclusao.setWidthPercentage(90);
//            tabelaConclusao.setHorizontalAlignment(Element.ALIGN_LEFT);
//
//            PdfPCell conclusaoItem = new PdfPCell(new Phrase("COLOCAR A conclusao", subFont));
//            conclusaoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
//            conclusaoItem.setBorder(PdfPCell.NO_BORDER);
//            tabelaConclusao.addCell(informacoesComplementaresItem);
//
//            conclusao.add(tabelaConclusao);


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


            document.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    /**
     * @param view
     */
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

