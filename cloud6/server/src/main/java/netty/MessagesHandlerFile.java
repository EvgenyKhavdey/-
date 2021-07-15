package netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MessagesHandlerFile extends SimpleChannelInboundHandler<Message> {

    private FileOutputStream oos;
    private File file;
    private long fileSize;
    private int count;
    private String root = "./server/serverFile/";
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
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
}
