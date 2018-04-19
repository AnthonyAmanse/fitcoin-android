package com.example.anthony.fitcoinandroid;

import android.graphics.drawable.Drawable;

public class UserInfoModel {
    String name;
    String image;

    public UserInfoModel(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
