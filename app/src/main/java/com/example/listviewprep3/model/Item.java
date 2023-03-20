package com.example.listviewprep3.model;

public class Item {
    private String text;
    private int image;

    public Item(String text, int image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public int getImage() {
        return image;
    }
}
