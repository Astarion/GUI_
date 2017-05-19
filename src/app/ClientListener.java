package app;

import concurrentUtils.Stoppable;
import javafx.scene.control.TextArea;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Alex on 12.05.2017.
 */
public class ClientListener implements Stoppable {

    private Socket socket;
    private TextArea chat;
    private boolean isAlive;
    private Thread thread;

    public ClientListener(Socket socket, TextArea chat) {
        this.socket = socket;
        this.chat = chat;
        this.thread = new Thread(this);
        this.isAlive = true;
        this.thread.start();
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String message;
            while (isAlive) {
                message = dataInputStream.readUTF();
                chat.appendText(message + "\n");
                System.out.println(message);

            }

        } catch (IOException e) {
            if(isAlive)
                isAlive = false;
            thread.interrupt();
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        isAlive = false;
        this.thread.interrupt();
    }
}
