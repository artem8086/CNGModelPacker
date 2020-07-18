package art.soft;

import art.soft.animation.AnimSet;
import art.soft.ascii.BitFont;
import art.soft.animation.keyframe.KeyFrameSet;
import art.soft.animation.keyframe.KeyFrameAnim;
import art.soft.ascii.AsciiAnim;
import art.soft.ascii.AsciiSet;
import art.soft.model.ModelAnim;
import art.soft.model.ModelSet;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author Артём Святоха
 */
public class Loader {

    private static final String FONT_FILE = "font.bin";

    private static Loader loader;

        // Пулы анимаций
    public KeyFrameAnim frameAnimsPool;
    public ModelAnim modelAnimsPool;
    public AsciiAnim asciiAnimsPool;

        // Контейнеры анимационных данных, для устранения дубликатов в памияти
    private final HashMap<String, KeyFrameSet> allAnims = new HashMap<>();
    private final HashMap<String, ModelSet> allModels = new HashMap<>();
    private final HashMap<String, AsciiSet> allAsciis = new HashMap<>();

    public final ModelPacker packer;
    
    private final Toolkit tk;
    private MediaTracker md;

    public BitFont font;

    // Настройки отладки
    //public boolean debugDecoration;
    //public boolean debugPolyModel;
    //public boolean debugStatic;
    //public boolean debugDynamic;
    //public boolean debugDamageRegion;
    //public boolean debugCamera;

    private Loader(ModelPacker packer) {
        this.packer = packer;
        //
        tk = Toolkit.getDefaultToolkit();
        md = new MediaTracker(packer.getFrame());
    }

    static Loader createLoader(ModelPacker packer) {
        loader = new Loader(packer);
        loader.font = new BitFont(FONT_FILE);
        return loader;
    }

    public static Loader getLoader() {
        return loader;
    }

    public void log(String str) {
        System.err.println(str);
    }

    public DataInputStream openFile(String file) {
        try {
            return new DataInputStream(new BufferedInputStream(
                    new FileInputStream(ModelPacker.DATA_DIR + file)));
        } catch (FileNotFoundException ex) {
            log("File not found: " + file);
            ex.printStackTrace();
            return null;
        }
    }

    private Image loadImage(String im){
        Image i = tk.getImage(ModelPacker.DATA_DIR + im);
        md.addImage(i, 0);
        try {
            md.waitForID(0);
        } catch (InterruptedException ex) {
            log(ex.getMessage());
        }
        md.removeImage(i, 0);
        return i;
    }

    public AnimSet loadAnimation(String path) throws IOException {
        if (path == null || "".equals(path)) return null;
        int type = ANIM_TYPE_NONE;
        if (path.endsWith(".pak")) {
            type = ANIM_TYPE_KEY_FRAME;
            path = path.substring(0, path.length() - 4);
        } else
        if (path.endsWith(".ascii")) {
            type = ANIM_TYPE_ASCII;
            path = path.substring(0, path.length() - 5);
        } else
        if (path.endsWith(".vec")) {
            type = ANIM_TYPE_MODEL;
            path = path.substring(0, path.length() - 4);
        } else
        if (path.endsWith(".vecEx")) {
            type = ANIM_TYPE_MODEL_EXTENDED;
            path = path.substring(0, path.length() - 6);
        }
        return loadAnimation(type, path);
    }

    public boolean writeAnimation(String path, DataOutputStream os) throws IOException {
        if (path == null || "".equals(path)) {
            os.writeByte(ANIM_TYPE_NONE);
            return false;
        }
        if (path.endsWith(".pak")) {
            os.writeByte(ANIM_TYPE_KEY_FRAME);
            os.writeUTF(path.substring(0, path.length() - 4));
        } else
        if (path.endsWith(".ascii")) {
            os.writeByte(ANIM_TYPE_ASCII);
            os.writeUTF(path.substring(0, path.length() - 6));
        } else
        if (path.endsWith(".vec")) {
            os.writeByte(ANIM_TYPE_MODEL);
            os.writeUTF(path.substring(0, path.length() - 4));
        } else
        if (path.endsWith(".vecEx")) {
            os.writeByte(ANIM_TYPE_MODEL_EXTENDED);
            os.writeUTF(path.substring(0, path.length() - 6));
        } else {
            os.writeByte(ANIM_TYPE_NONE);
            return false;
        }
        return true;
    }

    public static final int ANIM_TYPE_NONE = 0;
    public static final int ANIM_TYPE_KEY_FRAME = 1;
    public static final int ANIM_TYPE_ASCII = 2;
    public static final int ANIM_TYPE_MODEL = 3;
    public static final int ANIM_TYPE_MODEL_EXTENDED = 4;

    public AnimSet loadAnimation(DataInputStream is) throws IOException {
        int type = is.readUnsignedByte();
        if (type == ANIM_TYPE_NONE) return null;
        else return loadAnimation(type, is.readUTF());
    }

    public void skipAnimation(DataInputStream is) throws IOException {
        int type = is.readUnsignedByte();
        if (type == ANIM_TYPE_NONE) return;
        is.readUTF();
    }

    public AnimSet loadAnimation(int type, String path) throws IOException {
        switch (type) {
        case ANIM_TYPE_KEY_FRAME: {
            KeyFrameSet anim = allAnims.get(path);
            if (anim == null) {
                anim = new KeyFrameSet(loadImage(path + ".png"), path);
                allAnims.put(path, anim);
            } else {
                anim.loadIndx ++;
            }
            return anim;
        }
        case ANIM_TYPE_ASCII: {
            AsciiSet ascii = allAsciis.get(path);
            if (ascii == null) {
                ascii = new AsciiSet(path);
                allAsciis.put(path, ascii);
            } else {
                ascii.loadIndx ++;
            }
            return ascii;
        }
        case ANIM_TYPE_MODEL:
        case ANIM_TYPE_MODEL_EXTENDED: {
            ModelSet model = allModels.get(path);
            if (model == null) {
                model = new ModelSet(path, type == ANIM_TYPE_MODEL_EXTENDED, null);
                allModels.put(path, model);
            } else {
                model.loadIndx ++;
            }
            return model;
        }
        default:
            return null;
        }
    }

    public void removeKeyFrame(KeyFrameSet anim) {
        if (anim.loadIndx == 0) {
            allAnims.remove(anim.getPath());
        } else {
            anim.loadIndx --;
        }
    }

    public void removeModel(ModelSet model) {
        if (model.loadIndx == 0) {
            allModels.remove(model.getPath());
            AnimSet[] animSets = model.animSets;
            if (animSets != null) {
                for (AnimSet set : animSets) {
                    set.remove();
                }
            }
        } else {
            model.loadIndx --;
        }
    }

    public void removeAscii(AsciiSet ascii) {
        if (ascii.loadIndx == 0) {
            allAsciis.remove(ascii.getPath());
        } else {
            ascii.loadIndx --;
        }
    }
}
