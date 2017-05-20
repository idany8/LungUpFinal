package com.example.idan.lungupfinal;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.example.idan.lungupfinal.Classes.MySharedPreferences;

import java.util.Calendar;

/**
 * Created by Idan on 23/04/2017.
 */

public class App extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
//        MySharedPreferences msp = new MySharedPreferences(this);
//        Log.d("starting App", "in");
//
//        if (!msp.getBooleanFromSharedPreferences("ALARM_SET",false)) {
//            createAlarmNotifications();
//            msp.putBooleanIntoSharedPrefernces("ALARM_SET",true);
//                            }
    }

    private void createAlarmNotifications() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,14);
        calendar.set(Calendar.MINUTE,46);
        calendar.set(Calendar.SECOND,30);
        Intent intent = new Intent(getApplicationContext(), NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        Log.d("starting App", "done");


    }


}
