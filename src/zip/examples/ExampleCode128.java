// This is a JPEG image generation example

// TODO merge into Example.java

import java.io.File;
import java.io.IOException;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;

public class ExampleCode128 {

    public static void outputtingBarcode128x1() throws BarcodeException,
            IOException, OutputException {
        Barcode barcode3;
        barcode3 = BarcodeFactory.createCode128("CODE128x1");
        barcode3.setResolution(300);
        BarcodeImageHandler.savePNG(barcode3, new File("Code128-1.png"));
    }

    public static void outputtingBarcode128x2() throws BarcodeException,
            IOException, OutputException {
        Barcode barcode3;
        barcode3 = BarcodeFactory.createCode128("2HgH328355316700000241500100");
        barcode3.setResolution(300);
        BarcodeImageHandler.savePNG(barcode3, new File("Code128-2.png"));
    }

    public static void outputtingBarcode128x3() throws BarcodeException,
            IOException, OutputException {
        Barcode barcode3;
        barcode3 = BarcodeFactory.createCode128("Code128-3");
        barcode3.setResolution(300);
        BarcodeImageHandler.savePNG(barcode3, new File("Code128-3.png"));
    }

    public static void outputtingBarcode128x4() throws BarcodeException,
            IOException, OutputException {
        Barcode barcode3;
        barcode3 = BarcodeFactory
                .createCode128("314159265358979323846264338327950288");
        barcode3.setResolution(300);
        BarcodeImageHandler.savePNG(barcode3, new File("Code128-4.png"));
    }
}