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

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.orhanobut.hawk.Hawk.*;

public class ConclusaoActivity extends AppCompatActivity {

    RadioButton FuncionandoCorretamente, ComDefeito, MedidorIrregularidade, Reintegracao, garantia;
    String FuncionandoCorretamenteStatus, ComDefeitoStatus, MedidorIrregularidadeStatus, ReintegracaoStatus, garantiaStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conclusao);

        Log.d("CONCLUSAO", String.valueOf(count()));
        Log.d("1", String.valueOf(get("statusConformidade")));


        @SuppressLint("WrongViewCast") Button next = findViewById(R.id.gerarRelatorio);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                delete("FuncionandoCorretamente");
                delete("ComDefeito");
                delete("MedidorIrregularidade");
                delete("Reintegracao");
                delete("garantia");

                FuncionandoCorretamente = findViewById(R.id.FuncionandoCorretamente);
                ComDefeito = findViewById(R.id.ComDefeito);
                MedidorIrregularidade = findViewById(R.id.MedidorIrregularidade);
                Reintegracao = findViewById(R.id.Reintegracao);
                garantia = findViewById(R.id.garantia);



                if(FuncionandoCorretamente.isChecked()){
                    FuncionandoCorretamenteStatus = "Medidor funcionando corretamente";
                    put("FuncionandoCorretamente",FuncionandoCorretamenteStatus);

                }
                if (ComDefeito.isChecked()){
                    ComDefeitoStatus = "Medidor com defeito";
                    put("ComDefeito",ComDefeitoStatus);

                }
                if (MedidorIrregularidade.isChecked()){
                    MedidorIrregularidadeStatus = "Medidor com irregularidade";
                    put("MedidorIrregularidade",MedidorIrregularidadeStatus);

                }
                if (Reintegracao.isChecked()){
                    ReintegracaoStatus = "Reintegração";
                    put("Reintegracao",ReintegracaoStatus);

                }
                if (garantia.isChecked()){
                    garantiaStatus = "Garantia";
                    put("garantia",garantiaStatus);

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
            table.setWidthPercentage(90);

            PdfPCell c1 = new PdfPCell(new Phrase("Dados do Processo: ", subFont));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase(" " ,smallBold));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);

            Phrase p = new Phrase("Nome do Avaliador : " , smallBold);
            p.add(new Chunk((String) Hawk.get("NomeAvaliador"), smallNormal));
            c1 = new PdfPCell(p);
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Matrícula : " + get("MatriculaAvaliador"),smallBold ));
            c1.setHorizontalAlignment(Element.ALIGN_LEFT);
            c1.setBorder(PdfPCell.NO_BORDER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Solicitação: " + get("TipoSolicitação") + " "+ get("TOINumbero"),smallBold ));
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
            tableServico.setWidthPercentage(90);

            PdfPCell servicoItem = new PdfPCell(new Phrase("Serviço:  ", subFont));
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            servicoItem = new PdfPCell(new Phrase(" ", subFont));
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            servicoItem = new PdfPCell(new Phrase("Nº da nota de Serviço: " , smallBold));
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            servicoItem = new PdfPCell(new Phrase("Nº Invólucro:", smallBold));
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            servicoItem = new PdfPCell(new Phrase("Nº da Instalação:" , smallBold));
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            servicoItem = new PdfPCell(new Phrase("Nome do Cliente: ", smallBold));
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            servicoItem = new PdfPCell(new Phrase("Nº de Documento do Cliente: ", smallBold));
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            servicoItem = new PdfPCell(new Phrase("Endereço : ", smallBold));
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            servicoItem = new PdfPCell(new Phrase("Data-Hora Início : ", smallBold));
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            servicoItem = new PdfPCell(new Phrase("Data-Hora Fim : ", smallBold));
            servicoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            servicoItem.setBorder(PdfPCell.NO_BORDER);
            tableServico.addCell(servicoItem);

            ServicoPara.add(tableServico);

            //---------------------------------MEDIDOR
            addEmptyLine(ServicoPara, 1);
            Paragraph sessaoMedidor = new Paragraph();

            //tabela com dados do serviço
            PdfPTable tabelaMedidor = new PdfPTable(3);
            tabelaMedidor.setWidthPercentage(90);

            PdfPCell medidorItem = new PdfPCell(new Phrase("Identificação do Medidor:  ", subFont));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase(" ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase(" ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Nº de Série: ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Nº Geral:  ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Instalação: ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Modelo: ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Fabricante: ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Tensão Nominal (V): ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Corrente Nominal (A): ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Tipo: ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Kd/Ke: ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("RR: ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Nº Elementos: ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Ano de Fabricação: ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Classe: ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Fios: ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Portaria Inmetro: ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Leitura de Retirada", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Leitura de Calibração ", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            medidorItem = new PdfPCell(new Phrase("Leitura Pós Calibração", smallBold));
            medidorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            medidorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaMedidor.addCell(medidorItem);

            sessaoMedidor.add(tabelaMedidor);

            //--------------------------------------------REAULTADOS DOS ENSAIOS
            addEmptyLine(sessaoMedidor, 1);
            Paragraph resultadoEnsaios = new Paragraph();
            resultadoEnsaios.add(new Paragraph("Resultados do Ensaios", catFont));
            resultadoEnsaios.setAlignment(Element.ALIGN_CENTER);


            //--------------------------------INSPEÇÃO VISUAL
            addEmptyLine(resultadoEnsaios, 1);
            Paragraph inspecaoVisual = new Paragraph();

            //tabela com dados do serviço
            PdfPTable tabelaInspecaoVisual = new PdfPTable(3);
            tabelaInspecaoVisual.setWidthPercentage(90);
            tabelaInspecaoVisual.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell inspecaoVisualItem = new PdfPCell(new Phrase("Inspeção Visual: ", subFont));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisualItem = new PdfPCell(new Phrase(" " , smallBold));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisualItem = new PdfPCell(new Phrase(" " , smallBold));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisualItem = new PdfPCell(new Phrase("Status: " , smallBold));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisualItem = new PdfPCell(new Phrase(" " , smallBold));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisualItem = new PdfPCell(new Phrase(" " , smallBold));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisualItem = new PdfPCell(new Phrase("Selo nº1:  " , smallBold));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisualItem = new PdfPCell(new Phrase("Selo nº2:  ", smallBold));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisualItem = new PdfPCell(new Phrase("Selo nº3:  ", smallBold));
            inspecaoVisualItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            inspecaoVisualItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInspecaoVisual.addCell(inspecaoVisualItem);

            inspecaoVisualItem = new PdfPCell(new Phrase("Observação:  ", smallBold));
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
            tabelaRegistrador.setWidthPercentage(90);
            tabelaRegistrador.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell MostradorItem = new PdfPCell(new Phrase("Registrador/Mostrador:  ", subFont));
            MostradorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            MostradorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaRegistrador.addCell(MostradorItem);

            MostradorItem = new PdfPCell(new Phrase("Marcha em Vazio  ", subFont));
            MostradorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            MostradorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaRegistrador.addCell(MostradorItem);


            MostradorItem = new PdfPCell(new Phrase("Status: " , smallBold)); //registrador
            MostradorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            MostradorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaRegistrador.addCell(MostradorItem);

            MostradorItem = new PdfPCell(new Phrase("Status: " , smallBold)); //marcha em vazio
            MostradorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            MostradorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaRegistrador.addCell(MostradorItem);

            MostradorItem = new PdfPCell(new Phrase("Observação: " , smallBold));//registrador
            MostradorItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            MostradorItem.setBorder(PdfPCell.NO_BORDER);
            tabelaRegistrador.addCell(MostradorItem);

            MostradorItem = new PdfPCell(new Phrase("Observação: " , smallBold));//marcha em vazio
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
            tabelaCircuitoPotecial.setWidthPercentage(90);
            tabelaCircuitoPotecial.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell ciecuitoPotencialItem = new PdfPCell(new Phrase("Circuito de Potencial/ Elo de Calibração: ", subFont));
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            ciecuitoPotencialItem = new PdfPCell(new Phrase("Conformidade/ Condições de Carga: ", subFont));
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            ciecuitoPotencialItem = new PdfPCell(new Phrase("Status: " , smallBold));//Circuito de Potencial/ Elo de Calibração:
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            ciecuitoPotencialItem = new PdfPCell(new Phrase("Status: " , smallBold)); //"Conformidade/ Condições de Carga:
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            ciecuitoPotencialItem = new PdfPCell(new Phrase(" " , smallBold)); //Circuito de Potencial/ Elo de Calibração:
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            ciecuitoPotencialItem = new PdfPCell(new Phrase("Carga Nominal Erro(%):" , smallBold)); //"Conformidade/ Condições de Carga:
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            ciecuitoPotencialItem = new PdfPCell(new Phrase(" " , smallBold)); //Circuito de Potencial/ Elo de Calibração:
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            ciecuitoPotencialItem = new PdfPCell(new Phrase("Carga Pequena Erro(%): " , smallBold)); //"Conformidade/ Condições de Carga:
            ciecuitoPotencialItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            ciecuitoPotencialItem.setBorder(PdfPCell.NO_BORDER);
            tabelaCircuitoPotecial.addCell(ciecuitoPotencialItem);

            circuitoPotencial.add(tabelaCircuitoPotecial);

            //--------------------------------------------SITUAÇÕES OBSERVADAS
            addEmptyLine(circuitoPotencial, 1);
            Paragraph situacaoObservada = new Paragraph();
            situacaoObservada.add(new Paragraph("Situações Observadas", catFont));
            situacaoObservada.setAlignment(Element.ALIGN_CENTER);

            PdfPTable tabelaSituacoesObservadas = new PdfPTable(2);
            tabelaSituacoesObservadas.setWidthPercentage(90);
            tabelaSituacoesObservadas.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell situacoesObservadasItem = new PdfPCell(new Phrase("COLOCAR AS SITUAÇÕES ", subFont));
            situacoesObservadasItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            situacoesObservadasItem.setBorder(PdfPCell.NO_BORDER);
            tabelaSituacoesObservadas.addCell(situacoesObservadasItem);

            situacoesObservadasItem = new PdfPCell(new Phrase("Observações: ", subFont));
            situacoesObservadasItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            situacoesObservadasItem.setBorder(PdfPCell.NO_BORDER);
            tabelaSituacoesObservadas.addCell(situacoesObservadasItem);


            situacaoObservada.add(tabelaSituacoesObservadas);

            //--------------------------------------------INFORMAÇÕES COMPLEMENTARES
            addEmptyLine(situacaoObservada, 1);
            Paragraph informacoesComplementares = new Paragraph();
            informacoesComplementares.add(new Paragraph("Informações Complementares", catFont));
            informacoesComplementares.setAlignment(Element.ALIGN_CENTER);

            PdfPTable tabelaInformacoesComplementares = new PdfPTable(1);
            tabelaInformacoesComplementares.setWidthPercentage(90);
            tabelaInformacoesComplementares.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell informacoesComplementaresItem = new PdfPCell(new Phrase("COLOCAR AS SITUAÇÕES ", subFont));
            informacoesComplementaresItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            informacoesComplementaresItem.setBorder(PdfPCell.NO_BORDER);
            tabelaInformacoesComplementares.addCell(informacoesComplementaresItem);


            informacoesComplementares.add(tabelaInformacoesComplementares);

            //--------------------------------------------CONCLUSAO
            addEmptyLine(informacoesComplementares, 1);
            Paragraph conclusao = new Paragraph();
            conclusao.add(new Paragraph("Conclusão: ", catFont));
            conclusao.setAlignment(Element.ALIGN_CENTER);

            PdfPTable tabelaConclusao = new PdfPTable(1);
            tabelaConclusao.setWidthPercentage(90);
            tabelaConclusao.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell conclusaoItem = new PdfPCell(new Phrase("COLOCAR A conclusao", subFont));
            conclusaoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            conclusaoItem.setBorder(PdfPCell.NO_BORDER);
            tabelaConclusao.addCell(informacoesComplementaresItem);

            conclusao.add(tabelaConclusao);

            //--------------------------------------------ANEXOS - FOTOS
            addEmptyLine(situacaoObservada, 1);
            Paragraph anexos = new Paragraph();
            anexos.add(new Paragraph("Anexos: ", catFont));
            anexos.setAlignment(Element.ALIGN_CENTER);

            PdfPTable tabelaAnexos = new PdfPTable(2);
            tabelaAnexos.setWidthPercentage(100);
            tabelaAnexos.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell anexoItem = new PdfPCell(new Phrase("COLOCAR FOTO1 ", subFont));
            anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            anexoItem.setBorder(PdfPCell.NO_BORDER);
            tabelaAnexos.addCell(anexoItem);

            anexoItem = new PdfPCell(new Phrase("COLOCAR FOTO 2 ", subFont));
            anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            anexoItem.setBorder(PdfPCell.NO_BORDER);
            tabelaAnexos.addCell(anexoItem);

            anexoItem = new PdfPCell(new Phrase("COLOCAR LEGENDA 1 ", subFont));
            anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            anexoItem.setBorder(PdfPCell.NO_BORDER);
            tabelaAnexos.addCell(anexoItem);

            anexoItem = new PdfPCell(new Phrase("COLOCAR LEGENDA 2 ", subFont));
            anexoItem.setHorizontalAlignment(Element.ALIGN_LEFT);
            anexoItem.setBorder(PdfPCell.NO_BORDER);
            tabelaAnexos.addCell(anexoItem);

            anexos.add(tabelaAnexos);

//            //--------------------------------------------ASSINATURA DOS RESPONSÁVEIS
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

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }


    private static void createTableDadosGerais(Section subCatPart) throws BadElementException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(90);
        PdfPCell c1 = new PdfPCell(new Phrase("Nome do Avaliador : " + get("NomeAvaliador") ));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Matrícula : " + get("MatriculaAvaliador") ));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Solicitação: " + get("TipoSolicitação") + " "+ get("TOINumbero") ));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);




        subCatPart.add(table);

    }

    private static void createTableServiço(Section subCatPart) throws BadElementException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(90);
        PdfPCell c1 = new PdfPCell(new Phrase("Nº da nota de Serviço: "));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(" "));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Nº Invólucro:"));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Nº da Instalação:"));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Nome do Cliente: "));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Nº de Documento do Cliente: "));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(" "));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(" "));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);


        c1 = new PdfPCell(new Phrase("Endereço"));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(" "));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Rua/Avenida: "));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(" "));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Nº: "));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Complemento: "));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Bairro: "));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("CEP: "));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c1.setBorder(PdfPCell.NO_BORDER);
        table.addCell(c1);


        subCatPart.add(table);

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
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

