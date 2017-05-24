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
    @FXML
    private TextArea online;

    public static String getClientName() {
        return clientName;
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //TODO size and allign
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        stage.setTitle("Chat login");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @FXML
    private void logIn() throws IOException {
        final Parameters params = getParameters();
        final List<String> parameters = params.getRaw();
        String host = parameters.get(0);
        Integer port = Integer.parseInt(parameters.get(1));
        socket = new Socket(host, port);
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        clientName = nickName.getText();
        dataOutputStream.writeUTF(clientName);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        stage = (Stage) logButton.getScene().getWindow();
        stage.setTitle(clientName + "'s Chat");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();

        ClientListener listener = new ClientListener(socket, chat, online);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void send() {
        try {
            String str = message.getText().replaceAll("^[\\s]*", "")
                    .replaceAll("^[\n]*", "")
                    .replaceAll("[\n]*$", "");
            if (str.equals("")) {
                message.clear();
                return;
            }
            dataOutputStream.writeUTF(str);
            message.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
