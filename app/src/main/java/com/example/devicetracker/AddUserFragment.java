package com.example.devicetracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devicetracker.databinding.FragmentAddUserBinding;
import com.example.devicetracker.models.AssignedUser;
import com.example.devicetracker.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class AddUserFragment extends Fragment {

    private FragmentAddUserBinding mBinding;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private User user= new User();
    private User currentUser= new User();
    List<AssignedUser> assignedUser= new ArrayList<>();


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding= FragmentAddUserBinding.inflate(inflater,container,false);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

        databaseReference= FirebaseDatabase.getInstance().getReference("User");
        mAuth= FirebaseAuth.getInstance();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnListener();

    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.addUserLayout.parentLayout.setVisibility(View.GONE);
    }

    private void btnListener() {
        mBinding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDataFromFirebase();
            }
        });

        mBinding.addUserLayout.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCurrentUser();


            }
        });
    }

    private void getDataFromFirebase() {
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        String email= Objects.requireNonNull(mBinding.EditTextEmail.getText()).toString();
      Query query= databaseReference.orderByChild("email").equalTo(email);
      query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    user= dataSnapshot.getValue(User.class);
                    mBinding.addUserLayout.parentLayout.setVisibility(View.VISIBLE);
                    mBinding.addUserLayout.tvUsername.setText(user.getUserName());
                    mBinding.addUserLayout.tvUserEmail.setText(user.getEmail());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Add User: "," "+error.getMessage());

            }
        });

    }



    private void updateCurrentUser( ) {
        String uID= mAuth.getUid();
        String assignedUserID= user.getId();
        Map<String,String> data = new HashMap<>();
        data.put("userName",user.getUserName());
        data.put("email",user.getEmail());
        data.put("id",assignedUserID);
        data.put("IsLocationSharing"," ");


        assert uID != null;
        FirebaseDatabase databaseReference = FirebaseDatabase.getInstance();


        databaseReference.getReference("Users").child(uID).child("assignedUser").child(assignedUserID).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                mBinding.addUserLayout.parentLayout.setVisibility(View.INVISIBLE);
                mBinding.EditTextEmail.setText("");
            }

        });


    }
}