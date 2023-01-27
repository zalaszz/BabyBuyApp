package com.bh75uh.androidassignment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bh75uh.androidassignment.loginregister.Login;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListItemsActivity extends FireBaseDB {
    DemoAdapter adapter;
    static boolean needsRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        // Lookup the recyclerview in activity layout
        RecyclerView rv = findViewById(R.id.recylerV);
        // Create adapter passing in the sample user data
        adapter = new DemoAdapter(queryDocuments(getUserID()), this);

        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(rv);

        // Attach the adapter to the recyclerview to populate items
        rv.setAdapter(adapter);
        // Set layout manager to position the items
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

        if(ListItemsActivity.needsRefresh) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    adapter.notifyDataSetChanged();
                    finish();
                    startActivity(getIntent());
                    ListItemsActivity.needsRefresh = false;
                }
            }, 500);
        }
    }

    /**
     * Opening the AddItemDialog
     * @param view View clicked
     */
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
        if(item.getItemId() == R.id.menu_logout){ //Logging the user off
            LogOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Opening EditItemActivity
     * @param view View clicked
     */
    public void onClickEditBtn(View view){
        startActivity(new Intent(this, EditItemActivity.class).putExtra("id", String.valueOf(view.getTag())));
    }

    //Adding swipe capabilities to the items
    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition(); // this is how you can get the position
            Item object = adapter.getItem(position);

            //Deleting an item when sliding left and then updating the recycler
            deleteDocument(object.getID(), () -> Toast.makeText(getApplicationContext(), "Item Deleted with success!", Toast.LENGTH_LONG));
            adapter.notifyDataSetChanged();
        }
    };
}
