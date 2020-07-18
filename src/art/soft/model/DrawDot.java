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
public class DrawDot extends ColorFace {

    private int vert;

    @Override
    public void read(DataInputStream is) throws IOException {
        super.read(is);
        //
        vert = is.readUnsignedShort();
    }

    @Override
    public void draw(ModelAnim anim, Graphics g, boolean flipX, boolean flipY) {
        ModelSet model = anim.getData();
        if (Loader.getLoader().packer.getDialog().normals.isSelected() && normal != 0) {
            int x = model.xVerts[vert];
            int y = model.yVerts[vert];
            //
            g.setColor(Color.RED);
            int xn = ((byte) normal) >> 1;
            int yn = ((byte) (normal >> 8)) >> 1;
            //System.out.println("x = " + xn + ", y = " + yn);
            g.drawLine(x, y, x + (flipX ? xn : - xn), y + (flipY ? yn : - yn));
        }
        //
        g.setColor(color);
        g.fillRect(model.xVerts[vert], model.yVerts[vert], 1, 1);
    }
}
