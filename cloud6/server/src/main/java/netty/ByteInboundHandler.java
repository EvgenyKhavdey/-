package netty;

import java.nio.charset.StandardCharsets;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByteInboundHandler extends ChannelInboundHandlerAdapter {

    private final ScriptEngine engine;

    public ByteInboundHandler() {
        ScriptEngineManager factory = new ScriptEngineManager();
        engine = factory.getEngineByName("nashorn");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client accepted...");// указывает когда подключился клиент, можно подключить авторизацию
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("received: {}", msg);
        ByteBuf buf = (ByteBuf) msg;

        StringBuilder s = new StringBuilder();
        while (buf.isReadable()) {
            char b = (char) buf.readByte();
            s.append(b);
        }
        log.debug("msg: {}", s);

        String expression = s.toString();
        String result = engine.eval(expression).toString() + "\r\n";// логика для получения калькулятора

        log.debug("evaluated: {}", result);
        ByteBuf res = ctx.alloc().buffer();// разобраться непонятная вещь
        res.writeBytes(result.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(res);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Client disconnected...");// указывает когда клиент отключился
    }
}
