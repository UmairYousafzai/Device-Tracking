package com.example.devicetracker;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.devicetracker.databinding.FragmentLoginBinding;
import com.example.devicetracker.utils.SharedPreferenceHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginFragment extends Fragment {


    private NavController navController;
    private FragmentLoginBinding mBinding;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;
    private long pressedTime=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentLoginBinding.inflate(inflater,container,false);
        navController = NavHostFragment.findNavController(this);
        BottomNavigationView bottomNavigationView= requireActivity().findViewById(R.id.bottom_view);
        bottomNavigationView.setVisibility(View.GONE);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = new ProgressDialog(requireContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(getResources(),R.color.primaryAppColor,null)));
        dialog.setMessage("Login...");
        dialog.setCancelable(false);


        mAuth= FirebaseAuth.getInstance();
        btnListener();

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

    private void btnListener() {
        mBinding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action =  LoginFragmentDirections.actionLoginFragmentToSignUpFragment();

                navController.navigate(action);
            }
        });
        mBinding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                login();

            }
        });
    }

    private void login() {


        String email= Objects.requireNonNull(mBinding.EditTextEmail.getText()).toString();
        String password= Objects.requireNonNull(mBinding.EditTextPassword.getText()).toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern))
        {
            if (password.length()>=6) {
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            SharedPreferenceHelper.getInstance(requireContext()).setLogin_State(true);
                            Toast.makeText(requireContext(), "Sign In Successful", Toast.LENGTH_SHORT).show();
                            NavDirections action = LoginFragmentDirections.actionLoginFragmentToAssignedUsersFragment();
                            navController.navigate(action);
                            dialog.dismiss();
                        }
                        else
                        {
                            dialog.dismiss();
                            Toast.makeText(requireContext(), " "+task.getException(), Toast.LENGTH_SHORT).show();
                            mBinding.TextInputPassword.setError("Password Is Incorrect");


                        }


                    }
                });
            }
            else {
                mBinding.TextInputPassword.setError("Password Is Incorrect");
                dialog.dismiss();

            }

        }
        else
        {
            mBinding.TextInputEmail.setError("Enter Valid Email");
            dialog.dismiss();

        }

    }
}