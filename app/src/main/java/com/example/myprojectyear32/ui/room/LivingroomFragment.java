
package com.example.myprojectyear32.ui.room;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myprojectyear32.R;
import com.example.myprojectyear32.data.notification.Notification;
import com.example.myprojectyear32.ui.bar.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LivingroomFragment extends Fragment {

    FloatingActionButton floatingActionButton;
    CardView lightingCardView;
    CardView sensorCardView;
    CardView securityCardView;
    CardView fanCardView;
    Notification notification;
    DatabaseReference reference;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
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

        reference = FirebaseDatabase.getInstance().getReference().child("Notification");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxID = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        date = dateFormat.format(calendar.getTime());
        lightingCardView = (CardView) view.findViewById(R.id.lightingLRCard);
        lightingCardView.setOnClickListener(v -> {
            notification = new Notification();
            notification.setDescription("Bật đèn phòng khách.");
            notification.setImage(R.mipmap.lighting);
            notification.setTime(date);

            reference.child(String.valueOf(maxID + 1)).setValue(notification);
            Toast.makeText(getActivity(), notification.getDescription(), Toast.LENGTH_SHORT).show();
        });
        fanCardView = (CardView) view.findViewById(R.id.fanLRCard);
        fanCardView.setOnClickListener(v -> {
            notification = new Notification();
            notification.setDescription("Bật quạt phòng khách.");
            notification.setImage(R.mipmap.fan);
            notification.setTime(date);

            reference.child(String.valueOf(maxID + 1)).setValue(notification);
            Toast.makeText(getActivity(), notification.getDescription(), Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}