package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.aboutme;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sfu_kras.stud.aeremin_ki18.sjflap.MainActivity;
import com.sfu_kras.stud.aeremin_ki18.sjflap.R;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.google_maps.MapsFragment;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.message.ChatFragment;

public class AboutmeFragment extends Fragment {

    private AboutmeViewModel mViewModel;
    private Button btnAddress;

    public static AboutmeFragment newInstance() {
        return new AboutmeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_aboutme, container, false);
        btnAddress = root.findViewById(R.id.btn_address);
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapsFragment fragment = new MapsFragment(); // Фрагмент, которым собираетесь заменить первый фрагмент
                FragmentTransaction transaction = getFragmentManager().beginTransaction(); // Или getSupportFragmentManager(), если используете support.v4
                transaction.replace(R.id.nav_host_fragment, fragment); // Заменяете вторым фрагментом. Т.е. вместо метода `add()`, используете метод `replace()`
                transaction.addToBackStack(null); // Добавляете в backstack, чтобы можно было вернутся обратно

                transaction.commit(); // Коммитете
            }
        });

        ((TextView) root.findViewById(R.id.name_view)).setText(
                ((MainActivity)getActivity()).getUser().getName()
        );

        ((Button) root.findViewById(R.id.btn_address)).setText(
                ((MainActivity)getActivity()).getUser().getAddress()
        );


        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AboutmeViewModel.class);
        // TODO: Use the ViewModel
        //ImageView avatar = getActivity().findViewById(R.id.avatar_view);
        //avatar.setClipToOutline(true);
    }

}