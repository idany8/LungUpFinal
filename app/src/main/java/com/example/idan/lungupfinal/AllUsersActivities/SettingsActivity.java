package com.example.idan.lungupfinal.AllUsersActivities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.idan.lungupfinal.Classes.Patient;
import com.example.idan.lungupfinal.Classes.MySharedPreferences;
import com.example.idan.lungupfinal.NotificationReciever;
import com.example.idan.lungupfinal.R;
import com.example.idan.lungupfinal.soundmeter.InitActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {
    private TextView userName,initLastTime;
    private String patUid;
    private Long initDate;
    private Patient curPat;
    private FirebaseAuth mAuth;
    private TextView mUsrName;
    @Override
    public void onResume()
    {
        super.onResume();

        MySharedPreferences msp = new MySharedPreferences(this);
        initDate = msp.getLongFromSharedPreferences("MIC_INIT_DATE",-1);
        //String tmp= msp.getStringFromSharedPrefernces("rec5", "na");

        FirebaseDatabase.getInstance().getReference().child("users").child(patUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                curPat = dataSnapshot.getValue(Patient.class);

                initSettings();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                patUid = null;
                Log.d("checkerror", "its null");
            } else {
                patUid = extras.getString("PATIENT_UID");
            }
        }
        mAuth = FirebaseAuth.getInstance();
        mUsrName= (TextView)findViewById(R.id.user_name_tv);
        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient loggedPat = dataSnapshot.getValue(Patient.class);
                mUsrName.setText(loggedPat.getName());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });






    }


    private void initSettings() {

        userName = (TextView) findViewById(R.id.sett_header_user_name);
        userName.setText(curPat.getName());
        findViewById(R.id.sett_init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, InitActivity.class);
                startActivity(intent);
            }
        });


        initLastTime = (TextView) findViewById(R.id.sett_init_last);

        if (initDate==-1){
            initLastTime.setText("Never initialized");
        }else{
            initLastTime.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(initDate));
        }

        findViewById(R.id.btn_alaram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY,14);
                calendar.set(Calendar.MINUTE,26);
                calendar.set(Calendar.SECOND,50);
                Intent intent = new Intent(getApplicationContext(), NotificationReciever.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
                Log.d("notificationSet", "done");

//                PendingIntent pintent = PendingIntent.getService(DashboardScreen.this, 0, intent, 0);
//                AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//                alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);

            }
        });


    }
}
