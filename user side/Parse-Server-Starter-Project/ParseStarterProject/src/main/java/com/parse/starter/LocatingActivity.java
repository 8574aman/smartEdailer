package com.parse.starter;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocatingActivity extends AppCompatActivity {

    Boolean flag;
    ListView requestListView;
    ArrayList<String> requests = new ArrayList<String>();
    ArrayList<String> aNumber = new ArrayList<String>();

    ArrayAdapter arrayAdapter;

   // ArrayList<Double> requestLatitudes = new ArrayList<Double>();
   // ArrayList<Double> requestLongitudes = new ArrayList<Double>();

  //  ArrayList<String> usernames = new ArrayList<String>();

    LocationManager locationManager;

    LocationListener locationListener;
  //  TextView name ;
  //  TextView number;
   // TextView address ;

    //String names=" ";
   // String numbers=" ";
   //String address1=" ";


    public void updateListView(Location location) {



        if (location != null) {


            ParseQuery<ParseObject> query = ParseQuery.getQuery("EmerRequest");

            final ParseGeoPoint geoPointLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
            query.whereNear("location", geoPointLocation);

            query.setLimit(6);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e==null)
                    {
                        requests.clear();
                        aNumber.clear();
                        if(objects.size()>0)
                        {
                            for(ParseObject object : objects)
                            {
                                Toast.makeText(LocatingActivity.this,"data added, success",Toast.LENGTH_SHORT);

                                requests.add(object.getString("Name")+"\n"+object.getString("Address")+"\n");
                                aNumber.add(object.getString("MobileNumber"));

                            }
                        }

                    }
                    else{
                        Toast.makeText(LocatingActivity.this,"108 added",Toast.LENGTH_SHORT);
                        requests.clear();
                        requests.add("108 Ambulance");
                        requests.add("100 police");
                        requests.add("112 general");
                        aNumber.add("108");
                        aNumber.add("100");
                        aNumber.add("112");


                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            });








            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {

                List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

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



                    TextView t1 = (TextView)findViewById(R.id.textView2);
                    t1.setText(" "+address);

                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        }
        else
        {   Toast.makeText(LocatingActivity.this,"turn on location and Internet",Toast.LENGTH_SHORT);
            requests.clear();
            requests.add("108 Ambulance");
            requests.add("100 police");
            requests.add("112 general");
            aNumber.add("108");
            aNumber.add("100");
            aNumber.add("112");
            TextView t1 = (TextView)findViewById(R.id.textView2);
            t1.setText(" please turn on location \n and internet");
            arrayAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 0, locationListener);

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    updateListView(lastKnownLocation);

                }

            }


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reqmaker);
        getSupportActionBar().hide();


       requestListView = (ListView) findViewById(R.id.requestlistview);
//
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, requests);
//
        requests.clear();
        aNumber.clear();

        requests.add("finding emergency contacts");

        requestListView.setAdapter(arrayAdapter);


        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(LocatingActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                    String phone = "+91"+aNumber.get(i);
                    Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent1);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
if(aNumber.get(0).matches("108")) {
    if (lastKnownLocation != null) {
        ParseObject request = new ParseObject("VictimRequest");
        request.put("Numberid", aNumber.get(i));
        //request.put("username", ParseUser.getCurrentUser().getUsername());
        ParseGeoPoint parseGeoPoint = new ParseGeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        request.put("Viclocation", parseGeoPoint);
        request.saveInBackground();
    }
}




                }

            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                updateListView(null);

            }
        }, 10000);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateListView(location);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (lastKnownLocation != null) {

                    updateListView(lastKnownLocation);

                }


            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LocatingActivity.this,"permission asked",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);


            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LocatingActivity.this,"permission asked",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 3);


            }

            }


        }

    }




