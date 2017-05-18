package com.example.idan.lungupfinal.PatientActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.idan.lungupfinal.AllUsersActivities.LoginActivity;
import com.example.idan.lungupfinal.AllUsersActivities.PatientDetailedPerformance;
import com.example.idan.lungupfinal.AllUsersActivities.SettingsActivity;
import com.example.idan.lungupfinal.Chat.ChatListActivity;
import com.example.idan.lungupfinal.Classes.P_Exercise;
import com.example.idan.lungupfinal.Classes.Patient;
import com.example.idan.lungupfinal.R;
import com.example.idan.lungupfinal.soundmeter.HitGameActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.Calendar;

public class PatientMenuActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView usr_name;
    private TextView mHeader;
    private Patient current_user;
    private ImageView qr_assign_btn;

    private boolean qr_flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_menu);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
       // img_popup_qr = (ImageView)findViewById(R.id.qr_popup_image);
        qr_assign_btn = (ImageView) findViewById (R.id.btn_assign_care_giver_ptm);
        usr_name = (TextView)findViewById(R.id.user_name_tv);
        mHeader = (TextView)findViewById(R.id.tv_pt_header);
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_user= dataSnapshot.getValue(Patient.class);
                Log.d("usersdata",""+current_user.getName());
                usr_name.setText(current_user.getName());

                ArrayList<P_Exercise> loggedUsrEL = current_user.getP_exercises();


                mHeader.setText(checkForTodayExercises(loggedUsrEL));

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        initialPTmenu();
    }

    private String checkForTodayExercises(ArrayList<P_Exercise> loggedUsrEL) {

        Calendar calendar = Calendar.getInstance();
        int day =calendar.get(Calendar.DAY_OF_WEEK);
        boolean haveEx=false;
        for (int i=0;i<loggedUsrEL.size();i++)
        {
            if ( (loggedUsrEL.get(i).getWeekDaysArray().get(day) != 0) || (loggedUsrEL.get(i).getWeekDaysArray().get(day) == -1) ) {
                haveEx = true;
                Log.d("weekdays1", "" + loggedUsrEL.get(i).getWeekDaysArray().get(day));
            }
        }
        if (haveEx) return "You have planned exercises for today!";

        return "You have no planned exercises for today";
    }

    public void onBackPressed(){

    }
    private void initialPTmenu(){

        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(PatientMenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_perform_exercise_ptm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               // Intent intent = new Intent(PatientMenuActivity.this, SpinnerGame.class);
                Intent intent = new Intent(PatientMenuActivity.this,HitGameActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_exercises_list_ptm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(PatientMenuActivity.this, PatientExercisesList.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_performance_ptm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(PatientMenuActivity.this, PatientDetailedPerformance.class);
                startActivity(intent);
            }
        });
        qr_assign_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (qr_flag){
                    qr_assign_btn.setImageResource(R.drawable.btn4_pt);
                    qr_flag=false;
                }else {
                    int width = qr_assign_btn.getWidth();
                    int height = qr_assign_btn.getHeight();

                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(mAuth.getCurrentUser().getUid(), BarcodeFormat.QR_CODE, width, height);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qr_assign_btn.setImageBitmap(bitmap);
                        Toast.makeText(PatientMenuActivity.this, "ok",
                                Toast.LENGTH_SHORT).show();

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    qr_flag=true;
                }


            }});



        findViewById(R.id.btn_messages_ptm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(PatientMenuActivity.this, ChatListActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_settings_ptm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent intent = new Intent(PatientMenuActivity.this, CreateNewExerciseActivity.class);
//                startActivity(intent);
                Intent i = new Intent(PatientMenuActivity.this, SettingsActivity.class);
                i.putExtra("PATIENT_UID", mAuth.getCurrentUser().getUid());
                startActivity(i);
            }
        });



    }
}
