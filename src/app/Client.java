package app;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.Random;

/**
 * Created by 14Malgavka on 10.02.2017.
 */
public class Client extends Application {
    @FXML
    private TextField nickName;
    @FXML
    public Button logButton;
    @FXML
    private TextArea area;
    @FXML
    private ScrollPane pane;
    @FXML
    private TextArea message;

    private static String clientName;
    private Stage stage;



    private Socket socket;

    public static String getClientName() {
        return clientName;

    }

    public static void main(String[] args) throws IOException {


        launch(args);
//        try {
//            String host = args[0];
//            Integer port = Integer.parseInt(args[1]);
//            Socket socket = new Socket(host, port);
//            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//
//            String str;
//            Thread threadForListeningOutput = new Thread(new ClientListener(socket));
//            threadForListeningOutput.start();
//
//            dataOutputStream.writeUTF(getClientName());
//
//            while (true) {
//                str = bufferedReader.readLine();
//                dataOutputStream.writeUTF(str);
//
//                if (str.equals("quit")) {
//                    System.out.println("app.Client's connection was closed");
//                    socket.close();
//                    break;
//                }
//            }
//        } catch (NumberFormatException e) {
//            System.out.println("Wrong params in app.Client");
//        } catch (IllegalArgumentException e) {
//            System.out.println("Illegal argument: " + e.getMessage());
//        } catch (UnknownHostException e) {
//            System.out.println("Problem with host occured: " + e.getMessage() + "\nEnter correct host address");
//        } catch (ConnectException e) {
//            System.out.println("Connection problem occured:\n" + e.getMessage());
//        } catch (SocketException e) {
//            System.out.println("Connection was closed by host:\n" + e.getMessage());
//        }


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setTitle("Chat login");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    @FXML
    public void logIn() throws IOException {
        stage = (Stage) logButton.getScene().getWindow();
        clientName = nickName.getText();
        stage.setTitle("Chat");
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("chat.fxml")), 900, 600));
        stage.show();
    }

    @FXML
    public void send() {

        try {
            String host = "localhost";
            Integer port = 1500;
            socket = new Socket(host, port);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String str;
            Thread threadForListeningOutput = new Thread(new ClientListener(socket));
            threadForListeningOutput.start();

            dataOutputStream.writeUTF(getClientName());

            while (true) {
                str = bufferedReader.readLine();
                dataOutputStream.writeUTF(str);

                if (str.equals("quit")) {
                    System.out.println("app.Client's connection was closed");
                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        area.appendText("ffffff" + '\n');
        area.appendText("aaaaaaaaaa");
    }
}
//ДЗ: изучить интерфейсы BlockingQueue
//Доделать методы Stop(), применяя флаг isActive и в диспетчере и в хосте
//Должен ли быть isAlive volatile или нет