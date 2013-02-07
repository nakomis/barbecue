/*
 * Test Harness for calling examples
 * 
 * TODO add guidelines or instructions similar to the Barbecue Main runner class.
 */

/**
 * @author Werner Keil
 * 
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            System.out.println("Running PNG example...");
            FormatExample.outputtingBarcodeAsPNG();

            System.out.println("Running JPEG example...");
            FormatExample.outputtingBarcodeAsJPEG();

            System.out.println("Running Code128 example...");
            ExampleCode128.outputtingBarcode128x1();
            ExampleCode128.outputtingBarcode128x2();
            ExampleCode128.outputtingBarcode128x3();
            ExampleCode128.outputtingBarcode128x4();
            
            // 2HGH328355316700000259000100
        } catch (Exception er) {
            System.out.println("Error: " + er.getMessage());
            er.printStackTrace();
        }
    }
}
