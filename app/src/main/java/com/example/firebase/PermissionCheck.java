package com.example.firebase;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionCheck {
    private int requestCode = 101;
    private Activity activity;
    PermissionCheck() {}
    PermissionCheck(Activity act) {
        activity = act;
    }
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
                Toast.makeText(activity, "Location Permission is required!",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(activity, "Location Permission is required!",
                        Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
            }
        }
    }
    //Ask for required permissions
}
