package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sfu_kras.stud.aeremin_ki18.sjflap.MainActivity;
import com.sfu_kras.stud.aeremin_ki18.sjflap.R;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.home.CustomAdapter;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.home.HomeElement;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.home.HomeFragment;

import java.io.File;
import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private static final int DATASET_COUNT = 10;

    private SearchViewModel mViewModel;

    protected GridView mRecyclerView;
    protected GridAdapter mAdapter;
    protected ArrayList<HomeElement> mDataset = new ArrayList<>();
    protected EditText textSearch;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initDataset();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        textSearch = root.findViewById(R.id.search_field);
        mRecyclerView = (GridView) root.findViewById(R.id.project_view);
        System.out.println(mDataset.size());
        mAdapter = new GridAdapter(getContext(), R.layout.text_row_item, mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBoard(mDataset.get(position).getText());
            }
        });

        textSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    performSearch(v.getText().toString());
                }
                return false;
            }
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void performSearch(String text) {
        mDataset.clear();
        initDataset();
        if (mDataset.size() > 0) {
            for (int i = mDataset.size() - 2; i >= 0; i--)
                if (!mDataset.get(i).getText().contains(text)) {
                    mDataset.remove(i);
                }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        // TODO: Use the ViewModel
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initDataset() {
        //mDataset = new HomeData[DATASET_COUNT];
        MainActivity main = (MainActivity) getActivity();
        ArrayList<File> files = main.getFileHelper().getFiles();
        Drawable img;
        String txt;

        for (int i = 0; i <= files.size(); i++) {
            if (i == files.size()) {
                img = getActivity().getDrawable(R.drawable.ic_new);
                txt = "";
            } else {
                img = getActivity().getDrawable(R.drawable.ic_dfa);
                txt = files.get(i).getName();
                if (txt.length() > 3) {
                    txt = txt.substring(0, txt.length() - 4);
                }
            }
            mDataset.add(
                    new HomeElement(i, txt, img)
            );
        }
    }


    public void openBoard(String path) {
        ((MainActivity) getActivity()).setCurrentFile(path);
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(
                R.id.action_search_to_work_panel
        );

    }

}