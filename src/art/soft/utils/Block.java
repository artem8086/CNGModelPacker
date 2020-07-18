package art.soft.utils;

import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Артём Святоха
 */
public class Block {

    public String name;

    public String verts;

    public Face faces[];

    public Block blocks[];

    public String useBlocks[];

    public boolean sort = false;

    // Трансформации блока и всех подблоков
    public Transform[] transforms;

    //
    void generateNormals(Model model) {
        if (faces != null) {
            for (Face face : faces) {
                face.genereteNormal(model);
            }
        }
        if (blocks != null) {
            for (Block block : blocks) {
                block.generateNormals(model);
            }
        }
    }

    void initNormals(Model model) {
        if (faces != null) {
            for (Face face : faces) {
                face.initNormal(model);
            }
        }
        if (blocks != null) {
            for (Block block : blocks) {
                block.initNormals(model);
            }
        }
    }
    //
    void init(Model model) {
        if (name != null) model.allBlocks.put(name, this);
        if (faces != null) {
            for (Face face : faces) {
                face.init(model, verts);
            }
        }
        if (blocks != null) {
            for (Block block : blocks) {
                block.init(model);
            }
        }
    }

    protected ArrayList<Face> transform(Model model) {
        ArrayList<Face> allFaces = new ArrayList<>();
        if (faces != null) {
            for (Face face : faces) {
                allFaces.add(face);
            }
        }
        if (useBlocks != null) {
            for (String block : useBlocks) {
                allFaces.addAll(model.allBlocks.get(block).transform(model));
            }
        }
        if (blocks != null) {
            for (Block block : blocks) {
                allFaces.addAll(block.transform(model));
            }
        }
        // Трансформация
        if (transforms != null) {
            ArrayList<Face> newFaces = new ArrayList<>();
            Vector3 vec = new Vector3();
            for (Transform transform : transforms) {
                transform.initTransform();
            }
            for (Face face : allFaces) {
                Face newFace = new Face(face);
                newFace.createNewVerts(model);
                newFaces.add(newFace);
                ArrayList<Vector> verts = newFace.getVerticales(model);
                //
                if (newFace.type == Face.FaceTypes.drawArc ||
                        newFace.type == Face.FaceTypes.fillArc) {
                    for (Transform transform : transforms) {
                        newFace.startAngle = (newFace.startAngle - (int) transform.angle) % 360;
                    }
                    //
                    Vector v0 = verts.get(0);
                    Vector v1 = verts.get(1);
                    verts.add(new Vector(v0.x, v1.y, v0.z));
                    verts.add(new Vector(v1.x, v0.y, v1.z));
                }
                //
                for (Vector vert : verts) {
                    vec.set(vert.x, vert.y, vert.z);
                    //
                    for (Transform transform : transforms) {
                        vec.mul(transform.matrix4);
                    }
                    //
                    vert.set((int) vec.x, (int) vec.y, vec.z);
                }
                //
                if (newFace.type == Face.FaceTypes.drawArc ||
                        newFace.type == Face.FaceTypes.fillArc) {
                    setAABB(verts);
                }
                //
                if (newFace.normal != null) {
                    vec.set(newFace.normal.x, newFace.normal.y, 0f);
                    //
                    for (Transform transform : transforms) {
                        vec.mul(transform.matrix3);
                    }
                    //
                    vec.z = 0f;
                    float len = vec.len();
                    vec.x = (vec.x / len) * 127f;
                    vec.y = (vec.y / len) * 127f;
                    newFace.normal.x = (byte) vec.x;
                    newFace.normal.y = (byte) vec.y;
                }
            }
            allFaces = newFaces;
        }
        //
        if (sort) {
            allFaces.sort(new Comparator<Face>() {
                @Override
                public int compare(Face o1, Face o2) {
                    return o1.getZ(model) < o2.getZ(model) ? 1 : 0;
                }
            });
        }
        return allFaces;
    }

    private void setAABB(ArrayList<Vector> verts) {
        Vector v0 = verts.get(0);
        Vector v1 = verts.get(1);
        Vector v2 = verts.get(2);
        Vector v3 = verts.get(3);
        int x0 = Math.min(v0.x, Math.min(v1.x, Math.min(v2.x, v3.x)));
        int x1 = Math.max(v0.x, Math.max(v1.x, Math.max(v2.x, v3.x)));
        int y0 = Math.min(v0.y, Math.min(v1.y, Math.min(v2.y, v3.y)));
        int y1 = Math.max(v0.y, Math.max(v1.y, Math.max(v2.y, v3.y)));
        v0.x = x0;
        v0.y = y0;
        v1.x = x1;
        v1.y = y1;
        verts.remove(2);
        verts.remove(2);
    }
}
