package art.soft.model.extended;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class DamageRegion {

    public int x, y, w, h;

    private int damage, mask;

    public float forceX, forceY;

    public void writeDmgReg(DataOutputStream dos) throws IOException {
        dos.writeShort(x);
        dos.writeShort(y);
        dos.writeShort(w);
        dos.writeShort(h);
        dos.writeShort(damage);
        dos.writeShort(mask);
        dos.writeFloat(forceX);
        dos.writeFloat(forceY);
    }

    public void readDmgReg(DataInputStream dis) throws IOException {
        x = dis.readShort();
        y = dis.readShort();
        w = dis.readUnsignedShort();
        h = dis.readUnsignedShort();
        damage = dis.readShort();
        mask = dis.readUnsignedShort() << 16;
        forceX = dis.readFloat();
        forceY = dis.readFloat();
    }

    public int getDamage() {
        return damage;
    }

    public int getMask() {
        return mask;
    }
}
