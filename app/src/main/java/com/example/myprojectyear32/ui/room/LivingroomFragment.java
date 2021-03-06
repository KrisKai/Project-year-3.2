
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
    private boolean lightBulbState = false;

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
        TextView tempTV = view.findViewById(R.id.sensorTV);
        TextView humidTV = view.findViewById(R.id.humidTV);
        mSensor = view.findViewById(R.id.sensorLRCard);

        SessionManager session = new SessionManager(getContext());
        HashMap<String,String> userDetails = session.getUserDetailFromSession();
        String userName = userDetails.get(SessionManager.KEY_USERNAME);
        String mCode = userDetails.get(SessionManager.KEY_CONNECTION);
        String mLightBulbStatus = userDetails.get(SessionManager.KEY_LIGHTINGLR);
        String mDoorStatus = userDetails.get(SessionManager.KEY_DOORLR);
        String mSensorStatus = userDetails.get(SessionManager.KEY_SENSORLR);

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

        String[] cutTextMsg = mSensorStatus.split("\\s+");

        String[] tempF = cutTextMsg[0].split("=");
        String[] humidityF = cutTextMsg[1].split("=");
        tempTV.setText(tempF[1]);
        humidTV.setText(humidityF[1]);

        mLightBulb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!lightBulbState){
                if(isChecked){
                    notification = new Notification();
                    notification.setDescription("B???t ????n ph??ng kh??ch.");
                    notification.setImage(R.mipmap.lighting);
                    notification.setTime(date);

                    notiReference.child(String.valueOf(maxID + 1)).setValue(notification);
                    statusReference.child("lighting").setValue("True");
                    MQTTPublisher.Connect(getContext(), mCode);
                    new Handler().postDelayed(() -> {
                        //do sth
                        MQTTPublisher.Publisher("led_on");
                    },1000);
                } else {
                    notification = new Notification();
                    notification.setDescription("T???t ????n ph??ng kh??ch.");
                    notification.setImage(R.mipmap.lighting);
                    notification.setTime(date);
                    notiReference.child(String.valueOf(maxID + 1)).setValue(notification);
                    MQTTPublisher.Connect(getContext(), mCode);
                    new Handler().postDelayed(() -> {
                        //do sth
                        MQTTPublisher.Publisher("led_off");
                    },1000);
                    statusReference.child("lighting").setValue("False");
                }
            }else {
                lightBulbState = false;
            }

        });
        mDoor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!doorState){
                if(isChecked){
                    notification = new Notification();
                    notification.setDescription("M??? c???a ph??ng kh??ch.");
                    notification.setImage(R.mipmap.security);
                    notification.setTime(date);
                    notiReference.child(String.valueOf(maxID + 1)).setValue(notification);
                    statusReference.child("door").setValue("True");
                    MQTTPublisher.Connect(getContext(), mCode);
                    new Handler().postDelayed(() -> {
                        //do sth
                        MQTTPublisher.Publisher("door_open");
                    },1000);
                } else {
                    notification = new Notification();
                    notification.setDescription("????ng c???a ph??ng kh??ch.");
                    notification.setImage(R.mipmap.sensor);
                    notification.setTime(date);
                    notiReference.child(String.valueOf(maxID + 1)).setValue(notification);
                    MQTTPublisher.Connect(getContext(), mCode);
                    new Handler().postDelayed(() -> {
                        //do sth
                        MQTTPublisher.Publisher("door_close");
                    },1000);
                    statusReference.child("door").setValue("False");
                }
            } else {
                doorState = false;
            }

        });
        mSensor.setOnClickListener(v -> {

            notification = new Notification();
            notification.setDescription("C???p nh???p nhi???t ????? ph??ng.");
            notification.setImage(R.mipmap.sensor);
            notification.setTime(date);
            MQTTPublisher.Connect(getContext(), mCode);
            new Handler().postDelayed(() -> {
                //do sth
                MQTTPublisher.Subcriber("living");
                MQTTPublisher.Publisher("sensor");
                MQTTPublisher.MessageOutput();
                new Handler().postDelayed(() -> {
                    String message = MQTTPublisher.msg;
                    if(message.contains("Temp")){
                        String[] cutText = message.split("\\s+");
                        String[] temp = cutText[0].split("=");
                        String[] humidity = cutText[1].split("=");
                        tempTV.setText(temp[1]);
                        humidTV.setText(humidity[1]);
                        statusReference.child("sensor").setValue(message);
                        notification = new Notification();
                        notification.setDescription(message);
                        notification.setImage(R.mipmap.sensor);
                        notification.setTime(date);
                        notiReference.child(String.valueOf(maxID + 1)).setValue(notification);
                    }
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