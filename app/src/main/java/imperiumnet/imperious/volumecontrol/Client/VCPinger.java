package imperiumnet.imperious.volumecontrol.Client;

import android.content.Context;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import imperiumnet.imperious.volumecontrol.callbacks.OnDisconnectListener;
import imperiumnet.imperious.volumecontrol.callbacks.OnTaskCompletedListener;

import static imperiumnet.imperious.volumecontrol.Utils.Utils.checkConnection;
import static imperiumnet.imperious.volumecontrol.Utils.Utils.sendData;

/**
 * Created by blaze on 8/24/2016.
 */
public class VCPinger {

    boolean permitted;
    OnTaskCompletedListener onTaskCompletedListener;
    OnDisconnectListener onDisconnectListener;

    public VCPinger(Context context) {
        this.permitted = true;
        onTaskCompletedListener = (OnTaskCompletedListener) context;
        onDisconnectListener = (OnDisconnectListener) context;
    }

    public void startPinger(final Socket socket, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (socket.isConnected()) {
                    try {
                        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                        char[] buffer = new char[1024];
                        pingServer(socket);
                        while (permitted && isr.read(buffer) != -1) {
                            System.out.println("ping \"received\"");
                            if (!checkConnection(socket, context)) {
                                onTaskCompletedListener.onTaskCompleted();
                                break;
                            }
                            pingServer(socket);
                        }
                    } catch (Exception e) {
                        System.out.println("exception in pinger");
                        e.printStackTrace();
                    }
                }
                onDisconnectListener.disconnect();
            }
        }).start();
    }

    public void pingServer(Socket socket) throws IOException, InterruptedException {
        sendData(socket, "ping");
        Thread.sleep(5000);
    }

    public void stopPinger() {
        permitted = false;
    }
}