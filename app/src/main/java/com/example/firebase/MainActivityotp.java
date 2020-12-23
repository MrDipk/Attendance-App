package com.example.firebase;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

public class MainActivityotp extends AppCompatActivity {
    String uotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activityotp);

        Toolbar toolbar = findViewById(R.id.toolbarMain2);
        toolbar.setTitle("Verification");
        toolbar.setBackgroundResource(R.color.colorPrimary);

        final EditText otp = (EditText) findViewById(R.id.editTextotp2);
        Button go = (Button) findViewById(R.id.btngo2);

        //getting id from preference
        SharedPreferences sharedPreferences = getSharedPreferences("MYPREFERENCE", Context.MODE_PRIVATE);
        final String sotp = sharedPreferences.getString("otp", "");

         go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uotp = otp.getText().toString();
                if (!uotp.isEmpty()) {

                    if (sotp.equals(uotp)) {
                        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("otpFirstTimeInstall", "Yes1");
                        editor.apply();
                       Intent i = new Intent(MainActivityotp.this, Main2Activity.class);
                       startActivity(i);
                    } else {
                        Toast.makeText(getApplication(), "OTP did not match!" , Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplication(), "OTP is empty!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.back:
                finish();
                return(true);
           }
        return(super.onOptionsItemSelected(item));
    }


}
