package app;

import com.sun.xml.internal.bind.v2.TODO;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.util.Pair;
import netUtils.Host;
import netUtils.MessageHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Alex on 12.05.2017.
 */
public class SendClientsMessageHandler implements MessageHandler {

    private Host host;

    SendClientsMessageHandler(Host host) {
        this.host = host;
    }

    @Override
    public String handle(String message, Socket socket) {
        try {
            ArrayList<Pair<Socket, String>> clients = host.getAllClients();
            DataOutputStream dataOutputStream;
            String clientName;

            int j = 0;
            while (socket != host.getClientSocket(j) && j < clients.size()) {
                j++;
            }

            //TODO what if j>clients.size()

            clientName = host.getClientName(j);
            if (message.equals("\nLeft")) {
                host.removeClient(j);
                for (int i = 0; i < clients.size() /*&& socket!=clients.get(i).getKey()*/; i++) {
                    Socket clientSocket = host.getClientSocket(i);
                    if (!clientSocket.isClosed()) {
                        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                        dataOutputStream.writeUTF(clientName + " has left chat room");
                    }

                }
            } else if (message.equals("\nAdd")) {
                for (int i = 0; i < clients.size(); i++) {
                    Socket clientSocket = host.getClientSocket(i);
                    if (!clientSocket.isClosed() && clientSocket!= socket) {
                        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                        dataOutputStream.writeUTF(clientName + " has joined chat room");
                    }
                }
            } else {
                for (int i = 0; i < clients.size(); i++) {
                    Socket clientSocket = host.getClientSocket(i);
                    if (!clientSocket.isClosed()) {
                        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                        dataOutputStream.writeUTF(clientName + ":>" + message);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "OK";
    }

    @Override
    public String handle(String message) {  //users online
        return null;
    }

}
