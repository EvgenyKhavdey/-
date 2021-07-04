import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ChatHandler implements  Runnable{
    private Socket socket;
    private byte [] buffer;
    private InputStream is;
    private OutputStream os;
    File file;


    public ChatHandler(Socket socket) {
        this.socket = socket;
        buffer = new byte[256];
    }

    @Override
    public void run() {
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
            try {
                DataInputStream ois = new DataInputStream(is);
                Long sizeFile = ois.readLong();
                String nameFile = ois.readUTF();
                file = new File("./server/" + nameFile);
                FileOutputStream oos = new FileOutputStream(file);

                while (true) {
                    int next = (int) Math.min(sizeFile, buffer.length);
                    int count = is.read(buffer, 0, next);
                    if (count <= 0) {
                        throw new RuntimeException("Error");
                    }
                    oos.write(buffer, 0, count);

                    sizeFile -= count;
                    if (sizeFile == 0) {
                        break;
                    }
                }
                oos.flush();
                oos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            File dir = new File("./server/");
            System.out.println(Arrays.toString(dir.list()));

            while (true){
                int read = is.read(buffer);
                System.out.println("Received: " + new String(buffer, 0,read));
                os.write(buffer, 0 ,read);
                os.flush();
            }
        } catch (Exception e) {
            System.out.println("Client connection exception");
        }finally {
           try {
               is.close();
               os.close();
           }catch (IOException ioException){
               ioException.printStackTrace();
           }
        }
    }
}
