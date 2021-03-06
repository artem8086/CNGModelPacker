package art.soft;

import art.soft.model.ModelAnim;
import com.sun.glass.events.KeyEvent;
import java.awt.Dialog;
import javax.swing.JFrame;

/**
 *
 * @author Артём Святоха
 */
public class ControlPanel extends javax.swing.JDialog {

    /**
     * Creates new form ControlPanel
     */
    public ControlPanel(JFrame parent) {
        super(parent, Dialog.ModalityType.MODELESS);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frameIndex = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        AnimChoser = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        flipX = new javax.swing.JCheckBox();
        flipY = new javax.swing.JCheckBox();
        triangulation = new javax.swing.JCheckBox();
        normals = new javax.swing.JCheckBox();
        dmgRegs = new javax.swing.JCheckBox();
        bullets = new javax.swing.JCheckBox();
        reset = new javax.swing.JButton();
        play = new javax.swing.JButton();
        next = new javax.swing.JButton();
        fast = new javax.swing.JButton();
        fastReload = new javax.swing.JButton();
        fullReset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Панель управления");
        setLocation(new java.awt.Point(1000, 100));
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);

        frameIndex.setModel(new javax.swing.SpinnerNumberModel(0, 0, 65536, 1));
        frameIndex.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                frameIndexStateChanged(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Индекс кадра:");

        AnimChoser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnimChoserActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Анимация:");

        flipX.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        flipX.setText("Отобразить по X");

        flipY.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        flipY.setText("Отобразить по Y");

        triangulation.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        triangulation.setText("Триангуляция");

        normals.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        normals.setText("Нормали");

        dmgRegs.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        dmgRegs.setText("Регионы урона");

        bullets.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        bullets.setText("Снаряды");

        reset.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        reset.setText("|<");
        reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetActionPerformed(evt);
            }
        });

        play.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        play.setText("||>");
        play.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playActionPerformed(evt);
            }
        });

        next.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        next.setText(">");
        next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextActionPerformed(evt);
            }
        });

        fast.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        fast.setText(">>");
        fast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fastActionPerformed(evt);
            }
        });

        fastReload.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        fastReload.setText("Быстрая перезагрузка");
        fastReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fastReloadActionPerformed(evt);
            }
        });

        fullReset.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        fullReset.setText("Полная перезагрузка");
        fullReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(AnimChoser, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(frameIndex, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(flipX)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(triangulation)
                                .addComponent(dmgRegs)
                                .addComponent(bullets)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(reset)
                                    .addGap(4, 4, 4)
                                    .addComponent(play)
                                    .addGap(4, 4, 4)
                                    .addComponent(next)
                                    .addGap(4, 4, 4)
                                    .addComponent(fast))
                                .addComponent(fastReload, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(normals)
                                .addComponent(fullReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(flipY))))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frameIndex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(4, 4, 4)
                .addComponent(AnimChoser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(flipX)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(flipY)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(triangulation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(normals)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dmgRegs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bullets)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reset)
                    .addComponent(play)
                    .addComponent(next)
                    .addComponent(fast))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fastReload)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fullReset)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fastReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fastReloadActionPerformed
        Loader.getLoader().packer.loadModel(true);
    }//GEN-LAST:event_fastReloadActionPerformed

    private void fullResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullResetActionPerformed
        Loader.getLoader().packer.loadModel(false);
    }//GEN-LAST:event_fullResetActionPerformed

    private void resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetActionPerformed
        Loader.getLoader().packer.reset();
    }//GEN-LAST:event_resetActionPerformed

    private void playActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playActionPerformed
        Loader.getLoader().packer.play ^= true;
    }//GEN-LAST:event_playActionPerformed

    private void nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextActionPerformed
        Loader.getLoader().packer.getAnimation().incAnim(false);
    }//GEN-LAST:event_nextActionPerformed

    private void fastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fastActionPerformed
        ModelAnim anim = Loader.getLoader().packer.getAnimation();
        anim.time = 0;
        anim.incAnim(false);
    }//GEN-LAST:event_fastActionPerformed

    private void AnimChoserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnimChoserActionPerformed
        Loader.getLoader().packer.setAnimation((String) AnimChoser.getSelectedItem());
    }//GEN-LAST:event_AnimChoserActionPerformed

    private void frameIndexStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_frameIndexStateChanged
        Loader.getLoader().packer.setAnimation((Integer) frameIndex.getValue());
    }//GEN-LAST:event_frameIndexStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ControlPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ControlPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ControlPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ControlPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ControlPanel dialog = new ControlPanel(null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JComboBox<String> AnimChoser;
    public javax.swing.JCheckBox bullets;
    public javax.swing.JCheckBox dmgRegs;
    private javax.swing.JButton fast;
    public javax.swing.JButton fastReload;
    public javax.swing.JCheckBox flipX;
    public javax.swing.JCheckBox flipY;
    public javax.swing.JSpinner frameIndex;
    public javax.swing.JButton fullReset;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton next;
    public javax.swing.JCheckBox normals;
    private javax.swing.JButton play;
    private javax.swing.JButton reset;
    public javax.swing.JCheckBox triangulation;
    // End of variables declaration//GEN-END:variables
}
