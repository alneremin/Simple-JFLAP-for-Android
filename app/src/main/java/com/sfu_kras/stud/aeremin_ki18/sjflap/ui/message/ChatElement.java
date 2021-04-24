package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.message;

import android.graphics.drawable.Drawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class ChatElement {

    private String text;
    private Drawable img;
    private ConstraintSet set;

    // constructor
    public ChatElement(String text, Drawable img, ConstraintSet set) {
        this.text = text;
        this.img = img;
        this.set = set;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public ConstraintSet getSet() {
        return set;
    }

    public void setSet(ConstraintSet set) {
        this.set = set;
    }

}
