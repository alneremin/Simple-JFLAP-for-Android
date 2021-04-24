package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.sfu_kras.stud.aeremin_ki18.sjflap.R;

import java.util.ArrayList;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private ArrayList<ChatListElement> localDataSet;
    private Fragment chatListFragment;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView userNameTextView;
        private final TextView userTextView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            userNameTextView = (TextView) view.findViewById(R.id.chat_list_name);
            userTextView = (TextView) view.findViewById(R.id.chat_list_text);
            imageView = (ImageView) view.findViewById(R.id.chat_img);

        }
        public TextView getUserNameTextView() {
            return userNameTextView;
        }
        public TextView getUserTextView() {
            return userTextView;
        }
        public ImageView getImageView() {
            return imageView;
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet ArrayList<HomeElement> containing the data to populate views to be used
     * by RecyclerView.
     */
    public ChatListAdapter(ArrayList<ChatListElement> dataSet, Fragment chatListFragment) {

        this.chatListFragment = chatListFragment;
        this.localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_list_row_item, viewGroup, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatFragment fragment = new ChatFragment(); // Фрагмент, которым собираетесь заменить первый фрагмент
                FragmentTransaction transaction = chatListFragment.getFragmentManager().beginTransaction(); // Или getSupportFragmentManager(), если используете support.v4
                transaction.replace(R.id.nav_host_fragment, fragment); // Заменяете вторым фрагментом. Т.е. вместо метода `add()`, используете метод `replace()`
                transaction.addToBackStack(null); // Добавляете в backstack, чтобы можно было вернутся обратно

                transaction.commit(); // Коммитете
            }
        });

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.getUserTextView().setText(localDataSet.get(position).getUserText());
        viewHolder.getUserNameTextView().setText(localDataSet.get(position).getUserName());
        viewHolder.getImageView().setImageDrawable(localDataSet.get(position).getImg());

        //localDataSet.get(position).getSet().applyTo(viewHolder.getConstraintLayout());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

