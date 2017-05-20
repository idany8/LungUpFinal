package com.example.idan.lungupfinal;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.idan.lungupfinal.Classes.P_Exercise;
import com.example.idan.lungupfinal.Classes.Patient;
import com.example.idan.lungupfinal.PatientActivities.PatientExercisesList;
import com.example.idan.lungupfinal.PatientActivities.PatientMenuActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Idan on 16/05/2017.
 */
public class NotificationReciever extends BroadcastReceiver {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public void onReceive(final Context context, Intent intent) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient current_user= dataSnapshot.getValue(Patient.class);
                Log.d("usersdata",""+current_user.getName());
                ArrayList<P_Exercise> loggedUsrEL = current_user.getP_exercises();

//
//                mHeader.setText(checkForTodayExercises(loggedUsrEL));
//                PatientMenuActivity.checkForTodayExercises(loggedUsrEL);
//
                if (PatientMenuActivity.havePlannedExercisesToday(loggedUsrEL)) {
                    sendNotification(context, current_user.getName(), "You have scheduled exercises for today!");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    private void sendNotification (Context context,String name, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent reapeatIntent = new Intent(context, PatientExercisesList.class); /// maybe something else than loginactivity
        reapeatIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pentingIntent = PendingIntent.getActivity(context, 100, reapeatIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pentingIntent)
                .setSmallIcon(R.drawable.logo_upper)
                .setContentTitle(name)
                .setContentText(message)
                .setAutoCancel(true);

        notificationManager.notify(100, builder.build());
    }
}
