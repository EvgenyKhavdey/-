import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class NettyNetwork  {

    private final CallBack callBack;
    private SocketChannel channel;
    EventLoopGroup worker;

    public NettyNetwork(CallBack callBack) throws Exception {
        this.callBack = callBack;
        Thread thread = new Thread(() -> {
                worker = new NioEventLoopGroup();
                try {
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(worker)
                            .channel(NioSocketChannel.class)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel c) throws Exception {
                                    channel = c;
                                    c.pipeline().addLast(
                                            new ObjectEncoder(),
                                            new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                            new ClientCommandHandler(callBack)
                                    );
                                }
                            });

                    ChannelFuture future = bootstrap.connect("localhost", 8189).sync();
                    future.channel().closeFuture().sync();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    worker.shutdownGracefully();
                }
        });
        thread.setDaemon(true);
        thread.start();
    }


    public void writeMessage(AbstractCommand message) {
        channel.writeAndFlush(message);
    }

    public void close(){
        worker.shutdownGracefully();
    }

}

