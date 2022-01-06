package com.trav.guide.assignment3.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trav.guide.assignment3.MapsFragment;
import com.trav.guide.assignment3.R;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button btnRegPlace, btnGetLoc;
    private EditText edtLat, edtLang, edtplacename;
    private DatabaseReference dbref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider( this ).get( HomeViewModel.class );
        View root = inflater.inflate( R.layout.fragment_home, container, false );
        btnRegPlace = (Button) root.findViewById( R.id.btnRegisterplace );

        btnGetLoc = (Button) root.findViewById( R.id.btngetLoc );
        edtLat = (EditText) root.findViewById( R.id.edtPlcloclat );
        edtLang = (EditText) root.findViewById( R.id.edtPlcloclang );
        edtplacename = (EditText) root.findViewById( R.id.edtPlcName );


        dbref = FirebaseDatabase.getInstance().getReference().child( "Places" );

        btnGetLoc.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent it = new Intent(getApplicationContext(), OwnerView.class);
                it.putExtra("regStatus", "yes");
                startActivity(it);*/
                getcurrentlocation();

            }
        } );
        btnRegPlace.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, lng, lat, pic;
                pic = "null";
                name = edtplacename.getText().toString();
                lng = edtLang.getText().toString().trim();
                lat = edtLat.getText().toString().trim();
                if (!name.equals( null )) {
                    if (!lat.equals( null ) && !lng.equals( null )) {
                        addplace( name, lng, lat, pic );

                    }


                }
            }
        } );
        return root;
    }

    private void addplace(String name, String lng, String lat, String pic) {
        HashMap<String, String> placeData = new HashMap<>();
        placeData.put( "PlcName", name );
        placeData.put( "xy", lat + ", " + lng );
        String key = dbref.push().getKey().toString();
        dbref.child( key ).setValue( placeData ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    edtplacename.setText( "" );
                    edtLang.setText( "" );
                    edtLat.setText( "" );
                    Toast.makeText( getContext(), "Added Sucessfully", Toast.LENGTH_LONG ).show();

                } else {
                    Toast.makeText( getContext(), "Failed To Add place", Toast.LENGTH_LONG ).show();
                }
            }
        } );
        //startActivity( new Intent( getContext(), MapsFragment.class ) );
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void getcurrentlocation() {
        //initialize task Location
        /// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        try {
            FusedLocationProviderClient fusedLocationProviderClient = null;

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient( getContext() );

            @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener( new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        edtLat.setText( location.getLatitude() + "" );
                        edtLang.setText( location.getLongitude() + "" );

                    } else {
                        Toast.makeText( getContext(), location.getLatitude() + "X : Y" + location.getLongitude() + "", Toast.LENGTH_SHORT ).show();

                    }
                }
            } );
        } catch (Exception e) {
            Toast.makeText( getContext(), "" + e.getMessage(), Toast.LENGTH_LONG );
        }
    }

    public boolean checkPerms() {
        if (ActivityCompat.checkSelfPermission( getContext(), Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions( getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44 );
            return true;
        }
    }


}