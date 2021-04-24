package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.message;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentTransaction;
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

public class ChatFragment extends Fragment {

    private ChatViewModel mViewModel;
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 9;

    protected HomeFragment.LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected ChatAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<ChatElement> mDataset = new ArrayList<>();
    protected ConstraintLayout defaultLayout;
    protected ImageButton btnBack;
    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defaultLayout = getLayoutInflater().inflate(R.layout.chat_row_item, null).findViewById(R.id.chat_layout);

        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.
        initDataset();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        mRecyclerView = root.findViewById(R.id.chat_view);
        btnBack = root.findViewById(R.id.btn_chat_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatListFragment fragment = new ChatListFragment(); // Фрагмент, которым собираетесь заменить первый фрагмент

                FragmentTransaction transaction = getFragmentManager().beginTransaction(); // Или getSupportFragmentManager(), если используете support.v4
                transaction.replace(R.id.nav_host_fragment, fragment); // Заменяете вторым фрагментом. Т.е. вместо метода `add()`, используете метод `replace()`
                transaction.addToBackStack(null); // Добавляете в backstack, чтобы можно было вернутся обратно

                transaction.commit(); // Коммитете
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = HomeFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (HomeFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }

        mAdapter = new ChatAdapter(mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }


    private void initDataset() {
        //mDataset = new HomeData[DATASET_COUNT];
        Drawable img;
        ConstraintSet set;

        ConstraintSet defaultLayoutSet = new ConstraintSet();
        defaultLayoutSet.clone(defaultLayout);

        for (int i = 0; i < DATASET_COUNT; i++) {

            set = new ConstraintSet();
            set.clone(defaultLayoutSet);
            if (i % 2 != 0) {

                set.clear(R.id.chat_text, ConstraintSet.START);
                set.clear(R.id.chat_text, ConstraintSet.END);
                set.clear(R.id.chat_img, ConstraintSet.END);

                set.connect(R.id.chat_text, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 8);
                set.connect(R.id.chat_text, ConstraintSet.END, R.id.chat_img, ConstraintSet.START);
                set.connect(R.id.chat_img, ConstraintSet.START, R.id.chat_text, ConstraintSet.END, 16);
                set.connect(R.id.chat_img, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            }
            // TODO здесь должен загружаться аватар и сообщение из БД
            img = getActivity().getDrawable(R.drawable.img_avatar);

            mDataset.add(
                    new ChatElement(
                            "this is text. Number is " + i,
                            img,
                            set
                    )
            );
        }
    }
}