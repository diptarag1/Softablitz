package test;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

import javax.swing.JFrame;


public class Mc2 extends Canvas{

    ScreenCap sc;
    boolean running = true;
    JFrame f;

    public void update(Graphics g) {
        try {
            g.drawString("Bruh",50,50);
            ImageIcon im = new ImageIcon(this.sc.bufImage);
            im.paintIcon(this, g, 50, 60);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Mc2 m = new Mc2();
        m.f=new JFrame();
        m.f.add(m);
        m.sc = new ScreenCap();
        Thread t = new Thread(m.sc);
        t.start();
        m.f.setSize(1000,800);
        m.f.setVisible(true);
        m.f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        while(m.running) {
            Graphics g = m.getGraphics();
            m.update(g);
        }
    }

}

class ScreenCap extends Thread {

    Robot robot;
    BufferedImage bufImage;

    public ScreenCap() {
        try {
            robot = new Robot();
        }
        catch(Exception e) {
            System.out.println("bruh");
        }
    }

    public void run()  {
        while(true) {
            try {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Rectangle screenRectangle = new Rectangle(screenSize);
                bufImage = robot.createScreenCapture(screenRectangle);

            } catch (Exception E) {
                System.out.println("Meme");
            }
        }
    }
}
