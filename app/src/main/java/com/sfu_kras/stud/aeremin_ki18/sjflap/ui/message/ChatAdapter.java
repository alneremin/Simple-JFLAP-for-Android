package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import com.sfu_kras.stud.aeremin_ki18.sjflap.R;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private ArrayList<ChatElement> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;
        private final  ConstraintLayout constraintLayout;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            textView = (TextView) view.findViewById(R.id.chat_text);
            imageView = (ImageView) view.findViewById(R.id.chat_img);
            constraintLayout = (ConstraintLayout) view.findViewById(R.id.chat_layout);
        }
        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView() {
            return imageView;
        }
        public ConstraintLayout getConstraintLayout() {
            return constraintLayout;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet ArrayList<HomeElement> containing the data to populate views to be used
     * by RecyclerView.
     */
    public ChatAdapter(ArrayList<ChatElement> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.getTextView().setText(localDataSet.get(position).getText());
        viewHolder.getImageView().setImageDrawable(localDataSet.get(position).getImg());
        viewHolder.getConstraintLayout().setConstraintSet(localDataSet.get(position).getSet());
        //localDataSet.get(position).getSet().applyTo(viewHolder.getConstraintLayout());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

