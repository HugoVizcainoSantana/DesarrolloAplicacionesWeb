package daw.spring.component;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import daw.spring.model.Home;
import daw.spring.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

@Component
public class InvoiceGenerator {
    private static String FILE = "c:/temp/FirstPdf.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static User currentUser;
    private static Home currentHome;
    private final ResourceLoader resourceLoader;
    private URL logoPath;

    @Autowired
    public InvoiceGenerator(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        try {
            logoPath = resourceLoader.getResource("classpath:static/images/icono.png").getURL();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addMetaData(Document document) {
        document.addTitle("Factura OnControlHome");
        document.addKeywords("OnControlHome");
        document.addAuthor("OnControlHome");
        document.addCreator("OnControlHome");
    }

    private void addTitlePage(Document document) throws DocumentException, IOException {
        //Add Logo and title
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 2});
        table.addCell(createImageCell(logoPath));

        PdfPCell titleCell = new PdfPCell();
        Paragraph p = new Paragraph("Factura OnControlHome");
        p.setAlignment(Element.ALIGN_LEFT);
        titleCell.addElement(p);
        titleCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        titleCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(titleCell);
        document.add(table);

        Paragraph subtitle = new Paragraph();
        addEmptyLine(subtitle, 1);
        subtitle.add(new Paragraph("Factura para el usuario: " + currentUser.getFirstName() + " " + currentUser.getLastName() + ", creada el " + new Date(), smallBold));
        addEmptyLine(subtitle, 3);
        subtitle.add(new Paragraph("This document describes something which is very important ", smallBold));
        document.add(subtitle);
    }

    private PdfPCell createImageCell(URL path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    /*
        private void addContent(Document document) throws DocumentException {
            Anchor anchor = new Anchor("First Chapter", catFont);
            anchor.setName("First Chapter");

            // Second parameter is the number of the chapter
            Chapter catPart = new Chapter(new Paragraph(anchor), 1);

            Paragraph subPara = new Paragraph("Subcategory 1", subFont);
            Section subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph("Hello"));

            subPara = new Paragraph("Subcategory 2", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph("Paragraph 1"));
            subCatPart.add(new Paragraph("Paragraph 2"));
            subCatPart.add(new Paragraph("Paragraph 3"));

            // add a list
            createList(subCatPart);
            Paragraph paragraph = new Paragraph();
            addEmptyLine(paragraph, 5);
            subCatPart.add(paragraph);

            // add a table
            createTable(subCatPart);

            // now add all this to the document
            document.add(catPart);

            // Next section
            anchor = new Anchor("Second Chapter", catFont);
            anchor.setName("Second Chapter");

            // Second parameter is the number of the chapter
            catPart = new Chapter(new Paragraph(anchor), 1);

            subPara = new Paragraph("Subcategory", subFont);
            subCatPart = catPart.addSection(subPara);
            subCatPart.add(new Paragraph("This is a very important message"));

            // now add all this to the document
            document.add(catPart);

        }

        private void createTable(Section subCatPart)
                throws BadElementException {
            PdfPTable table = new PdfPTable(3);

            // t.setBorderColor(BaseColor.GRAY);
            // t.setPadding(4);
            // t.setSpacing(4);
            // t.setBorderWidth(1);

            PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Table Header 2"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Table Header 3"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            table.setHeaderRows(1);

            table.addCell("1.0");
            table.addCell("1.1");
            table.addCell("1.2");
            table.addCell("2.1");
            table.addCell("2.2");
            table.addCell("2.3");

            subCatPart.add(table);

        }

        private void createList(Section subCatPart) {
            List list = new List(true, false, 10);
            list.add(new ListItem("First point"));
            list.add(new ListItem("Second point"));
            list.add(new ListItem("Third point"));
            subCatPart.add(list);
        }
    */
    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public byte[] generateInvoiceAsStream(Home home, User user) throws DocumentException, IOException {
        currentHome = home;
        currentUser = user;
        Document invoice = new Document();
        //Prepare Stream for download
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PdfWriter.getInstance(invoice, stream);
        //Write document
        invoice.open();
        addMetaData(invoice);
        addTitlePage(invoice);
        //addContent(invoice);
        invoice.close();

        return stream.toByteArray();
    }


}
