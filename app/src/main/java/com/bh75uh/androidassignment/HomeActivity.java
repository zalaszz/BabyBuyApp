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

public class HomeActivity extends FireBaseDB {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onStart() {
        super.onStart();

        TextView tv = findViewById(R.id.userEmail);
        tv.setText(getUser().getEmail());
    }

    public void onClickLogOutBtn(View v){
        LogOut();
        startActivity(new Intent(HomeActivity.this, Login.class));
    }

    public void onClickChangeActivity(View v){
        startActivity(new Intent(HomeActivity.this, ListItemsActivity.class));
    }
}