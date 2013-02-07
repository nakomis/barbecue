package net.sourceforge.barbecue.output;

public class CenteredLabelLayout extends LabelLayout {

    public CenteredLabelLayout(int x, int y, int width) {
        super(x, y, width, 0);
    }

    protected void calculate() {
        int vgap = (int) Math.sqrt(textLayout.getBounds().getHeight());
        textX = (float) ((((width - x) - textLayout.getBounds().getWidth()) / 2) + x);
        textY = (float) (y + textLayout.getBounds().getHeight() + vgap);
        int height = (int) (textLayout.getBounds().getHeight() + vgap + 1);
        bgX = x;
        bgY = y;
        bgWidth = width - x;
        bgHeight = height;
    }
}
