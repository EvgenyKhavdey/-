import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Message;

public class ChatController implements Initializable {

    public ListView<String> listView;
    public TextField statusBar;
    private String root = "client/clientFiles";
    private ObjectDecoderInputStream is;
    private ObjectEncoderOutputStream os;
    private NettyNetwork network;

    public void send(ActionEvent actionEvent) {
        String content = statusBar.getText();
        File file = new File(content);
        String name = file.getName();
        long sizeFile = file.length();
        byte[] buffer = new byte[256];
        int count;
        try(FileInputStream ois = new FileInputStream(file);
        ) {
            while ((count = ois.read(buffer)) != -1) {
                network.writeMessage(new Message(buffer, name, sizeFile));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = new NettyNetwork(new CallBack() {
            @Override
            public void call(Message s) {
                Platform.runLater(() -> statusBar.setText(s.toString()));
            }
        });


//                (s -> Platform.runLater(() -> statusBar.setText(s.toString())));
//


    }

}
