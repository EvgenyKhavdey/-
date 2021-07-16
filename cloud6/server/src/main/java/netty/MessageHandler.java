package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import model.Message;

import java.io.*;


@Slf4j
public class MessageHandler extends SimpleChannelInboundHandler<Message> {
    private ByteBuf buf;
    RandomAccessFile oos;
    private int byteRead;
    String direct = "./server/serverFile/";
    File file;
    byte[] buffer;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx){
        buf = ctx.alloc().buffer();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx){
        buf.release();
        buf = null;
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        file = new File(direct + message.getName());
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


}
