package imperiumnet.imperious.volumecontrol.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import imperiumnet.imperious.volumecontrol.Client.VCClient;
import imperiumnet.imperious.volumecontrol.R;
import imperiumnet.imperious.volumecontrol.activities.MainActivity;
import imperiumnet.imperious.volumecontrol.callbacks.OnBtnPressedListener;

/**
 * Created by blaze on 8/30/2016.
 */
public class ConnectDialog extends DialogFragment {

    VCClient client;
    OnBtnPressedListener listener;
    EditText port;
    private static final int INTERNET_PERMISSION = 1;
    private static final int NETWORK_ACCESS_STATE_PERMISSION = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.connect_fragment, null);
        listener = (OnBtnPressedListener) getActivity();
        port = (EditText) view.findViewById(R.id.connect_frag_port);
        port.setHint(String.format("Port (default: %s)",
                PreferenceManager.getDefaultSharedPreferences(getContext()).getString("default_port", "4044")));
        Button btnConfirm = (Button) view.findViewById(R.id.connect_btn_confirm);
        Button btnCancel = (Button) view.findViewById(R.id.connect_btn_cancel);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) checkPermissions();
                else initiateFirstConnection();
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    void initiateFirstConnection() {
        try {
            if (client == null)
                client = new VCClient(Integer.parseInt((!port.getText().toString().isEmpty())
                        ? port.getText().toString() : PreferenceManager.getDefaultSharedPreferences(getContext())
                        .getString("default_port", "4044")), getContext());
            client.keepAlive();
            listener.onBtnPressed(client);
            MainActivity.hasConnectedOnce = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void checkPermissions() {
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET);
        int result2 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_NETWORK_STATE);
        if (result != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION);
        }
        if (result2 != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, NETWORK_ACCESS_STATE_PERMISSION);
        }
        if (result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED) {
            initiateFirstConnection();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == INTERNET_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateFirstConnection();
            }
        } else if (requestCode == NETWORK_ACCESS_STATE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateFirstConnection();
            }
        }
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }
}
