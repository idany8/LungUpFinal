package com.example.idan.lungupfinal.AllUsersActivities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {
    private TextView userName,initLastTime;
    private String patUid;
    private Long initDate, alarmTimingVal;
    private Patient curPat;
    private FirebaseAuth mAuth;
    private TextView mUsrName;
    private Switch switch_not;
    private LinearLayout notificationLayout;
    private TimePicker timePicker1;
    private TextView time,alarmTime;
    private Calendar calendar;
    private String format = "";

    MySharedPreferences msp;
    @Override
    public void onResume()
    {
        super.onResume();

        msp = new MySharedPreferences(this);
        initDate = msp.getLongFromSharedPreferences("MIC_INIT_DATE",-1);

        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
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
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if (extras == null) {
//                patUid = null;
//                Log.d("checkerror", "its null");
//            } else {
//                patUid = extras.getString("PATIENT_UID");
//            }
//        }
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

        initAlarmNotification();

    }

    private void initAlarmNotification() {

        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        switch_not = (Switch) findViewById(R.id.switcher_notifications) ;
        alarmTime = (TextView) findViewById(R.id.tv_alarm_time);
        notificationLayout = (LinearLayout) findViewById(R.id.not_picker_layout);

        Log.d("alarmLog", "alarm_set value is: "+ msp.getBooleanFromSharedPreferences("ALARM_SET",false));

        if (msp.getBooleanFromSharedPreferences("ALARM_SET",false)) {
            switch_not.setChecked(true);
            alarmTime.setText(msp.getStringFromSharedPrefernces("ALARM_TIME", "no alarm"));
        }else {
            alarmTime.setText("-----");
            switch_not.setChecked(false);
        }


        switch_not.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    notificationLayout.setVisibility(View.VISIBLE);
                    Log.d("alarmLog", "open dialog");
                }
                else {
                    notificationLayout.setVisibility(View.INVISIBLE);
                    cancelAlartm();
                }
            }
        });
    }

    private void cancelAlartm() {

        Intent intent = new Intent(getApplicationContext(), NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);

        msp.putBooleanIntoSharedPrefernces("ALARM_SET",true);
        msp.putStringIntoSharedPrefernces("ALARM_TIME","~~~~~");
        alarmTime.setText("~~~~~");

        Log.d("alarmLog", "canceled alaram");
    }

    public void setTime(View view) {

        int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();

        Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,min);
                Intent intent = new Intent(getApplicationContext(), NotificationReciever.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        msp.putBooleanIntoSharedPrefernces("ALARM_SET",true);
        msp.putStringIntoSharedPrefernces("ALARM_TIME",hour+":"+min);
        Log.d("alarmLog", "alarm set");
        alarmTime.setText(msp.getStringFromSharedPrefernces("ALARM_TIME", "~~~~~"));
        notificationLayout.setVisibility(View.INVISIBLE);

    }

}
