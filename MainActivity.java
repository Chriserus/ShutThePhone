package com.student.krborowi.shutthephone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM = 14.0f;
    private AudioManager audioManager;
    private Button silencePhoneBtn;
    private EditText passwordEditText, loginEditText;
    private GoogleMap mMap;
    private TextView locationTextView;
    private MapHandler mapHandler;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private LatLng userLocation;
    public static final float LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final long LOCATION_UPDATE_MIN_TIME = 5000;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialize application interface
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mLocationManager.removeUpdates(mLocationListener);
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

        //Setting all view variables (buttons and fields)
        initializeAllViewItems();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Object to handle all mMap related actions - mainly used in "onMapReady" method
        mapHandler = new MapHandler();

        //ask for phone silencing permissions and gps access permissions if not granted
        askForPermissionsIfNeeded();

        //button functionality
        silencePhoneBtn.setOnClickListener((View v) -> runPhoneSilencerService());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);

    }

    private void askForPermissionsIfNeeded() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    private void runPhoneSilencerService() {
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
        //Text to see point location on top of the view
        locationTextView.setText(userLocation.toString());
    }

    public boolean isHavingClasses() {
        //TODO: implement functionality using JSOS api (or google calendar api/icalendar api)
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapHandler.setAllMapProperties();

        //Adding university polygon to the mMap
        mMap.addPolygon(new PolygonOptions().clickable(true).addAll(mapHandler.getUniversityLatLngList()));

        //Camera set up - zooming
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapHandler.getUniversityLoc(), DEFAULT_ZOOM)); //This goes up to 21

    }

    private void initializeAllViewItems() {
        silencePhoneBtn = findViewById(R.id.silencePhoneBtn);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginEditText = findViewById(R.id.loginEditText);
        locationTextView = findViewById(R.id.locationTextView);
    }
}
