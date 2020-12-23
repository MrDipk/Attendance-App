package com.example.firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OurReceiver extends BroadcastReceiver {
    OurReceiver() {}
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, SplashScreen.class);
        context.startActivity(intent1);
    }
}
