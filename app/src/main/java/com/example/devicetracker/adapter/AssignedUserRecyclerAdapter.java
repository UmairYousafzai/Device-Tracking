package com.example.devicetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicetracker.Notification.SendNoticationClass;
import com.example.devicetracker.databinding.UserCardBinding;
import com.example.devicetracker.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AssignedUserRecyclerAdapter extends RecyclerView.Adapter<AssignedUserRecyclerAdapter.AssignedUserViewHolder> {

    private LayoutInflater layoutInflater;
    public List<User> userList;
    private Context context;
    private SendNoticationClass sendNoticationClass = new SendNoticationClass();
    private FirebaseAuth mAuth;

    public AssignedUserRecyclerAdapter(Context context) {
        userList = new ArrayList<>();
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
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
                    final boolean[] isRequestAccepted = {false};

                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        User user = userList.get(getAdapterPosition());

                        FirebaseDatabase.getInstance().getReference("Users").child(user.getId()).child("requestAccepted")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        isRequestAccepted[0] = snapshot.getValue(Boolean.class);
                                        if (isRequestAccepted[0]) {
                                            sendNotification(user);

                                        } else {
                                            Toast.makeText(context, "Permission from user is pending.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                    }

                }
            });


        }

        public void sendNotification(User user)
        {

            String senderUserEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
            String message = Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName() + " Want To Access Your Live Location.";
            sendNoticationClass.UpdateToken();
            FirebaseDatabase.getInstance().getReference().child("Tokens").child(user.getId()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String receiverUserToken = dataSnapshot.getValue(String.class);

                    sendNoticationClass.sendNotifications(receiverUserToken, senderUserEmail, "Device Tracker", message, context);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }
}
