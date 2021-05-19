package com.example.myprojectyear32.ui.room;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myprojectyear32.R;
import com.example.myprojectyear32.ui.bar.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class BedroomFragment extends Fragment {

    FloatingActionButton floatingActionButton;

    public BedroomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bedroom, container, false);
        floatingActionButton = view.findViewById(R.id.floatingActionButtonBR);
        floatingActionButton.setOnClickListener(v -> {
            HomeFragment nextFrag= new HomeFragment();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, nextFrag)
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }
}