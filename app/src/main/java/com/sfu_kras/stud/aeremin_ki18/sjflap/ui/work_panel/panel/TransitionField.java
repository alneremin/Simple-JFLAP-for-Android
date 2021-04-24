package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.work_panel.panel;

public class TransitionField {

    private float middleX;
    private float middleY;


    private String readStr;

    public TransitionField(float middleX, float middleY, String readStr) {
        this.middleX = middleX;
        this.middleY = middleY;
        this.readStr = readStr;
    }


    public String getReadStr() {
        return readStr;
    }

    public void setReadStr(String readStr) {
        this.readStr = readStr;
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



}
