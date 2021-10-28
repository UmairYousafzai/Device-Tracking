package com.example.devicetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicetracker.Notification.SendNoticationClass;
import com.example.devicetracker.databinding.UserCardBinding;
import com.example.devicetracker.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AssignedUserRecyclerAdapter extends RecyclerView.Adapter<AssignedUserRecyclerAdapter.AssignedUserViewHolder> {

    private LayoutInflater layoutInflater;
    public List<User> userList;
    private Context context;
    private SendNoticationClass sendNoticationClass = new SendNoticationClass();

    public AssignedUserRecyclerAdapter(Context context) {
        userList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public AssignedUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        UserCardBinding binding = UserCardBinding.inflate(layoutInflater, parent, false);

        return new AssignedUserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignedUserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.mBinding.setUser(user);
        holder.mBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    public void setUserList(List<User> list) {
        if (list != null && list.size() > 0) {
            userList = list;
            notifyDataSetChanged();

        }
    }


    public class AssignedUserViewHolder extends RecyclerView.ViewHolder {
        UserCardBinding mBinding;

        public AssignedUserViewHolder(@NonNull UserCardBinding binding) {
            super(binding.getRoot());
            mBinding = binding;


            mBinding.btnTrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        User user = userList.get(getAdapterPosition());

                        sendNoticationClass.UpdateToken();
                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(user.getId()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String usertoken = dataSnapshot.getValue(String.class);

                                sendNoticationClass.sendNotifications(usertoken, "Device Tracker", "Allow Access to live location", context);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
            });
        }
    }
}
