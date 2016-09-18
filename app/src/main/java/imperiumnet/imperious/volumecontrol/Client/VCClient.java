package imperiumnet.imperious.volumecontrol.Client;

import android.content.Context;
import android.preference.PreferenceManager;

import java.io.IOException;

import imperiumnet.imperious.volumecontrol.Client.base.Client;

import static imperiumnet.imperious.volumecontrol.Utils.Utils.checkConnection;
import static imperiumnet.imperious.volumecontrol.Utils.Utils.sendData;

/**
 * Created by blaze on 8/22/2016.
 */
public class VCClient extends Client {

    Context context;
    private VCPinger pingPong;
    boolean permitted;

    public VCClient(int port, Context context) throws IOException {
        super(port, context);
        this.context = context;
        this.permitted = true;
    }

    public void connect() {
        super.connect();
    }

    public boolean disconnect() throws IOException {
        if (pingPong != null) {
            pingPong.stopPinger();
            pingPong = null;
        }
        return super.disconnect();
    }

    public boolean disconnectByUserRequest() throws IOException {
        killKeepAlive();
        if (pingPong != null) {
            pingPong.stopPinger();
            pingPong = null;
        }
        return super.disconnect();
    }

    public void raiseVolume(int increment) {
        sendData(socket, increment, 1);
    }

    public void lowerVolume(int increment) {
        sendData(socket, increment, 2);
    }

    public void mute() {
        sendData(socket, 0, 3);
    }

    @Override
    public void onTaskComplete() {
        try {
            pingPong = new VCPinger(context);
            pingPong.startPinger(socket, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void keepAlive() {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("auto_reconnect", false))
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (!permitted) permitted = true;
                    try {
                        while (permitted) {
                            System.out.println("keepalive ran");
                            if (isClosed() && checkConnection(context)) {
                                System.out.println("reconnecting from KEEP ALIVE");
                                connect();
                            }
                            Thread.sleep(5000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        else connect();
    }

    public boolean isConnected() {
        return socket == null || socket.isConnected();
    }

    public boolean isClosed() {
        return socket == null || socket.isClosed();
    }

    public boolean isSocketNull() {
        return socket == null;
    }

    public void killKeepAlive() {
        permitted = false;
    }

    public boolean isKeptAlive() {
        return permitted;
    }

}