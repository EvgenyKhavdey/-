import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;
import model.*;

@Slf4j
public class ChatController implements Initializable{

    public ListView<String> serverView;
    public TextArea serverText;
    private String root = "client/clientFiles";
    private NettyNetwork network;
    private FileChooser fileChooser;
    private FileMessage fileMessage;
    private File dir;
    private String address;
    public Stage stage;
    @FXML
    private TextField login_field;
    @FXML
    private PasswordField password_field;
    @FXML
    private Button authorizationButton;

    @FXML
    public void send(ActionEvent event) {
        if(!login_field.getText().trim().equals("") && !password_field.getText().trim().equals("")) {
            network.writeMessage(new User(login_field.getText().trim(), password_field.getText().trim()));
        }
        else {
            windowError("windowError.fxml");
        }
    }

    public void renew(ActionEvent event){
        network.writeMessage(new ListRequest());
    }


    public void downloadFile(ActionEvent event) {
        fileMessage = new FileMessage();
        fileChooser.setTitle("Select file for download");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("All files", "*.html", "*.jpg",
                        "*.jfif", "*.png", "*.txt", "*.mpeg4", "*.mp3", "*.wav", "*.docx", "*.xlsx", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);
        fileMessage = new FileMessage();
        fileMessage.setFile(
                new File(String.valueOf(
                        fileChooser.showOpenDialog(serverText.getScene().getWindow()))));
        fileMessage.setName(fileMessage.getFile().getName());
        fileMessage.setSize(fileMessage.getFile().length());
        network.writeMessage(fileMessage);
    }

    public void saveFile(ActionEvent event) {
        String fileName = serverView.getSelectionModel().getSelectedItem();
        if(fileName != null) {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select directory for save");
            fileChooser.setInitialFileName(fileName);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*.html", "*.jpg",
                    "*.jfif", "*.png", "*.txt", "*.mpeg4", "*.mp3", "*.wav", "*.docx", "*.xlsx", "*.xls"));
            address = String.valueOf((fileChooser.showSaveDialog(stage)).getAbsolutePath());
            network.writeMessage(new FileResponse(fileName));
            stage.close();
        }
    }

    public void deleteFile(ActionEvent event){
        network.writeMessage(new FileDelete(serverView.getSelectionModel().getSelectedItem()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser = new FileChooser();
        try {
            network = new NettyNetwork(command -> {
                    switch (command.getType()) {
                        case AUTHORIZATION_PASSED:
                            Platform.runLater(() -> {
                                windowOpen("chat.fxml");
                            });
                            break;
                        case AUTHORIZATION_NO:
                            Platform.runLater(() -> {
                                windowError("windowError.fxml");
                            });
                            break;
                        case LIST_RESPONSE:
                            ListResponse files = (ListResponse) command;
                            Platform.runLater(() -> {
                                serverView.getItems().clear();
                                serverView.getItems().addAll(files.getNames());
                            });
                            break;
                        case FILE_REQUEST:
                            FileResponse fileResponse = (FileResponse) command;
                            try {
                                Files.copy(fileResponse.getFile().toPath(), (new File(address + fileResponse.getName())).toPath());
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                            break;
                    }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        dir = new File(root);
        if (!dir.exists()) dir.mkdir();
    }

    public void windowOpen(String file){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(file));
        try {
            loader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        stage = (Stage) authorizationButton.getScene().getWindow();
        Parent root = loader.getRoot();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void windowError(String file){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(file));
        try {
            loader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        Stage stageError = new Stage();
        Parent root = loader.getRoot();
        stageError.setScene(new Scene(root));
        stageError.show();
    }

    public void closeConnection() {
        if (network != null) {
            try {
                network.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
