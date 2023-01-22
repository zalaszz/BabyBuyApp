package com.bh75uh.androidassignment;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.function.Consumer;
import java.util.function.Function;


public interface IFireBase {
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseUser user = mAuth.getCurrentUser();

    public default void onStartFbImplementation(IFirebaseAuth auth, Runnable runnable){
        // Check if user is signed in (non-null) and update UI accordingly.
        if(user == null) {
            auth.onStartAuth();
            return;
        }
        runnable.run();
//        startActivity(new Intent(activity, Home.class));
    }
}
