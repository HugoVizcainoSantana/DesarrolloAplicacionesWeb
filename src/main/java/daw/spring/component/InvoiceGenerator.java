package daw.spring.component;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import daw.spring.model.Analytics;
import daw.spring.model.Device;
import daw.spring.model.Home;
import daw.spring.model.User;
import daw.spring.service.AnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Component
public class InvoiceGenerator {
    private static final String COMPANY_NAME = "OnControlHome";
    private static final Logger log = LoggerFactory.getLogger(InvoiceGenerator.class);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private User currentUser;
    private Home currentHome;
    private AnalyticsService analyticsService;
    private URL logoPath;

    @Autowired
    public InvoiceGenerator(ResourceLoader resourceLoader, AnalyticsService service) {
        try {
            logoPath = resourceLoader.getResource("classpath:static/images/icono.png").getURL();
        } catch (IOException e) {
            log.error(e.toString());
        }
        analyticsService = service;

    }

    private static void addMetaData(Document document) {
        document.addTitle("Factura " + COMPANY_NAME);
        document.addKeywords(COMPANY_NAME);
        document.addAuthor(COMPANY_NAME);
        document.addCreator(COMPANY_NAME);
    }

    private static PdfPCell createImageCell(URL path) throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private void addContent(Document document) throws DocumentException {
        // Second parameter is the number of the chapter
        Paragraph titleDevices = new Paragraph("Tienes "
                + currentHome.getDeviceList().size()
                + " dispositivos contratados ", subFont);
        addEmptyLine(titleDevices, 1);
        document.add(titleDevices);
        document.add(addDevicesTable());
        addEmptyLine(titleDevices, 1);
        Paragraph titleAnalytic = new Paragraph("Reporte de Análiticas de hoy", subFont);
        addEmptyLine(titleAnalytic, 1);
        document.add(titleAnalytic);
        document.add(addAnalytic());
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

        c1 = new PdfPCell(new Phrase("Número de serie"));
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
            Paragraph p2 = new Paragraph(d.getType().name(), redFont);
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
            serialNumberCell.setBorder(Rectangle.BOX);
            table.addCell(serialNumberCell);
        }
        return table;
    }

    private PdfPTable addAnalytic() throws DocumentException {
        List<Analytics> analyticsList;
        analyticsList = analyticsService.findAllByDate();

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 1, 1, 1, 1});

        PdfPCell c1 = new PdfPCell(new Phrase("Identificador"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Estado Inicial"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Estado Final"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Dispositivo"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Hora"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);

        for (Analytics a : analyticsList) {

            PdfPCell idCell = new PdfPCell();
            Paragraph p1 = new Paragraph("" + a.getId());
            p1.setAlignment(Element.ALIGN_CENTER);
            idCell.addElement(p1);
            idCell.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
            idCell.setBorder(Rectangle.BODY);
            table.addCell(idCell);

            PdfPCell pStateCell = new PdfPCell();
            Paragraph p2 = new Paragraph("" + a.getPreviousState());
            p2.setAlignment(Element.ALIGN_CENTER);
            pStateCell.addElement(p2);
            pStateCell.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
            pStateCell.setBorder(Rectangle.BODY);
            table.addCell(pStateCell);

            PdfPCell nStateCell = new PdfPCell();
            Paragraph p3 = new Paragraph("" + a.getNewState());
            p3.setAlignment(Element.ALIGN_CENTER);
            nStateCell.addElement(p3);
            nStateCell.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
            nStateCell.setBorder(Rectangle.BODY);
            table.addCell(nStateCell);

            PdfPCell deviceCell = new PdfPCell();
            Paragraph p4 = new Paragraph("" + a.getDevice().getDescription());
            p3.setAlignment(Element.ALIGN_CENTER);
            deviceCell.addElement(p4);
            deviceCell.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
            deviceCell.setBorder(Rectangle.BODY);
            table.addCell(deviceCell);

            PdfPCell dateCell = new PdfPCell();
            Paragraph p5 = new Paragraph("" + a.getDate());
            p3.setAlignment(Element.ALIGN_CENTER);
            dateCell.addElement(p5);
            dateCell.setVerticalAlignment(Element.ALIGN_JUSTIFIED);
            dateCell.setBorder(Rectangle.BOX);
            table.addCell(dateCell);
        }
        return table;
    }

    private void addTitlePage(Document document) throws DocumentException, IOException {
        //Add Logo and title
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 2});
        table.addCell(createImageCell(logoPath));

        PdfPCell titleCell = new PdfPCell();
        Paragraph p = new Paragraph("Factura " + COMPANY_NAME);
        p.setAlignment(Element.ALIGN_LEFT);
        titleCell.addElement(p);
        titleCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        titleCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(titleCell);
        document.add(table);

        Paragraph subtitle = new Paragraph();
        addEmptyLine(subtitle, 1);
        subtitle.add(new Paragraph("Factura para el usuario: " + currentUser.getFirstName() + " " + currentUser.getLastName() + ", creada el " + new Date(), smallBold));
        subtitle.add(new Paragraph("Dirección: " + currentHome.getAddress(), smallBold));
        subtitle.add(new Paragraph("Código postal : " + currentHome.getPostCode(), smallBold));
        addEmptyLine(subtitle, 2);
        document.add(subtitle);
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
