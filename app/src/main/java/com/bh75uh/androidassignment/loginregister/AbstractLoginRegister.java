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

import com.bh75uh.androidassignment.FireBaseDB;
import com.bh75uh.androidassignment.HomeActivity;
import com.bh75uh.androidassignment.IFirebaseAuth;
import com.bh75uh.androidassignment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class AbstractLoginRegister extends FireBaseDB {
    protected Activity login;
    protected Activity register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onStartFbImplementation(IFirebaseAuth auth){
        //Sending two lambda expressions to FireBaseDB, the first is to execute in case user is not logged in and the second if he's logged in
        super.onStartFbImplementation(auth, () -> startActivity(new Intent(login != null ? login : register, HomeActivity.class)));
    }

    public void onClickBtn(View view){
        if(view.getId() == R.id.log_btn){ //Checking if the button pressed matches the login btn
            String log_email = String.valueOf(((EditText) findViewById(R.id.log_email)).getText());
            String log_password = String.valueOf(((EditText) findViewById(R.id.log_password)).getText());
            loginUser(log_email, log_password);
        }else if(view.getId() == R.id.reg_btn){ //Checking if the button pressed matches the register btn
            String reg_email = String.valueOf(((EditText) findViewById(R.id.reg_email)).getText());
            String reg_password = String.valueOf(((EditText) findViewById(R.id.reg_password)).getText());
            String reg_confirmPassword = String.valueOf(((EditText) findViewById(R.id.reg_confirmPassword)).getText());
            registerUser(reg_email, reg_password, reg_confirmPassword);
        }else if(view.getId() == R.id.log_regBtn){
            startActivity(new Intent(login, Register.class));
        }
    }

    public void loginUser(@NonNull String email, @NonNull String password){
        //Verifying if the fields are empty
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(login, "Empty Fields.", Toast.LENGTH_LONG).show();
            return;
        }
        //Logging in the user in
        mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("loginSuccess", "signInWithCustomToken:success");
                    startActivity(new Intent(login, HomeActivity.class));
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("loginFailed", "signInWithCustomToken:failure", task.getException());
                    Toast.makeText(login, "Authentication failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void registerUser(String email, String password, String confirmPassword){
        //Verifying if passwords match
        if(!password.equals(confirmPassword)) {
            Toast.makeText(register, "Passwords must match!", Toast.LENGTH_LONG).show();
            return;
        }
        //Registering the user
        mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("registerSuccess", "registerWithCustomToken:success");
                    startActivity(new Intent(register, Login.class));
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("registerFailed", "registerWithCustomToken:failure", task.getException());
                    Toast.makeText(register, "Registration failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
