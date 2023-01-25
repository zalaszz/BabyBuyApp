package com.bh75uh.androidassignment;

import android.content.Intent;
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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListItems extends AppCompatActivity implements IFireBase {
    DemoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        // Lookup the recyclerview in activity layout
        RecyclerView rv = (RecyclerView) findViewById(R.id.recylerV);

        Query query = db.collection(mAuth.getCurrentUser().getUid()).orderBy("itemName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>().setQuery(query, Item.class).build();

        // Create adapter passing in the sample user data
        adapter = new DemoAdapter(options);
        // Attach the adapter to the recyclerview to populate items
        rv.setAdapter(adapter);
        // Set layout manager to position the items
        rv.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void openDialog(View view){
        AddItemDialog addItemDialog = new AddItemDialog(this);
        addItemDialog.show(getSupportFragmentManager(), "addItemDialog");
    }

    public void onClickEditBtn(View view){
        startActivity(new Intent(this, EditItemActivity.class).putExtra("id", String.valueOf(view.getTag())));
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
                        items.add(new Item(document.getData(), document.getId()));
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });

        Log.d("COCK", items.toString());

        return items;
    }
}
