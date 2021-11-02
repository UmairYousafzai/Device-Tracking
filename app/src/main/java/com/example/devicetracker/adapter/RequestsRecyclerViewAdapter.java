package com.example.devicetracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicetracker.Notification.SendNoticationClass;
import com.example.devicetracker.databinding.LocationRequestCardBinding;
import com.example.devicetracker.databinding.UserCardBinding;
import com.example.devicetracker.models.LocationSharingUser;
import com.example.devicetracker.models.User;
import com.example.devicetracker.services.LocationService;
import com.example.devicetracker.utils.CONSTANTS;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RequestsRecyclerViewAdapter extends RecyclerView.Adapter<RequestsRecyclerViewAdapter.RequestsViewholder> {


    private LayoutInflater layoutInflater;
    public List<LocationSharingUser> userList;
    private Context context;

    public RequestsRecyclerViewAdapter(Context context) {
        userList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public RequestsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        LocationRequestCardBinding binding = LocationRequestCardBinding.inflate(layoutInflater, parent, false);

        return new RequestsViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsViewholder holder, int position) {
        LocationSharingUser user = userList.get(position);

        //sharing state 1= pending , 2= running, 3= stop
        user.setSharingState(1);
        holder.mBinding.status.setText("Pending");
        holder.mBinding.setUser(user);
        holder.mBinding.executePendingBindings();





    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    public void setUserList(List<LocationSharingUser> list) {
        if (list != null && list.size() > 0) {
            userList = list;
            notifyDataSetChanged();

        }
    }


    public class RequestsViewholder extends RecyclerView.ViewHolder {
        LocationRequestCardBinding mBinding;

        public RequestsViewholder(@NonNull LocationRequestCardBinding binding) {
            super(binding.getRoot());
            mBinding = binding;

            mBinding.btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
                        FirebaseAuth auth= FirebaseAuth.getInstance();
                        LocationSharingUser user = new LocationSharingUser();
                        user = userList.get(getAdapterPosition());

                        Map<String,String> data = new HashMap<>();
                        data.put("IsLocationSharing",String.valueOf(1));
                        databaseReference.child(user.getId()).child("")

                        databaseReference.child(user.getId()).child("assignedUser").child(auth.getUid()).child("IsLocationSharing").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(context, LocationService.class);
                                intent.setAction(CONSTANTS.ACTION_START_LOCATION_SERVICE);
                                context.startService(intent);

                                Toast.makeText(context, "Location Sharing Enable", Toast.LENGTH_SHORT).show();
                            }

                        });
                    }
                    }


            });

            mBinding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LocationService.class);
                    intent.setAction(CONSTANTS.ACTION_STOP_LOCATION_SERVICE);
                    context.startService(intent);

                    Toast.makeText(context, "Location Sharing Disable", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}
