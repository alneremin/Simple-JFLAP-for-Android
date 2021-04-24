package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.work_panel.panel;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

class State {

    private float x;
    private float y;
    private String name;
    private int id;
    private boolean _initial;
    private boolean _final;

    public State(float x, float y, int id) {

        this.x = x;
        this.y = y;
        this.id = id;
        this.name = "q" + id;
        this._initial = false;
        this._final = false;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean is_initial() {
        return _initial;
    }

    public void set_initial(boolean _initial) {
        this._initial = _initial;
    }

    public boolean is_final() {
        return _final;
    }

    public void set_final(boolean _final) {
        this._final = _final;
    }



}