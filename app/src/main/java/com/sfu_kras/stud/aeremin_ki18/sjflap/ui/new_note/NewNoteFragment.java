package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.new_note;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sfu_kras.stud.aeremin_ki18.sjflap.R;

public class NewNoteFragment extends Fragment {

    private NewNoteViewModel mViewModel;

    public static NewNoteFragment newInstance() {
        return new NewNoteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_note, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NewNoteViewModel.class);
        // TODO: Use the ViewModel
    }

}