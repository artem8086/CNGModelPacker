package art.soft.utils;

import art.soft.model.extended.BulletPos;
import art.soft.model.extended.DamageRegion;
import static art.soft.model.extended.MeshFrame.READ_MASK_BULLETS;
import static art.soft.model.extended.MeshFrame.READ_MASK_DMG_REGS;
import static art.soft.model.extended.MeshFrame.READ_MASK_FORCE_X;
import static art.soft.model.extended.MeshFrame.READ_MASK_FORCE_Y;
import static art.soft.model.extended.MeshFrame.READ_MASK_FORM;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Артём Святоха
 */
public class ModelFrame extends Block {

    public int indx;

    public String refer, com;
    
    public ArrayList<PairTransform> translates;

    public float zoom = 1f;

    public int time = -1;

    public String next;

    // extemded data
    
    public int form;

    public float forceX, forceY;

    public DamageRegion damageRegs[];

    public BulletPos bullets[];
    
    //
    int nextIndx;

    ArrayList<Face> allFaces;

    int start, end;

    void init(Model model, int indx) {
        this.indx = indx;
        nextIndx = next == null ? indx + 1 : model.getFrameIndx(next);
        //
        if (translates != null) {
            for (PairTransform tr : translates) {
                tr.initIndex(model, verts);
            }
        }
        //
        String tName = name;
        name = null;
        super.init(model);
        name = tName;
    }

    private void optimazeTranletes() {
        for (int i = 0; i < translates.size() - 1; i++) {
            int tIndx = translates.get(i).vertIndx;
            for (int j = i + 1; j < translates.size(); j++) {
                if (tIndx == translates.get(j).vertIndx) {
                    translates.remove(tIndx);
                }
            }
        }
    }

    private boolean transletesContain(int indx) {
        for (PairTransform tr : translates) {
            if (tr.vertIndx == indx) return true;
        }
        return false;
    }

    private boolean transletesAllContain(int indx) {
        for (PairTransform tr : translates) {
            if (tr.vertIndx == indx) return true;
            if (tr.addVertIndx == indx) return true;
        }
        return false;
    }

    void doTransform(Model model) {
        allFaces = transform(model);
        //
        if (translates != null) {
            optimazeTranletes();
            //
            for (Face face : allFaces) {
                for (Vector vert : face.getVerticales(model)) {
                    if (vert.oldIndx != -1 && vert.oldIndx != vert.indx) {
                        if (transletesContain(vert.oldIndx)) {
                            translates.add(new PairTransform(vert.indx, translates.get(vert.oldIndx).addVertIndx));
                        }
                    }
                }
            }
        }
        //
    }

    void changeIndx(int indx, int newIndx) {
        if (translates != null) {
            boolean optimaze = false;
            for (PairTransform tr : translates) {
                if (tr.addVertIndx == indx) {
                    tr.addVertIndx = newIndx;
                }
                if (tr.vertIndx == indx) {
                    tr.vertIndx = newIndx;
                    optimaze = true;
                }
            }
            //
            if (optimaze) optimazeTranletes();
        }
        //
        for (Face face : allFaces) {
            face.changeIndx(indx, newIndx);
        }
    }

    boolean containIndx(int indx) {
        if (translates != null && transletesAllContain(indx)) {
            return true;
        }
        for (Face face : allFaces) {
            if (face.containIndx(indx)) return true;
        }
        return false;
    }

    ArrayList<Vector> getVerticals(Model model) {
        ArrayList<Vector> verts = new ArrayList<>();
        boolean add;
        for (Face face : allFaces) {
            for (Vector vert : face.getVerticales(model)) {
                add = true;
                for (Vector addVert : verts) {
                    if (vert == addVert) {
                        add = false;
                        break;
                    }
                }
                if (add) verts.add(vert);
            }
        }
        return verts;
    }

    ArrayList<Vector> getTraslatesVerts(Model model) {
        if (translates != null) {
            ArrayList<Vector> verts = new ArrayList<>();
            for (PairTransform tr : translates) {
                Vector vert = model.getVert(tr.addVertIndx);
                if ((vert.indx & 0x10000) == 0) {
                    verts.add(vert);
                }
            }
            return verts;
        }
        return null;
    }

    public void sortTranslates(Model model) {
        if (translates != null) {
            ArrayList<PairTransform> newTranslates = new ArrayList<>(translates.size());
            int start = 0;
            int end = model.commonVerts;
            for (int j = 1; j >= 0; j--) {
                for (; start < end; start++) {
                    Vector vert = model.getVert(start);
                    for (int i = 0; i < translates.size(); i++) {
                        PairTransform tr = translates.get(i);
                        if (tr.vertIndx == vert.indx) {
                            newTranslates.add(tr);
                            translates.remove(i);
                            break;
                        }
                    }
                }
                start = this.start;
                end = this.end;
            }
            translates = newTranslates;
        }
    }

    int getVert(int vert, Model model) {
        vert &= 0xFFFF;
        if (vert >= model.commonVerts) {
            vert -= start - model.commonVerts;
        }
        return vert;
    }

    void write(DataOutputStream dos, Model model) throws IOException {
        int index = indx;
        if (refer != null) {
            index = model.getFrame(refer).indx;
            if (index >= indx) throw new RuntimeException("Refer index >= frame index!");
        }
        dos.writeShort(index);
        //
        dos.writeShort(time);
        if (time != -1) {
            dos.writeShort(nextIndx);
        }
        //
        writeData(dos);
        //
        if (time != -2) {
            if (refer == null) {
                dos.writeInt(start | (end << 16));
                if (translates != null) {
                    if (translates.size() > 255) throw new RuntimeException("too many transletes!");
                    dos.writeByte(translates.size());
                    for (PairTransform tr : translates) {
                        tr.write(dos);
                    }
                } else {
                    dos.writeByte(0);
                }
                //
                dos.writeShort(allFaces.size());
                for (Face face : allFaces) {
                    face.write(dos, this, model);
                }
            }
            //
            dos.writeFloat(zoom);
        }
    }

    private void writeData(DataOutputStream dos) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(baos);
        int mask = 0;
        if (form != 0) {
            mask |= READ_MASK_FORM;
            os.writeByte(form);
        }
        if (forceX != 0f) {
            mask |= READ_MASK_FORCE_X;
            os.writeFloat(forceX);
        }
        if (forceY != 0f) {
            mask |= READ_MASK_FORCE_Y;
            os.writeFloat(forceX);
        }
        if (damageRegs != null) {
            mask |= READ_MASK_DMG_REGS;
            os.writeByte(damageRegs.length);
            for (DamageRegion dmgReg : damageRegs) {
                dmgReg.writeDmgReg(os);
            }
        }
        if (bullets != null) {
            mask |= READ_MASK_BULLETS;
            os.writeByte(bullets.length);
            for (BulletPos bullet : bullets) {
                bullet.writeBulletPos(os);
            }
        }
        os.close();
        if (baos.size() == 0) {
            dos.writeShort(0);
        } else {
            dos.writeShort(baos.size());
            dos.writeByte(mask);
            dos.write(baos.toByteArray());
        }
    }
}