package art.soft.utils;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class Normal {

    public String name;

    public byte x, y;

    void write(DataOutputStream dos) throws IOException {
        dos.writeByte(y);
        dos.writeByte(x);
        //dos.writeShort((((int) x) & 0xFF) | (((int) y) << 8));
    }

    public Normal() {}

    public Normal(Normal normal) {
        this.name = normal.name;
        this.x = normal.x;
        this.y = normal.y;
    }

    public Normal(String name, byte x, byte y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Normal{" + "name=" + name + ", x=" + x + ", y=" + y + '}';
    }
}
