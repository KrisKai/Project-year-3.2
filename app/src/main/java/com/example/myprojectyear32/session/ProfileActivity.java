package com.example.myprojectyear32.session;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myprojectyear32.MainActivity;
import com.example.myprojectyear32.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    private String firstName, lastName, email, doB, phoneNumber, gender, password, username;
    EditText mFirstname, mLastname, mEmail, mDob, mPhoneNumber, mGender;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView returnBtn = findViewById(R.id.returnProfile);
        returnBtn.setOnClickListener(v->{
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        });
        getUserDetails();

        bindDetails();

        Button saveBtn = findViewById(R.id.saveProfile);
        saveBtn.setOnClickListener(v->{
            checkPasswordDialog();
            Toast.makeText(this,mFirstname.getText().toString(),Toast.LENGTH_SHORT).show();
        });



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
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        mDob.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(ProfileActivity.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        mPhoneNumber = findViewById(R.id.phonePFET);
        mPhoneNumber.setText(phoneNumber);
        mGender = findViewById(R.id.genderPFET);
        mGender.setText(gender);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDob.setText(sdf.format(myCalendar.getTime()));
    }


    private void checkPasswordDialog() {
        AlertDialog.Builder checkDialog = new AlertDialog.Builder(ProfileActivity.this);
        View view = getLayoutInflater().inflate(R.layout.password_dialog,null);
        checkDialog.setView(view);
        AlertDialog alertDialog = checkDialog.create();
        alertDialog.show();
        EditText mPassword = view.findViewById(R.id.password_checkDialog);
        EditText mRePassword = view.findViewById(R.id.rePassword_checkDialog);
        Button mButton = view.findViewById(R.id.confirm_checkDialog);
        mButton.setOnClickListener(v->{
            if(mPassword.getText().toString().equals(password)){
                if(mRePassword.getText().toString().equals(password)){
                    pushUpdatedUserDetails();

                    alertDialog.dismiss();
                }
                else {
                    Toast.makeText(this,"Your Re-Enter Password is not correct",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this,"Your Password is not correct",Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void pushUpdatedUserDetails(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
//        reference.child(username).child("passWord").setValue();
        reference.child(username).child("lastName").setValue(mLastname.getText().toString());
        reference.child(username).child("firstName").setValue(mFirstname.getText().toString());
        reference.child(username).child("gender").setValue(mGender.getText().toString());
        reference.child(username).child("email").setValue(mEmail.getText().toString());
        reference.child(username).child("phoneNumber").setValue(mPhoneNumber.getText().toString());
        reference.child(username).child("DoB").setValue(mDob.getText().toString());

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