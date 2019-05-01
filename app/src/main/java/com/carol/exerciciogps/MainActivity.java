package com.carol.exerciciogps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView textoBusca;
    private static final int REQUEST_PERMISSION_GPS = 1001;


    private double lat, lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textoBusca = findViewById(R.id.textoBusca);
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                double g = 0;
                Location nova = null;
                if (nova == null) {
                    nova = location;
                } else {
                    g = g + location.distanceTo(nova);
                    nova = nova;
                }
                textoBusca.setText(
                        String.format(
                                Locale.getDefault(),
                                "Lat: %f, Long: %f",
                                lat,
                                lon
                        )
                );
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


       Button botaoPermissao =  findViewById(R.id.botaoPermissao);
        botaoPermissao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (permiteGPS()) return;
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_GPS);
              }
            });


        Button botaoAtiva = findViewById(R.id.botaoAtiva);
        botaoAtiva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            2000, 5, locationListener);
                    return;
                }else Toast.makeText(MainActivity.this, getString(R.string.cliquegps),
                        Toast.LENGTH_SHORT).show();

            }
        });

        Button botaoDesativa = findViewById(R.id.botaDesativa);
        botaoDesativa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && (Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED)) != null)
            locationManager.removeUpdates(locationListener);
        else Toast.makeText(MainActivity.this, (getString(R.string.gpsoff)),
                Toast.LENGTH_SHORT).show();

      }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
        if (!permiteGPS()) return;
        textoBusca = findViewById(R.id.textoBusca);
        String locali = textoBusca.getText().toString();
        startActivity(new Intent(Intent.ACTION_VIEW,
        Uri.parse(String.format("geo:%f,%f?q=" + locali,
        lat, lon))).setPackage("com.google.android.apps.maps"));
             }
          });
        }


   private boolean permiteGPS() {
        return ActivityCompat.checkSelfPermission(MainActivity.this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED;
        }

        }