package model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Message implements Serializable {

    private Date sendAt;
    private String name;
    private long fileSize;
    private byte[] buffer;


    public Message(byte[] buffer) {
        this.buffer = buffer;
        sendAt = new Date();
    }

    public Message(String name, long fileSize) {
        this.sendAt = sendAt;
        this.name = name;
        this.fileSize = fileSize;
    }

    public Message(String name) {
        this.name = name;
        sendAt = new Date();
    }

}
