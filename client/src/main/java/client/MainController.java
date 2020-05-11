package client;

import common.AbstractMessage;
import common.FileMessage;
import common.FileRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;


public class MainController implements Initializable {
    private final String CLIENT_DIRECTORY = "client/src/main/client_storage/";
    private final String SERVER_DIRECTORY = "server/src/main/server_storage/";

    @FXML
    HBox listView;
    @FXML
    ListView<String> filesList = new ListView<>();

    @FXML
    ListView<String> filesServerList = new ListView<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    AbstractMessage am = Network.readObject();
                    if (am instanceof FileMessage) {
                        FileMessage fm = (FileMessage) am;
                        if (fm.getOperationType().equals("UPLOAD")) {
                            Files.write(Paths.get(SERVER_DIRECTORY + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                            refreshLocalFilesList();
                        } else if (fm.getOperationType().equals("DOWNLOAD")) {
                            Files.write(Paths.get(CLIENT_DIRECTORY + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                            refreshLocalFilesList();
                        } else if (fm.getOperationType().equals("DELETE")) {
                            if (fm.getListName().equals("filesServerList")) {
                                Files.delete(Paths.get(SERVER_DIRECTORY + fm.getFilename()));
                                refreshLocalFilesList();
                            } else {
                                Files.delete(Paths.get(CLIENT_DIRECTORY + fm.getFilename()));
                                refreshLocalFilesList();
                            }
                        }
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            } finally {
                Network.stop();
            }
        });
        t.setDaemon(true);
        t.start();
        refreshLocalFilesList();
    }

    public void pressOnDownloadBtn(ActionEvent actionEvent) {
        String value = "filesList";
        if (filesServerList.getSelectionModel().getSelectedItems().get(0) != null) {
            System.out.println(filesServerList.getSelectionModel().getSelectedItems().get(0));
            Network.sendMsg(new FileRequest(filesServerList.getSelectionModel().getSelectedItems().get(0), "DOWNLOAD", value));
        }
    }

    public void pressOnSendBtn(ActionEvent actionEvent) {
        String value = "filesServerList";
        if (filesList.getSelectionModel().getSelectedItems().get(0) != null) {
            Network.sendMsg(new FileRequest(filesList.getSelectionModel().getSelectedItems().get(0), "UPLOAD", value));
        }

    }

    public void pressOnDeleteClien(ActionEvent actionEvent) {
        String value = "filesList";
        if (filesList.getSelectionModel().getSelectedItems().get(0) != null) {
            Network.sendMsg(new FileRequest(filesList.getSelectionModel().getSelectedItems().get(0), "DELETE", value));
        }
    }

    public void pressOnDeleteServer(ActionEvent actionEvent) {
        String value = "filesServerList";
        if (filesServerList.getSelectionModel().getSelectedItems().get(0) != null) {
            Network.sendMsg(new FileRequest(filesServerList.getSelectionModel().getSelectedItems().get(0), "DELETE", value));
        }

    }

    public void pressOnRefresh(ActionEvent actionEvent) {
        refreshLocalFilesList();
    }


    public void refreshLocalFilesList() {
        if (Platform.isFxApplicationThread()) {
            try {
                filesList.getItems().clear();
                Files.list(Paths.get(CLIENT_DIRECTORY)).map(p -> p.getFileName().toString()).forEach(o -> filesList.getItems().add(o));
                filesServerList.getItems().clear();
                Files.list(Paths.get(SERVER_DIRECTORY)).map(p -> p.getFileName().toString()).forEach(o -> filesServerList.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Platform.runLater(() -> {
                try {
                    filesList.getItems().clear();
                    Files.list(Paths.get(CLIENT_DIRECTORY)).map(p -> p.getFileName().toString()).forEach(o -> filesList.getItems().add(o));
                    filesServerList.getItems().clear();
                    Files.list(Paths.get(SERVER_DIRECTORY)).map(p -> p.getFileName().toString()).forEach(o -> filesServerList.getItems().add(o));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
