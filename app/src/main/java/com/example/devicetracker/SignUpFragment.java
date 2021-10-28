package com.example.devicetracker;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.devicetracker.databinding.FragmentSignUpBinding;
import com.example.devicetracker.models.AssignedUser;
import com.example.devicetracker.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignUpFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FragmentSignUpBinding mBinding;
    private NavController navController;
    private ProgressDialog dialog;
    private FirebaseDatabase mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSignUpBinding.inflate(inflater,container,false);
        BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.bottom_view);
        bottomNavigationView.setVisibility(View.GONE);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        navController = NavHostFragment.findNavController(this);
        btnListener();
        dialog = new ProgressDialog(requireContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(getResources(),R.color.primaryAppColor,null)));
        dialog.setMessage("Creating...");
        dialog.setCancelable(false);


    }

    private void btnListener() {
        mBinding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                addUserToFirebase();
            }
        });
    }

    private void addUserToFirebase() {

        String email= Objects.requireNonNull(mBinding.EditTextEmail.getText()).toString();
        String password= Objects.requireNonNull(mBinding.EditTextPassword.getText()).toString();
        String userName= Objects.requireNonNull(mBinding.EditTextUsername.getText()).toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern))
        {
            if (password.length()>=6) {
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            List<AssignedUser> list= new ArrayList<>();

                            User user= new User(mAuth.getUid(),email,password," ",userName);
                            mDatabase.getReference().child("Users").child(Objects.requireNonNull(mAuth.getUid())).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(requireContext(), "SignUp Successful", Toast.LENGTH_SHORT).show();
                                    navController.popBackStack();
                                    dialog.dismiss();
                                }
                            });

                        }
                        else
                        {
                            dialog.dismiss();
                            Toast.makeText(requireContext(), " "+task.getException(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
            else {
                mBinding.TextInputPassword.setError("Password Is Short");
                dialog.dismiss();

            }

        }
        else
        {
            mBinding.TextInputEmail.setError("Enter Valid Email");
        }



    }

}