package com.example.awaaz.Class;

import android.graphics.Bitmap;

public class Frame {
    private int id;
    private Bitmap image;
    private SignAnalyzer signAnalyzer;

    public SignAnalyzer getSignAnalyzer() {
        return signAnalyzer;
    }

    public void setSignAnalyzer(SignAnalyzer signAnalyzer) {
        this.signAnalyzer = signAnalyzer;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Frame(int id, Bitmap image) {
        this.id = id;
        this.image = image;
        signAnalyzer = new SignAnalyzer();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
