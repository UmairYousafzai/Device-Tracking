package com.example.devicetracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devicetracker.databinding.FragmentAddUserBinding;
import com.example.devicetracker.databinding.FragmentAssignedUsersBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AssignedUsersFragment extends Fragment {


    private FragmentAssignedUsersBinding mbinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mbinding= FragmentAssignedUsersBinding.inflate(inflater,container,false);

        BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.bottom_view);
        bottomNavigationView.setVisibility(View.VISIBLE);
        return mbinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}