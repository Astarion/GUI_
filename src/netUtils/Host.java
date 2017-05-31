package netUtils;

import concurrentUtils.Channel;
import concurrentUtils.Stoppable;
import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Created by 14Malgavka on 10.02.2017.
 */
public class Host implements Stoppable {

    private Integer port;
    private Channel channel;
    private ServerSocket serverSocket;
    private MessageHandlerFactory messageHandlerFactory;
    private volatile boolean isActive;
    //    private ArrayList<Socket> allClients = new ArrayList<Socket>();
    private volatile ArrayList<Pair<Socket, String>> allClients = new ArrayList<>();
    private Object lock = new Object();

    public Host(Integer port, Channel channel, MessageHandlerFactory messageHandlerFactory) {
        this.port = port;
        this.channel = channel;
        this.messageHandlerFactory = messageHandlerFactory;
        isActive = true;
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            System.out.printf("Port problems\n%s%n", e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (isActive) {
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String clientName = dataInputStream.readUTF();
                MessageHandler handler = messageHandlerFactory.createMessageHandler(this);
//                allClients.add(new Pair<>(socket, clientName));
                channel.put(new Session(new Pair<>(socket, clientName), handler, this));

                //For debug only
//                for (int i = 0; i < allClients.size(); i++) {
//                    System.out.println(allClients.get(i).getValue());
//                }
            }
        } catch (SocketException e) {
            System.out.println("Some problems: " + e.getMessage());
        } catch (IOException e) {
            System.out.printf("Port problems\n%s%n", e.getMessage());
        }
    }

    @Override
    public void stop() {
        if (isActive)
            isActive = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Pair<Socket, String>> getAllClients() {
        return allClients;
    }

    public String getClientName(int index) {
        return allClients.get(index).getValue();
    }

    public Socket getClientSocket(int index) {
        return allClients.get(index).getKey();
    }

    public void addClient(Pair<Socket, String> client) {
        synchronized (lock) {
            allClients.add(client);
        }
    }

    public void removeClient(int index) {

        synchronized (lock) {
            allClients.remove(index);
        }
    }

    public void removeClient(Socket socket) {
        synchronized (lock) {
            int j = 0;
            while (socket != allClients.get(j).getKey() && j < allClients.size()) {
                j++;
            }
            allClients.remove(j);
        }
    }

    public boolean isActive()
    {
        return isActive;
    }
}
//TODO снихронизация доступа к массиву клиентов