package com.bh75uh.androidassignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Item {
    private Object itemName, price, geotag, purchased, active;
    private String documentId;

    public Item(Map map, String documentId) {
        this.itemName = map.get("itemName");
        this.price = map.get("price");
        this.purchased = map.get("purchased");
        this.geotag = map.get("geotag");
        this.active = map.get("active");

        this.documentId = documentId;
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

    public boolean isActive() {
        return (boolean) active;
    }

    public String getDocumentId(){
        return documentId;
    }
}
