package com.example.anthony.fitcoinandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class UserInfoModel {
    String name;
    String png;

    public UserInfoModel(String name, String image) {
        this.name = name;
        this.png = image;
    }

    public String getName() {
        return name;
    }

    public String getPng() {
        return png;
    }

    public Bitmap getBitmap() {
        if (png == null || png.equals("")) {
            return null;
        }
        byte[] decodedString = Base64.decode(png, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
