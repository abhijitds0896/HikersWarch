package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }

    }

    public void updateLocationInfo(Location location){

        Log.i("Locationinfo",location.toString());

        TextView latTextView=(TextView)findViewById(R.id.latTextView);
        TextView longTextView=(TextView)findViewById(R.id.longTextView);
        TextView altTextView=(TextView)findViewById(R.id.altTextView);
        TextView accTextView=(TextView)findViewById(R.id.accTextView);

        latTextView.setText("Latitude" + location.getLatitude());

        longTextView.setText("Longitude" +location.getLongitude());

        altTextView.setText("Altitude"+ location.getAltitude());

        accTextView.setText("Accuracy" +location.getAccuracy());

        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        try {

            String address="couldn't find address";

            List<Address> listAddressess = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if(listAddressess!=null && listAddressess.size()>0) {

                Log.i("PlaceInfo", listAddressess.get(0).toString());

                address="";

                if(listAddressess.get(0).getSubThoroughfare() !=null){
                    address+=listAddressess.get(0).getSubThoroughfare()+ " ";
                }
                if(listAddressess.get(0).getThoroughfare() !=null){
                    address+=listAddressess.get(0).getThoroughfare()+ "\n";
                }

                if(listAddressess.get(0).getLocality() !=null){
                    address+=listAddressess.get(0).getLocality()+ "\n";
                }

                if(listAddressess.get(0).getPostalCode() !=null){
                    address+=listAddressess.get(0).getPostalCode()+ "\n";
                }

                if(listAddressess.get(0).getCountryName() !=null){
                    address+=listAddressess.get(0).getCountryName()+ "\n";
                }

            }

            TextView addressTextView=(TextView)findViewById(R.id.adTextView);

            addressTextView.setText(address);

        }catch (IOException e){
            e.printStackTrace();
        }




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
              updateLocationInfo(location);
            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startListening();
        }else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                 Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                 if (location!=null) {
                     updateLocationInfo(location);
                 }
            }
        }
    }
}