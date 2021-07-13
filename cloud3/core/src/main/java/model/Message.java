package model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Message implements Serializable {

    private Date sendAt;
    private String name;
    private long sizeFile;
    private byte[] buffer;


    public Message(byte[] buffer, String name, long sizeFile) {
        this.buffer = buffer;
        this.name = name;
        this.sizeFile = sizeFile;
        sendAt = new Date();

    }

}
