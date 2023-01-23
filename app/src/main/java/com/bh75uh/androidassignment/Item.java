package com.bh75uh.androidassignment;

import java.util.ArrayList;

public class Item {
    private String mName;
    private boolean mOnline;

    public Item(String name, boolean online) {
        mName = name;
        mOnline = online;
    }

    public String getName() {
        return mName;
    }

    public boolean isOnline() {
        return mOnline;
    }

    private static int lastContactId = 0;

    public static ArrayList<Item> createItemsList(int numItems) {
        ArrayList<Item> items = new ArrayList<Item>();

        for (int i = 1; i <= numItems; i++) {
            items.add(new Item("Person " + ++lastContactId, i <= numItems / 2));
        }

        return items;
    }
}
