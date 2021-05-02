package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.home;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.google.common.io.LineReader;
import com.sfu_kras.stud.aeremin_ki18.sjflap.MainActivity;
import com.sfu_kras.stud.aeremin_ki18.sjflap.R;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.message.ChatFragment;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.work_panel.WorkPanel;

import java.util.ArrayList;
import java.util.Map;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<HomeElement> localDataSet;
    private HomeFragment homeFragment;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final EditText idNumberEdit;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            textView = (TextView) view.findViewById(R.id.name_file_text_view);
            imageView = (ImageView) view.findViewById(R.id.IvPicture);
            idNumberEdit = (EditText) view.findViewById(R.id.id_number);
        }
        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView() { return imageView; }
        public EditText getIdNumberEdit() { return idNumberEdit; }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet ArrayList<HomeElement> containing the data to populate views to be used
     * by RecyclerView.
     */
    public CustomAdapter(ArrayList<HomeElement> dataSet, HomeFragment homeFragment) {

        this.localDataSet = dataSet;
        this.homeFragment = homeFragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        
        viewHolder.getTextView().setText(localDataSet.get(position).getText());
        viewHolder.getImageView().setImageDrawable(localDataSet.get(position).getImg());
        viewHolder.getIdNumberEdit().setText(String.valueOf(
                localDataSet.get(position).getId()));
        LinearLayout ll = (LinearLayout) viewHolder.getImageView().getParent();
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v.findViewById(R.id.name_file_text_view);
                openBoard(tv.getText().toString());
            }
        });
    }

    public void openBoard(String path) {

        ((MainActivity) homeFragment.requireActivity()).setCurrentFile(path);
        NavController navController = NavHostFragment.findNavController(this.homeFragment);
        navController.navigate(
                R.id.action_home_to_work_panel
        );

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

