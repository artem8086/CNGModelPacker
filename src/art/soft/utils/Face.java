package art.soft.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Артём Святоха
 */
public class Face {
    public enum FaceTypes {
        fillPolygon, drawRect, fillRect, drawArc, fillArc, drawRoundRect, fillRoundRect
    };

    public FaceTypes type = FaceTypes.fillPolygon;

    public String color;
    public int a, r, g, b;

    public int arcW, arcH;
    public int startAngle, arcAngle;

    public String verts[];

    public String com;

    public String anim, vert, norm, autoNorm;
    public boolean flipX = false, flipY = false;
    public int frame = 0;

    public Normal normal;

    //
    int animIndx, vertIndx;
    int[] vertsIndx;

    public Face() {}

    public Face(Face face) {
        this.type = face.type;
        this.color = face.color;
        this.a = face.a;
        this.r = face.r;
        this.g = face.g;
        this.b = face.b;
        //
        this.arcW = face.arcW;
        this.arcH = face.arcH;
        this.startAngle = face.startAngle;
        this.arcAngle = face.arcAngle;
        //
        this.frame = face.frame;
        this.animIndx = face.animIndx;
        this.vertIndx = face.vertIndx;
        if (face.normal != null) {
            this.normal = new Normal(face.normal);
        }
        if (face.vertsIndx != null) {
            vertsIndx = new int[face.vertsIndx.length];
            System.arraycopy(face.vertsIndx, 0, vertsIndx, 0, vertsIndx.length);
        }
    }

    void init(Model model, String parent) {
        animIndx = model.getAnimIndx(anim);
        if (verts != null) {
            vertsIndx = new int[verts.length];
            for (int i = 0; i < vertsIndx.length; i++) {
                vertsIndx[i] = model.getVertIndx(parent, verts[i]);
            }
        }
        vertIndx = model.getVertIndx(parent, vert);
    }

    void createNewVerts(Model m) {
        if (vertsIndx != null) {
            for (int i = 0; i < vertsIndx.length; i++) {
                vertsIndx[i] = m.addVert(m.getVert(vertsIndx[i]));
            }
        }
        vertIndx = m.addVert(m.getVert(vertIndx));
    }

    ArrayList<Vector> getVerticales(Model model) {
        ArrayList<Vector> faceVerts = new ArrayList<Vector>();
        Vector vec;
        if (vertsIndx != null) {
            for (int i = 0; i < vertsIndx.length; i++) {
                vec = model.getVert(vertsIndx[i]);
                if (vec != null) faceVerts.add(vec);
            }
        }
        vec = model.getVert(vertIndx);
        if (vec != null) faceVerts.add(vec);
        //
        return faceVerts;
    }

    void changeIndx(int indx, int newIndx) {
        if (vertIndx == indx) vertIndx = newIndx;
        if (vertsIndx != null) {
            for (int i = 0; i < vertsIndx.length; i++) {
                if (vertsIndx[i] == indx) vertsIndx[i] = newIndx;
            }
        }
    }

    boolean containIndx(int indx) {
        if (vertIndx == indx) return true;
        if (vertsIndx != null) {
            for (int i = 0; i < vertsIndx.length; i++) {
                if (vertsIndx[i] == indx) return true;
            }
        }
        return false;
    }

    void genereteNormal(Model model) {
        if (autoNorm != null && vertsIndx != null && vertsIndx.length >= 3) {
            normal = new Normal();
            //
            Vector v0 = model.getVert(vertsIndx[0]);
            Vector a = model.getVert(vertsIndx[1]).sub(v0);
            Vector b = model.getVert(vertsIndx[2]).sub(v0);
            //
            float x = a.y * b.z - b.y * a.z;
            float y = a.z * b.x - b.z * a.x;
            //
            float len = (float) Math.sqrt(x * x + y * y);
            x = (x / len) * 127f;
            y = (y / len) * 127f;
            //
            normal.x = (byte) x;
            normal.y = (byte) y;
            //
            if (!"".equals(autoNorm)) {
                normal.name = autoNorm;
                model.normals.put(autoNorm, normal);
            }
            norm = null;
            //
            //System.out.println(normal);
        } else if (normal != null) {
            if (normal.name != null) {
                model.normals.put(normal.name, normal);
            }
            norm = null;
        }
    }

    void initNormal(Model model) {
        if (autoNorm == null && norm != null) {
            normal = new Normal(model.normals.get(norm));
        }
    }

    float getZ(Model model) {
        if (vertsIndx == null) {
            return model.getVert(vertIndx).z;
        } else {
            float z = 0;
            for (int vIndx : vertsIndx) {
                z += model.getVert(vIndx).z;
            }
            z /= vertsIndx.length;
            return z;
        }
    }

    void write(DataOutputStream dos, ModelFrame frame, Model model) throws IOException {
        if (animIndx != -1) {
            dos.writeByte(0);
            dos.writeByte(animIndx);
            dos.writeByte(this.frame);
            dos.writeByte((flipX ? 1 : 0) | (flipY ? 2 : 0));
            dos.writeShort(frame.getVert(vertIndx, model));
        } else {
            writeType(dos);
            //
            int col = 0;
            if (color != null) {
                col = Integer.parseInt(color, 16);
            } else {
                col = (a << 24) |
                        ((r & 0xFF) << 16) |
                        ((g & 0xFF) << 8) |
                        (b & 0xFF);
            }
            dos.writeInt(col ^ 0xFF00_0000);
            //
            switch (type) {
                case fillPolygon:
                    for (int vert : vertsIndx) {
                        dos.writeShort(frame.getVert(vert, model));
                    }
                    break;
                case drawRect:
                case fillRect:
                    dos.writeShort(frame.getVert(vertsIndx[0], model));
                    dos.writeShort(frame.getVert(vertsIndx[1], model));
                    break;
                case fillRoundRect:
                case drawRoundRect:
                    dos.writeShort(frame.getVert(vertsIndx[0], model));
                    dos.writeShort(frame.getVert(vertsIndx[1], model));
                    dos.writeShort(arcW | (arcH << 8));
                    break;
                case fillArc:
                case drawArc:
                    dos.writeShort(frame.getVert(vertsIndx[0], model));
                    dos.writeShort(frame.getVert(vertsIndx[1], model));
                    dos.writeShort(startAngle);
                    dos.writeShort(arcAngle);
                    break;
            }
        }
        if (normal != null) {
            normal.write(dos);
        } else {
            dos.writeShort(0);
        }
    }

    private void writeType(DataOutputStream dos) throws IOException {
        int tByte = 0;
        switch (type) {
            case fillPolygon:
                if (vertsIndx.length < 3) tByte = vertsIndx.length;
                else tByte = vertsIndx.length + 9 - 3;
                break;
            case drawRect: tByte = 3; break;
            case fillRect: tByte = 4; break;
            case drawRoundRect: tByte = 5; break;
            case fillRoundRect: tByte = 6; break;
            case drawArc: tByte = 7; break;
            case fillArc: tByte = 8; break;
        }
        dos.writeByte(tByte);
    }
}
