package com.bh75uh.androidassignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Item {
    private Object itemName, price, geotag, purchased, description, imagePath, id;

    public Item(Map map) {
        this.itemName = map.get("itemName");
        this.price = map.get("price");
        this.purchased = map.get("purchased");
        this.geotag = map.get("geotag");
        this.description = map.get("description");
        this.imagePath = map.get("imagePath");
    }

    public void setID(String id){
        this.id = id;
    }

    public String getID(){
        return (String) id;
    }

    public Item() {}

    public String getItemName() {
        return (String) itemName;
    }

    public String getPrice() {
        return (String) price;
    }

    public boolean wasPurchased() {
        return (boolean) purchased;
    }

    public String getGeoTag() {
        return (String) geotag;
    }

    public String getDescription() {
        return (String) description;
    }

    public String getImagePath(){
        return (String) imagePath;
    }
}
