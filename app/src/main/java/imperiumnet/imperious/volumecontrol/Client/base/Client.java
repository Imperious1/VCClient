package imperiumnet.imperious.volumecontrol.Client.base;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by blaze on 8/22/2016.
 */
public abstract class Client {

    protected Socket socket;
    protected int port;

    public Client(int port) {
        this.port = port;
    }

    protected void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress("192.168.1.140", port));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onTaskComplete();
            }
        }).start();
    }

    protected boolean disconnect() throws IOException {
        if (!socket.isClosed()) {
            socket.close();
        }
        return socket.isConnected();
    }

    //What to do after connection established
    public abstract void onTaskComplete();
}