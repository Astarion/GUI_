package app;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Alex on 12.05.2017.
 */
public class ClientListener implements Runnable {

    private Socket socket;

    public ClientListener(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String message;
            while (true) {
                message = dataInputStream.readUTF();
                System.out.println(message);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
