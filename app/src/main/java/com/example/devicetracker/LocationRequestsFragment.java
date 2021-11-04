package com.example.devicetracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devicetracker.adapter.RequestsRecyclerViewAdapter;
import com.example.devicetracker.databinding.FragmentLocationRequestsBinding;
import com.example.devicetracker.models.LocationSharingUser;
import com.example.devicetracker.services.LocationService;
import com.example.devicetracker.utils.UserViewModel;

import java.util.List;

public class LocationRequestsFragment extends Fragment {

    private FragmentLocationRequestsBinding mBinding;
    private UserViewModel userViewModel;
    private RequestsRecyclerViewAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding= FragmentLocationRequestsBinding.inflate(inflater,container,false);

        userViewModel= new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();



        setUpRecyclerView();
        getRequestFromDB();
    }

    private void getRequestFromDB() {
        userViewModel.getAllUser().observe(getViewLifecycleOwner(), new Observer<List<LocationSharingUser>>() {
            @Override
            public void onChanged(List<LocationSharingUser> locationSharingUsers) {
                adapter.setUserList(locationSharingUsers);
            }
        });
    }

    private void setUpRecyclerView() {

        mBinding.locationRequestRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter= new RequestsRecyclerViewAdapter(requireContext(),requireActivity());
        mBinding.locationRequestRecycler.setAdapter(adapter);
    }
}