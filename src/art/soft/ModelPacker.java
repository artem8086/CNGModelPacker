package art.soft;

import art.soft.animation.AnimSet;
import art.soft.animation.Animation;
import art.soft.model.ModelAnim;
import art.soft.model.ModelSet;
import art.soft.utils.Model;
import com.esotericsoftware.jsonbeans.Json;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 *
 * @author Артём Святоха
 */
public class ModelPacker extends Thread implements KeyListener {

    public static final String DATA_DIR = "G:\\java\\Projects\\NewYearRescueComplete\\data\\data\\";

    public static final int TIME_FRAME = 33;

    public static final BasicStroke bs = new BasicStroke(2);
    
    public static final BasicStroke bs1 = new BasicStroke(1);

    
    File file;

    private final ControlPanel dialog;

    public ControlPanel getDialog() {
        return dialog;
    }

    private final JFrame frame;
    private ModelAnim animation;
    private int curIndex;
    private int x, y;
    private float zoom = 1f;
    public boolean play = false;

    private final JComponent component;
    private HashMap<String, Integer> animNames;

    private ModelPacker(File file) {
        super();
        frame = new JFrame("Предпросмотр и запоковка векторных моделей");
        this.file = file;

        frame.addKeyListener(this);
        
        frame.setLocation(200,50);
        frame.setSize(800, 640);
        frame.setFocusable(true);
        frame.setVisible(true);

        component = new JComponent() {
            @Override
            public void update(Graphics g) {
                paint(g);
            }

            public void paint(Graphics g) {
                g.setColor(Color.WHITE);
                translateX = translateY = 0;
                int w = getWidth();
                int h = getHeight();
                g.fillRect(0, 0, w, h); 
                g.setColor(Color.BLACK);
                g.drawString("play: " + play, 20, 30);
                //
                translate(g, w >> 1, h >> 1);
                //
                ((Graphics2D) g).setStroke(bs1);
                g.setColor(Color.red);
                g.drawLine(0, - h, 0, h);
                g.drawLine(- w, 0, w, 0);
                ((Graphics2D) g).setStroke(bs);
                //
                if (animation != null) {
                    animation.play(g, x, y, zoom,
                            dialog.flipX.isSelected(),
                            dialog.flipY.isSelected());
                }
            }
        };
        component.setDoubleBuffered(true);
        component.setVisible(true);

        frame.add(component);

        dialog = new ControlPanel(frame);
        dialog.setVisible(true);
        //
        frame.requestFocus();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Открыть JSON модель");
        fc.setCurrentDirectory(new File("models"));
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            ModelPacker mp = new ModelPacker(fc.getSelectedFile());
            Loader.createLoader(mp);
            mp.loadModel(false);
            //ControlPanel.main(args);
            mp.start();
            mp.frame.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent ev){
                    System.exit(0);
                }
            });
        }
    }

    public void reset() {
        setAnimation(curIndex);
        if (animation != null) animation.reset();
        play = false;
    }

    public void setAnimation(int n) {
        curIndex = n;
        if (animation != null) {
            animation.setAnimation(n);
            curIndex = animation.getCurrentNum();
        }
    }

    public void setAnimation(String anim) {
        Integer key = animNames.get(anim); 
        if (key != null) {
            setAnimation(key);
            if (animation != null) animation.reset();
            play = false;
        }
    }

    public void loadModel(boolean fast) {
        try {
            int indx = curIndex;
            Model model = (new Json()).fromJson(Model.class, file);
            model.loadSave(this);
            //
            System.out.println("Load model...");
            ModelAnim anim = (ModelAnim) (new ModelSet(model.name,
                    true, fast ? animation.getData().animSets : null)).getAnimation();
            anim.main = true;
            //
            animNames = model.getAnimFramesAccessor();
            dialog.AnimChoser.removeAllItems();
            for (String name : animNames.keySet()) {
                dialog.AnimChoser.addItem(name);
            }
            //dialog.AnimChoser.setSelectedIndex(curIndex);
            //
            if (animation != null) animation.pool();
            animation = anim;
            //
            setAnimation(indx);
            play = false;
            //
            System.out.println("Loading complete!\n");
        } catch (Exception e) {
            animation = null;
            e.printStackTrace();
        }
        
    }

    public ModelAnim getAnimation() {
        return animation;
    }

    public Graphics getGraphics() {
        return component.getGraphics();
    }

    public Frame getFrame() {
        return frame;
    }

    public int getWidth() {
        return component.getWidth();
    }

    public int getHeight() {
        return component.getHeight();
    }

    public static int translateX = 0, translateY = 0;

    public static void translate(Graphics g, int x, int y) {
        g.translate(x, y);
        translateX -= x;
        translateY -= y;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_R: loadModel(false); break;
            case KeyEvent.VK_F: loadModel(true); break;
            case KeyEvent.VK_S:
                x = 0; y = 0; zoom = 1f;
                break;
            case KeyEvent.VK_INSERT:
                AnimSet set = animation.getAnimSet();
                Animation anim = set.getAnimation();
                anim.setAnimation(animation.getCurrentNum());
                animation.next = anim;
                break;
            case KeyEvent.VK_EQUALS: setAnimation(++curIndex); break;
            case KeyEvent.VK_MINUS: setAnimation(--curIndex); break;
            case KeyEvent.VK_UP: y -= 10; break;
            case KeyEvent.VK_DOWN: y += 10; break;
            case KeyEvent.VK_LEFT: x -= 10; break;
            case KeyEvent.VK_RIGHT: x += 10; break;
            case KeyEvent.VK_PAGE_UP: zoom += 0.1f; break;
            case KeyEvent.VK_PAGE_DOWN: zoom -= 0.1f; break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_N:
                animation.time = 0;
                animation.incAnim(false);
                break;
            case KeyEvent.VK_SPACE:
                animation.incAnim(false);
                break;
            case KeyEvent.VK_P:
                play ^= true;
                break;
        }
    }

    
    @Override
    public void run() {
        boolean frameSkip = false;
        do {
            long cycleTime = System.currentTimeMillis();
            //
            if (play && animation != null) animation.incAnim(true);
            if (!frameSkip) {
                //Graphics g = getGraphics();
                //
                //translate(g, - (w >> 1), - (h >> 1));
                //
                component.paintImmediately(0, 0, getWidth(), getHeight());
                //component.repaint();
            }
            //
            cycleTime += TIME_FRAME - System.currentTimeMillis();
            if (cycleTime > 0) {
                try{
                    Thread.sleep(cycleTime);
                }catch(InterruptedException e) {}
                frameSkip = false;
            } else {
                frameSkip = true;
            }
        } while (true);
    }
}
