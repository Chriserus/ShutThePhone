package com.student.krborowi.shutthephone;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    //variables
    private static final float DEFAULT_ZOOM = 14.0f;
    AudioManager audioManager;
    Button silencePhoneBtn;
    EditText passwordEditText, loginEditText;
    GoogleMap map;
    TextView locationTextView;
    Polygon universityPolygon;
    boolean mLocationPermissionGranted;
    LatLng universityLoc;
    static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        silencePhoneBtn = findViewById(R.id.silencePhoneBtn);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginEditText = findViewById(R.id.loginEditText);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        locationTextView = findViewById(R.id.locationTextView);



        //ask for phone silencing permissions if not granted
        askForPermissionsIfNeeded();

        //button functionality
        silencePhoneBtn.setOnClickListener((View v) -> runPhoneSilencerService());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


    }

    //TODO:asking for gps not implemented
    private void askForPermissionsIfNeeded() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
        if (!mLocationPermissionGranted){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void runPhoneSilencerService() {
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }

    public boolean isOnUniversity() {
       // PolyUtil.containsLocation(userLocation, universityPolygon, false);
        return false;
    }

    public boolean isHavingClasses() {
        //TODO: implement functionality using JSOS api
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        universityLoc = new LatLng(51.108980, 17.061714);
        universityPolygon = map.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(51.108955, 17.053984),
                        new LatLng(51.107353, 17.056216),
                        new LatLng(51.107086, 17.063868),
                        new LatLng(51.108671, 17.068253),
                        new LatLng(51.112091, 17.060255)));
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        universityPolygon.setTag("alpha");
        float zoomLevel = DEFAULT_ZOOM; //This goes up to 21
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(universityLoc, zoomLevel));
        locationTextView.setText(universityLoc.toString());
    }
}
