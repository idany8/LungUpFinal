package com.example.idan.lungupfinal.CageGiverActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.idan.lungupfinal.Chat.Chat;
import com.example.idan.lungupfinal.Chat.ChatListActivity;
import com.example.idan.lungupfinal.AllUsersActivities.LoginActivity;
import com.example.idan.lungupfinal.Classes.Patient;
import com.example.idan.lungupfinal.R;
import com.example.idan.lungupfinal.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class CaregiverMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<String> usersList;
    private TextView usr_name;
    private User current_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregivers_menu);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        usr_name = (TextView) findViewById(R.id.user_name_tv);

        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_user = dataSnapshot.getValue(User.class);
                Log.d("usersdata", "" + current_user.getName());
                usr_name.setText(current_user.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        findViewById(R.id.btn_assigned_ptns_cgmenu).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CaregiverMenuActivity.this, AssignedUsersActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_create_npat_cgmenu).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CaregiverMenuActivity.this, CreateNewPatient.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_create_ex_cgmenu).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CaregiverMenuActivity.this, CreateNewExerciseActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(CaregiverMenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.btn_messages_cgmenu).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CaregiverMenuActivity.this, ChatListActivity.class);
                startActivity(intent);

            }

        });

        findViewById(R.id.btn_assign_qr_ptn_cgmenu).setOnClickListener((View.OnClickListener) this);


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
        if (i== R.id.btn_assign_qr_ptn_cgmenu){
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        }
    }

    public User parseData(DataSnapshot dataSnapshot) {
        User user = new User();

        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
            if (userSnapshot.getKey().equals("email")) {
                user.setEmail((String) userSnapshot.getValue());
            }
            System.out.println(userSnapshot.getKey() + ": " + userSnapshot.getValue());

        }
        return user;
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
                    Toast.makeText(CaregiverMenuActivity.this, "Users already assigned", Toast.LENGTH_LONG).show();
                }else{
                    uid1RU.add(secondUid);
                    user1.setRelatedUsers(uid1RU);
                    FirebaseDatabase.getInstance().getReference().child("users").child(firstUid).setValue(user1);

                    FirebaseDatabase.getInstance().getReference().child("chats").child(secondUid).child(firstUid).setValue(new Chat(user1.getName()));
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

//    public ArrayList<User> getRelatedUsersList(ArrayList<String> users_id, DatabaseReference mDatabase)
//    {
////        final ArrayList<User> ruList= new ArrayList<User>();
////        for (String tmpUid:users_id) {
////            mDatabase.child("users").child(tmpUid).addListenerForSingleValueEvent(new ValueEventListener() {
////                @Override
////                public void onDataChange(DataSnapshot dataSnapshot) {
////                    User user = dataSnapshot.getValue(User.class);
////                    Log.d("usersdata3", "" + user.getName());
////                    Log.d("usersdata4", "" + user.getRelatedUsers());
////                    ruList.add(user);
////                    Log.d("rulist", "" + ruList);
////
//////                                    ArrayList<User> test = getRelatedUsersList(user.getRelatedUsers(), mDatabase);
//////                                    Log.d("userstest", "" + test);
////                }
////
////                @Override
////                public void onCancelled(DatabaseError databaseError) {
////
////                }
////            });
////        }
////        return ruList;
//
//       //FirebaseDatabase.getInstance().getReference()
//
//
//
//
//       // final ArrayList<User> ruList= new ArrayList<User>();
//
////       // for (String userUid : users_id)
////        //{
////            mDatabase.child("users").child("HKdpZb3Hh7NN68t1NWDLfeP2jIB2").addListenerForSingleValueEvent(new ValueEventListener() {
////                @Override
////                public void onDataChange(DataSnapshot dataSnapshot) {
////                    User user= dataSnapshot.getValue(User.class);
////                    ruList.add(user);
////                }
////
////                @Override
////                public void onCancelled(DatabaseError databaseError) {
////
////                }
////            });
////        //}
//    return ruList;
//    }
}