package daw.spring.component;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import daw.spring.model.Device;
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
        document.add(subtitle);
    }

    private PdfPCell createImageCell(URL path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private void addContent(Document document) throws DocumentException {
        // Second parameter is the number of the chapter
        Paragraph titleDevices = new Paragraph("Lista de dispositivos", subFont);
        addEmptyLine(titleDevices, 3);
        document.add(titleDevices);
        document.add(addDevicesTable());
    }

    private PdfPTable addDevicesTable() throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 1, 1});

        PdfPCell c1 = new PdfPCell(new Phrase("Dispositivo"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Tipo"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Serial Number"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        for (Device d : currentHome.getDeviceList()) {

            PdfPCell descriptionCell = new PdfPCell();
            Paragraph p1 = new Paragraph(d.getDescription());
            p1.setAlignment(Element.ALIGN_CENTER);
            descriptionCell.addElement(p1);
            descriptionCell.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
            descriptionCell.setBorder(Rectangle.BODY);
            table.addCell(descriptionCell);

            PdfPCell typeCell = new PdfPCell();
            Paragraph p2 = new Paragraph(d.getType().name());
            p2.setAlignment(Element.ALIGN_CENTER);
            typeCell.addElement(p2);
            typeCell.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
            typeCell.setBorder(Rectangle.BODY);
            table.addCell(typeCell);

            PdfPCell serialNumberCell = new PdfPCell();
            Paragraph p3 = new Paragraph(d.getSerialNumber());
            p3.setAlignment(Element.ALIGN_CENTER);
            serialNumberCell.addElement(p3);
            serialNumberCell.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
            serialNumberCell.setBorder(Rectangle.BODY);
            table.addCell(serialNumberCell);

        }

        return table;
    }


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
        addContent(invoice);
        invoice.close();

        return stream.toByteArray();
    }


}
