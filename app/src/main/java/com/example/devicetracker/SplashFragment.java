package com.example.devicetracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devicetracker.utils.SharedPreferenceHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rbddevs.splashy.Splashy;

public class SplashFragment extends Fragment {

    private NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navController = NavHostFragment.findNavController(this);
        BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.bottom_view);
        bottomNavigationView.setVisibility(View.GONE);
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean login_status = SharedPreferenceHelper.getInstance(requireContext()).getLogin_State();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(login_status){

                    NavDirections acction = SplashFragmentDirections.actionSplashFragmentToAssignedUser();
                    navController.navigate(acction);

                }
                else
                {
                    NavDirections acction = SplashFragmentDirections.actionSplashFragmentToLoginFragment();
                    navController.navigate(acction);
                }
            }
        }, 1000);
    }
}