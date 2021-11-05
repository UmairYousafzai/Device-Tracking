package com.example.devicetracker.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.devicetracker.DataBase.DeviceTrackerDatabase;
import com.example.devicetracker.Notification.SendNoticationClass;
import com.example.devicetracker.R;
import com.example.devicetracker.databinding.LocationRequestCardBinding;
import com.example.devicetracker.databinding.UserCardBinding;
import com.example.devicetracker.models.LocationSharingUser;
import com.example.devicetracker.models.User;
import com.example.devicetracker.services.LocationService;
import com.example.devicetracker.utils.CONSTANTS;
import com.example.devicetracker.utils.Permission;
import com.example.devicetracker.utils.UserRepository;
import com.google.android.gms.tasks.OnCanceledListener;
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
    private LocationSharingUser user = new LocationSharingUser();
    private ProgressDialog progressDialog ;
    private Activity activity;


    public RequestsRecyclerViewAdapter(Context context, Activity activity) {
        userList = new ArrayList<>();
        this.context = context;
        progressDialog= new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.getWindow().
                setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(context.getResources(), R.color.primaryAppColor,null)));
        this.activity = activity;
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
        holder.mBinding.status.setText("Loading");
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
        else {
            userList.clear();
            notifyDataSetChanged();
        }
    }


    public class RequestsViewholder extends RecyclerView.ViewHolder {
        LocationRequestCardBinding mBinding;

        public RequestsViewholder(@NonNull LocationRequestCardBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
            FirebaseAuth auth= FirebaseAuth.getInstance();

            mBinding.btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Permission permission = new Permission(context,activity);
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if (permission.isLocationEnabled())
                        {
                            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                                progressDialog.show();
                                user = userList.get(getAdapterPosition());
                                databaseReference.child(Objects.requireNonNull(auth.getUid())).child("requestAccepted").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        databaseReference.child(user.getId()).child("assignedUser").child(auth.getUid()).child("IsLocationSharing").setValue(1)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Intent intent = new Intent(context, LocationService.class);
                                                        intent.setAction(CONSTANTS.ACTION_START_LOCATION_SERVICE);
                                                        context.startService(intent);

                                                        Toast.makeText(context, "Location Sharing Enable", Toast.LENGTH_SHORT).show();
                                                        mBinding.btnDone.setVisibility(View.GONE);
                                                        progressDialog.dismiss();
                                                    }


                                                });
                                    }
                                }).addOnCanceledListener(new OnCanceledListener() {
                                    @Override
                                    public void onCanceled() {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Enable to start location service", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }
                        else
                        {
                            permission.showOpenLocationSettingDialog();
                        }
                    }
                    else
                    {
                        permission.getLocationPermission();
                    }



                    }


            });

            mBinding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION)
                    {
                        progressDialog.show();
                        LocationSharingUser user= userList.get(getAdapterPosition());
                        databaseReference.child(auth.getUid()).child("requestAccepted").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                databaseReference.child(user.getId()).child("assignedUser").child(auth.getUid()).child("IsLocationSharing").setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("lastLocation").setValue("");
                                        Intent intent = new Intent(context, LocationService.class);
                                        intent.setAction(CONSTANTS.ACTION_STOP_LOCATION_SERVICE);
                                        context.startService(intent);
                                        Toast.makeText(context, "Location Sharing Disable", Toast.LENGTH_SHORT).show();
                                        DeviceTrackerDatabase deviceTrackerDatabase= DeviceTrackerDatabase.getInstance(context);
                                        deviceTrackerDatabase.dao().deleteUser(user);
                                        progressDialog.dismiss();
                                    }

                                }).addOnCanceledListener(new OnCanceledListener() {
                                    @Override
                                    public void onCanceled() {
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });

                    }


                }
            });


        }
    }
}
