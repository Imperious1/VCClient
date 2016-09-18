package imperiumnet.imperious.volumecontrol.Client.base;

import android.content.Context;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by blaze on 8/22/2016.
 */
public abstract class Client {

    protected Socket socket;
    protected int port;
    Context context;

    public Client(int port, Context context) {
        this.port = port;
        this.context = context;
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

    public abstract void onTaskComplete();
}