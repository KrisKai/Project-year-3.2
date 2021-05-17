
package com.example.myprojectyear32.ui.room;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.myprojectyear32.R;
import com.example.myprojectyear32.data.notification.Notification;
import com.example.myprojectyear32.session.SessionManager;
import com.example.myprojectyear32.ui.bar.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class LivingroomFragment extends Fragment {

    FloatingActionButton floatingActionButton;
    Notification notification;
    DatabaseReference notiReference, statusReference;
    SwitchCompat mLightBulb, mFan, mSensor, mDoor;
    private String date;
    private long maxID=0;

    public LivingroomFragment() {
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
        View view = inflater.inflate(R.layout.fragment_livingroom, container, false);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButtonLR);
        floatingActionButton.setOnClickListener(v -> {
            HomeFragment nextFrag = new HomeFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, nextFrag)
                    .addToBackStack(null)
                    .commit();
        });

        SessionManager session = new SessionManager(getContext());
        HashMap<String,String> userDetails = session.getUserDetailFromSession();
        String userName = userDetails.get(SessionManager.KEY_USERNAME);

        notiReference = FirebaseDatabase.getInstance().getReference().child("User").child(userName).child("Notification");

        statusReference = FirebaseDatabase.getInstance().getReference().child("User").child(userName).child("Status").child("LivingRoom");

        notiReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxID = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        date = dateFormat.format(calendar.getTime());
        mLightBulb = view.findViewById(R.id.lightLRswitch);
        mLightBulb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    notification = new Notification();
                    notification.setDescription("Bật đèn phòng khách.");
                    notification.setImage(R.mipmap.lighting);
                    notification.setTime(date);

                    notiReference.child(String.valueOf(maxID + 1)).setValue(notification);
                    Toast.makeText(getActivity(), notification.getDescription(), Toast.LENGTH_SHORT).show();
                    statusReference.child("lighting").setValue("True");
                } else {
                    statusReference.child("lighting").setValue("False");
                }
            }
        });
        mFan = view.findViewById(R.id.fanLRswitch);
        mFan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    notification = new Notification();
                    notification.setDescription("Bật quạt phòng khách.");
                    notification.setImage(R.mipmap.fan);
                    notification.setTime(date);

                    notiReference.child(String.valueOf(maxID + 1)).setValue(notification);
                    Toast.makeText(getActivity(), notification.getDescription(), Toast.LENGTH_SHORT).show();
                    statusReference.child("fan").setValue("True");
                } else {
                    statusReference.child("fan").setValue("False");
                }
            }
        });
        mDoor = view.findViewById(R.id.doorLRswitch);
        mDoor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    notification = new Notification();
                    notification.setDescription("Mở cửa phòng khách.");
                    notification.setImage(R.mipmap.security);
                    notification.setTime(date);

                    notiReference.child(String.valueOf(maxID + 1)).setValue(notification);
                    Toast.makeText(getActivity(), notification.getDescription(), Toast.LENGTH_SHORT).show();
                    statusReference.child("door").setValue("True");
                } else {
                    statusReference.child("door").setValue("False");
                }
            }
        });

        return view;
    }
}