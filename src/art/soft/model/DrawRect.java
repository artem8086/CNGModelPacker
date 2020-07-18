package art.soft.model;

import art.soft.Loader;
import java.awt.Color;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class DrawRect extends ColorFace {

    private int vert12;

    @Override
    public void read(DataInputStream is) throws IOException {
        super.read(is);
        //
        vert12 = is.readUnsignedShort();
        vert12 |= is.readUnsignedShort() << 16;
    }

    @Override
    public void draw(ModelAnim anim, Graphics g, boolean flipX, boolean flipY) {
        ModelSet model = anim.getData();
        int indx1 = vert12 & 0xFFFF;
        int indx2 = vert12 >>> 16;
        int x1 = model.xVerts[indx1];
        int y1 = model.yVerts[indx1];
        int x2 = model.xVerts[indx2];
        int y2 = model.yVerts[indx2];
        //
        if (x1 > x2) {
            int t = x1; x1 = x2; x2 = t;
        }
        if (y1 > y2) {
            int t = y1; y1 = y2; y2 = t;
        }
        g.setColor(color);
        g.drawRect(x1, y1, x2 - x1, y2 - y1);
        //
        if (Loader.getLoader().packer.getDialog().normals.isSelected() && normal != 0) {
            int x = model.xVerts[indx1];
            int y = model.yVerts[indx1];
            //
            g.setColor(Color.RED);
            int xn = ((byte) normal) >> 1;
            int yn = ((byte) (normal >> 8)) >> 1;
            //System.out.println("x = " + xn + ", y = " + yn);
            g.drawLine(x, y, x + (flipX ? xn : - xn), y + (flipY ? yn : - yn));
        }
    }
}
