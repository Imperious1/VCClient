package imperiumnet.imperious.volumecontrol.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import imperiumnet.imperious.volumecontrol.R;
import imperiumnet.imperious.volumecontrol.adapters.CustomArrayAdapter;

/**
 * Created by blaze on 8/28/2016.
 */
public class IncrementDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.increment_frag_layout, null);
        ListView list = (ListView) view.findViewById(R.id.increment_list);
        list.setAdapter(new CustomArrayAdapter(getContext(), new String[]{"2", "4", "6", "8", "10", "12", "14", "16", "18", "20"}) {
            @Override
            public void onClick2(View v, int position) {
                alterIncrement(position);
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    void alterIncrement(int position) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        switch(position) {
            case 0:
                editor.putInt("increment", 0);
                break;
            case 1:
                editor.putInt("increment", 1);
                break;
            case 2:
                editor.putInt("increment", 2);
                break;
            case 3:
                editor.putInt("increment", 3);
                break;
            case 4:
                editor.putInt("increment", 4);
                break;
            case 5:
                editor.putInt("increment", 5);
                break;
            case 6:
                editor.putInt("increment", 6);
                break;
            case 7:
                editor.putInt("increment", 7);
                break;
            case 8:
                editor.putInt("increment", 8);
                break;
            case 9:
                editor.putInt("increment", 91);
                break;
        }
        editor.apply();
    }

}
