package com.example.myprojectyear32.ui.bar;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myprojectyear32.R;
import com.example.myprojectyear32.ui.room.BathroomFragment;
import com.example.myprojectyear32.ui.room.BedroomFragment;
import com.example.myprojectyear32.ui.room.KitchenFragment;
import com.example.myprojectyear32.ui.room.LivingroomFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    CardView livingroomCardView;
    CardView bedroomCardView;
    CardView bathroomCardView;
    CardView kitchenCardView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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