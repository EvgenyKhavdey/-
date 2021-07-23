package model;

public class FileDelete implements AbstractCommand{
    private String name;

    public FileDelete(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public CommandType getType() {
        return CommandType.FILE_DELETE;
    }
}
