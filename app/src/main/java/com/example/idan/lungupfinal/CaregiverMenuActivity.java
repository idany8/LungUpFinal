package com.example.idan.lungupfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CaregiverMenuActivity extends AppCompatActivity {
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
                Toast.makeText(CaregiverMenuActivity.this, " " + mAuth.getCurrentUser().getUid() + " -- " + mAuth.getCurrentUser().getDisplayName(),
                        Toast.LENGTH_LONG).show();




                final ArrayList<User> ruList= new ArrayList<User>();
                mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user= dataSnapshot.getValue(User.class);
                        Log.d("usersdata",""+user.getName());
                        Log.d("usersdata2",""+user.getRelatedUsers());
                        usersList = user.getRelatedUsers();
                        //ArrayList<User> test = getRelatedUsersList(user.getRelatedUsers(), mDatabase);
                       // Log.d("userstest", "" + test);

//                        for (String tmpUid:usersList) {
//                            mDatabase.child("users").child(tmpUid).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    User user = dataSnapshot.getValue(User.class);
//                                    Log.d("usersdata3", "" + user.getName());
//                                    Log.d("usersdata4", "" + user.getRelatedUsers());
//                                    ruList.add(user);
//                                    Log.d("rulist", "" + ruList);
////                                    ArrayList<User> test = getRelatedUsersList(user.getRelatedUsers(), mDatabase);
////                                    Log.d("userstest", "" + test);
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                        }

                        Log.d("rulistAfter", "" + ruList);


//                        ArrayList<User> test = getRelatedUsersList(user.getRelatedUsers(),mDatabase);
//                        Log.d("userstest",""+test);
                    }
                    public void onComplete(){

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






                        ///
//                        mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                User xx = parseData(dataSnapshot);
//                                Log.d("values", "" + xx.getEmail());
//                                //System.out.println(xx.getEmail());
////
//// for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
////                            System.out.println(userSnapshot.getKey() + ": " + userSnapshot.getValue());
////                            //Log.d("userprint",""+xx.getName());
////                        }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//
//                        });
            }

        });


    }


    public void onBackPressed() {

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