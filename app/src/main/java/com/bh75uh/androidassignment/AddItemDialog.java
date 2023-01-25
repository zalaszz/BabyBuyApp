package com.bh75uh.androidassignment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class AddItemDialog extends AppCompatDialogFragment implements IFireBase {

    public View view;
    public Activity parent;
    public ListItems parentClass;

    public AddItemDialog(Object parent){
        this.parent = (Activity) parent;
        this.parentClass = (ListItems) parent;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.add_dialog, null);

        builder.setView(view).setTitle("Add Item")
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        }).setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createUserDB();
            }
        });

        return builder.create();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        onStartFbImplementation(() -> {}, () -> {});
//    }

    public void createUserDB(){
        String itemName = ((EditText) view.findViewById(R.id.txtItemName)).getText().toString();
        String price = ((EditText) view.findViewById(R.id.txtPrice)).getText().toString();
        String location = ((EditText) view.findViewById(R.id.txtLocation)).getText().toString();

        Map<String, Object> userData = new HashMap<>();
        userData.put("itemName", itemName);
        userData.put("price", price);
        userData.put("purchased", false);
        userData.put("geotag", location);
        userData.put("active", true);

        db.collection(mAuth.getCurrentUser().getUid())
                .add(userData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(parent, "Item added successfully", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(parent, "Error! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
