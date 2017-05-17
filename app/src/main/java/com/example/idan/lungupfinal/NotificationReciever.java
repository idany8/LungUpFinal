package com.example.idan.lungupfinal;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.idan.lungupfinal.PatientActivities.PatientExercisesList;

/**
 * Created by Idan on 16/05/2017.
 */
public class NotificationReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent reapeatIntent = new Intent(context, PatientExercisesList.class); /// maybe something else than loginactivity
        reapeatIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pentingIntent =  PendingIntent.getActivity(context,100,reapeatIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pentingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("notification title")
                .setContentText("notification text")
                .setAutoCancel(true);

        notificationManager.notify(100,builder.build());
    }
}
