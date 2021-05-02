package com.example.myprojectyear32.ui.bar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myprojectyear32.MainActivity;
import com.example.myprojectyear32.R;
import com.example.myprojectyear32.data.notification.Notification;
import com.example.myprojectyear32.data.notification.NotificationAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {

    protected RecyclerView recyclerView;
    protected ArrayList<Notification> notificationList;
    protected NotificationAdapter notificationAdapter;
    private DatabaseReference reference;
    private ImageView returnBtn;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        returnBtn = view.findViewById(R.id.returnNoti);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView)view.findViewById(R.id.notification_list);

        notificationList = new ArrayList<>();

        notificationAdapter = new NotificationAdapter(notificationList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setAdapter(notificationAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        addNotification();
        return view;
    }

    public void addNotification() {
        reference = FirebaseDatabase.getInstance().getReference().child("Notification");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot d: snapshot.getChildren()){
                    Notification notification = d.getValue(Notification.class);
                    notificationList.add(notification);
                }

                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}