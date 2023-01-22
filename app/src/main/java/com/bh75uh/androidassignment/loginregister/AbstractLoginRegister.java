package com.bh75uh.androidassignment.loginregister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bh75uh.androidassignment.Home;
import com.bh75uh.androidassignment.IFireBase;
import com.bh75uh.androidassignment.IFirebaseAuth;
import com.bh75uh.androidassignment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AbstractLoginRegister extends AppCompatActivity implements IFireBase {
    protected Activity login;
    protected Activity register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onStartFbImplementation(IFirebaseAuth auth){
        IFireBase.super.onStartFbImplementation(auth, () -> startActivity(new Intent(login != null ? login : register, Home.class)));
    }

    public void onClickBtn(View view){
        //Change fields ids
        String email = String.valueOf(((EditText) findViewById(R.id.editTextTextEmailAddress)).getText());
        String password = String.valueOf(((EditText) findViewById(R.id.editTextTextPassword)).getText());

        if(view.getId() == R.id.button2){
            loginUser(email, password);
        }else if(view.getId() == R.id.buttonGuga){
            registerUser(email, password);
        }else if(view.getId() == R.id.button3){
            startActivity(new Intent(login, Register.class));
        }
    }

    public void loginUser(@NonNull String email, @NonNull String password){
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(login, "Empty Fields.", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("loginSuccess", "signInWithCustomToken:success");

                    startActivity(new Intent(login, Home.class));
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("loginFailed", "signInWithCustomToken:failure", task.getException());
                    Toast.makeText(login, "Authentication failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    // login register with duplicated code
    public void registerUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("registerSuccess", "registerWithCustomToken:success");

                    startActivity(new Intent(register, Login.class));
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("registerFailed", "registerWithCustomToken:failure", task.getException());
                    Toast.makeText(register, "Registration failed.", Toast.LENGTH_LONG).show();
                    //updateUI(null);

                }
            }
        });
    }
}