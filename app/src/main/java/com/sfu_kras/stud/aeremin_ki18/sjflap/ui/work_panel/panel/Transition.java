package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.work_panel.panel;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.Objects;

public class Transition {

    private int startStateId;
    private int endStateId;
    private String readStr;
    private Paint paint;

    public Transition(int startStateId, int endStateId, String readStr) {
        this.startStateId = startStateId;
        this.endStateId = endStateId;
        this.readStr = readStr;

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return startStateId == that.startStateId && endStateId == that.endStateId && readStr.equals(that.readStr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startStateId, endStateId, readStr);
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getStartStateId() {
        return startStateId;
    }

    public void setStartStateId(int startStateId) {
        this.startStateId = startStateId;
    }

    public int getEndStateId() {
        return endStateId;
    }

    public void setEndStateId(int endStateId) {
        this.endStateId = endStateId;
    }

    public String getReadStr() {
        return readStr;
    }

    public void setReadStr(String readStr) {
        this.readStr = readStr;
    }
}
