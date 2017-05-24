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

            if (j > clients.size())
                return "Error: No such client";

            clientName = host.getClientName(j);
            switch (message) {
                case "\nLeft": {
                    host.removeClient(j);
                    for (int i = 0; i < clients.size(); i++) {
                        Socket clientSocket = host.getClientSocket(i);
                        if (!clientSocket.isClosed()) {
                            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                            dataOutputStream.writeUTF(clientName + " has left chat room");
                            dataOutputStream.writeUTF("\n" + allClientsNames(socket));
                        }
                    }
                    break;
                }
                case "\nAdd": {
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF("\n" + allClientsNames(socket));
                    for (int i = 0; i < clients.size(); i++) {
                        Socket clientSocket = host.getClientSocket(i);
                        if (!clientSocket.isClosed() && clientSocket != socket) {
                            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                            dataOutputStream.writeUTF(clientName + " has joined chat room");
                            dataOutputStream.writeUTF("\n" + allClientsNames(socket));
                        }
                    }
                    break;
                }
                default: {
                    for (int i = 0; i < clients.size(); i++) {
                        Socket clientSocket = host.getClientSocket(i);
                        if (!clientSocket.isClosed()) {
                            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                            dataOutputStream.writeUTF(clientName + ":>" + message);
                        }
                    }
                    break;
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

    private String allClientsNames(Socket socket) {
        String names;
        ArrayList<Pair<Socket, String>> clients = host.getAllClients();
        names = clients.get(0).getValue() + "\n";
        for (int i = 1; i < clients.size(); i++) {
            names = names.concat(clients.get(i).getValue() + "\n");
        }
        return names;
    }
}
