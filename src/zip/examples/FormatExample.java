import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;

public class FormatExample {

    public static void usingBarbecueAsSwingComponent() throws BarcodeException {
        JPanel panel = new JPanel();

        // Always get a Barcode from the BarcodeFactory
        Barcode barcode = BarcodeFactory.createCode128A("My Barcode");

        /*
         * Because Barcode extends Component, you can use it just like any other
         * Swing component. In this case, we can add it straight into a panel
         * and it will be drawn and layed out according to the layout of the
         * panel.
         */
        panel.add(barcode);
    }

    public static void drawingBarcodeDirectToGraphics()
            throws BarcodeException, OutputException {
        // Always get a Barcode from the BarcodeFactory
        Barcode barcode = BarcodeFactory.createCode128A("My Barcode");

        // We are created an image from scratch here, but for printing in Java,
        // your print renderer should have a Graphics internally anyway
        BufferedImage image = new BufferedImage(500, 500,
                BufferedImage.TYPE_BYTE_GRAY);
        // We need to cast the Graphics from the Image to a Graphics2D:
        // this is OK
        Graphics2D g = (Graphics2D) image.getGraphics();

        // Barcode supports a direct draw method to Graphics2D that lets you
        // position the barcode on the canvas
        barcode.draw(g, 10, 56);
    }

    public static void outputtingBarcodeAsPNG() throws BarcodeException,
            IOException, OutputException {
        // get a Barcode from the BarcodeFactory
        Barcode barcode = BarcodeFactory.createCode128A("My Barcode");
        File f = new File("mybarcode.png");
        // Let the barcode image handler do the hard work
        BarcodeImageHandler.savePNG(barcode, f);
    }

    public static void outputtingBarcodeAsJPEG() throws BarcodeException,
            IOException, OutputException {
        Barcode barcode3 = BarcodeFactory.createCode128C("123456");
        barcode3.setResolution(300);
        BarcodeImageHandler.saveJPEG(barcode3, new File("123456.jpg"));
    }

}
