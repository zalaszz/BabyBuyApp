package com.bh75uh.androidassignment.loginregister;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bh75uh.androidassignment.R;

public class Login extends AbstractLoginRegister {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.login = this;
    }

    @Override
    public void onStart() {
        super.onStart();
        super.onStartFbImplementation(() -> {
            Log.d("currentUserCheck", "User in login activity not logged in");
            Toast.makeText(login, "Please login to your account or create one!", Toast.LENGTH_LONG).show();
        });
    }
}