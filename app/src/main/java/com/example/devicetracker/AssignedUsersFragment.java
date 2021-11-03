package com.example.devicetracker;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devicetracker.adapter.AssignedUserRecyclerAdapter;
import com.example.devicetracker.databinding.FragmentAddUserBinding;
import com.example.devicetracker.databinding.FragmentAssignedUsersBinding;
import com.example.devicetracker.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssignedUsersFragment extends Fragment {


    private FragmentAssignedUsersBinding mbinding;
    private AssignedUserRecyclerAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private List<User> userList= new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mbinding= FragmentAssignedUsersBinding.inflate(inflater,container,false);

        BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.bottom_view);
        bottomNavigationView.setVisibility(View.VISIBLE);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

        return mbinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseDatabase= FirebaseDatabase.getInstance();
        mAuth= FirebaseAuth.getInstance();

        progressDialog= new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(getResources(),R.color.primaryAppColor,null)));
        progressDialog.show();
        setUpRecyclerView();
        getAssignedUser();
    }

    private void getAssignedUser() {
        String uID= mAuth.getUid();
        assert uID != null;
        reference= firebaseDatabase.getReference().child("Users").child(uID).child("assignedUser");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snapshot1: snapshot.getChildren())
                {
                    User user= snapshot1.getValue(User.class);
                    userList.add(user);
                }
                adapter.setUserList(userList);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("Error AssignedUser: "," "+error.getMessage());
            }
        });

    }

    private void setUpRecyclerView() {

        mbinding.userRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter= new AssignedUserRecyclerAdapter(requireContext(),this);
        mbinding.userRecyclerView.setAdapter(adapter);

    }
}