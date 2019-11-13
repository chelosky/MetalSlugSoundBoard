package com.chelosky.metalslugsoundboard.Models;

public class ItemModel {
    private int imageItem;
    private String nameItem;
    private int soundItem;
    private int soundItemRAW;


    public ItemModel(int imageItem, String nameItem, int soundItem, int soundItemRAW) {
        this.imageItem = imageItem;
        this.nameItem = nameItem;
        this.soundItem = soundItem;
        this.soundItemRAW = soundItemRAW;
    }

    public int getSoundItem() {
        return soundItem;
    }

    public void setSoundItem(int soundItem) {
        this.soundItem = soundItem;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String name) {
        this.nameItem = name;
    }

    public int getImageItem() {
        return imageItem;
    }

    public void setImageItem(int imageItem) {
        this.imageItem = imageItem;
    }

    public int getSoundItemRAW() {
        return soundItemRAW;
    }

    public void setSoundItemRAW(int soundItemRAW) {
        this.soundItemRAW = soundItemRAW;
    }
}
