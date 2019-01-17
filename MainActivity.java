package com.student.krborowi.shutthephone;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Initialize application interface
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askForPermissionsIfNeeded();
        Button button = findViewById(R.id.button2);
        Intent startIntent = new Intent(MainActivity.this, WatcherService.class);
        button.setOnClickListener((View v) -> {
            startForegroundService(startIntent);
            finish();
        });
    }

    private void askForPermissionsIfNeeded() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (!notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}
