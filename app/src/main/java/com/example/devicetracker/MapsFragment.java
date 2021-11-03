package com.example.devicetracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.devicetracker.utils.CustomLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {
    private GoogleMap mMap;
    private CustomLocation customLocation;
    private List<LatLng> latLngList= new ArrayList<>();
    int couter=0;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap=googleMap;
//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        getLocationFromFireBase();
    }

    private void getLocationFromFireBase() {
        customLocation= new CustomLocation(requireContext());

        String requestedUserID= MapsFragmentArgs.fromBundle(getArguments()).getRequestedUserID();
        FirebaseDatabase.getInstance().getReference("Users").child(requestedUserID).child("lastLocation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String[] locationString = location.split(",");
//                LatLng marker = new LatLng(Double.parseDouble(locationString[0]), Double.parseDouble(locationString[1]));
//                mMap.addMarker(new MarkerOptions().position(marker).title(address).icon(BitmapFromVector(R.drawable.ic_current_location_svgrepo_com)));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker,15.0f));
                String location = snapshot.getValue(String.class);

                if (location!=null&&location.length()>0)
                {
                    mMap.clear();

                    String[] locationString = location.split(",");
                    LatLng marker = new LatLng(Double.parseDouble(locationString[0]), Double.parseDouble(locationString[1]));
                    if (couter<=1)
                    {
                        String address= customLocation.getCompleteAddressString(Double.parseDouble(locationString[0]), Double.parseDouble(locationString[1]));
                        mMap.addMarker(new MarkerOptions().position(marker).title(address).icon(BitmapFromVector(R.drawable.current_location)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker,17.0f));
                        latLngList.add(marker);

                    }
                    else {
                        latLngList.add(marker);

                    }

                    if (latLngList!=null&&latLngList.size()>2)
                    {
                        String address= customLocation.getCompleteAddressString(Double.parseDouble(locationString[0]), Double.parseDouble(locationString[1]));
                        mMap.addMarker(new MarkerOptions().position(latLngList.get(latLngList.size()-1)).title(address).icon(BitmapFromVector(R.drawable.current_location)));

                        Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                                .clickable(true)
                                .addAll(latLngList).color(getResources().getColor(R.color.blue)));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngList.get(latLngList.size()-1),17.0f));

                    }
                    couter++;
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private BitmapDescriptor BitmapFromVector(int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(requireContext(), vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}