package com.bh75uh.androidassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bh75uh.androidassignment.loginregister.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Home extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
    }

    interface interfaceTeste{
        public void onStartTeste();
    }

    @Override
    protected void onStart() {
        TextView tv = findViewById(R.id.blyat);

        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        interfaceTeste fufa = () -> {
            Log.d("currentUserCheck", "not logged in");
            tv.setText("");
            startActivity(new Intent(Home.this, Login.class));
        };

        if(currentUser == null) {
            fufa.onStartTeste();
            return;
        }

        tv.setText(currentUser.getEmail());
    }

    public void onClickLogOutBtn(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Home.this, Login.class));
    }

    public void onClickChangeActivity(View v){
        startActivity(new Intent(Home.this, ListItems.class));
    }
}