package com.example.idan.lungupfinal.AllUsersActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.idan.lungupfinal.Adapters.MyAdapter;
import com.example.idan.lungupfinal.CageGiverActivities.AssignedUsersActivity;
import com.example.idan.lungupfinal.CageGiverActivities.CreateNewExerciseActivity;
import com.example.idan.lungupfinal.CageGiverActivities.CreateNewPatient;
import com.example.idan.lungupfinal.CageGiverActivities.PatientSumActivity;
import com.example.idan.lungupfinal.Chat.Chat;
import com.example.idan.lungupfinal.Chat.ChatListActivity;
import com.example.idan.lungupfinal.AllUsersActivities.LoginActivity;
import com.example.idan.lungupfinal.Classes.Exercise;
import com.example.idan.lungupfinal.Classes.Patient;
import com.example.idan.lungupfinal.PatientActivities.PatientMenuActivity;
import com.example.idan.lungupfinal.R;
import com.example.idan.lungupfinal.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class FamMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<String> usersList;
    private TextView usr_name;
    private User current_user;
    private String patUid;
    private boolean patAssigned=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fam_menu);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        usr_name = (TextView) findViewById(R.id.user_name_tv);

        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("token").setValue(FirebaseInstanceId.getInstance().getToken());
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_user = dataSnapshot.getValue(User.class);
                Log.d("usersdata", "" + current_user.getName());
                usr_name.setText(current_user.getName());
                if (current_user.getRelatedUsers().size()!=0) {
                    patUid = current_user.getRelatedUsers().get(0);
                    patAssigned=true;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(FamMenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_messages_famm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (patAssigned){
                    Intent intent = new Intent(FamMenuActivity.this, ChatListActivity.class);
                    startActivity(intent);
                }else alertDialog();

            }

        });

        findViewById(R.id.btn_assign_qr_ptn_famm).setOnClickListener((View.OnClickListener) this);
        findViewById(R.id.btn_settings_famm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                if (patAssigned){
                    Intent intent = new Intent(FamMenuActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }else alertDialog();

//                Intent i = new Intent(FamMenuActivity.this, SettingsActivity.class);
//                i.putExtra("PATIENT_UID", mAuth.getCurrentUser().getUid()); /////// change
//                startActivity(i);
            }
        });

        findViewById(R.id.btn_performance_famm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent intent = new Intent(FamMenuActivity.this, PatientDetailedPerformance.class);
//                startActivity(intent);
                if (patAssigned){
                    Intent i = new Intent(FamMenuActivity.this, PatientDetailedPerformance.class);
                    i.putExtra("PATIENT_UID", patUid);
                    startActivity(i);
                }else alertDialog();


            }
        });


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
                assignUsers(mAuth.getCurrentUser().getUid(),result.getContents());

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onBackPressed() {

    }
    public void onClick(View view) {
        int i = view.getId();
        if (i== R.id.btn_assign_qr_ptn_famm){
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        }
    }

    public void assignUsers(String uid1, String uid2){
        final String firstUid = uid1;
        final String secondUid = uid2;
        FirebaseDatabase.getInstance().getReference().child("users").child(uid1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient user1= dataSnapshot.getValue(Patient.class);
                ArrayList<String> uid1RU = user1.getRelatedUsers();
                if (uid1RU.contains(secondUid)){
                    Toast.makeText(FamMenuActivity.this, "Users already assigned", Toast.LENGTH_LONG).show();
                }else{
                    uid1RU.add(secondUid);
                    user1.setRelatedUsers(uid1RU);
                    FirebaseDatabase.getInstance().getReference().child("users").child(firstUid).setValue(user1);

                    FirebaseDatabase.getInstance().getReference().child("chats").child(secondUid).child(firstUid).setValue(new Chat(user1.getName()));
                    patAssigned=true;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("users").child(secondUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient user2= dataSnapshot.getValue(Patient.class);
                ArrayList<String> uid2RU = user2.getRelatedUsers();
                if (!uid2RU.contains(firstUid)) {
                    uid2RU.add(firstUid);
                    user2.setRelatedUsers(uid2RU);
                    FirebaseDatabase.getInstance().getReference().child("users").child(secondUid).setValue(user2);
                    FirebaseDatabase.getInstance().getReference().child("chats").child(firstUid).child(secondUid).setValue(new Chat(user2.getName()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    void alertDialog()
    {
        new AlertDialog.Builder(this)
                .setTitle("Assigned Patient Error")
                .setMessage("You have no assigned patient yet,\n" +
                        "Please click on Assign Patient button and scan patient QR code.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}