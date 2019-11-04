package com.example.toursapp2.Views;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toursapp2.Adapter.CityAdapter;
import com.example.toursapp2.Adapter.GetAddressIntentService;
import com.example.toursapp2.Adapter.PartnerItemAdapter;
import com.example.toursapp2.Model.CityModel;
import com.example.toursapp2.Model.PartnerItemModel;
import com.example.toursapp2.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectLocation extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;

    private LocationAddressResultReceiver addressResultReceiver;

    private TextView currentAddTv,txtSelectCity;

    private Location currentLocation;

    private Button locatebtn;


    private LocationCallback locationCallback;

    FirebaseUser user;
    FirebaseFirestore firebaseFirestore;

    FirebaseAuth auth;
    String cust_id,mobile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_layout);
        addressResultReceiver = new LocationAddressResultReceiver(new Handler());

        locatebtn=findViewById(R.id.locate_me_btn);
        txtSelectCity=findViewById(R.id.select_city_button);
        currentAddTv = findViewById(R.id.current_address);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        txtSelectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(SelectLocation.this);




                dialog.setContentView(R.layout.citylist_layout);
                dialog.setTitle("Custom Alert Dialog");


                final TextView txtpune,txtmumbai,txtbanglore;
                txtpune=dialog.findViewById(R.id.txt_pune);
                txtmumbai=dialog.findViewById(R.id.txt_mumbai);
                txtbanglore=dialog.findViewById(R.id.txt_banglore);

                txtpune.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String city=txtpune.getText().toString();
                        Intent intent=new Intent(SelectLocation.this,HomePage.class);
                        intent.putExtra("city",city);
                        startActivity(intent);
                    }
                });

                txtmumbai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String city=txtmumbai.getText().toString();
                        Intent intent=new Intent(SelectLocation.this,HomePage.class);
                        intent.putExtra("city",city);
                        startActivity(intent);

                    }
                });

                txtbanglore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String city=txtbanglore.getText().toString();
                        Intent intent=new Intent(SelectLocation.this,HomePage.class);
                        intent.putExtra("city",city);
                        startActivity(intent);
                    }
                });

                dialog.show();

            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                currentLocation = locationResult.getLocations().get(0);
                getAddress();
            };
        };
        startLocationUpdates();

        locatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city=currentAddTv.getText().toString();
                Intent intent=new Intent(SelectLocation.this,HomePage.class);
                intent.putExtra("city",city);
                startActivity(intent);
            }
        });



    }

    @SuppressWarnings("MissingPermission")
    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(2000);
            locationRequest.setFastestInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    null);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getAddress() {

        if (!Geocoder.isPresent()) {
            Toast.makeText(SelectLocation.this,
                    "Can't find current address, ",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, GetAddressIntentService.class);
        intent.putExtra("add_receiver", addressResultReceiver);
        intent.putExtra("add_location", currentLocation);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "Location permission not granted, " +
                                    "restart the app if you want the feature",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }
    private class LocationAddressResultReceiver extends ResultReceiver {
        LocationAddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == 0) {
                //Last Location can be null for various reasons
                //for example the api is called first time
                //so retry till location is set
                //since intent service runs on background thread, it doesn't block main thread
                Log.d("Address", "Location null retrying");
                getAddress();
            }

            if (resultCode == 1) {
                Toast.makeText(SelectLocation.this,
                        "Address not found, " ,
                        Toast.LENGTH_SHORT).show();
            }

            String currentAdd = resultData.getString("address_result");

            showResults(currentAdd);
        }
    }

    private void showResults(String currentAdd){
        currentAddTv.setText(currentAdd);
    }


    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}