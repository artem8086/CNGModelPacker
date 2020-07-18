package art.soft.utils;

import art.soft.Loader;
import art.soft.ModelPacker;
import com.esotericsoftware.jsonbeans.Json;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Артём Святоха
 */
public class Model {
    public String name;

    public PairAnim anims[];

    public Verticals[] vertsSets;

    public Block blocks[];

    public ModelFrame frames[];

    //
    int commonVerts;

    ArrayList<Vector> verticals = new ArrayList<>();

    final HashMap<String, Block> allBlocks = new HashMap<>();

    final HashMap<String, Normal> normals = new HashMap<>();

    Vector getVert(String parent, String index) {
        int vNumber = 0;
        if (parent != null) {
            try {
                vNumber = Double.valueOf(parent).intValue();
            } catch (NumberFormatException e) {
                for (; vNumber < vertsSets.length; vNumber++) {
                    if (parent.equals(vertsSets[vNumber].name)) break;
                }
            }
        }
        if (index != null) {
            if (index.length() > 1 && index.charAt(0) == '{'
                    && index.charAt(index.length() - 1) == '}') {
                Vector vec = (new Json()).fromJson(Vector.class, index);
                vec.indx = -1;
                return getVert(addVert(vec));
            }
            try {
                int indx = Double.valueOf(index).intValue();
                return vertsSets[vNumber].verts[indx];
            } catch (NumberFormatException e) {
                for (Vector vert : vertsSets[vNumber].verts) {
                    if (index.equals(vert.name)) {
                        return vert;
                    }
                }
            }
        }
        return null;
    }

    int getVertIndx(String parent, String index) {
        Vector vert = getVert(parent, index);
        if (vert != null) return vert.indx;
        return -1;
    }

    int getAnimIndx(String index) {
        if (index != null) {
            try {
                int indx = Integer.parseInt(index);
                return indx;
            } catch (NumberFormatException e) {
                for (int i = anims.length - 1; i >= 0; i--) {
                    if (index.equals(anims[i].name)) {
                        return i;
                    }
                }
                throw new RuntimeException("anim index[" + index + "] not found!");
            }
        }
        return -1;
    }

    int getFrameIndx(String index) {
        if (index != null) {
            try {
                return Integer.parseInt(index);
            } catch (NumberFormatException e) {
                for (int i = frames.length - 1; i >= 0; i--) {
                    if (index.equals(frames[i].name)) {
                        return i;
                    }
                }
                throw new RuntimeException("vert index[" + index + "] not found!");
            }
        }
        return -1;
    }

    ModelFrame getFrame(String index) {
        if (index != null) {
            try {
                int indx = Integer.parseInt(index);
                return frames[indx];
            } catch (NumberFormatException e) {
                for (ModelFrame frame : frames) {
                    if (index.equals(frame.name)) {
                        return frame;
                    }
                }
            }
        }
        throw new RuntimeException("frame index[" + index + "] not found!");
    }

    Vector getVert(int indx) {
        if (indx != -1) {
            return verticals.get(indx & 0xFFFF);
        }
        return null;
    }

    int addVert(Vector vec) {
        if (vec != null) {
            if (vec.indx != -1) {
                int oldIndx = vec.indx;
                vec = new Vector(vec);
                vec.oldIndx = oldIndx;
            }
            vec.indx = verticals.size();
            verticals.add(vec);
            return vec.indx;
        }
        return -1;
    }

    private void changeIndx(int indx, int newIndx) {
        verticals.get(indx).indx = newIndx;
        for (ModelFrame frame : frames) {
            frame.changeIndx(indx, newIndx);
        }
    }

    private boolean vertIsUsed(int indx) {
        for (ModelFrame frame : frames) {
            if (frame.containIndx(indx)) return true;
        }
        return false;
    }

    public HashMap<String, Integer> getAnimFramesAccessor() {
        HashMap<String, Integer> anims = new HashMap<>();
        for (ModelFrame frame : frames) {
            if (frame.name != null) {
                anims.put(frame.name, frame.indx);
            }
        }
        return anims;
    }

    public void loadSave(ModelPacker model) {
        System.out.println("Init Idexes...");
        initIndexes();
        //
        System.out.println("Generate normals...");
        generateNormals();
        //
        System.out.println("Transform...");
        for (ModelFrame frame : frames) {
            frame.doTransform(this);
        }
        //
        System.out.println("Remove duplicates...");
        removeDuplicates();
        //
        System.out.println("Optimization...");
        optimization();
        //
        System.out.println("Sort transletes...");
        for (ModelFrame frame : frames) {
            frame.sortTranslates(this);
        }
        //
        System.out.println("Common verticales = " + commonVerts);
        System.out.println("Number of verticales = " + verticals.size());
        //
        System.out.println("Write model...");
        writeModel();
    }

