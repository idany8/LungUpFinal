package com.example.idan.lungupfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientMenuActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView usr_name;
    private User current_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_menu);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        usr_name = (TextView)findViewById(R.id.user_name_tv);
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_user= dataSnapshot.getValue(User.class);
                Log.d("usersdata",""+current_user.getName());
                usr_name.setText(current_user.getName());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        initialPTmenu();
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
                Intent intent = new Intent(PatientMenuActivity.this, SpinnerGame.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_exercises_list_ptm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent intent = new Intent(PatientMenuActivity.this, CreateNewExerciseActivity.class);
//                startActivity(intent);
            }
        });

        findViewById(R.id.btn_performance_ptm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent intent = new Intent(PatientMenuActivity.this, CreateNewExerciseActivity.class);
//                startActivity(intent);
            }
        });
        findViewById(R.id.btn_assign_care_giver_ptm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent intent = new Intent(PatientMenuActivity.this, CreateNewExerciseActivity.class);
//                startActivity(intent);
            }
        });
        findViewById(R.id.btn_messages_ptm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent intent = new Intent(PatientMenuActivity.this, CreateNewExerciseActivity.class);
//                startActivity(intent);
            }
        });
        findViewById(R.id.btn_settings_ptm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent intent = new Intent(PatientMenuActivity.this, CreateNewExerciseActivity.class);
//                startActivity(intent);
            }
        });



    }
}
