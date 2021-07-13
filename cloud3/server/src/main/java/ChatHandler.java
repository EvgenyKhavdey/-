import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatHandler implements Runnable {

    private String root = "server/serverFiles";
    private Socket socket;
    private byte[] buffer;
    private DataInputStream is;
    private DataOutputStream os;

    public ChatHandler(Socket socket) {
        this.socket = socket;
        buffer = new byte[256];
    }

    @Override
    public void run() {
        try {
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            while (true) {
                processFileMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void processFileMessage() throws IOException {

        String fileName = is.readUTF();
        System.out.println("Received fileName: " + fileName);
        long size = is.readLong();

        System.out.println("Received fileSize: " + size);
        try(FileOutputStream fos = new FileOutputStream(root + "/" + fileName)) {
            for (int i = 0; i < (size + 255) / 256; i++) {
                int read = is.read(buffer);
                fos.write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        os.writeUTF("File: " + fileName + " received");

    }
}
