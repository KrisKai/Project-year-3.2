package com.example.myprojectyear32;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.example.myprojectyear32.session.LoginActivity;
import com.example.myprojectyear32.session.SessionManager;
import com.example.myprojectyear32.ui.bar.ChatbotFragment;
import com.example.myprojectyear32.ui.bar.HomeFragment;
import com.example.myprojectyear32.ui.bar.NotificationFragment;
import com.example.myprojectyear32.ui.bar.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SessionManager session = new SessionManager(MainActivity.this);
        if(session.checkLogin()){
            loadFragment(new HomeFragment());
            BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_chatbot:
                        fragment = new ChatbotFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_notification:
                        fragment = new NotificationFragment();
                        loadFragment(fragment);
                        return true;
                    case R.id.navigation_setting:
                        fragment = new SettingFragment();
                        loadFragment(fragment);
                        return true;
                }
                return false;
            });
        }else{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setStatusBarColor(Color.parseColor("#DADDDC"));
    }
}