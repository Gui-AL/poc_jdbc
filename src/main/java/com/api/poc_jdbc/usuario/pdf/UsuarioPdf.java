package com.api.poc_jdbc.usuario.pdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import com.itextpdf.kernel.colors.Color;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class UsuarioPdf {

    public UsuarioPdf gerarPdf(HttpServletResponse response, List<Map<String, Object>> params) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            document.add(new Paragraph("Lista de Usuários")
                    .setFont(font)
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            Table tabela = new Table(3).useAllAvailableWidth();

            tabela.addCell(new Cell().add(new Paragraph("ID").setFont(font).setFontSize(12))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            tabela.addCell(new Cell().add(new Paragraph("Nome").setFont(font).setFontSize(12))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            tabela.addCell(new Cell().add(new Paragraph("Status").setFont(font).setFontSize(12))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));

            for (Map<String, Object> p : params) {
                tabela.addCell(new Cell().add(new Paragraph(p.get("id").toString()))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE));

                tabela.addCell(new Cell().add(new Paragraph(p.get("nome").toString()))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE));

                tabela.addCell(new Cell().add(new Paragraph(p.get("ativo").toString()))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE));
            }

            document.add(tabela);

            document.close();
            writer.close();

            byte[] pdfByte = byteArrayOutputStream.toByteArray();
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(pdfByte);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=usuario_lista.pdf");
            outputStream.flush();
            outputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
