package model;

public class UserAutho implements AbstractCommand{
    @Override
    public CommandType getType() {
        return CommandType.AUTHORIZATION_PASSED;
    }
}
