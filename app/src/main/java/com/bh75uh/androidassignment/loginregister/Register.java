package com.bh75uh.androidassignment.loginregister;

import android.os.Bundle;
import android.util.Log;

import com.bh75uh.androidassignment.R;

public class Register extends AbstractLoginRegister {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.register = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        super.onStartFbImplementation(() -> Log.d("currentUserCheck", "User in register activity not logged in"));
    }
}