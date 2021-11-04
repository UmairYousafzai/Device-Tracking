package com.example.devicetracker.Message;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.devicetracker.Message.Adapter.ChatUserListAdapter;
import com.example.devicetracker.R;
import com.example.devicetracker.databinding.FragmentChatUserListBinding;
import com.example.devicetracker.models.AssignedUser;
import com.example.devicetracker.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.http.FormUrlEncoded;

public class ChatUserListFragment extends Fragment {
    private FragmentChatUserListBinding mBinding;
    private List<AssignedUser> assignedUserList= new ArrayList<>();
    private ChatUserListAdapter adapter;
    private ProgressDialog progressDialog;
    private long pressedTime=0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding= FragmentChatUserListBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        progressDialog= new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(getResources(),R.color.primaryAppColor,null)));
        progressDialog.show();

        getDataFromFireBase();
        setUpRecyclerView();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (pressedTime + 2000 > System.currentTimeMillis()) {
                    requireActivity().finish();
                } else {
                    Toast.makeText(requireContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
                }
                pressedTime = System.currentTimeMillis();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void getDataFromFireBase() {
        FirebaseAuth auth= FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(auth.getUid())).child("assignedUser")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1: snapshot.getChildren())
                        {
                            AssignedUser user = snapshot1.getValue(AssignedUser.class);
                            assignedUserList.add(user);
                        }
                        adapter.setAssignUserList(assignedUserList);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), " "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setUpRecyclerView() {
        mBinding.chatUserRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter= new ChatUserListAdapter(this);
        mBinding.chatUserRecyclerview.setAdapter(adapter);


    }
}