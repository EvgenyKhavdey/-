package model;

public class UserAuthoNo implements AbstractCommand{

    @Override
    public CommandType getType() {
        return CommandType.AUTHORIZATION_NO;
    }
}
