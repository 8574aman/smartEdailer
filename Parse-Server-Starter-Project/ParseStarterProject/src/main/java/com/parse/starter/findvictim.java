package com.parse.starter;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class findvictim extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findvictim);

        intent = getIntent();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {

            List<Address> listAddresses = geocoder.getFromLocation(intent.getDoubleExtra("reqlat",0), intent.getDoubleExtra("reqlon",0), 1);
            //String s1 = intent.getDoubleExtra("reqlon",0);
            //Toast.makeText(findvictim.this, ,Toast.LENGTH_SHORT).show();
            if (listAddresses != null && listAddresses.size() > 0) {

                Log.i("PlaceInfo", listAddresses.get(0).toString());

                String address = "";

                if (listAddresses.get(0).getAddressLine(0) != null) {

                    address += listAddresses.get(0).getAddressLine(0) ;

                }

                if (listAddresses.get(0).getAddressLine(1) != null) {

                    address += listAddresses.get(0).getAddressLine(1) ;

                }

                if (listAddresses.get(0).getAddressLine(2) != null) {

                    address += listAddresses.get(0).getAddressLine(2) ;

                }


                if (listAddresses.get(0).getAddressLine(3) != null) {

                    address += listAddresses.get(0).getAddressLine(3) ;

                }



                //   Toast.makeText(LocatingActivity.this, address, Toast.LENGTH_SHORT).show();
                TextView t1 = (TextView)findViewById(R.id.textView2);
                t1.setText(" "+address);
                //    Log.i("addrss",listAddresses.toString());

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

       // Toast.makeText(this,intent.getStringExtra("username"),Toast.LENGTH_LONG).show();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        RelativeLayout mapLayout = (RelativeLayout)findViewById(R.id.mapRelativeLayout);
        mapLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                //LatLng driverLocation = new LatLng(intent.getDoubleExtra("driverLatitude", 0), intent.getDoubleExtra("driverLongitude", 0));

                LatLng requestLocation = new LatLng(intent.getDoubleExtra("reqlat", 0), intent.getDoubleExtra("reqlon", 0));

                ArrayList<Marker> markers = new ArrayList<>();

                //markers.add(mMap.addMarker(new MarkerOptions().position(driverLocation).title("Your Location")));
                markers.add(mMap.addMarker(new MarkerOptions().position(requestLocation).title("Request Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : markers) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();


                int padding = 250; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                mMap.animateCamera(cu);

            }
        });
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

        // Add a marke
    }
}
