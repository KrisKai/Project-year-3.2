package com.example.myprojectyear32.session;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import com.example.myprojectyear32.MainActivity;
import com.example.myprojectyear32.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usernameET, passwordET;
    private boolean checkState = true;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.userNameET);
        passwordET = findViewById(R.id.passWordET);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            if(isConnected(LoginActivity.this)){
                checkEmpty();
                if(checkState){
                    logIn();
                }
            }
            else {
                showWifiDialog();
            }

        });

        Button signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void showWifiDialog() {
        AlertDialog.Builder wifiDialog = new AlertDialog.Builder(LoginActivity.this);
        wifiDialog.setMessage("Please connect the internet to login")
                .setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }

    private boolean isConnected(LoginActivity login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())){
            return true;
        }
        else {
            return false;
        }

    }

    private void logIn() {
        reference = FirebaseDatabase.getInstance().getReference().child("User");
        String username = Objects.requireNonNull(usernameET.getEditText()).getText().toString();
        String password = Objects.requireNonNull(passwordET.getEditText()).getText().toString();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(username).exists()){
                    String systemPassword = snapshot.child(username).child("passWord").getValue(String.class);
                    String lastname = snapshot.child(username).child("lastName").getValue(String.class);
                    String firstname = snapshot.child(username).child("firstName").getValue(String.class);
                    String gender = snapshot.child(username).child("gender").getValue(String.class);
                    String email = snapshot.child(username).child("email").getValue(String.class);
                    String phonenumber = snapshot.child(username).child("phoneNumber").getValue(String.class);
                    String dob = snapshot.child(username).child("DoB").getValue(String.class);
                    String connectCode = snapshot.child(username).child("connectCode").getValue(String.class);
                    String lightingLR = snapshot.child(username).child("Status").child("LivingRoom").child("lighting").getValue(String.class);
                    String doorLR = snapshot.child(username).child("Status").child("LivingRoom").child("door").getValue(String.class);
                    String sensorLR = snapshot.child(username).child("Status").child("LivingRoom").child("sensor").getValue(String.class);


                    assert systemPassword != null;
                    if(systemPassword.equals(password)){
                        SessionManager session = new SessionManager(LoginActivity.this);
                        session.createLoginSession(username, password,firstname, lastname, phonenumber, email, dob, gender, connectCode);
                        session.createStatus(doorLR, lightingLR, sensorLR);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this,"Please make sure your passwords match",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,"Your username is invalid",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkEmpty() {
        if(usernameET.getEditText().getText().toString().isEmpty()){
            usernameET.setError("Please enter your username");
            usernameET.setErrorEnabled(true);
            checkState = false;
        }
        usernameET.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0){
                    usernameET.setErrorEnabled(false);
                    checkState = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordET.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()!=0){
                    passwordET.setErrorEnabled(false);
                    checkState = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(passwordET.getEditText().getText().toString().isEmpty()){
            passwordET.setError("Please enter your password");
            checkState = false;
        }

    }
}