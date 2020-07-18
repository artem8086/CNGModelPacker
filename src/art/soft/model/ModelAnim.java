package art.soft.model;

import art.soft.Loader;
import art.soft.animation.AnimSet;
import art.soft.animation.Animation;
import java.awt.Graphics;

/**
 *
 * @author Артём Святоха
 */
public class ModelAnim extends Animation {

    public boolean main = false;

    private Animation animations[];

    private ModelSet data;

    private int current;

    public int time;

    ModelAnim() {}

    public ModelAnim(AnimSet set) {
        setAnimSet(set);
    }

    @Override
    public void setAnimSet(AnimSet set) {
        data = (ModelSet) set;
        AnimSet[] animSets = data.animSets;
        if (animSets != null) {
            int lenght = animSets.length;
            animations = new Animation[lenght];
            for (lenght--; lenght >= 0; lenght --) {
                (animations[lenght] = animSets[lenght].getAnimation()).setAnimation(0);
            }
        }
    }

    @Override
    public AnimSet getAnimSet() {
        return data;
    }

    public ModelSet getData() {
        return data;
    }

    public int getCurrentNum() {
        return current;
    }

    public int getTime() {
        return time;
    }

    public Animation getAnimation(int n) {
        return animations[n];
    }

    @Override
    public void setAnimation(int num) {
        if (current != num) {
            if (num < 0) num = 0; else
            if (num >= data.meshes.length) num = 0;
            MeshData mesh = data.meshes[num];
            time = mesh.getTime();
            while (time == 0xFFFE) {
                mesh = data.meshes[num = mesh.getNext()];
                time = mesh.getTime();
            }
            current = num;
            if (main) Loader.getLoader().packer.getDialog().frameIndex.setValue(num);
        } else if (time <= 0) {
            time = data.meshes[num].getTime();
        }
    }

    @Override
    public void reset() {
        time = getCurAnim().getTime();
        if (animations != null) {
            for (int i = animations.length - 1; i >= 0; i --) {
                animations[i].reset();
            }
        }
    }

    public MeshData getCurAnim() {
        return data.meshes[current];
    }

    public void play(Graphics g, int x, int y, float zoom, boolean flipX, boolean flipY) {
        getCurAnim().draw(g, this, x, y, zoom, flipX, flipY);
        if (next != null) next.play(g, x, y, flipX, flipY);
    }

    @Override
    public void play(Graphics g, int x, int y) {
        getCurAnim().draw(g, this, x, y, 1f, false, false);
        if (next != null) next.play(g, x, y);
    }

    @Override
    public void play(Graphics g, int x, int y, boolean flipX, boolean flipY) {
        getCurAnim().draw(g, this, x, y, 1f, flipX, flipY);
        if (next != null) next.play(g, x, y, flipX, flipY);
    }

    @Override
    public void pool() {
        if (next != null) next.pool();
        if (animations != null) {
            for (int i = animations.length - 1; i >= 0; i --) {
                animations[i].pool();
            }
            animations = null;
        }
        data = null;
        main = false;
        Loader loader = Loader.getLoader();
        next = loader.modelAnimsPool;
        loader.modelAnimsPool = this;
    }

    @Override
    public boolean incAnim(boolean cycle) {
        if (next != null) next.incAnim(cycle);
        if (animations != null) {
            for (int i = animations.length - 1; i >= 0; i --) {
                animations[i].incAnim(cycle);
            }
        }
        //
        if (time == 0xFFFF) return true;
        if (time > 0) time --;
        else setAnimation(getCurAnim().getNext());
        //
        return false;
    }
    
}
