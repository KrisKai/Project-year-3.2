package com.example.myprojectyear32.ui.bar;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.myprojectyear32.MainActivity;
import com.example.myprojectyear32.R;
import com.example.myprojectyear32.session.LoginActivity;
import com.example.myprojectyear32.session.ProfileActivity;
import com.example.myprojectyear32.session.SessionManager;

public class SettingFragment extends Fragment {

    private TextView logoutTV;
    private TextView profileTV;
    private ImageView returnBtn;

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
        returnBtn = view.findViewById(R.id.returnSetting);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        profileTV = view.findViewById(R.id.profileTV);
        profileTV.setOnClickListener(v ->{
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        });
        logoutTV = view.findViewById(R.id.logoutTV);
        logoutTV.setOnClickListener(v -> {
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