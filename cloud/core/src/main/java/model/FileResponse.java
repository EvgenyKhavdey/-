package model;

import java.io.File;

public class FileResponse implements AbstractCommand{
    private String name;
    private long size;
    private File file;

    public FileResponse(String name, long size, File file) throws Exception{
        this.name = name;
        this.size = size;
        this.file = file;
    }

    public FileResponse(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setSize(long size) {
        this.size = size;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }



    @Override
    public CommandType getType() {
        return CommandType.FILE_REQUEST;
    }
}
