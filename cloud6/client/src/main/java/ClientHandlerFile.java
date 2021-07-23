import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import model.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ClientHandlerFile  extends ChannelInboundHandlerAdapter {
    private ByteBuf buf;
    private String direct = "client/clientFiles/";
    private File file;
    byte[] buffer;
    private String name;
    private CallBack callBack;


    public ClientHandlerFile(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        name = callBack.foo(name);
        file = new File(direct + name);
        String name = file.getName();
        long fileSize = file.length();
        ctx.writeAndFlush(new Message(name, fileSize));
        buffer = new byte[256];
        int count;
        try(FileInputStream ois = new FileInputStream(file);
        ) {
            while ((count = ois.read(buffer)) != -1) {
                ctx.writeAndFlush(new Message(buffer));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

}
