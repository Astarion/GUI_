package app;

import javafx.application.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.Printer;
import javafx.scene.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.*;
import java.util.List;


/**
 * Created by 14Malgavka on 10.02.2017.
 */
public class Client extends Application {


    private Socket socket;
    private DataOutputStream dataOutputStream;
    private static String clientName;
    private Stage stage;

    @FXML
    private TextField nickName;
    @FXML
    private Button logButton;
    @FXML
    private TextArea chat;
    @FXML
    private TextArea message;

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
//        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        final Parameters params = getParameters();
        final List<String> parameters = params.getRaw();
        String host = parameters.get(0);
        Integer port = Integer.parseInt(parameters.get(1));

        socket = new Socket(host, port);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        stage.setTitle("Chat login");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @FXML
    private void logIn() throws IOException {
        stage = (Stage) logButton.getScene().getWindow();

        clientName = nickName.getText();
//        socket = new Socket("localhost", 1500);
//        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(clientName);



        stage.setTitle("Chat");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
        ClientListener listener = new ClientListener(socket, chat);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    //Indicates that this client should be deleted from the allClients
                    dataOutputStream.writeUTF("-1");
                    listener.stop();
                    // threadForListeningOutput.stop();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                event.consume();
            }
        });

    }

    @FXML
    private void send() {

        try {
//            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

            String str = message.getText();
            dataOutputStream.writeUTF(str);
            message.clear();
//            if (str.equals("quit")) {
//                System.out.println("app.Client's connection was closed");
//                socket.close();
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
//ДЗ: изучить интерфейсы BlockingQueue
//Доделать методы Stop(), применяя флаг isActive и в диспетчере и в хосте
//Должен ли быть isAlive volatile или нет