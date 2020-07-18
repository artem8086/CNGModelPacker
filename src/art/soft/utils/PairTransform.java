package art.soft.utils;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Артём Святоха
 */
public class PairTransform {

    public String vert, add;

    //
    int vertIndx, addVertIndx;

    public PairTransform() {}

    public PairTransform(int vertIndx, int addVertIndx) {
        this.vertIndx = vertIndx;
        this.addVertIndx = addVertIndx;
    }

    void initIndex(Model model, String parent) {
        vertIndx = model.getVertIndx(parent, vert);
        addVertIndx = model.getVertIndx(parent, add);
    }

    void write(DataOutputStream dos) throws IOException {
        dos.writeInt((vertIndx & 0xFFFF) | (addVertIndx << 16));
    }
}
