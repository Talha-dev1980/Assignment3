    package com.trav.guide.assignment3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsFragment extends Fragment {

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng myLoc = new LatLng( 33.56, 73.45 );
            googleMap.animateCamera( CameraUpdateFactory.newLatLngZoom(
                    new LatLng( 33.56, 73.45 ), 13
            ) );
            googleMap.addMarker( new MarkerOptions().position( myLoc ).title( "dummy " ) );
            googleMap.moveCamera( CameraUpdateFactory.newLatLng( myLoc ) );
            /*FirebaseDatabase.getInstance().getReference().child( "Places" ).addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap:snapshot.getChildren()){
                        String xy=snap.child( "xy" ).getValue().toString();
                        String[] loc=xy.split( ", " );

                        LatLng myLoc = new LatLng( Double.parseDouble( loc[0]), Double.parseDouble( loc[1]) );
                        googleMap.addMarker( new MarkerOptions().position( myLoc ).title( snap.child( "PlcName" ).getValue().toString()+" " ) );
                        googleMap.moveCamera( CameraUpdateFactory.newLatLng( myLoc ) );
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            } );
*/
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate( R.layout.fragment_maps, container, false );


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById( R.id.navMap );
        //if (mapFragment != null) {
        mapFragment.getMapAsync( callback );
        //}
    }
}