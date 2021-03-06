package chat;

import stream.LiveStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MessageReciever extends Thread {
    private String group;
    private MulticastSocket msocket;
    private DatagramPacket dpacket;
    private InetAddress ia;
    private static int PORT = 4444;
    private LiveStream stream;
    private boolean running;


    private ByteArrayInputStream bis;
    private ObjectInputStream ois;

    public MessageReciever(String group, LiveStream stream) {
        try {
            this.group = group;
            this.stream = stream;
            ia = InetAddress.getByName(group);
            running = true;

            msocket = new MulticastSocket(PORT);
            msocket.joinGroup(ia);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void recieveMessage() {
        try {
            byte[] buffer = new byte[5000];
            dpacket = new DatagramPacket(buffer, buffer.length);
            msocket.receive(dpacket);
            byte[] data = dpacket.getData();

            bis = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bis);
            ChatMessage message = (ChatMessage) ois.readObject();

            stream.pushMessage(message);
            System.out.println("Pushed" + message.toString());
        }
        catch (Exception e) {
            //System.out.println("Message recieve memes");
        }
    }

    public void stopThread() {
        msocket.close();
        running = false;
    }

    public void run() {
        while(running) {
            recieveMessage();
        }
    }
}
