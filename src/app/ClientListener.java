package app;

import concurrentUtils.Stoppable;
import javafx.scene.control.TextArea;
import javafx.util.Pair;
import netUtils.Host;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

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
            while (!socket.isClosed()) {
                message = dataInputStream.readUTF();
                chat.appendText(message + "\n");
                System.out.println(message);

            }

        } catch (IOException e) {
            // if(isAlive)
            // isAlive = false;
            // thread.interrupt();
            // e.printStackTrace();
           close();
        }
    }

    @Override
    public void stop() {
        isAlive = false;
        this.thread.interrupt();
    }

    public  void close() {

        ArrayList<Pair<Socket, String>> clients = Host.allClients;
        try {
            int j = 0;
            while (this.socket != clients.get(j).getKey() && j < clients.size()) {
                j++;
            }
            String clientName = clients.get(j).getValue();
            Host.allClients.remove(j);
            for (int i = 0; i < clients.size() ; i++) {
                Socket clientSocket = clients.get(i).getKey();
                DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                dataOutputStream.writeUTF(clientName + ' ' + "has left chat room");
            }
            if (!socket.isClosed()) {
                socket.close(); // закрываем

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
