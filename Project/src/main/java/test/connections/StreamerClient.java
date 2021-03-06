package test.connections;

import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

public class StreamerClient extends Canvas implements Runnable {

    private BufferedImage currentFrame;
    private ImageIcon im;
    private Webcam webcam;
    private JFrame j;

    public static int HEADER_SIZE = 8;
    public static int MAX_PACKETS = 255;
    public static int SESSION_START = 128;
    public static int SESSION_END = 64;
    public static int DATAGRAM_MAX_SIZE = 65507 - HEADER_SIZE;
    public static int MAX_SESSION_NUMBER = 255;
    public static String OUTPUT_FORMAT = "jpg";

    public static int COLOUR_OUTPUT = BufferedImage.TYPE_INT_RGB;
    public static double SCALING = 0.5;
    public static int FRAMES_PER_SEC = 50;
    public String IP_ADDRESS;
    public static int PORT = 4444;
    public static boolean SHOW_MOUSEPOINTER = true;

    public StreamerClient(String id) {
        try {
            IP_ADDRESS = "225.4.5." + id;
            webcam = Webcam.getDefault();
            webcam.setViewSize(new Dimension(640,480));
            webcam.open();
        }
        catch(Exception e) {

        }
    }

    public static byte[] bufferedImageToByteArray(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }

    public static BufferedImage scale(BufferedImage source, int w, int h) {
        Image image = source.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);
        BufferedImage result = new BufferedImage(w, h, COLOUR_OUTPUT);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return result;
    }

    public static BufferedImage shrink(BufferedImage source, double factor) {
        int w = (int) (source.getWidth() * factor);
        int h = (int) (source.getHeight() * factor);
        return scale(source, w, h);
    }

    public static BufferedImage copyBufferedImage(BufferedImage image) {
        BufferedImage copyOfIm = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = copyOfIm.createGraphics();
        g.drawRenderedImage(image, null);
        g.dispose();
        return copyOfIm;
    }

    private boolean sendImage(byte[] imageData, String multicastAddress,
                              int port) {
        InetAddress ia;
        boolean ret = false;
        int ttl = 2;

        try {
            ia = InetAddress.getByName(multicastAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return ret;
        }

        MulticastSocket ms = null;

        try {
            ms = new MulticastSocket();
            ms.setTimeToLive(ttl);
            DatagramPacket dp = new DatagramPacket(imageData, imageData.length,
                    ia, port);
            ms.send(dp);
            ret = true;
        } catch (IOException e) {
            e.printStackTrace();
            ret = false;
        } finally {
            if (ms != null) {
                ms.close();
            }
        }

        return ret;
    }

    public void run() {
        int sessionNumber = 0;
        j = new JFrame();
        j.add(this);
        j.setSize(1000,800);
        j.setVisible(true);
        j.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        try {
            while (true) {
                this.captureCam();
                byte[] imageByteArray = bufferedImageToByteArray(this.currentFrame, OUTPUT_FORMAT);
                int packets = (int) Math.ceil(imageByteArray.length / (float) DATAGRAM_MAX_SIZE);

                if (packets > MAX_PACKETS) {
                    System.out.println("Image is too large to be transmitted!");
                    continue;
                }

                for (int i = 0; i <= packets; i++) {
                    int flags = 0;
                    flags = i == 0 ? flags | SESSION_START : flags;
                    flags = (i + 1) * DATAGRAM_MAX_SIZE > imageByteArray.length ? flags | SESSION_END : flags;

                    int size = (flags & SESSION_END) != SESSION_END ? DATAGRAM_MAX_SIZE : imageByteArray.length - i * DATAGRAM_MAX_SIZE;

                    byte[] data = new byte[HEADER_SIZE + size];
                    data[0] = (byte) flags;
                    data[1] = (byte) sessionNumber;
                    data[2] = (byte) packets;
                    data[3] = (byte) (DATAGRAM_MAX_SIZE >> 8);
                    data[4] = (byte) DATAGRAM_MAX_SIZE;
                    data[5] = (byte) i;
                    data[6] = (byte) (size >> 8);
                    data[7] = (byte) size;

                    System.arraycopy(imageByteArray, i * DATAGRAM_MAX_SIZE, data, HEADER_SIZE, size);
                    this.sendImage(data, this.IP_ADDRESS, 4444);

                    if ((flags & SESSION_END) == SESSION_END) break;
                }

                Thread.sleep(1000/FRAMES_PER_SEC);
                sessionNumber = sessionNumber < MAX_SESSION_NUMBER ? ++sessionNumber : 0;
                this.update(this.getGraphics());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void captureCam() {
        //Keep updating currentFrame by capturing webcam photos
        try {
            this.currentFrame = webcam.getImage();
            im = new ImageIcon(currentFrame);
        }
        catch(Exception e) {
            System.out.println("Meme");
        }
    }

    public void update(Graphics g) {
        try {
            g.drawString("Streamer",50,50);
            //System.out.println(currentFrame);
            im.paintIcon(this, g, 50, 60);
        } catch (Exception e) {
            System.out.println("Newmemesssss");
            e.printStackTrace();
        }
    }
}