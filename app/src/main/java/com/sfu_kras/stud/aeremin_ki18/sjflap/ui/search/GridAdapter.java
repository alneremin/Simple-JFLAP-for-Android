package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sfu_kras.stud.aeremin_ki18.sjflap.R;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.home.HomeElement;

import java.util.ArrayList;
import java.util.List;


public class GridAdapter extends ArrayAdapter<HomeElement> {

    private int resourceLayout;
    private Context mContext;

    public GridAdapter(Context context, int resource, List<HomeElement> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        HomeElement p = getItem(position);

        if (p != null) {
            TextView textView = (TextView) v.findViewById(R.id.textView);
            ImageView imageView = (ImageView) v.findViewById(R.id.IvPicture);

            if (textView != null) {
                textView.setText(p.getText());
            }

            if (imageView != null) {
                imageView.setImageDrawable(p.getImg());
            }
        }

        return v;
    }
}

