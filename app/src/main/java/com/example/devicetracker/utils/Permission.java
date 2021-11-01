package com.example.devicetracker.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Permission {
    
    private Context mContext;
    private Activity mActivity;

    public Permission(Context mContext, Activity mActivity) {
        this.mContext = mContext;
        this.mActivity = mActivity;
    }
    
    public void getLocationPermission() {

        String[] permissionArray = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        SweetAlertDialog alertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);

        alertDialog.setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        ActivityCompat.requestPermissions(mActivity,
                                permissionArray,
                                CONSTANTS.PERMISSION_REQUEST_CODE);
                    }
                })
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });


        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {

            alertDialog.setTitle("Location Permission Needed");
            alertDialog.setContentText("This Permission Needed For The Better Experience Of The App. ");
            alertDialog.show();


        } else {
            ActivityCompat.requestPermissions(mActivity, permissionArray, CONSTANTS.PERMISSION_REQUEST_CODE);

        }


    }

    public void getCOARSELocationPermission() {

        String[] permissionArray = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
        SweetAlertDialog alertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);

        alertDialog.setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        ActivityCompat.requestPermissions(mActivity,
                                permissionArray,
                                CONSTANTS.PERMISSION_REQUEST_CODE);
                    }
                })
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });


        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {

            alertDialog.setTitle("Location Permission Needed");
            alertDialog.setContentText("This Permission Needed For The Better Experience Of The App. ");
            alertDialog.show();


        } else {
            ActivityCompat.requestPermissions(mActivity, permissionArray, CONSTANTS.PERMISSION_REQUEST_CODE);

        }


    }



    public void getPhoneStatePermission() {

        String[] permissionArray = new String[]{Manifest.permission.READ_PHONE_STATE};
        SweetAlertDialog alertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE);

        alertDialog.setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        ActivityCompat.requestPermissions(mActivity,
                                permissionArray,
                                CONSTANTS.PERMISSION_REQUEST_CODE);
                    }
                })
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });


        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_PHONE_STATE)) {

            alertDialog.setTitle("SMS Permission Needed");
            alertDialog.setContentText("This Permission Needed For The Better Experience Of The App. ");
            alertDialog.show();


        } else {
            ActivityCompat.requestPermissions(mActivity, permissionArray, CONSTANTS.PERMISSION_REQUEST_CODE);

        }


    }
    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
