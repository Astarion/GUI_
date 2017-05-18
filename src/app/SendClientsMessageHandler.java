package app;

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
    @FXML
    private TextArea area;

    SendClientsMessageHandler(Host host) {
        this.host = host;
    }

    @Override
    public String handle(String message, Socket socket) {
        try {
            ArrayList<Pair<Socket, String>> clients = host.getAllClients();
            int j =0;
            while(socket != clients.get(j).getKey() && j < clients.size())
            {
                j++;
            }
            //TODO Если j > clients.size()
            String clientName;
//            if (j> clients.size())
//                clientName="default";
//            else
                clientName = clients.get(j).getValue();

            DataOutputStream dataOutputStream;
            for (int i = 0; i < clients.size(); i++) {
                Socket clientSocket = clients.get(i).getKey();
                dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                dataOutputStream.writeUTF(clientName + ":>" + message);
                area.appendText("ffffff" + '\n');
                area.appendText("aaaaaaaaaa");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "OK";
    }

    @Override
    public String handle(String message) {
        return null;
    }
}
