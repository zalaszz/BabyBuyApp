package com.bh75uh.androidassignment;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.function.Consumer;
import java.util.function.Function;


public interface IFireBase {
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = mAuth.getCurrentUser();

    public default void onStartFbImplementation(IFirebaseAuth auth, Runnable activity){
        // Check if user is signed in (non-null) and update UI accordingly.
//        if(user == null) {
//            auth.onStartAuth();
//            return;
//        }
//        activity.run();
    }
}
