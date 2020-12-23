package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    ProgressBar roundProgress;
    Toolbar toolbar;
    int flag=0;
    private int requestCode = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_main);
        //switched from minAPI 16 to 21 for this
        toolbar.setTitle("Sign-in");
        toolbar.setBackgroundResource(R.color.colorPrimary);
        roundProgress = (ProgressBar) findViewById(R.id.progressBarFireWait2);

        Button go = (Button) findViewById(R.id.btngo);
        final EditText num = (EditText) findViewById(R.id.editTextid);
        //Ask for permissions
        PermissionCheck pc = new PermissionCheck(MainActivity.this);
        pc.checkPermission();

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase df = FirebaseDatabase.getInstance();
                final DatabaseReference tbl_user;
                final String phno = num.getText().toString();
                roundProgress.setVisibility(View.VISIBLE);

                if (!phno.isEmpty()) {
                    tbl_user = df.getReference("user");
                        tbl_user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    final employeeInfo emp = dataSnapshot1.getValue(employeeInfo.class);
                                    if(emp.num.equals(phno))
                                    {
                                        //Toast.makeText(getApplicationContext(),checkIn.name,Toast.LENGTH_SHORT).show();
                                        flag=1;
                                        SharedPreferences sharedPreferences = getSharedPreferences("MYPREFERENCE", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("id",emp.id);
                                        editor.putString("name", emp.name);
                                        editor.putString("otp", emp.vcode);
                                        editor.apply();
                                        SharedPreferences sharedPreferen = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                                        SharedPreferences.Editor edito = sharedPreferen.edit();
                                        edito.putString("FirstTimeInstall", "Yes");
                                        edito.apply();
                                        //Calling OTP Page
                                        startActivity(new Intent(MainActivity.this, MainActivityotp.class));
                                    }
                                }
                                if (flag==0) Toast.makeText(getApplicationContext(),"Incorrect Phone Number",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Failed to load data!", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                else {
                    Toast.makeText(getApplication(), "Enter ID", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        roundProgress.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
       finishAffinity();
      // onDestroy();
    }
}
