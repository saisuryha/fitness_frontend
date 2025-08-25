package com.saveetha.fitnesschallenge.model;

public class Friend {
    private String name;
    private int imageResId;
    private boolean isSelected;

    public Friend(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
        this.isSelected = false;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
