package netty;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import model.*;
import model.FileResponse;

@Slf4j
public class CommandHandler extends SimpleChannelInboundHandler<AbstractCommand> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractCommand command) throws Exception {
        log.debug("received: {}", command.getType());
        switch (command.getType()) {
            case LIST_REQUEST:
                ctx.writeAndFlush(new ListResponse(Paths.get("server", "filesServer")));
                break;
            case FILE_MESSAGE:
                FileMessage message = (FileMessage) command;
                Files.copy(message.getFile().toPath(), (new File(Server.filesServer + message.getName())).toPath());
                ctx.writeAndFlush(new ListResponse(Paths.get("server", "filesServer")));
                break;
            case FILE_REQUEST:
                File file = new File(Server.filesServer + ((FileResponse) command).getName());
                ctx.writeAndFlush(new FileResponse(file.getName(), file.length(), file));
                break;
            case FILE_DELETE:
                Files.delete(Paths.get(Server.filesServer + ((FileDelete) command).getName()));
                ctx.writeAndFlush(new ListResponse(Paths.get("server", "filesServer")));
                break;
            case AUTHORIZATION:
                ResultSet result = new DatabaseHandler().getUser((User) command);
                int count = 0;

                while (result.next()){
                    count++;
                }
                if(count >= 1){
                    System.out.println("Пользователь найден");
                }
        }

    }


}
