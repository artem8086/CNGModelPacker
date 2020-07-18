package art.soft.utils;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class Vector {

    int oldIndx = -1;

    public int indx = -1;

    public String name;

    public int x, y;
    public float z = 1f; // значение паралакса

    public String com;

    public Vector() {}

    public Vector(Vector v) {
        if (v != null) {
            x = v.x; y = v.y; z = v.z;
        }
    }

    public Vector(int x, int y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(int x, int y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector sub(Vector v) {
        return new Vector(x - v.x, y - v.y, z - v.z);
    }

    public void Save(DataOutputStream dos) throws IOException {
        dos.writeShort(x);
        dos.writeShort(y);
        dos.writeFloat(z);
    }

    @Override
    public boolean equals(Object vec) {
        if (vec == null) return false;
        Vector v = (Vector) vec;
        return x == v.x && y == v.y && z == v.z;
    }

    @Override
    public String toString() {
        return "{" + "indx=" + indx + ", x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}
