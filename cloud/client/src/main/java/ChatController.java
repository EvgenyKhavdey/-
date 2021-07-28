import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.FileMessage;
import model.FileResponse;
import model.ListResponse;
import model.FileDelete;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ChatController implements Initializable {


    public ListView<String> serverView;
    public TextArea serverText;
    public TextArea clientText;

    private String root = "client/clientFiles";
    private NettyNetwork network;
    private FileChooser fileChooser;
    private FileMessage fileMessage;

    private File dir;
    private String address;


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
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select directory for save");
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*.html", "*.jpg",
                "*.jfif", "*.png", "*.txt", "*.mpeg4", "*.mp3", "*.wav", "*.docx", "*.xlsx", "*.xls"));
        address = String.valueOf((fileChooser.showSaveDialog(stage)).getAbsolutePath());
        network.writeMessage(new FileResponse(fileName));
    }

    public void deleteFile(ActionEvent event){
        network.writeMessage(new FileDelete(serverView.getSelectionModel().getSelectedItem()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser = new FileChooser();
        network = new NettyNetwork(command -> {
            switch (command.getType()) {
                case LIST_RESPONSE:
                    ListResponse files = (ListResponse) command;
                    Platform.runLater(() -> {
                        serverView.getItems().clear();
                        serverView.getItems().addAll(files.getNames());
                    });
                    break;
                case FILE_REQUEST:
                    FileResponse fileResponse =(FileResponse) command;
                    try {
                        Files.copy(fileResponse.getFile().toPath(), (new File(address + fileResponse.getName())).toPath());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
            }
        });
        dir = new File(root);
        if (!dir.exists()) dir.mkdir();
    }

}
