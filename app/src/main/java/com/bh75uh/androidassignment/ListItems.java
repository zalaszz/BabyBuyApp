package com.bh75uh.androidassignment;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ListItems extends AppCompatActivity {
    ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        // Lookup the recyclerview in activity layout
        RecyclerView rv = (RecyclerView) findViewById(R.id.recylerV);

        // Initialize contacts
        items = Item.createItemsList(20);
        // Create adapter passing in the sample user data
        DemoAdapter adapter = new DemoAdapter(items);
        // Attach the adapter to the recyclerview to populate items
        rv.setAdapter(adapter);
        // Set layout manager to position the items
        rv.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }
}
