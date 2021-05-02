package com.example.myprojectyear32.session;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myprojectyear32.R;
import com.example.myprojectyear32.data.user.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    TextView returnLogin;
    AppCompatButton signupBtn;
    DatabaseReference reference;
    private boolean checkState = true;
    protected TextInputLayout usernameET, passwordET, repasswordET, lastnameET, emailET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        returnLogin = findViewById(R.id.loginReturn);
        returnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(v -> {
            if(isConnected(SignupActivity.this)){
                if(checkInputText()){
                    createNewUser();
                }
            }else {
                showWifiDialog();
            }
        });

        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        repasswordET = findViewById(R.id.rePasswordET);
        lastnameET = findViewById(R.id.lastNameET);
        emailET = findViewById(R.id.emailET);

        usernameET.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6 ) {
                    usernameET.setError("Length must be >= 6");
                    usernameET.setErrorEnabled(true);
                } else {
                    usernameET.setErrorEnabled(false);
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
                if (s.length() < 4 ) {
                    passwordET.setError("Length must be >= 4");
                    passwordET.setErrorEnabled(true);
                } else {
                    passwordET.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        repasswordET.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equalsIgnoreCase(passwordET.getEditText().getText().toString())) {
                    repasswordET.setError("Please re-type your password.");
                    repasswordET.setErrorEnabled(true);
                } else {
                    repasswordET.setErrorEnabled(false);
                }
            }
        });
    }

    private void showWifiDialog() {
        AlertDialog.Builder wifiDialog = new AlertDialog.Builder(SignupActivity.this);
        wifiDialog.setMessage("Please connect the internet to sign up")
                .setCancelable(false)
                .setPositiveButton("Connect", (dialog, which) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.cancel();
                })
                .show();
    }

    private boolean isConnected(SignupActivity signup) {
        ConnectivityManager connectivityManager = (ConnectivityManager) signup.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())){
            return true;
        }
        else {
            return false;
        }

    }

    private void createNewUser() {
        reference = FirebaseDatabase.getInstance().getReference().child("User");
        String username = usernameET.getEditText().getText().toString();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(username).exists()){
                    usernameET.setError("Username already exists");
                }else {
                    User user = new User();
                    user.setPassWord(passwordET.getEditText().getText().toString());
                    user.setLastName(lastnameET.getEditText().getText().toString());
                    user.setEmail(emailET.getEditText().getText().toString());
                    user.setFirstName("None");
                    user.setDoB("None");
                    user.setGender("None");
                    user.setPhoneNumber("None");

                    reference.child(username).setValue(user);
                    Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean checkInputText() {
        if(usernameET.getEditText().length() == 0){
            usernameET.setError("Username is required");
            usernameET.setErrorEnabled(true);
            checkState = false;
        }

        if(passwordET.getEditText().length() == 0){
            passwordET.setError("Password is required");
            checkState = false;
        }

        if(repasswordET.getEditText().length() == 0){
            repasswordET.setError("Re-type password is required");
            checkState = false;
        }

        if(lastnameET.getEditText().length() == 0){
            lastnameET.setError("Last name is required");
            checkState = false;
        }
        if(emailET.getEditText().length() == 0){
            emailET.setError("Email is required");
            checkState = false;
        }
        return checkState;
    }
}