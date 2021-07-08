import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    private InputStream is;
    private OutputStream os;
    private byte [] buffer;
    File file;

    public ListView<String> listView;
    public TextField textField;


    public void send(ActionEvent actionEvent) throws IOException {
        String msg = textField.getText();
        os.write(msg.getBytes(StandardCharsets.UTF_8));
        os.flush();
        textField.clear();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buffer = new byte[256];
        try{
            Socket socket = new Socket("localhost", 8189);
            is = socket.getInputStream();
            os = socket.getOutputStream();

            file = new File("client/pp.jpg");
            try {
                DataOutputStream oos = new DataOutputStream(os);
                oos.writeLong(file.length());
                oos.writeUTF(file.getName());
                int count;
                FileInputStream ois = new FileInputStream(file);

                while ((count = ois.read(buffer)) != -1){
                    oos.write(buffer,0,count);
                }
                oos.flush();
                ois.close();
            } catch (Exception e){
                e.printStackTrace();
            }

            Thread readThread = new Thread(() -> {
                try {
                    while (true) {
                        int read = is.read(buffer);
                        String msg = new String(buffer, 0, read);
                        Platform.runLater(() -> listView.getItems().add(msg));
                        listView.getItems().add(msg);
                    }
                }catch (Exception e){
                    System.err.println("Exception while read");
                }
            });
            readThread.setDaemon(true);
            readThread.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        File dir = new File("./client/");
        listView.getItems().clear();
        listView.getItems().addAll(dir.list());
    }

}
