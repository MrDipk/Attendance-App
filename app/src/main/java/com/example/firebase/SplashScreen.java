package com.example.firebase;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SplashScreen extends AppCompatActivity {
    TextView splashMessage;
    ProgressBar roundProgress;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedBundleInstance) {
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.material_splash);
        //Initialize
        splashMessage = findViewById(R.id.splashMessage1);
        roundProgress = findViewById(R.id.progressBar);
        //Animate
        final Animation animate = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animate.reset();
        roundProgress.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Handler slowdown = new Handler();
        Runnable delayme1 = new Runnable() {
            @Override
            public void run() {
                splashMessage.setText("");
                splashMessage.setText("Checking permissions...");
                splashMessage.clearAnimation();
                splashMessage.startAnimation(animate);
                roundProgress.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                //SharedPreferences
                sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                String firstTime = sharedPreferences.getString("FirstTimeInstall", "");
                String otpfirstTime = sharedPreferences.getString("otpFirstTimeInstall", "");
                if (firstTime.equals("Yes") && otpfirstTime.equals("Yes1")) {
                    startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                }
                else
                {
                    Intent notifyIntent = new Intent(SplashScreen.this, NotificationIntentService.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast
                            (SplashScreen.this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, 14);
                    calendar.set(Calendar.MINUTE, 20);
                    calendar.set(Calendar.SECOND, 1);
                    AlarmManager manager = (AlarmManager) SplashScreen.this.getSystemService(Context.ALARM_SERVICE);
                    manager.setRepeating(AlarmManager.RTC_WAKEUP,  calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntent);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        };
        slowdown.postDelayed(delayme1, 2000);
    }
}
