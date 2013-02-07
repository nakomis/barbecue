package net.sourceforge.barbecue;
/***********************************************************************************************************************
Copyright (c) 2003, International Barcode Consortium
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this list of
      conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of
      conditions and the following disclaimer in the documentation and/or other materials
      provided with the distribution.
 * Neither the name of the International Barcode Consortium nor the names of any contributors may be used to endorse
      or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
 ***********************************************************************************************************************/

///CLOVER:OFF



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.sourceforge.barbecue.formatter.FormattingException;
import net.sourceforge.barbecue.formatter.SVGFormatter;

/**
 * Contributed by Ryan Martell.
 * 
 * @author rmartell
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public class BarcodeTool extends JFrame {
    private static final String[] barcodeData = {
            "Code128",
            "Creates a Code 128 barcode that dynamically switches between character sets to "
                    + "give the smallest possible encoding. This will encode  all numeric characters, "
                    + "upper and lower case alpha characters and control characters  from the standard "
                    + "ASCII character set. The size of the barcode created will be the  smallest possible "
                    + "for the given data, and use of this \"optimal\" encoding will  generally give smaller "
                    + "barcodes than any of the other 3 \"vanilla\" encodings.",
            "Code128A",
            "Creates a Code 128 barcode using the A character set. This will encode  all numeric characters, upper case alpha characters and control characters  from the standard ASCII character set. The Code 128 barcode supports on-the-fly  character set changes using the appropriate code change symbol. The type A barcode  also supports a one character 'shift' to set B.",
            "Code128B",
            "Creates a Code 128 barcode using the B character set. This will encode  all numeric characters and upper and lower case alpha characters  from the standard ASCII character set. The Code 128 barcode supports on-the-fly  character set changes using the appropriate code change symbol. The type B barcode  also supports a one character 'shift' to set A.",
            "Code128C",
            "Creates a Code 128 barcode using the C character set. This will encode  only numeric characters in a double density format (e.g. 1 digit in the barcode  encodes two digits in the data). The Code 128 barcode supports on-the-fly  character set changes using the appropriate code change symbol. No shifts are  possible with the type C barcode.",
            "EAN128",
            "Creates an EAN128 barcode",
            "EAN13",
            "Creates an EAN13 barcode",
            "GlobalTradeItemNumber",
            "Creates a Global Treade Item Number (GTIN) based on the UCC/EAN 128 symbology.",
            "SCC14ShippingCode",
            "Creates an SCC-14 shipping code number based on the UCC/EAN 128 symbology.",
            "ShipmentIdentificationNumber",
            "Creates a shipment identification number based on the UCC/EAN 128 symbology.",
            "SSCC18",
            "Creates an SSCC-18 number based on the UCC/EAN 128 symbology.",
            "UCC128",
            "Creates a UCC 128 barcode. This will encode numeric characters and must  include the correct application identifier for the application domain in which  you wish to use the barcode.",
            "USPS",
            "Creates a US Postal Service barcode based on the UCC/EAN 128 symbology.",
            "Code39", "Creates a Code 39 barcode.", "Codabar",
            "Creates a Codabar barcode.", "PDF417",
            "Creates a PDF417 (2 dimensional) barcode." };

    private JLabel                appIDLabel;
    private JTextField            appIDTextField;
    private JComboBox             barcodeSelection;
    private JTextField            dataField;
    private JPanel                barcodePanel;

    private String[] getBarcodeTypes() {
        String[] result = new String[barcodeData.length / 2];
        for (int i = 0; i < barcodeData.length; i += 2) {
            result[i / 2] = barcodeData[i];
        }
        return result;
    }

    public BarcodeTool() {
        createGUI();
        addQuitListener();
        addDataListener();
    }

    private void addDataListener() {
        dataField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent evt) {
                try {
                    syncBarcode();
                } catch (BarcodeException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }

            public void removeUpdate(DocumentEvent evt) {
                try {
                    syncBarcode();
                } catch (BarcodeException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }

            public void changedUpdate(DocumentEvent e) {
                // Nothing to do
            }
        });
    }

    private void setBarcode(final Barcode bar) {
        barcodePanel.removeAll();
        barcodePanel.add(bar, BorderLayout.CENTER);
        barcodePanel.updateUI();
    }

    void syncBarcode() throws BarcodeException {
        String currentValue = (String) barcodeSelection.getSelectedItem();
        String barcodeText = dataField.getText();

        if ((barcodeText == null) || (barcodeText.length() == 0)) {
            barcodeText = " ";
        }

        boolean appIDVisible = currentValue.equals("UCC128");
        if (appIDVisible != appIDTextField.isVisible()) {
            appIDTextField.setVisible(appIDVisible);
            appIDLabel.setVisible(appIDVisible);
        }

        if (currentValue.equals("UCC128")) {
            Barcode b = BarcodeFactory.createUCC128(appIDTextField.getText(),
                    barcodeText);
            setBarcode(b);
        } else if (currentValue.equals("Code39")) {
            Barcode b = BarcodeFactory.createCode39(barcodeText, true);
            setBarcode(b);
        } else {
            try {
                Class<?> factory = net.sourceforge.barbecue.BarcodeFactory.class;
                Method createMethod = factory.getMethod(
                        "create" + currentValue, new Class[] { "".getClass() });
                Object result = createMethod.invoke(null,
                        new Object[] { barcodeText });
                setBarcode((Barcode) result);
            } catch (NoSuchMethodException e) {
                // throw new RuntimeException(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                // throw new RuntimeException(e.getMessage(), e);
            } catch (java.lang.reflect.InvocationTargetException e) {
                // throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    private void createGUI() {
        getContentPane().add(createDataInputPanel(), BorderLayout.NORTH);
        getContentPane().add(createBarcodePanel(), BorderLayout.CENTER);
        getContentPane().add(createButtonPanel(), BorderLayout.SOUTH);
        pack();
    }

    private void addQuitListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                quit();
            }
        });
    }

    private JPanel createBarcodePanel() {
        barcodePanel = new JPanel(new BorderLayout());
        barcodePanel.setPreferredSize(new Dimension(200, 200));
        return barcodePanel;
    }

    private JPanel createDataInputPanel() {
        JLabel typeLabel = new JLabel();
        typeLabel.setText("Type:");

        JPanel barcodeSelectionPanel = new JPanel();
        barcodeSelectionPanel.add(typeLabel);

        barcodeSelection = new JComboBox(getBarcodeTypes());
        barcodeSelection.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt) {
                barcodeSelected();
            }
        });

        barcodeSelectionPanel.add(barcodeSelection);

        JPanel dataInputPanel = new JPanel();
        dataInputPanel.add(barcodeSelectionPanel);

        JLabel dataLabel = new JLabel();
        dataLabel.setText("Value:");
        JPanel dataPanel = new JPanel();
        dataPanel.add(dataLabel);

        dataField = new JTextField();
        dataField.setColumns(20);
        dataPanel.add(dataField);

        dataInputPanel.add(dataPanel);

        appIDLabel = new JLabel();
        appIDLabel.setLabelFor(appIDTextField);
        appIDLabel.setText("App ID:");
        JPanel appIDPanel = new JPanel();
        appIDPanel.add(appIDLabel);

        appIDTextField = new JTextField();
        appIDTextField.setColumns(5);
        appIDTextField.setText("123");
        appIDPanel.add(appIDTextField);

        dataInputPanel.add(appIDPanel);
        return dataInputPanel;
    }

    private JPanel createButtonPanel() {
        JButton quitButton = new JButton();
        quitButton.setText("Quit");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                quit();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(quitButton);

        JButton saveAsButton = new JButton();
        saveAsButton.setText("Save as...");
        saveAsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                saveAs();
            }
        });

        buttonPanel.add(saveAsButton);
        return buttonPanel;
    }

    void save(Barcode b, File f) throws Exception {
        String filename = f.getName().toLowerCase();
        if (filename.endsWith(".svg")) {
            saveSVG(b, f);
        } else if (filename.endsWith(".png")) {
            BarcodeImageHandler.savePNG(b, f);
        } else if (filename.endsWith(".jpg")) {
            BarcodeImageHandler.saveJPEG(b, f);
        } else if (filename.endsWith(".gif")) {
            BarcodeImageHandler.saveGIF(b, f);
        } else {
            throw new RuntimeException("can't save: " + f.toString());
        }

    }

    private void saveSVG(Barcode b, File f) {
        try {
            FileWriter writer = new FileWriter(f);
            StringBuffer sb = new StringBuffer();
            writer.write(sb.toString());
            SVGFormatter formatter = new SVGFormatter(writer);
            formatter.format(b);

            writer.close();
        } catch (java.io.IOException ex) {
            System.err.println("Exception on write:");
            ex.printStackTrace();
        } catch (FormattingException e) {
            System.err.println("Exception on write:");
            e.printStackTrace();
        }
    }

    void barcodeSelected() {
        try {
            syncBarcode();
        } catch (BarcodeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    void quit() {
        System.exit(0);
    }

    void saveAs() {
        if (barcodePanel.getComponentCount() < 1) {
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        int retVal = chooser.showSaveDialog(null);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            Barcode b = (Barcode) barcodePanel.getComponent(0);
            File f = chooser.getSelectedFile();
            try {
                save(b, f);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String args[]) {
        try {
            if (!UIManager.getSystemLookAndFeelClassName().toLowerCase()
                    .endsWith("metallookandfeel")) {
                UIManager.setLookAndFeel(UIManager
                        .getSystemLookAndFeelClassName());
            }
        } catch (Exception e) {
            // Nothing to do
        }

        new BarcodeTool().setVisible(true);
    }
}

// /CLOVER:ON
