package com.bh75uh.androidassignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListItems extends AppCompatActivity implements IFireBase {
    DemoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);


        Query query = db.collection(mAuth.getCurrentUser().getUid()).orderBy("itemName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>().setQuery(query, Item.class).build();

        // Lookup the recyclerview in activity layout
        RecyclerView rv = findViewById(R.id.recylerV);

        // Create adapter passing in the sample user data
        adapter = new DemoAdapter(options, this);
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

    public void openDialog(View view){
        AddItemDialog addItemDialog = new AddItemDialog(this);
        addItemDialog.show(getSupportFragmentManager(), "addItemDialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overall_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_logout){
            mAuth.signOut();
            Log.d("COCO", "HI");
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                        items.add(new Item(document.getData()));
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });

        return items;
    }
}
