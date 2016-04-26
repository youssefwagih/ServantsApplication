package com.example.youssefwagih.servantsapplication.UI;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.youssefwagih.servantsapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button saveStudentLocationButton;
    private LatLng studentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        saveStudentLocationButton = (Button) findViewById(R.id.saveLocatonButton);
        saveStudentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("locationLat", studentLocation.latitude);
                intent.putExtra("locationLng", studentLocation.longitude);
                //---set the data to pass back---
                setResult(RESULT_OK, intent);
                //---close the activity---
                finish();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                Toast.makeText(getApplicationContext(), "point: " + latLng.toString(), Toast.LENGTH_LONG).show();
                // Add a marker in Sydney and move the camera
                studentLocation = new LatLng(latLng.latitude, latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(studentLocation).title("Student Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(studentLocation));
                // this another version
            }
        });
    }
}
