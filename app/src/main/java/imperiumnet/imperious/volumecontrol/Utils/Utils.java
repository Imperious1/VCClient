package imperiumnet.imperious.volumecontrol.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import imperiumnet.imperious.volumecontrol.models.VolRequestModel;

/**
 * Created by blaze on 8/22/2016.
 */
public class Utils {

    public static String toJson(VolRequestModel model) {
        return new Gson().toJson(model);
    }

    public static VolRequestModel fromJson(String json) {
        return new Gson().fromJson(json, VolRequestModel.class);
    }

    public static boolean connectionOk(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean checkConnection(Socket socket, Context context) {
        return (socket.isClosed() || checkConnection(context));
    }

    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifi.isConnected();
    }

    public static void sendData(final Socket socket, final int increment, final int request) {
        if (socket != null && socket.isConnected())
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        PrintWriter mPrintWriter = new PrintWriter(socket.getOutputStream(), false);
                        mPrintWriter.write(String.format("%s <EOF>", Utils.toJson(
                                new VolRequestModel().setIncrement(increment).setReq(request))));
                        mPrintWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
    }


    public static void sendData(Socket socket, String rawData) {
        if (socket != null)
            try {
                PrintWriter mPrintWriter = new PrintWriter(socket.getOutputStream(), false);
                mPrintWriter.write(rawData);
                mPrintWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
