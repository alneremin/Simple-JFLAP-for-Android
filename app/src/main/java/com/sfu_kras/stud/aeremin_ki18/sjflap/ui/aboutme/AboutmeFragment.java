package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.aboutme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
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
import com.sfu_kras.stud.aeremin_ki18.sjflap.StartActivity;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.filemanage.DriveServiceHelper;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.google_maps.MapsFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class AboutmeFragment extends Fragment {

    private AboutmeViewModel mViewModel;
    private Button btnAddress;
    private TextView email;
    private CircleImageView img;
    private Button btnSync;
    private Button btnLogOut;
    private MainActivity main;
    public static AboutmeFragment newInstance() {
        return new AboutmeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_aboutme, container, false);
        btnAddress = root.findViewById(R.id.btn_address);
        email = root.findViewById(R.id.name_view);
        img = root.findViewById(R.id.cardView);
        main = ((MainActivity)getActivity());

        if (main.getUser() != null) {
            email.setText(main.getUser().getEmail());
        }

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

        main.getLiveDriveServiceHelper().observe(
                this, new Observer<DriveServiceHelper>() {
                    @Override
                    public void onChanged(DriveServiceHelper driveServiceHelper) {
                        if (driveServiceHelper != null) {
                            email.setTextSize(20);
                            email.setText(main.getSignInGoogle().getEmail());
                            main.getSignInGoogle().loadPhoto();
                        }
                    }
                }
        );

        main.getPhoto().observe(
                this, new Observer<Bitmap>() {
                    @Override
                    public void onChanged(Bitmap bmp) {
                        if (bmp != null) {
                            img.setImageBitmap(bmp);
                        }
                    }
                }
        );

        btnSync = root.findViewById(R.id.btn_sync_with_google);
        btnSync.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (main.getDriveServiceHelper() != null) {
                    try {
                        main.getFileManager().startSync();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    main.getSignInGoogle().startSignIn();
                }
            }
        });

        btnLogOut = root.findViewById(R.id.btn_log_out);
        btnLogOut.setOnClickListener(v -> {
            main.getSignInGoogle().signOut();
            Intent intent = new Intent(getContext(), StartActivity.class);
            main.startActivity(intent);
        });
        /*
        ((TextView) root.findViewById(R.id.name_view)).setText(
                ((MainActivity)getActivity()).getUser().getName()
        );

        ((Button) root.findViewById(R.id.btn_address)).setText(
                ((MainActivity)getActivity()).getUser().getAddress()
        );*/

        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AboutmeViewModel.class);
        // TODO: Use the ViewModel
        String mail = main.getSignInGoogle().getEmail();
        if (mail != null && !mail.isEmpty()) {
            email.setText(mail);
            email.setTextSize(20);
        }
        main.getSignInGoogle().loadPhoto();
    }

}