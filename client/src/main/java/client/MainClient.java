package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;

public class MainClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlClient = new FXMLLoader(getClass().getResource("/client.fxml"));
        Parent root = fxmlClient.load();
        primaryStage.setTitle("Cloud");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}



