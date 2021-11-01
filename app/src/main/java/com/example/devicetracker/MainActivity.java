package com.example.devicetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.devicetracker.databinding.ActivityMainBinding;
import com.example.devicetracker.utils.Permission;
import com.example.devicetracker.utils.SharedPreferenceHelper;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private ActivityMainBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        setSupportActionBar(mBinding.customToolbar);
        Objects.requireNonNull(getSupportActionBar()).hide();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(mBinding.bottomView,navController);
//        NavigationUI.setupWithNavController(mBinding.customToolbar,navController);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Permission permission= new Permission(this,this);
            permission.getLocationPermission();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_meenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.signOut)
        {
            SharedPreferenceHelper.getInstance(this).setLogin_State(false);
            navController.navigate(R.id.splashFragment);
            Objects.requireNonNull(getSupportActionBar()).hide();

        }

        return super.onOptionsItemSelected(item);
    }
}