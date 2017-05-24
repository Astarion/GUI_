package netUtils;

import concurrentUtils.Stoppable;
import javafx.util.Pair;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;

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
            messageHandler.handle("\nAdd", socket);
            while (!socket.isClosed()) {
                message = dataInputStream.readUTF();

                messageHandler.handle(message, socket);
            }


        } catch (IOException e) {
            System.out.println("Connection interrupted in Session");

            stop();
          // close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (socket != null) {
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("Server stopped");
                messageHandler.handle("\nLeft", socket);
//                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

//    public void close() {
//        ArrayList<Pair<Socket, String>> clients = Host.allClients;
//        int j=0;
//        while (this.socket != clients.get(j).getKey() && j < clients.size()) {
//            j++;
//        }
//        Host.allClients.remove(j); //убираем из списка
//        if (!socket.isClosed()) {
//            try {
//                socket.close(); // закрываем
//            } catch (IOException ignored) {}
//        }
//    }
}
