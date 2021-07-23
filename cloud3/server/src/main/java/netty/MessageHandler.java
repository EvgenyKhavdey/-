package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import model.Message;

import java.io.File;
import java.io.FileOutputStream;


@Slf4j
public class MessageHandler extends SimpleChannelInboundHandler<Message> {
    private ByteBuf buf;
    FileOutputStream oos;
    long sizeFile;
    String name = "./server/serverFile/";
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
            sizeFile = message.getSizeFile();
            file = new File(name + message.getName());
            oos = new FileOutputStream(file);
            buf.writeBytes(message.getBuffer());
            int i = buf.readableBytes();
            buf.readBytes(oos, i);
            oos.flush();
            buf.clear();



    }

}
