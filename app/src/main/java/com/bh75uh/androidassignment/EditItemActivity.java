package com.bh75uh.androidassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity implements IFireBase {

    public String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        id = getIntent().getStringExtra("id");
        readUserDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    public void readUserDB(){
        db.collection(mAuth.getCurrentUser().getUid())
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("YEET", String.valueOf(document.getId().equals(id)));
                        if(document.getId().equals(id)){
//                            Log.d("YEET", String.valueOf(id));
                            ((EditText) findViewById(R.id.txtEditItemName)).setText(String.valueOf(document.getData().get("itemName")));
                            ((EditText) findViewById(R.id.txtEditGeoTag)).setText(String.valueOf(document.getData().get("geotag")));
                            ((EditText) findViewById(R.id.txtEditPrice)).setText(String.valueOf(document.getData().get("price")));
                        }
                    }
                } else {
                    Log.w("TAG", "Error getting documents.", task.getException());
                }
            }
        });
    }
}