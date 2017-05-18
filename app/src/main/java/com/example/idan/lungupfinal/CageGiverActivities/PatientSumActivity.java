package com.example.idan.lungupfinal.CageGiverActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.idan.lungupfinal.AllUsersActivities.LoginActivity;
import com.example.idan.lungupfinal.AllUsersActivities.PatientDetailedPerformance;
import com.example.idan.lungupfinal.Classes.P_Exercise;
import com.example.idan.lungupfinal.Classes.Patient;
import com.example.idan.lungupfinal.Classes.PerfUnit;
import com.example.idan.lungupfinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PatientSumActivity extends AppCompatActivity {
    String patUid;
    TextView patientName;
    ImageButton editExPlan, detailedPerf;
    ListView lv_el,lv_lu;
    private TextView mUsrName;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sum);
        mAuth = FirebaseAuth.getInstance();
        initialSum();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                patUid = null;
            } else {
                patUid = extras.getString("PATIENT_UID");
            }
        }


        mUsrName= (TextView)findViewById(R.id.user_name_tv);
        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(PatientSumActivity.this, LoginActivity.class);
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




        initialListViews();



    }
    public void initialSum(){

        patientName = (TextView) findViewById(R.id.tv_sum_header);
        editExPlan = (ImageButton) findViewById(R.id.btn_sum_edit_ex);
        detailedPerf = (ImageButton) findViewById(R.id.btn_sum_detailed_perf);
        lv_el = (ListView) findViewById(R.id.lv_sum_ep);

         lv_lu = (ListView) findViewById(R.id.lv_sum_lu);
        editExPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatientSumActivity.this, ExercisesPlanActivity.class);
                i.putExtra("PATIENT_UID", patUid);
                startActivity(i);
            }
        });
        detailedPerf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatientSumActivity.this, PatientDetailedPerformance.class);
                i.putExtra("PATIENT_UID", patUid);
                startActivity(i);
            }
        });


    }

    public void initialListViews(){

        FirebaseDatabase.getInstance().getReference().child("users").child(patUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient reqPat = dataSnapshot.getValue(Patient.class);
                patientName.setText(reqPat.getName()+" Summary");
                if (reqPat.getP_exercises()!=null) {
                    ArrayList<P_Exercise> patientPExercises = reqPat.getP_exercises();


                    ArrayAdapter<P_Exercise> arrAdapter =new ArrayAdapter<P_Exercise>(PatientSumActivity.this, R.layout.list_item, R.id.txtitem, patientPExercises);

                    lv_el.setAdapter(arrAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        FirebaseDatabase.getInstance().getReference().child("users").child(patUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient reqPat = dataSnapshot.getValue(Patient.class);
                if (reqPat.getP_exercises()!=null) {
                    ArrayList<P_Exercise> patientPExercises = reqPat.getP_exercises();
                    //
                    ArrayList<PerfUnit> arrPatLU = getLastUsage(patientPExercises);
                    //
                    ArrayAdapter<PerfUnit> arrAdapter2 =new ArrayAdapter<PerfUnit>(PatientSumActivity.this, R.layout.list_item, R.id.txtitem, arrPatLU);
                    lv_lu.setAdapter(arrAdapter2);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });




    }
    public ArrayList<PerfUnit> getLastUsage (ArrayList<P_Exercise> patPExs){
        //ArrayList<String> arrLU = new ArrayList<>();
        ArrayList<PerfUnit> arrLU = new ArrayList<PerfUnit>();
        for (int i=0;i < patPExs.size() ; i++ )
        {
            arrLU.addAll( patPExs.get(i).getRecords() );
        }

        Collections.sort(arrLU, new Comparator<PerfUnit>() {
            @Override
            public int compare(PerfUnit pf1, PerfUnit pf2) {
                return (int) (pf1.getTime() - pf2.getTime());
            }
        });
        return arrLU;

    }
}
