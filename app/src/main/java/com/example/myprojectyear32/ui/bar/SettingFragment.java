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
import com.example.myprojectyear32.session.LoginActivity;
import com.example.myprojectyear32.session.ProfileActivity;
import com.example.myprojectyear32.session.SessionManager;

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

        //Phần kết nối vs rasp
        TextView mConnect = view.findViewById(R.id.connectionTV);
        mConnect.setOnClickListener(v->{
            AlertDialog.Builder checkDialog = new AlertDialog.Builder(getContext());
            View ìnflate = getLayoutInflater().inflate(R.layout.connection_dialog,null);
            checkDialog.setView(ìnflate);
            EditText mConnection = ìnflate.findViewById(R.id.connect_connectDialog);
            Button mConfirm = ìnflate.findViewById(R.id.confirm_connectDialog);
            mConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //make the connection here
                    SessionManager session = new SessionManager(getContext());
                    session.createConnection(mConnection.getText().toString());
                    Toast.makeText(getContext(),mConnection.getText().toString(),Toast.LENGTH_SHORT).show();
                }
            });


            AlertDialog alertDialog = checkDialog.create();
            alertDialog.show();
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