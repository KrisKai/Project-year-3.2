package com.example.myprojectyear32.ui.bar;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myprojectyear32.MainActivity;
import com.example.myprojectyear32.R;
import com.example.myprojectyear32.data.mqtt.MQTTPublisher;
import com.example.myprojectyear32.session.LoginActivity;
import com.example.myprojectyear32.session.ProfileActivity;
import com.example.myprojectyear32.session.SessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ImageView returnBtn = view.findViewById(R.id.returnSetting);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        TextView profileTV = view.findViewById(R.id.profileTV);
        profileTV.setOnClickListener(v ->{
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        });
        TextView mLogout = view.findViewById(R.id.logoutTV);
        mLogout.setOnClickListener(v -> {
            SessionManager session = new SessionManager(getContext());
            session.logoutFromSession();
            AlertDialog.Builder logoutDialog = new AlertDialog.Builder(getContext());
            logoutDialog.setMessage("Are you sure you want to log out?")
                    .setCancelable(false)
                    .setPositiveButton("Log Out", (dialog, which) ->
                        setAnimationForLogout())
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.cancel();
                    })
                    .show();
        });

        //Ph???n k???t n???i vs rasp
        TextView mConnect = view.findViewById(R.id.connectionTV);
        mConnect.setOnClickListener(v->{
            AlertDialog.Builder checkDialog = new AlertDialog.Builder(getContext());
            View ??nflate = getLayoutInflater().inflate(R.layout.connection_dialog,null);
            checkDialog.setView(??nflate);
            EditText mConnection = ??nflate.findViewById(R.id.connect_connectDialog);
            SessionManager session = new SessionManager(getContext());
            HashMap<String,String> userDetails = session.getUserDetailFromSession();
            String connect = userDetails.get(SessionManager.KEY_CONNECTION);
            String username = userDetails.get(SessionManager.KEY_USERNAME);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
            mConnection.setText(connect);
            AlertDialog alertDialog = checkDialog.create();
            alertDialog.show();
            Button mConfirm = ??nflate.findViewById(R.id.confirm_connectDialog);
            mConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //make the connection here

                    if(!connect.equals(mConnection.getText().toString())&&!mConnection.getText().equals("")){
                        session.createConnection(mConnection.getText().toString());
                        reference.child(username).child("connectCode").setValue(mConnection.getText().toString());
//                        MQTTPublisher.Connect(getContext(),mConnection.getText().toString());
                    }
                    if(mConnection.getText().equals("")) {
                        Toast.makeText(getContext(),"Please type the Server IP",Toast.LENGTH_SHORT).show();
                    }
                    alertDialog.dismiss();
                    Toast.makeText(getContext(),mConnection.getText().toString(),Toast.LENGTH_SHORT).show();
                }
            });



        });

        return view;
    }

    private void setAnimationForLogout() {
        AlertDialog.Builder loadingDialog = new AlertDialog.Builder(getContext());
        loadingDialog.setView(R.layout.logout_dialog);
        loadingDialog.setCancelable(false);
        AlertDialog alertDialog = loadingDialog.create();
        alertDialog.show();
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getContext(),LoginActivity.class);
            startActivity(intent);
        },2000);
    }
}