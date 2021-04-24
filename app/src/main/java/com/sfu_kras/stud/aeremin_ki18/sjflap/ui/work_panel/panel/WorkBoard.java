package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.work_panel.panel;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.sfu_kras.stud.aeremin_ki18.sjflap.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WorkBoard extends View {

    private Map<String, State> states;
    private ArrayList<Transition> transitions;
    private FADrawer drawer;

    private int last_id = 0;
    private float x = 100;
    private float y = 100;
    private float offsetX;
    private float offsetY;
    private Paint backgroundPaint;
    private float prevX;
    private float prevY;

    private MutableLiveData<TransitionField> transitionField;
    private MutableLiveData<StateField> stateField;

    public LiveData<TransitionField> getTransitionField() {
        return transitionField;
    }

    public void setTransitionField(MutableLiveData<TransitionField> transitionField) {
        this.transitionField = transitionField;
    }

    public MutableLiveData<StateField> getStateField() {
        return stateField;
    }

    public void setStateField(MutableLiveData<StateField> stateField) {
        this.stateField = stateField;
    }

    public void changeParamsInCurrentState(boolean _initial, boolean _final) {
        if (menuState != null) {
            menuState.set_initial(_initial);
            menuState.set_final(_final);
        }
    }

    public void addTextInLastTransition(String text) {
        if (text.equals("")) {
            newTransition.setReadStr("λ");
        } else {
            newTransition.setReadStr(text);
        }
    }

    private FAMode mode = FAMode.USING_CURSOR;
    private State startState = null;
    private State endState = null;
    private State menuState = null;

    private Transition newTransition = null;

    GestureDetector gestureDetector;

    public WorkBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.transitions = new ArrayList<>();
        this.states = new HashMap<>();

        transitionField = new MutableLiveData<>();
        stateField = new MutableLiveData<>();
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLUE);

        drawer = new FADrawer();
        gestureDetector = new GestureDetector(context, new GestureListener());

    }

    public Transition findTransition(float x, float y) {
        State state1, state2;

        for (Transition t: this.transitions) {
            state1 = states.get(String.valueOf(t.getStartStateId()));
            state2 = states.get(String.valueOf(t.getEndStateId()));
            float tan = ((state2.getY() - state1.getY())/(state2.getX() - state1.getX()));
            float b = state1.getY() - tan*state1.getX();

            if (Math.abs(tan * x + b - y) < 35.0) {
                return t;
            }
        }
        return null;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();


            Transition t = findTransition(x,y);
            if (t != null) {
                newTransition = t;
                startState = states.get(String.valueOf(t.getStartStateId()));
                endState = states.get(String.valueOf(t.getEndStateId()));
                showTextEditForTransition();
            } else {
                State s;
                s = findState(x, y);
                //if (s != null) {
                //    s.set_initial(!s.is_initial());
                //}
            }

            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            // triggers first for both single tap and long press
            ConstraintLayout cl = (ConstraintLayout) getParent().getParent();
            LinearLayout ll = cl.findViewById(R.id.state_params);

            if (ll.getVisibility() == View.VISIBLE) {
                ll.setVisibility(View.INVISIBLE);
                CheckBox c1 =(CheckBox)ll.getChildAt(0);
                CheckBox c2 =(CheckBox)ll.getChildAt(1);
                changeParamsInCurrentState(c1.isChecked(), c2.isChecked());
            }


            /*LinearLayout ll2 = (LinearLayout) getParent();
            EditText editText = ll2.findViewById(R.id.transitionText);
            if (editText.getVisibility() == View.VISIBLE) {
                editText.setVisibility(View.INVISIBLE);
                addTextInLastTransition(editText.getText().toString());
                editText.setText("");
            }*/

            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            // triggers after onDown only for single tap
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // triggers after onDown only for long press
            //super.onLongPress(event);
            float x = e.getX();
            float y = e.getY();

            //ConstraintLayout cl = (ConstraintLayout) getParent().getParent();
            //System.out.println(cl.findViewById(R.id.state_params));

            menuState = findState(x, y);
            if (menuState != null) {
                //s.set_final(!s.is_final());
                showMenuForState(menuState);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);

        float positionX = event.getX();
        float positionY = event.getY();

        int action = event.getAction();

        switch (this.mode) {
            case STATE_ADDING:
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        states.put(
                                String.valueOf(this.last_id),
                                new State(
                                        positionX,
                                        positionY,
                                        this.last_id
                                )
                        );
                        this.last_id++;
                        break;
                }
                break;
            case TRANSITION_ADDING:
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        prevX = positionX;
                        prevY = positionY;

                        offsetX = event.getX();
                        offsetY = event.getY();

                        x = offsetX;
                        y = offsetY;

                        this.startState = findState(positionX, positionY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x = prevX + event.getX() - offsetX;
                        y = prevY + event.getY() - offsetY;
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:

                        endState = findState(positionX, positionY);
                        if (endState != null && this.startState != null)
                        {
                            newTransition = transitionIsUnique(startState.getId(),
                                    endState.getId(),
                                    "λ");
                            if (newTransition == null) {
                                transitions.add(
                                        new Transition(startState.getId(),
                                                endState.getId(),
                                                "λ"
                                        )
                                );
                                newTransition = transitions.get(transitions.size() - 1);
                            }
                            showTextEditForTransition();
                        }
                        this.startState = null;
                        break;

                    default:
                        break;
                }
                break;
            case USING_CURSOR:
                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        prevX = positionX;
                        prevY = positionY;

                        offsetX = event.getX();
                        offsetY = event.getY();

                        x = offsetX;
                        y = offsetY;

                        this.startState = findState(positionX, positionY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        x = prevX + event.getX() - offsetX;
                        y = prevY + event.getY() - offsetY;
                        if (startState != null)
                        {
                            startState.setX(x);
                            startState.setY(y);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (startState != null)
                        {
                            startState.setX(x);
                            startState.setY(y);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case ANY_DELETING:
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        State lineToDelete = findState(positionX, positionY);
                        if (lineToDelete != null)
                        {
                            deleteDependentTransitions(lineToDelete.getId());
                            this.states.remove(String.valueOf(lineToDelete.getId()));
                        }
                        break;
                    default:
                        break;
                }
                break;
        }

        return (true);
    }

    public void showTextEditForTransition() {

        float middleX = (endState.getX() + startState.getX()) / 2;
        float middleY = (endState.getY() + startState.getY()) / 2;

        transitionField.setValue(new TransitionField(middleX, middleY, newTransition.getReadStr()));
    }

    public void showMenuForState(State s) {
        stateField.setValue(new StateField(x, y, s.is_initial(), s.is_final()));
    }

    public void deleteDependentTransitions(int id) {
        Transition t;
        if (this.transitions.size() > 0) {
            for (int i = this.transitions.size() - 1; i >= 0; i--) {
                t = this.transitions.get(i);
                if (t.getStartStateId() == id || t.getEndStateId() == id) {
                    this.transitions.remove(i);
                }
            }
        }
    }

    public Transition transitionIsUnique(int id1, int id2, String readStr) {
        for (Transition t: transitions) {
            if (t.getStartStateId() == id1 && t.getEndStateId() == id2)
                return t;
        }
        return null;
    }

    public State findState(float x, float y) {
        State state;
        if (this.states.size() > 0) {
            for (int i = this.states.size() - 1; i >= 0; i--) {
                state = (State) this.states.values().toArray()[i];
                if ((state.getX() - x) * (state.getX() - x) + (state.getY() - y) * (state.getY() - y) <= FADrawer.RADIUS * FADrawer.RADIUS)
                    return state;
            }
        }
        return null;
    }


    @Override
    public void draw(Canvas canvas) {

        drawer.setCanvas(canvas);

        drawer.drawTransitions(states, transitions);
        drawer.drawStates(states);
        if (this.startState != null)
            drawer.drawTransition(this.startState.getX(), this.startState.getY(), x, y);

        invalidate();
    }

    public FAMode getMode() {
        return mode;
    }

    public void setMode(FAMode mode) {
        this.mode = mode;
    }
}