package client;

import common.AbstractMessage;
import common.AuthMessage;
import common.AuthRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;


public class Login implements Initializable {
    private Network network;
    private MainClient mainClient;
    private MainController mainController = new MainController();
    private static boolean autStatus;

    @FXML
    TextField username;

    @FXML
    TextField password;

    @FXML
    Button button;

    @FXML
    public void pressAuthOk() {
        network.sendMsg(new AuthRequest(username.getText(), password.getText()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Network.start();
                try {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof AuthMessage) {
                        AuthMessage authMessage = (AuthMessage) am;
                        if (authMessage.isStatus() == true) {
                            Platform.runLater(() -> {
                                Stage stage = (Stage) button.getScene().getWindow();
                                stage.close();
                                try {
                                    changeWindow();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        } else {
                            System.out.println("логин или пароль неверны");
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void changeWindow() throws IOException {
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("/client.fxml"));
        Parent root;
        Stage primaryStage = new Stage();
        root = Loader.load();
        primaryStage.setTitle("Box Client");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}