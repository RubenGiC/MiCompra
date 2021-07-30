package org.rubengic.micompra;

import android.graphics.Bitmap;

public class ModelImage {
    String name_img;
    Bitmap bitmap_img;

    public ModelImage(String name_img, Bitmap bitmap_img) {
        this.name_img = name_img;
        this.bitmap_img = bitmap_img;
    }

    public String getName_img() {
        return name_img;
    }

    public void setName_img(String name_img) {
        this.name_img = name_img;
    }

    public Bitmap getBitmap_img() {
        return bitmap_img;
    }

    public void setBitmap_img(Bitmap bitmap_img) {
        this.bitmap_img = bitmap_img;
    }
}
