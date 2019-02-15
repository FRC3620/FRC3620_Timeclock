
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import javax.print.PrintService;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author wegscd
 */
public class PdfBoxHelloWorld {

    public static void main(String[] args) {
        Throwable boom = null;
        try {
            PdfBoxHelloWorld me = new PdfBoxHelloWorld();
            me.go();
        } catch (RuntimeException ex) {
            boom = ex;
        } catch (PrinterException | IOException | COSVisitorException ex) {
            boom = ex;
        }
        if (boom != null) {
            boom.printStackTrace();
        }
    }

    void go() throws IOException, COSVisitorException, PrinterException {
        // Create a document and add a page to it
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Create a new font object selecting one of the PDF base fonts
        PDFont font = PDType1Font.HELVETICA_BOLD;

        // Start a new content stream which will "hold" the to be created content
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        
        box (contentStream, inches(1.0), inches(1.0), inches(7.5), inches(10.5));
        box (contentStream, inches(2.0), inches(2.0), inches(7.5), inches(10.5));


        // Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
        contentStream.setFont(font, 12);
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(1 * INCHES, 1 * INCHES);
        contentStream.drawString("Hello World");
        contentStream.drawString("Hello World");
        contentStream.drawString("Hello World");
        contentStream.endText();
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(2 * INCHES, 2 * INCHES);
        contentStream.drawString("Hello World");
        contentStream.drawString("Hello World");
        contentStream.drawString("Hello World");
        contentStream.endText();

        // Make sure that the content stream is closed:
        contentStream.close();

        // Save the results and ensure that the document is properly closed:
        document.save("Hello World.pdf");
        document.close();

        PrintService printService = choosePrinter();
        if (printService != null) {
            printPDF("Hello World.pdf", printService);
        }

    }

    float INCHES = 72;

    PrintService choosePrinter() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        if (printJob.printDialog()) {
            return printJob.getPrintService();
        } else {
            return null;
        }
    }

    void printPDF(String fileName, PrintService printService)
            throws IOException, PrinterException {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintService(printService);
        PDDocument doc = PDDocument.load(fileName);
        doc.silentPrint(printerJob);
    }
    
    float inches (double i) {
        return (float) i*72;
    }

    void box(PDPageContentStream contentStream, float x1, float y1, float x2, float y2) throws IOException {
        contentStream.drawLine(x1, y1, x1, y2);
        contentStream.drawLine(x2, y1, x2, y2);
        contentStream.drawLine(x1, y1, x2, y1);
        contentStream.drawLine(x1, y2, x2, y2);
    }
}
