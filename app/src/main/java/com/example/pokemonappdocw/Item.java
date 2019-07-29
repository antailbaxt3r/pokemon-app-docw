package com.example.pokemonappdocw;

public class Item {
    String itemType;
    int itemCount;

    public Item(){
        //required empty function
    }

    public Item(String itemType, int itemCount) {
        this.itemType = itemType;
        this.itemCount = itemCount;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}

