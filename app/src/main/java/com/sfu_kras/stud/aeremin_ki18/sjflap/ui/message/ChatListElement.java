package com.sfu_kras.stud.aeremin_ki18.sjflap.ui.message;

import android.graphics.drawable.Drawable;
import androidx.constraintlayout.widget.ConstraintSet;

public class ChatListElement {

    private String userName;
    private String userText;
    private Drawable img;

    public ChatListElement(String userName, String userText, Drawable img) {
        this.userName = userName;
        this.userText = userText;
        this.img = img;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

}
