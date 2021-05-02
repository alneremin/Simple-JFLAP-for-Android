package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.work_panel;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sfu_kras.stud.aeremin_ki18.sjflap.MainActivity;
import com.sfu_kras.stud.aeremin_ki18.sjflap.R;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.work_panel.panel.FAMode;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.work_panel.panel.StateField;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.work_panel.panel.TransitionField;
import com.sfu_kras.stud.aeremin_ki18.sjflap.ui.work_panel.panel.WorkBoard;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkPanel extends Fragment {

    private WorkPanelViewModel mViewModel;

    public static WorkPanel newInstance() {
        return new WorkPanel();
    }

    private ArrayList<View> buttons;

    private WorkBoard workBoard;
    private EditText editText;
    private String titleText = "Title1";
    private LinearLayout linearLayout;



    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }
    public WorkBoard getWorkBoard() {
        return workBoard;
    }

    public void setWorkBoard(WorkBoard workBoard) {
        this.workBoard = workBoard;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_work_panel, container, false);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity main = (MainActivity) getActivity();
        workBoard = main.findViewById(R.id.work_board);
        editText = main.findViewById(R.id.transitionText);
        linearLayout = main.findViewById(R.id.state_params);
        EditText title = main.findViewById(R.id.text_title);
        if (main.getCurrentFile().isEmpty()) {
            title.setText(titleText);
        } else {
            title.setText(main.getCurrentFile());
        }
        workBoard.getStateField().observe(this, new Observer<StateField>() {
            @Override
            public void onChanged(StateField stateField) {

                linearLayout.setX(stateField.getMiddleX());
                linearLayout.setY(stateField.getMiddleY());
                CheckBox c1 =(CheckBox)linearLayout.getChildAt(0);
                CheckBox c2 =(CheckBox)linearLayout.getChildAt(1);
                c1.setChecked(stateField.is_initial());
                c2.setChecked(stateField.is_final());
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

        workBoard.getTransitionField().observe(this, new Observer<TransitionField>() {
            @Override
            public void onChanged(TransitionField transitionField) {

                editText.setX(transitionField.getMiddleX());
                editText.setY(transitionField.getMiddleY());
                if (!transitionField.getReadStr().equals("λ")) {
                    editText.setText(transitionField.getReadStr());
                }
                editText.setVisibility(View.VISIBLE);
            }
        });

        editText.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (editText.getVisibility() != View.INVISIBLE &&
                                    (event == null || !event.isShiftPressed())) {
                                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                                editText.setVisibility(View.INVISIBLE);
                                workBoard.addTextInLastTransition(v.getText().toString());
                                editText.setText("");

                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );

        /*editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && editText.getVisibility() != View.INVISIBLE) {
                    InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (getActivity().getCurrentFocus() != null)
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    editText.setVisibility(View.INVISIBLE);
                    workBoard.addTextInLastTransition(editText.getText().toString());
                    editText.setText("");
                }
            }
        });*/

        this.buttons = new ArrayList<>();
        buttons.add(main.findViewById(R.id.btn_cursor));
        buttons.add(main.findViewById(R.id.btn_state));
        buttons.add(main.findViewById(R.id.btn_transition));
        buttons.add(main.findViewById(R.id.btn_delete_elem));
        buttons.add(main.findViewById(R.id.btn_undo));
        buttons.add(main.findViewById(R.id.btn_redo));
        buttons.add(main.findViewById(R.id.btn_save_note));
        buttons.add(main.findViewById(R.id.btn_test_auto));

        for (int i = 0; i < buttons.size();i++) {
            buttons.get(i).setId(i);
            buttons.get(i).setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {

                    for (int i = 0; i < buttons.size();i++) {
                        buttons.get(i).setBackground(main.getDrawable(R.drawable.btn_work_panel_no_focused));
                    }
                    v.setBackground(main.getDrawable(R.drawable.btn_work_panel_pressed));

                    switch (v.getId()){
                        case 0:
                            workBoard.setMode(FAMode.USING_CURSOR);
                            break;
                        case 1:
                            workBoard.setMode(FAMode.STATE_ADDING);
                            break;
                        case 2:
                            workBoard.setMode(FAMode.TRANSITION_ADDING);
                            break;
                        case 3:
                            workBoard.setMode(FAMode.ANY_DELETING);
                            break;
                    }



                }
            });
        }

        mViewModel = ViewModelProviders.of(this).get(WorkPanelViewModel.class);
        // TODO: Use the ViewModel
    }

}