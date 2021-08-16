
package com.example.myprojectyear32.ui.room;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myprojectyear32.R;
import com.example.myprojectyear32.data.mqtt.MQTTPublisher;
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
import java.util.Objects;

public class LivingroomFragment extends Fragment {

    FloatingActionButton floatingActionButton;
    Notification notification;
    DatabaseReference notiReference, statusReference;
    SwitchCompat mLightBulb, mDoor;
    CardView  mSensor;
    private String date;
    private long maxID=0;
    private boolean doorState = false;
    private boolean fanState = false;
    private boolean lightBulbState = false;
    private boolean sensorState = false;

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
        floatingActionButton = view.findViewById(R.id.floatingActionButtonLR);
        floatingActionButton.setOnClickListener(v -> {
            HomeFragment nextFrag = new HomeFragment();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, nextFrag)
                    .addToBackStack(null)
                    .commit();
        });
        TextView sensorTV = view.findViewById(R.id.sensorTV);
        TextView sensorTV = view.findViewById(R.id.sensorTV);

        SessionManager session = new SessionManager(getContext());
        HashMap<String,String> userDetails = session.getUserDetailFromSession();
        String userName = userDetails.get(SessionManager.KEY_USERNAME);
        String mLightBulbStatus = userDetails.get(SessionManager.KEY_LIGHTINGLR);
        String mDoorStatus = userDetails.get(SessionManager.KEY_DOORLR);
        String mFanStatus = userDetails.get(SessionManager.KEY_FANLR);
        String mSensorStatus = userDetails.get(SessionManager.KEY_SENSORLR);

        mSensor = view.findViewById(R.id.sensorLRCard);

        TextView mSensorTV = view.findViewById(R.id.sensorTV);
        mSensorTV.setText(mSensorStatus);



        mLightBulb = view.findViewById(R.id.lightLRswitch);
        if(mLightBulbStatus.equals("True")){
            mLightBulb.setChecked(true);
            lightBulbState = true;
        }else {
            mLightBulb.setChecked(false);
            lightBulbState = false;
        }

        mDoor = view.findViewById(R.id.doorLRswitch);
        if(mDoorStatus.equals("True")){
            mDoor.setChecked(true);
            doorState = true;
        }else {
            mDoor.setChecked(false);
            doorState = false;
        }

        mLightBulb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!lightBulbState){
                if(isChecked){
                    notification = new Notification();
                    notification.setDescription("Bật đèn phòng khách.");
                    notification.setImage(R.mipmap.lighting);
                    notification.setTime(date);

                    notiReference.child(String.valueOf(maxID + 1)).setValue(notification);
                    Toast.makeText(getActivity(), notification.getDescription(), Toast.LENGTH_SHORT).show();
                    statusReference.child("lighting").setValue("True");
                    MQTTPublisher.Connect(getContext(), "192.168.1.200:1883");
                    new Handler().postDelayed(() -> {
                        //do sth
                        MQTTPublisher.Publisher("led1");
                    },1000);
                } else {
                    statusReference.child("lighting").setValue("False");
                    Toast.makeText(getActivity(), "Tắt đèn phòng khách", Toast.LENGTH_SHORT).show();
                }
            }else {
                lightBulbState = false;
            }

        });
        mDoor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!doorState){
                if(isChecked){
                    notification = new Notification();
                    notification.setDescription("Mở cửa phòng khách.");
                    notification.setImage(R.mipmap.security);
                    notification.setTime(date);

                    notiReference.child(String.valueOf(maxID + 1)).setValue(notification);
                    Toast.makeText(getActivity(), notification.getDescription(), Toast.LENGTH_SHORT).show();
                    statusReference.child("door").setValue("True");
                    MQTTPublisher.Connect(getContext(), "192.168.1.200:1883");
                    new Handler().postDelayed(() -> {
                        //do sth
                        MQTTPublisher.Publisher("door");
                    },1000);
                } else {
                    Toast.makeText(getActivity(), "Đóng cửa phòng khách", Toast.LENGTH_SHORT).show();
                    statusReference.child("door").setValue("False");
                }
            } else {
                doorState = false;
            }

        });
        mSensor.setOnClickListener(v -> {

            notification = new Notification();
            notification.setDescription("Cập nhập nhiệt độ phòng.");
            notification.setImage(R.mipmap.sensor);
            notification.setTime(date);
            MQTTPublisher.Connect(getContext(), "192.168.1.200:1883");
            new Handler().postDelayed(() -> {
                //do sth
                MQTTPublisher.Subcriber("living");
                MQTTPublisher.Publisher("sensor");
                MQTTPublisher.MessageOutput();
                new Handler().postDelayed(() -> {
                    String message = MQTTPublisher.msg;
                    if(message.contains("Temp")){
                        String[] cutText = message.split(" ");
                        sensorTV.setText(message);
                    }
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                },6000);
            },1000);



        });

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

        return view;
    }
}