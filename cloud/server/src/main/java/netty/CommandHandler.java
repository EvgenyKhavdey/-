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

import static netty.Const.USERS_ID;

@Slf4j
public class CommandHandler extends SimpleChannelInboundHandler<AbstractCommand> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractCommand command) throws Exception {
        log.debug("received: {}", command.getType());
        switch (command.getType()) {
            case LIST_REQUEST:
                ctx.writeAndFlush(new ListResponse(Paths.get("server/filesServer", Server.direct)));
                break;
            case FILE_MESSAGE:
                FileMessage message = (FileMessage) command;
                Files.copy(message.getFile().toPath(), (new File(Server.filesServer + Server.direct + "/" + message.getName())).toPath());
                ctx.writeAndFlush(new ListResponse(Paths.get("server/filesServer", "/" + Server.direct)));
                break;
            case FILE_REQUEST:
                File file = new File(Server.filesServer + Server.direct + "/" + ((FileResponse) command).getName());
                ctx.writeAndFlush(new FileResponse(file.getName(), file.length(), file));
                break;
            case FILE_DELETE:
                Files.delete(Paths.get(Server.filesServer + Server.direct + "/" + ((FileDelete) command).getName()));
                ctx.writeAndFlush(new ListResponse(Paths.get("server/filesServer", Server.direct)));
                break;
            case AUTHORIZATION:
                authorizationUser(ctx, command);
        }

    }

    private void authorizationUser(ChannelHandlerContext ctx, AbstractCommand command) throws Exception {
        ResultSet result = new DatabaseHandler().getUser((User) command);
        int count = 0;

        while (result.next()){
            Server.direct = result.getString("id");
            count++;
        }
        if(count >= 1){
            ctx.writeAndFlush(new UserAutho());

        }
    }




}
