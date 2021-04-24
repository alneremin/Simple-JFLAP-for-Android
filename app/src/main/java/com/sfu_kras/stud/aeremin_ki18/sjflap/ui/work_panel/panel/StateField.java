package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.work_panel.panel;

public class StateField {

    private float middleX;
    private float middleY;
    private boolean _initial;
    private boolean _final;

    public StateField(float middleX, float middleY, boolean _initial, boolean _final) {
        this.middleX = middleX;
        this.middleY = middleY;
        this._initial = _initial;
        this._final = _final;
    }

    public float getMiddleX() {
        return middleX;
    }

    public void setMiddleX(float middleX) {
        this.middleX = middleX;
    }

    public float getMiddleY() {
        return middleY;
    }

    public void setMiddleY(float middleY) {
        this.middleY = middleY;
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
