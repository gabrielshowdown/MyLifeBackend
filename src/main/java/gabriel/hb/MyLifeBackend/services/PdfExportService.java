package gabriel.hb.MyLifeBackend.services;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import gabriel.hb.MyLifeBackend.entities.ThemeHistory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfExportService {

    public byte[] generateThemePdf(ThemeHistory theme) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // 1. Fontes
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.DARK_GRAY);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Color.BLACK);

            // 2. Cabeçalho (Tema e Data)
            Paragraph title = new Paragraph("Tema: " + theme.getThemeName(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            if (theme.getCelebrationDate() != null) {
                String formattedDate = theme.getCelebrationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                Paragraph date = new Paragraph("Data da Celebração: " + formattedDate, textFont);
                date.setAlignment(Element.ALIGN_CENTER);
                document.add(date);
            }
            
            document.add(new Paragraph(" ")); // Espaço em branco

            // 3. Tabela de 4 colunas
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{1f, 1f, 1f, 1f}); // Larguras iguais

            // Cabeçalhos da Tabela baseados no seu anexo
            String[] headers = {"1 Leituras:", "2 Leituras:", "3 Leituras:", "Evangelhos:"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBorder(Rectangle.BOTTOM); // Linha apenas embaixo para ficar limpo
                cell.setPaddingBottom(8f);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
            }

            // 4. Preenchendo as linhas (Precisamos saber qual é a lista mais longa)
            List<String> list1 = theme.getPrimeiraLeitura();
            List<String> list2 = theme.getSegundaLeitura();
            List<String> list3 = theme.getTerceiraLeitura();
            List<String> list4 = theme.getEvangelhos();

            int maxSize = Math.max(Math.max(list1.size(), list2.size()), Math.max(list3.size(), list4.size()));

            for (int i = 0; i < maxSize; i++) {
                table.addCell(createCell(i < list1.size() ? list1.get(i) : "", textFont));
                table.addCell(createCell(i < list2.size() ? list2.get(i) : "", textFont));
                table.addCell(createCell(i < list3.size() ? list3.get(i) : "", textFont));
                table.addCell(createCell(i < list4.size() ? list4.get(i) : "", textFont));
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace(); // Trate a exceção no seu ambiente de produção
        }

        return baos.toByteArray();
    }

    // Método auxiliar para formatar as células sem bordas (visual mais limpo)
    private PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER); // Remove as bordas tipo "Excel" 
        cell.setPaddingTop(6f);
        cell.setPaddingBottom(6f);
        return cell;
    }
}