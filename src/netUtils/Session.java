package netUtils;

import concurrentUtils.Stoppable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by Alex on 17.02.2017.
 */
public class Session implements Stoppable {
    private Socket socket;
    private MessageHandler messageHandler;

    public Session(Socket socket, MessageHandler messageHandler) {
        this.socket = socket;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        InputStream inputStream;
        try {
            inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            String message;
            while (true) {
                message = dataInputStream.readUTF();
                if (message.equals("quit")) {
                    return;
                }
                messageHandler.handle(message, socket);
            }
        } catch (IOException e) {
            System.out.println("Connection interrupted");
        }
    }

    @Override
    public void stop() {
        if (socket != null) {
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("Server stopped");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
