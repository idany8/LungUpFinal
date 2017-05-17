package com.example.idan.lungupfinal.PatientActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idan.lungupfinal.Classes.P_Exercise;
import com.example.idan.lungupfinal.Classes.PerfUnit;
import com.example.idan.lungupfinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PerformCustomExercise extends AppCompatActivity {
   P_Exercise pexToPerform = new P_Exercise();
   int gg;
    private TextView mDescription, mExerciseName;
    private ImageView mImg;
    private ImageButton btnDone, btnCancel;
    private FirebaseAuth mAuthLoggedUser;
    private String patUid;
    private ArrayList<P_Exercise> patArrPex = new ArrayList<P_Exercise>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_custom_exercise);

        mAuthLoggedUser = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                //pexToPerform = null;
            } else {
                gg = extras.getInt("P_EXERCISE_TO_PERFORM");
               // pexToPerform = (P_Exercise) extras.getSerializable("P_EXERCISE_TO_PERFORM");
            }

        }
        mExerciseName = (TextView) findViewById(R.id.tv_cex_header);
        mExerciseName.setText(""+ gg);

        getP_Ex();
        Log.d("checkPex", "dddddd");


    }

    private void getP_Ex() {
        Log.d("checkPex", "ffff");
        FirebaseAuth mAuthLoggedUser = FirebaseAuth.getInstance();
        patUid = mAuthLoggedUser.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(patUid).child("p_exercises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    P_Exercise item = child.getValue(P_Exercise.class);
                    Log.d("checkPex", "" + item);
                    if (item.getId()== gg){
                        pexToPerform = item;
                    }else patArrPex.add(item);
                }
                initialCustomExercise();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("checkPex", "rrrrr");
            }

        });
    }

    private void DoneExercise(){
        pexToPerform.addRecords(new PerfUnit(System.currentTimeMillis(),0,pexToPerform.getExercise_name()));
        patArrPex.add(pexToPerform);
        FirebaseDatabase.getInstance().getReference().child("users").child(patUid).child("p_exercises").setValue(patArrPex);
    }

    private void initialCustomExercise() {
        mExerciseName = (TextView) findViewById(R.id.tv_cex_header);
        mDescription = (TextView) findViewById(R.id.tv_cex_description);
        mImg = (ImageView) findViewById(R.id.img_cex);
        btnCancel = (ImageButton) findViewById(R.id.btn_cex_cancel);
        btnDone = (ImageButton) findViewById(R.id.btn_cex_done);

        mExerciseName.setText(pexToPerform.getExercise_name());
        mDescription.setText(pexToPerform.getDescription());
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoneExercise();
                finish();

            }
        });
    }
}
