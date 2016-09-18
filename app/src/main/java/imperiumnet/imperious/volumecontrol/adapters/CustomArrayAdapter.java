package imperiumnet.imperious.volumecontrol.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by blaze on 8/28/2016.
 */
public abstract class CustomArrayAdapter extends ArrayAdapter<String> {

    private String[] data;

    public CustomArrayAdapter(Context context, String[] data) {
        super(context, 0, data);
        this.data = data;
    }

    public abstract void onClick2(View v, int position);

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick2(v, position);
            }
        });
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        convertView.setBackgroundResource(outValue.resourceId);
        convertView.setClickable(true);
        convertView.setFocusable(true);
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(data[position]);
        return convertView;
    }
}
