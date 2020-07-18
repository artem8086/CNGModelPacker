package art.soft.model.extended;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Артём Святоха
 */
public class BulletPos {

    public boolean flipY = false;

    public int x, y;

    public String name;

    public void writeBulletPos(DataOutputStream dos) throws IOException {
        dos.writeBoolean(flipY);
        dos.writeShort(x);
        dos.writeShort(y);
        dos.writeUTF(name);
    }

    public void readBulletPos(DataInputStream dis) throws IOException {
        flipY = dis.readBoolean();
        x = dis.readShort();
        y = dis.readShort();
        name = dis.readUTF();
        //data = Loader.getLoader().engine.getObj(dis.readUTF());
    }
}
