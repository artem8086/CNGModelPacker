package art.soft.model;

import art.soft.ControlPanel;
import art.soft.Loader;
import art.soft.animation.Animation;
import java.awt.Color;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class FaceAnim extends Face {

    private int anim_frame_flip, vert;

    @Override
    public void read(DataInputStream is) throws IOException {
        int anim = is.readUnsignedByte();
        int frame = is.readUnsignedByte();
        int flip = is.readUnsignedByte();
        int vert = is.readUnsignedShort();
        anim_frame_flip = anim | (frame << 8) | (flip << 16);
        this.vert = vert;
    }

    @Override
    public void draw(ModelAnim animation, Graphics g, boolean flipX, boolean flipY) {
        ModelSet model = animation.getData();
        ControlPanel dialog = Loader.getLoader().packer.getDialog();
        if (dialog.normals.isSelected() && normal != 0) {
            int x = model.xVerts[vert];
            int y = model.yVerts[vert];
            //
            g.setColor(Color.RED);
            int xn = ((byte) normal) >> 1;
            int yn = ((byte) (normal >> 8)) >> 1;
            g.drawLine(x, y, x + (flipX ? xn : - xn), y + (flipY ? yn : - yn));
        }
        //
        int animNum = anim_frame_flip & 0xFF;
        int frame = (anim_frame_flip >> 8) & 0xFF;
        flipX ^= (anim_frame_flip & 0x10000) != 0;
        flipY ^= (anim_frame_flip & 0x20000) != 0;
        Animation anim = animation.getAnimation(animNum);
        anim.setAnimation(frame);
        if (!flipX && !flipY) {
            anim.play(g, model.xVerts[vert], model.yVerts[vert]);
        } else {
            anim.play(g, model.xVerts[vert], model.yVerts[vert], flipX, flipY);
        }
    }
}