    private void initIndexes() {
        int i = 0;
        for (Verticals chVerts : vertsSets) {
            for (Vector vert : chVerts.verts) {
                vert.indx = i;
                verticals.add(vert);
                i++;
            }
        }
        //
        for (i = 0; i < frames.length; i++) {
            frames[i].init(this, i);
        }
        if (blocks != null) {
            for (Block block : blocks) {
                block.init(this);
            }
        }
    }

    void generateNormals() {
        for (int i = 0; i < frames.length; i++) {
            frames[i].generateNormals(this);
        }
        if (blocks != null) {
            for (Block block : blocks) {
                block.generateNormals(this);
            }
        }
        // Init
        for (int i = 0; i < frames.length; i++) {
            frames[i].initNormals(this);
        }
        if (blocks != null) {
            for (Block block : blocks) {
                block.initNormals(this);
            }
        }
    }

    private void removeDuplicates() {
        ArrayList<Vector> newVerts = new ArrayList<>();
        int indx = 0;
        int size = verticals.size();
        for (int i = 0; i < size; i++) {
            Vector vert = verticals.get(i);
            if (vert != null && vertIsUsed(i)) {
                for (int j = i + 1; j < size; j++) {
                    if (vert.equals(verticals.get(j))) {
                        changeIndx(j, indx);
                        verticals.set(j, null);
                        //verts.remove(j);
                    }
                }
                if (indx != i) changeIndx(i, indx);
                newVerts.add(vert);
                indx++;
            }
        }
        verticals = newVerts;
    }

    private void optimization() {
        ArrayList<ArrayList<Vector>> framesVerts = new ArrayList<>(frames.length);
        for (ModelFrame frame : frames) {
            framesVerts.add(frame.getVerticals(this));
        }
        ArrayList<Vector> newVerts = new ArrayList<>(verticals.size());
        boolean find = false;
        int newIndx;
        for (int i = 0; i < framesVerts.size() - 1; i++) {
            for (int n = 0; n < framesVerts.get(i).size(); n++) {
                Vector vert1 = framesVerts.get(i).get(n);
                if (vert1 != null) {
                    for (int j = i + 1; j < framesVerts.size(); j++) {
                        ArrayList<Vector> frVerts = framesVerts.get(j);
                        for (int k = 0; k < frVerts.size(); k++) {
                            if (vert1 == frVerts.get(k)) {
                                find = true;
                                frVerts.set(k, null);
                                //k--;
                            }
                        }
                    }
                    if (find) {
                        find = false;
                        newIndx = newVerts.size();
                        framesVerts.get(i).set(n, null);
                        changeIndx(vert1.indx, newIndx | 0x10000);
                        newVerts.add(vert1);
                        //n--;
                    }
                }
            }
        }
        commonVerts = newVerts.size();
        for (int i = 0; i < frames.length; i++) {
            int start = newVerts.size();
            ModelFrame frame = frames[i];
            frame.start = frame.end = start;
            for (Vector vert : framesVerts.get(i)) {
                if (vert != null) {
                //if ((vert.indx & 0x10000) == 0) {
                    changeIndx(vert.indx, frame.end | 0x10000);
                    newVerts.add(vert);
                    frame.end++;
                }
            }
        }
        for (ModelFrame frame : frames) {
            ArrayList<Vector> trVerts = frame.getTraslatesVerts(this);
            if (trVerts != null) {
                for (Vector vert : trVerts) {
                    if ((vert.indx & 0x10000) == 0) {
                        changeIndx(vert.indx, newVerts.size() | 0x10000);
                        newVerts.add(vert);
                    }
                }
            }
        }
        verticals = newVerts;
    }

    private void writeModel() {
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(
                    ModelPacker.DATA_DIR + name + ".vec"));
            //
            if (anims != null) {
                Loader loader = Loader.getLoader();
                dos.writeByte(anims.length);
                for (PairAnim anim : anims) {
                    loader.writeAnimation(anim.anim, dos);
                }
            } else {
                dos.writeByte(0);
            }
            //
            dos.writeShort(commonVerts);
            dos.writeShort(verticals.size());
            for (Vector vec : verticals) {
                vec.Save(dos);
            }
            dos.writeShort(frames.length);
            for (ModelFrame frame : frames) {
                frame.write(dos, this);
            }
            dos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
