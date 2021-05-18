package com.example.myprojectyear32.ui.bar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myprojectyear32.R;
import com.example.myprojectyear32.session.SessionManager;
import com.example.myprojectyear32.ui.room.BathroomFragment;
import com.example.myprojectyear32.ui.room.BedroomFragment;
import com.example.myprojectyear32.ui.room.KitchenFragment;
import com.example.myprojectyear32.ui.room.LivingroomFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    CardView livingroomCardView;
    CardView bedroomCardView;
    CardView bathroomCardView;
    CardView kitchenCardView;

    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView nameTextView = view.findViewById(R.id.nameTV);


        SessionManager session = new SessionManager(getContext());
        HashMap<String,String> userDetails = session.getUserDetailFromSession();
        String name = userDetails.get(SessionManager.KEY_LASTNAME);
        nameTextView.setText(name);


        livingroomCardView = (CardView) view.findViewById(R.id.livingroomCard);
        livingroomCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LivingroomFragment nextFrag= new LivingroomFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        bedroomCardView = (CardView) view.findViewById(R.id.bedroomCard);
        bedroomCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BedroomFragment nextFrag= new BedroomFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        bathroomCardView = (CardView) view.findViewById(R.id.bathroomCard);
        bathroomCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BathroomFragment nextFrag= new BathroomFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        kitchenCardView = (CardView) view.findViewById(R.id.kitchenCard);
        kitchenCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KitchenFragment nextFrag= new KitchenFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
}