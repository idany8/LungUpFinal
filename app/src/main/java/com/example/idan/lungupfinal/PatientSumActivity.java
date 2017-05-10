package com.example.idan.lungupfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class PatientSumActivity extends AppCompatActivity {
    String patUid;
    TextView patientName;
    ImageButton editExPlan, detailedPerf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sum);

        initialSum();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                patUid = null;
            } else {
                patUid = extras.getString("PATIENT_UID");
            }
        }

        if (patUid!=null) {
            patientName.setText(patUid);    /// catch intent error here
        }else patientName.setText("error");






        FirebaseDatabase.getInstance().getReference().child("users").child(patUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient requestedPatient= dataSnapshot.getValue(Patient.class);
                patientName.setText(requestedPatient.getName());
//
                Date date = new Date();
                PerfUnit pf = new PerfUnit(date,5.2);
                ArrayList<PerfUnit> pfarray = new ArrayList<PerfUnit>();
                pfarray.add(pf);
                pfarray.add(pf);

                P_Exercise pex = new P_Exercise("2TAW",pfarray);

                ArrayList<P_Exercise> exArray = new ArrayList<P_Exercise>();
                exArray.add(pex);
                requestedPatient.setP_exercises(exArray);
                FirebaseDatabase.getInstance().getReference().child("users").child(patUid).setValue(requestedPatient);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void initialSum(){

        patientName = (TextView) findViewById(R.id.tv_sum_header);
        editExPlan = (ImageButton) findViewById(R.id.btn_sum_edit_ex);
        detailedPerf = (ImageButton) findViewById(R.id.btn_sum_detailed_perf);

        editExPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatientSumActivity.this, ExercisesPlanActivity.class);
                i.putExtra("PATIENT_UID", patUid);
                startActivity(i);
            }
        });

    }
}
