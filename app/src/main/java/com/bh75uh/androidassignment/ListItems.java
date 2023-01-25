package com.bh75uh.androidassignment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListItems extends AppCompatActivity implements IFireBase{
    DemoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        // Lookup the recyclerview in activity layout
        RecyclerView rv = (RecyclerView) findViewById(R.id.recylerV);

        // Create adapter passing in the sample user data
        adapter = new DemoAdapter(readUserDB());
        // Attach the adapter to the recyclerview to populate items
        rv.setAdapter(adapter);
        // Set layout manager to position the items
        rv.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }

    public void openDialog(View view){
        AddItemDialog addItemDialog = new AddItemDialog(this);
        addItemDialog.show(getSupportFragmentManager(), "addItemDialog");
    }

    public ArrayList<Item> readUserDB(){
        ArrayList<Item> items = new ArrayList<>();

        db.collection(mAuth.getCurrentUser().getUid())
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d("SEXY TIME", document.getId() + " => " + document.getData());
                        items.add(new Item(document.getData()));
//                        Log.d("items", String.valueOf(items.size()));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });

        return items;
    }
}
