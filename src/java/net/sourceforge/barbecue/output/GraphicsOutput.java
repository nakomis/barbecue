/***********************************************************************************************************************
 * Copyright (c) 2003, International Barcode Consortium
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * Neither the name of the International Barcode Consortium nor the names of any contributors may be used to endorse
 * or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ***********************************************************************************************************************/

package net.sourceforge.barbecue.output;

import java.awt.*;
import java.awt.font.TextLayout;

/**
 * Graphics based outputter to draw barcodes to Graphics objects for printing
 * and display.
 * 
 * @author Ryan Martell
 * @author <a href="mailto:opensource@ianbourke.com">Ian Bourke</a>
 */
public class GraphicsOutput extends AbstractOutput {

    private final Graphics2D g;

    /**
     * Creates a Graphics2D AbstractOutput
     * 
     * @param graphics
     *            The graphics to output to
     * @param font
     *            The font for text rendering
     * @param fgColor
     *            Foreground Color
     * @param bgColor
     *            Background Color
     */
    public GraphicsOutput(Graphics2D graphics, Font font, Color fgColor,
            Color bgColor) {
        super(font, true, 1.0, fgColor, bgColor);
        this.g = (Graphics2D) graphics.create();
    }

    /**
     * From AbstractOutput - Saves current colour.
     */
    public void beginDraw() {
    }

    /**
     * From AbstractOutput - Restores colour.
     * 
     * @param width
     *            The output width (in pixels) of the barcode
     * @param height
     *            The output height (in pixels) of the barcode
     */
    public void endDraw(int width, int height) {
    }

    /**
     * From AbstractOutput - Draws a bar at the given coordinates onto the
     * output Graphics.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param width
     *            the width
     * @param height
     *            the height
     * @param paintWithForegroundColor
     *            if true, use the foreground color, otherwise use the
     *            background color
     */
    public int drawBar(int x, int y, int width, int height,
            boolean paintWithForegroundColor) {
        if (isPainting()) {
            if (paintWithForegroundColor) {
                g.setColor(getForegroundColor());
            } else {
                g.setColor(getBackgroundColor());
            }
            double scalar = getScalar();
            g.fillRect((int) (scalar * x), (int) (scalar * y),
                    (int) (scalar * width), (int) (scalar * height));
        }

        return width;
    }

    public int drawText(String text, LabelLayout labelLayout)
            throws OutputException {
        if (getFont() == null) {
            return 0;
        }

        if (isPainting()) {
            g.setFont(getFont());
            g.setColor(getBackgroundColor());
            g.fillRect(labelLayout.getBackgroundX(), labelLayout
                    .getBackgroundY(), labelLayout.getBackgroundWidth(),
                    labelLayout.getBackgroundHeight());
            g.setColor(getForegroundColor());
            TextLayout layout = new TextLayout(text, getFont(), g.getFontRenderContext());
            labelLayout.setTextLayout(layout);
            layout.draw(g, labelLayout.getTextX(), labelLayout.getTextY());
        }

        return labelLayout.getBackgroundHeight();
    }

    /**
     * Paint the background the background colour, based on the height and the
     * width.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param width
     *            the width to be painted
     * @param height
     *            the height to be painted
     */
    public void paintBackground(int x, int y, int width, int height) {
        if (!isPainting()) {
            return;
        }
        g.setColor(getBackgroundColor());
        g.fillRect(x, y, width, height);
    }

}
