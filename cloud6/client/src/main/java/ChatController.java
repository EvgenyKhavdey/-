import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import lombok.SneakyThrows;
import model.Message;

public class ChatController implements Initializable {

    public ListView<String> listView;
    public TextField statusBar;
    private String root = "client/clientFiles/";
    private ObjectDecoderInputStream is;
    private ObjectEncoderOutputStream os;
    private NettyNetwork network;
    private FileOutputStream oos;
    private File file;
    private long fileSize;
    private int count;
    private ChannelHandlerContext ctx;
    private ClientHandlerFile clientHandlerFile;


    @SneakyThrows
    public void send(ActionEvent actionEvent) {
        String content = statusBar.getText();
        network = new NettyNetwork(new CallBack() {
            @Override
            public void call(Message s) {

            }

            @Override
            public String foo(String s) {
                s = content;
                return s;
            }
        });
//        network.writeMessage(new Message(content));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = new NettyNetwork(new CallBack() {
            @Override
            public void call(Message message) {
                try{
                if(message.getName() != null) {
                    String name = message.getName();
                    fileSize = message.getFileSize();
                    file = new File(root + name);
                    oos = new FileOutputStream(file);
                } else if(message.getBuffer() != null) {
                    count = (int) Math.min(fileSize, message.getBuffer().length);
                    oos.write(message.getBuffer(),0,count);
                    fileSize -= count;
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public String foo(String s) {

                return s;
            }

        });


    }

}

