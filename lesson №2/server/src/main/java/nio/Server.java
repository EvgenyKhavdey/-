package nio;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import static java.lang.String.*;

public class Server {

    private static int cnt = 1;
    private ServerSocketChannel sc;
    private Selector selector;
    private String name = "user";

    public Server() throws IOException {
        sc = ServerSocketChannel.open();
        selector = Selector.open();
        sc.bind(new InetSocketAddress(8189));
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_ACCEPT);

        while (sc.isOpen()) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    handleAccept(key);
                }
                if (key.isReadable()) {
                    handleRead(key);
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server();
    }

    private void handleRead(SelectionKey key) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        SocketChannel channel = (SocketChannel) key.channel();
        String name = (String) key.attachment();
        int read;
        StringBuilder sb = new StringBuilder();
        String cmr1;
        String cmr2;
        while (true) {
            read = channel.read(buffer);
            buffer.flip();
            if (read == -1) {
                channel.close();
                break;
            }
            if (read > 0) {
                while (buffer.hasRemaining()) {
                    sb.append((char) buffer.get());
                }
                buffer.clear();
            } else {
                break;
            }
        }

//     В данной части кода выполняем проверку поступивших сообщений и выявляем ключевые стова(команды)
        if (sb.length() >= 2) {
            try {
                cmr1 = sb.substring(0, 2);
                cmr2 = sb.substring(0, 3);
                if (cmr1.equalsIgnoreCase("Ls")) {
                    listFilesDirectories(sb, channel);
                } else if (cmr2.equalsIgnoreCase("Cat")) {
                    fileContents(sb, channel);
                } else {
                    printMessage(sb, channel);
                }
            }catch (StringIndexOutOfBoundsException e){
                e.printStackTrace();
            }
        } else {
            printMessage(sb, channel);
        }
    }


    private void handleAccept(SelectionKey key) throws IOException {
        SocketChannel channel = sc.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ, name + cnt);
        cnt++;
    }

    private void printMessage(StringBuilder sb, SocketChannel channel) throws IOException {
        System.out.println("received: " + sb);
        channel.write(ByteBuffer.wrap((name + ": " + sb).getBytes(StandardCharsets.UTF_8)));
    }

//Данный метод выводит существующие файлы в заданной директории
    private void listFilesDirectories(StringBuilder sb, SocketChannel channel) throws IOException {
        String cmr = sb.substring(2);
        cmr = cmr.trim();
        Path root = Paths.get(cmr);
        try {
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println(file);
                    String fileString = file.toAbsolutePath().toString();
                    channel.write(ByteBuffer.wrap((fileString + "\r\n").getBytes(StandardCharsets.UTF_8)));
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (StringIndexOutOfBoundsException e){
            channel.write(ByteBuffer.wrap((name  + ": wrong team" + "\r\n").getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e){
            e.printStackTrace();
        }
        channel.write(ByteBuffer.wrap(("\r\n").getBytes(StandardCharsets.UTF_8)));
        channel.write(ByteBuffer.wrap((name + ": request completed" + "\r\n").getBytes(StandardCharsets.UTF_8)));
    }

// Данный метод выводит содержимое заданного файла (test.txt)
    private void fileContents(StringBuilder sb, SocketChannel channel) throws IOException {
        Path root = Paths.get("./");
        String cmr = sb.substring(3);
        String fileToFind = File.separator + cmr.trim();
        final Path[] addressFile = new Path[1];
        try {
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileString = file.toAbsolutePath().toString();
                    if (fileString.endsWith(fileToFind)) {
                        addressFile[0] = Paths.get(file.toAbsolutePath().toString());
                        return FileVisitResult.TERMINATE;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File(String.valueOf(addressFile[0]));
        if (file.exists()) {
            for (String line : Files.readAllLines(Paths.get(String.valueOf(addressFile[0])))) {
                channel.write(ByteBuffer.wrap((line).getBytes(StandardCharsets.UTF_8)));
            }
            channel.write(ByteBuffer.wrap(("\r\n").getBytes(StandardCharsets.UTF_8)));
            channel.write(ByteBuffer.wrap((name + ": request completed" + "\r\n").getBytes(StandardCharsets.UTF_8)));
        } else {
            channel.write(ByteBuffer.wrap((name  + ": wrong team" + "\r\n").getBytes(StandardCharsets.UTF_8)));
        }
    }
}
