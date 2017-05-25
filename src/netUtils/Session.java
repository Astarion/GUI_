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
    private Pair<Socket, String> client;
    private MessageHandler messageHandler;
    private Host host;

    public Session(Pair<Socket, String> client, MessageHandler messageHandler, Host host) {
        this.client = client;
        this.messageHandler = messageHandler;
        this.host = host;
    }

    @Override
    public void run() {
        InputStream inputStream;
        try {
            host.addClient(client);
            Socket clientSocket = client.getKey();
            DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            dataOutputStream.writeUTF("started");
            inputStream = clientSocket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            String message;
            messageHandler.handle("\nAdd", clientSocket);
            while (!clientSocket.isClosed()) {
                message = dataInputStream.readUTF();
//                if (message.equals("quit")) {
//                    return;
//                }
                messageHandler.handle(message, clientSocket);
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
        if (client.getKey() != null) {
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(client.getKey().getOutputStream());
                dataOutputStream.writeUTF("Server stopped");
                messageHandler.handle("\nLeft", client.getKey());
                client.getKey().close();
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
