package nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class BufferExample {

    public static void main(String[] args) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(5);



        RandomAccessFile raf = new RandomAccessFile("server/serverFile/test.txt", "rw");
        FileChannel channel = raf.getChannel();



        channel.position(0);

        buffer.clear();
        int d = channel.read(buffer);
        buffer.flip();

        System.out.println(d);
//        buffer.rewind();
//        while (buffer.hasRemaining()) {
//            System.out.println((char) buffer.get());
//        }
//
//        buffer.clear();
//        while (buffer.hasRemaining()) {
//            System.out.println((char) buffer.get());
//        }
    }
}
