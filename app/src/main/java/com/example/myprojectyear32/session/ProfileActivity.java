package com.example.myprojectyear32.session;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myprojectyear32.MainActivity;
import com.example.myprojectyear32.R;
import com.example.myprojectyear32.ui.bar.SettingFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private String firstName, lastName, email, doB, phoneNumber, gender, password, username;
    EditText mFirstname, mLastname, mEmail, mDob, mPhoneNumber, mGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView returnBtn = findViewById(R.id.returnProfile);
        returnBtn.setOnClickListener(v->{
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, new SettingFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        Button saveBtn = findViewById(R.id.saveProfile);
        saveBtn.setOnClickListener(v->{
            checkPasswordDialog();
        });

        getUserDetails();

        bindDetails();

    }

    private void bindDetails() {
        mFirstname = findViewById(R.id.firstnamePFET);
        mFirstname.setText(firstName);
        mLastname = findViewById(R.id.lastNamePFET);
        mLastname.setText(lastName);
        mEmail = findViewById(R.id.emailPFET);
        mEmail.setText(email);
        mDob = findViewById(R.id.dobPFET);
        mDob.setText(doB);
        mPhoneNumber = findViewById(R.id.phonePFET);
        mPhoneNumber.setText(phoneNumber);
        mGender = findViewById(R.id.genderPFET);
        mGender.setText(gender);
    }


    private void checkPasswordDialog() {
        AlertDialog.Builder checkDialog = new AlertDialog.Builder(ProfileActivity.this);
        View view = getLayoutInflater().inflate(R.layout.password_dialog,null);
        checkDialog.setView(view);
        EditText mPassword = view.findViewById(R.id.password_checkDialog);
        EditText mRePassword = view.findViewById(R.id.rePassword_checkDialog);
        Button mButton = view.findViewById(R.id.confirm_checkDialog);
        mButton.setOnClickListener(v->{
            if(mPassword.getText().toString().equals(password)){
                if(mRePassword.getText().toString().equals(password)){
                    pushUpdatedUserDetails();
                }
                else {
                    Toast.makeText(this,"Your Re-Enter Password is not correct",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this,"Your Password is not correct",Toast.LENGTH_SHORT).show();
            }

        });
        AlertDialog alertDialog = checkDialog.create();
        alertDialog.show();
    }

    private void pushUpdatedUserDetails(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
//        reference.child(username).child("passWord").setValue();
        reference.child(username).child("lastName").setValue(lastName);
        reference.child(username).child("firstName").setValue(firstName);
        reference.child(username).child("gender").setValue(gender);
        reference.child(username).child("email").setValue(email);
        reference.child(username).child("phoneNumber").setValue(phoneNumber);
        reference.child(username).child("DoB").setValue(doB);

    }

    private void getUserDetails() {
        SessionManager session = new SessionManager(this);
        HashMap<String,String> userDetails = session.getUserDetailFromSession();
        firstName = userDetails.get(SessionManager.KEY_FIRSTNAME);
        lastName = userDetails.get(SessionManager.KEY_LASTNAME);
        email = userDetails.get(SessionManager.KEY_EMAIL);
        doB = userDetails.get(SessionManager.KEY_DOB);
        phoneNumber = userDetails.get(SessionManager.KEY_PHONENUMBER);
        gender = userDetails.get(SessionManager.KEY_GENDER);
        password = userDetails.get(SessionManager.KEY_PASSWORD);
        username = userDetails.get(SessionManager.KEY_USERNAME);
    }
}