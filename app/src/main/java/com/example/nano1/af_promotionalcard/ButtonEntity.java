package com.example.nano1.af_promotionalcard;

/**
 * Created by nano1 on 3/11/2016.
 */
public class ButtonEntity {

    private String target;
    private String title;

    public ButtonEntity(String target, String title) {
        this.target = target;
        this.title = title;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

