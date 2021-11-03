package com.example.devicetracker.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class CustomLocation {
    private Context mContext;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private CustomLocationResults customLocationResults;
    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult!=null)
            {
                Location mLastLocation = locationResult.getLastLocation();
                customLocationResults.gotLocation(mLastLocation);
            }

        }
    };


    public CustomLocation(Context mContext) {
        this.mContext = mContext;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);

    }

    public void getLastLocation(CustomLocationResults results) {


        customLocationResults=results;
        startLocationUpdates();

    }



    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    public void startLocationUpdates()
    {




            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isLocationEnabled()) {



                    // Initializing LocationRequest
                    // object with appropriate methods
                    LocationRequest mLocationRequest = new LocationRequest();
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    mLocationRequest.setInterval(4000);
                    mLocationRequest.setFastestInterval(4000);

                    // setting LocationRequest
                    // on FusedLocationClient
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);

                    fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                }
            }

    }
    public void stopLocationUpdates()
    {
        LocationServices.getFusedLocationProviderClient(mContext).removeLocationUpdates(mLocationCallback);
    }


    public interface CustomLocationResults
    {
        void gotLocation(Location location);
    }

    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("MyCurrentloctionaddress", strReturnedAddress.toString());
            } else {
                Log.w("MyCurrentloctionaddress", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("MyCurrentloctionaddress", "Canont get Address!");
        }
        return strAdd;
    }
}
