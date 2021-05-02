package com.example.myprojectyear32.ui.room;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myprojectyear32.R;
import com.example.myprojectyear32.ui.bar.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BathroomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BathroomFragment extends Fragment {

    FloatingActionButton floatingActionButton;
    CardView lightingCardView;
    CardView sensorCardView;
    CardView securityCardView;
    CardView voiceCardView;

    public BathroomFragment() {
        // Required empty public constructor
    }

    public static BathroomFragment newInstance(String param1, String param2) {
        BathroomFragment fragment = new BathroomFragment();
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
        View view = inflater.inflate(R.layout.fragment_bathroom, container, false);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floatingActionButtonBaR);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment nextFrag= new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, nextFrag)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}