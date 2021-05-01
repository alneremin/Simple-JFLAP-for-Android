package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sfu_kras.stud.aeremin_ki18.sjflap.R;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.home.CustomAdapter;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.home.HomeElement;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.home.HomeFragment;

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
            for (int i = mDataset.size() - 1; i >= 0; i--)
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
        Drawable img;
        for (int i = 0; i < DATASET_COUNT; i++) {

            img = getActivity().getDrawable(R.drawable.ic_dfa);
            mDataset.add(
                    new HomeElement(
                            i,
                            "is element #" + i,
                            img
                    )
            );
        }
    }

}