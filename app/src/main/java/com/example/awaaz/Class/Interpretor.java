package com.example.awaaz.Class;

import android.content.Context;

public class Interpretor extends User {
    String name;
    Context context;

    public Interpretor(int id, String name, Context context) {
        super(id);
        this.name = name;
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Interpretor(int id) {
        super(id);
    }

    public void startLiveVideo() {

    }
}
