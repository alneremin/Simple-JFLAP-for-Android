package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.home;

import android.graphics.drawable.Drawable;

public class HomeElement {

    private int id;
    private String text;
    private Drawable img;

    // constructor
    public HomeElement(int id, String text, Drawable img) {
        this.id = id;
        this.text = text;
        this.img = img;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
