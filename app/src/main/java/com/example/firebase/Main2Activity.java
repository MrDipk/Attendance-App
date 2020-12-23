package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    Button login, leave;
    Double latitude, longitude;
    DatabaseReference tbl_user1;
    FirebaseDatabase df = FirebaseDatabase.getInstance();
    FusedLocationProviderClient mFusedLocationClient;
    ImageView img;
    ProgressBar roundFireWait;
    String id, d, temp;
    TextView name, dt, warn;
    int PERMISSION_ID = 44;
    VideoView videoView, videoView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        name=(TextView)findViewById(R.id.textViewname);
        dt = (TextView) findViewById(R.id.tvshowDT);
        warn = (TextView) findViewById(R.id.textViewWarn);
        login = (Button) findViewById(R.id.btnlogin);
        leave = (Button) findViewById(R.id.btnLeave);
        img = (ImageView) findViewById(R.id.imageViewReload);
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView2 = (VideoView) findViewById(R.id.videoViewThumbs);
        roundFireWait = (ProgressBar) findViewById(R.id.progressBarFireWait);
        dt.setText("Current Date-" + getdate());
        login.setVisibility(View.INVISIBLE);

        //Ask for permissions
        final PermissionCheck pc = new PermissionCheck(Main2Activity.this);
        pc.checkPermission();

        //Video
        String path = "android.resource://"+getPackageName()+"/"+R.raw.hands;
        videoView.setVideoURI(Uri.parse(path));
        String path2 = "android.resource://"+getPackageName()+"/"+R.raw.thumbs_up;
        videoView2.setVideoURI(Uri.parse(path2));

        //getting id from preference
        SharedPreferences sharedPreferences = getSharedPreferences("MYPREFERENCE", Context.MODE_PRIVATE);
        String idsh = sharedPreferences.getString("id", "");
        id = idsh;
        String nm=sharedPreferences.getString("name","");
        name.setText("WELCOME " + nm.toUpperCase());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "You are on leave.", Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        login.setVisibility(View.INVISIBLE);

        if (checkPermissions()) {
            getLastLocation();
            warn.setVisibility(View.GONE);
            videoView.setVisibility(View.GONE);
            videoView2.setVisibility(View.INVISIBLE);
            img.setVisibility(View.GONE);
            tbl_user1 = df.getReference("attendance");
            tbl_user1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(getdate().substring(0, 10)).hasChild(id)) {
                        attendence att = dataSnapshot.child(getdate().substring(0, 10)).child(id).getValue(attendence.class);
                        temp=att.dt_login.substring(11, 19);
                        int hr,min,sec,tot;
                        hr=Integer.parseInt(temp.substring(0,2));  min=Integer.parseInt(temp.substring(3,5));  sec=Integer.parseInt(temp.substring(6,8));
                        tot=(hr*3600 + min * 60 + sec);
                        //Toast.makeText(getApplicationContext(),Integer.toString(tot),Toast.LENGTH_LONG).show();
                        if (att.dt_login.isEmpty()) {
                            login.setText("LOG-IN");
                            leave.setVisibility(View.VISIBLE);
                            playVideo1();
                            roundFireWait.setVisibility(View.INVISIBLE);
                            videoView2.setVisibility(View.GONE);    //redundant
                        } else if (att.dt_logout.isEmpty()) {
                            login.setText("LOG-OUT");
                            leave.setVisibility(View.GONE);
                            playVideo2();
                            roundFireWait.setVisibility(View.GONE);
                        }
                        else {
                            login.setText("Good Day");
                            roundFireWait.setVisibility(View.GONE);
                            playVideo2();
                            Toast.makeText(getApplicationContext(), "Attendance already done.", Toast.LENGTH_LONG)
                            .show();
                        }
                    } else {
                        login.setText("LOG-IN");
                        roundFireWait.setVisibility(View.GONE);
                        playVideo1();
                        leave.setVisibility(View.VISIBLE);
                    }
                    login.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (login.getText().equals("LOG-IN")) {
                        login.setText("LOG-OUT");
                        attendence att_obj = new attendence(id, getdate(), "", latitude, longitude, 0.0, 0.0);
                        tbl_user1.child(getdate().substring(0, 10)).child(id).setValue(att_obj);
                    } else if (login.getText() == "LOG-OUT") {
                        login.setText("Attendance done for today");
                        login.setEnabled(false);
                        login.setVisibility(View.INVISIBLE);
                        tbl_user1.child(getdate().substring(0, 10)).child(id).child("dt_logout").setValue(getdate());
                        tbl_user1.child(getdate().substring(0, 10)).child(id).child("co_lt_logout").setValue(latitude);
                        tbl_user1.child(getdate().substring(0, 10)).child(id).child("co_ln_logout").setValue(longitude);
                    } else {
                        Toast.makeText(getApplicationContext(), "Today Attendance already done!!! Thank You", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            roundFireWait.setVisibility(View.GONE);
            playVideo1();
            warn.setVisibility(View.VISIBLE);
            img.setVisibility(View.VISIBLE);
            dt.setVisibility(View.GONE);
        }
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("FirstTimeInstall", "No");
            editor.apply();
            startActivity(new Intent(Main2Activity.this, MainActivity.class));
            return true;
        }
        return (super.onOptionsItemSelected(item));
    }

    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {

                                    latitude=location.getLatitude();
                                    longitude=location.getLongitude();

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Task<Void> voidTask = mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude=mLastLocation.getLatitude();
            longitude=mLastLocation.getLongitude();
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    private void playVideo1() {
        videoView.setVisibility(View.VISIBLE);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    private void playVideo2() {
        videoView2.setVisibility(View.VISIBLE);
        videoView2.start();
        videoView2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    public  String getdate(){

        Date date=new Date();
        SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        d=df.format(date);
        return d;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        //onDestroy();
    }
}