/*
 * Created by JFormDesigner on Wed Oct 21 15:19:30 IST 2020
 */

package stream;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import javax.swing.*;
import net.miginfocom.swing.*;
import user.Streamer;
import user.Viewer;

/**
 * @author Diptarag Ray Chaudhuri
 */
public class StreamWindow extends JFrame {
    private Streamer streamer;
    private Viewer viewer;
    
    private boolean paused;
    private boolean muted;
    
    public StreamWindow() {

        initComponents();
        //Edit jframe components here
        textArea2.setEditable(false);
        textArea3.setEditable(false);
        button3.setVisible(false);

        button1.setVisible(false);
        button2.setVisible(false);
        textField1.setVisible(false);
        textField2.setVisible(false);
    }

    private void changeModePressed(ActionEvent e) {
        // TODO add your code here
        streamer.changeMode();
    }

    private void allUsersMessagePressed(ActionEvent e) {
        // TODO add your code here
        String text = textField2.getText();
        System.out.println(text);
        viewer.sendAllUsersMessage(text);
    }

    private void subMessagePressed(ActionEvent e) {
        // TODO add your code here
        String text = textField1.getText();
        System.out.println(text);
        viewer.sendSubMessage(text);
    }

    private void PausePressed(ActionEvent e) {
        // TODO add your code here
        System.out.println(paused);
        if(paused) {
            paused = false;
            button4.setText("Pause");
            if(viewer != null) { viewer.unpause(); }
        }
        else {
            paused = true;
            button4.setText("Resume");
            if(viewer != null) { viewer.pause(); }
        }
    }

    private void mutePressed(ActionEvent e) {
        // TODO add your code here
        if(muted) {
            muted = false;
            button5.setText("Mute");
            if(viewer != null) { viewer.unmute(); }
            if(streamer != null) { streamer.unmute(); }
        }
        else {
            muted = true;
            button5.setText("Unmute");
            if(viewer != null) { viewer.mute(); }
            if(streamer != null) { streamer.mute(); }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Diptarag Ray Chaudhuri
        label3 = new JLabel();
        panel1 = new JPanel();
        label5 = new JLabel();
        scrollPane2 = new JScrollPane();
        textArea2 = new JTextArea();
        textField1 = new JTextField();
        button1 = new JButton();
        label6 = new JLabel();
        scrollPane3 = new JScrollPane();
        textArea3 = new JTextArea();
        label4 = new JLabel();
        button3 = new JButton();
        button4 = new JButton();
        button5 = new JButton();
        textField2 = new JTextField();
        button2 = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- label3 ----
        label3.setText("text");
        contentPane.add(label3, "cell 1 1");

        //======== panel1 ========
        {
            panel1.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border
            .EmptyBorder(0,0,0,0), "JFor\u006dDesi\u0067ner \u0045valu\u0061tion",javax.swing.border.TitledBorder.CENTER,javax
            .swing.border.TitledBorder.BOTTOM,new java.awt.Font("Dia\u006cog",java.awt.Font.BOLD,
            12),java.awt.Color.red),panel1. getBorder()));panel1. addPropertyChangeListener(new java.beans
            .PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e){if("bord\u0065r".equals(e.
            getPropertyName()))throw new RuntimeException();}});
            panel1.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //---- label5 ----
            label5.setText("Sub Only Chat");
            panel1.add(label5, "cell 0 0");

            //======== scrollPane2 ========
            {
                scrollPane2.setViewportView(textArea2);
            }
            panel1.add(scrollPane2, "cell 0 1 5 1,width 500:500:500,height 200:200:200");
            panel1.add(textField1, "cell 0 2 4 1,width 420:420:420");

            //---- button1 ----
            button1.setText("Post");
            button1.addActionListener(e -> subMessagePressed(e));
            panel1.add(button1, "cell 4 2,width 80:80:80");

            //---- label6 ----
            label6.setText("All Users Chat");
            panel1.add(label6, "cell 0 3");

            //======== scrollPane3 ========
            {
                scrollPane3.setViewportView(textArea3);
            }
            panel1.add(scrollPane3, "cell 0 4 5 1,width 500:500:500,height 200:200:200");
        }
        contentPane.add(panel1, "cell 27 1 21 5,height 500:500:500");
        contentPane.add(label4, "cell 1 2 17 1");

        //---- button3 ----
        button3.setText("Change Mode");
        button3.addActionListener(e -> changeModePressed(e));
        contentPane.add(button3, "cell 1 6");

        //---- button4 ----
        button4.setText("Pause");
        button4.addActionListener(e -> PausePressed(e));
        contentPane.add(button4, "cell 6 6");

        //---- button5 ----
        button5.setText("Mute");
        button5.addActionListener(e -> mutePressed(e));
        contentPane.add(button5, "cell 11 6");
        contentPane.add(textField2, "cell 27 6 18 1,width 420:420:420");

        //---- button2 ----
        button2.setText("Post");
        button2.addActionListener(e -> allUsersMessagePressed(e));
        contentPane.add(button2, "cell 27 6 18 1,width 80:80:80");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
    
    public void markAsStreamer(Streamer self) {
        button3.setVisible(true);
        button4.setVisible(false);

        this.streamer = self;

        addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        streamer.stopStreaming();
                        super.windowClosing(e);
                    }
                }
        );
    }

    public void markAsViewer(Viewer self, String streamerUsername) {
        this.viewer = self;
        button1.setVisible(false);
        button2.setVisible(true);
        textField1.setVisible(false);
        textField2.setVisible(true);

        addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.out.println("Madarchod "+ viewer.getUsername());
                        viewer.stopWatching();
                        super.windowClosing(e);
                    }
                }
        );

        System.out.println("Marked viewer");
        
        int[] userPairInfo = viewer.getUserPairInfo(streamerUsername);
        if(userPairInfo[1] == 1) { //If subbed
            textField1.setVisible(true);
            button1.setVisible(true);
        }
    }

    public void setIcon(BufferedImage image) {
        label4.setIcon(new ImageIcon(image));
    }

    public void setTitle(String title) {
        label3.setText(title);
    }

    public void setallUsersMessageBlockText(String messageBlock) {
        textArea3.setText(messageBlock);
    }

    public void setSubOnlyMessageBlockText(String messageBlock) {
        textArea2.setText(messageBlock);
    }

    public Viewer getViewer() { return this.viewer; }
    
    

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Diptarag Ray Chaudhuri
    private JLabel label3;
    private JPanel panel1;
    private JLabel label5;
    private JScrollPane scrollPane2;
    private JTextArea textArea2;
    private JTextField textField1;
    private JButton button1;
    private JLabel label6;
    private JScrollPane scrollPane3;
    private JTextArea textArea3;
    private JLabel label4;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JTextField textField2;
    private JButton button2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
