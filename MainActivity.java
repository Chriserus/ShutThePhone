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
import com.google.android.gms.maps.model.PolygonOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM = 14.0f;
    private AudioManager audioManager;
    private Button silencePhoneBtn;
    private EditText passwordEditText, loginEditText;
    private GoogleMap mMap;
    private TextView locationTextView;
    private boolean mLocationPermissionGranted;
    private MapHandler mapHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting all view variables (buttons and fields)
        silencePhoneBtn = findViewById(R.id.silencePhoneBtn);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginEditText = findViewById(R.id.loginEditText);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        locationTextView = findViewById(R.id.locationTextView);

        //Object to handle all mMap related actions - mainly used in "onMapReady" method
        mapHandler = new MapHandler();

        //ask for phone silencing permissions and gps access permissions if not granted
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

    public boolean isHavingClasses() {
        //TODO: implement functionality using JSOS api
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapHandler.setAllMapProperties();
        //Adding university polygon to the mMap
        mMap.addPolygon(new PolygonOptions().clickable(true).addAll(mapHandler.getUniversityLatLngList()));

        //Camera set up - zooming
        float zoomLevel = DEFAULT_ZOOM; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapHandler.getUniversityLoc(), zoomLevel));

        //Text to see point location
        locationTextView.setText(mapHandler.getUniversityLoc().toString());
    }
}
