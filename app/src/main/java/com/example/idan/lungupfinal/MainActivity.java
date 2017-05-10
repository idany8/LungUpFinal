package com.example.idan.lungupfinal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

//import com.example.nexus.MySharedPreferences;

/**
 * Created by Idan on 26/09/2016.
 */
public class MainActivity extends AppCompatActivity {

    public static final int RC_SIGN_IN = 0;
    DatabaseReference ref;
    private FirebaseAuth mAuth;
    private TextView txt_name;
    private Button btn_testScreen;
    boolean initial_flag=false;

    @Override
    public void onResume()
    {
        super.onResume();
//        MySharedPreferences msp = new MySharedPreferences(this);
//        if (initial_flag==false) {
//            initial_flag = initializeDone(msp.getStringFromSharedPrefernces("rec5", "na"));
//        }
//        btn_testScreen = (Button) findViewById(R.id.btn_testScreen);
//        if (initial_flag){
//            btn_testScreen.setBackgroundResource(R.drawable.btn_shape);
//        }else {
//            btn_testScreen.setBackgroundResource(R.drawable.btn_shape_bold);
//            btn_testScreen.setTextColor(Color.BLACK);
       // }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

// ...


        mAuth = FirebaseAuth.getInstance();

        Log.d("test","1111");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ref = FirebaseDatabase.getInstance().getReference();
        //txt_name = (TextView) findViewById(R.id.txt_username);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
//            String xx = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            Log.d("create1", "check: " + xx);
//            xx = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
//            Log.d("create1", "check: " + xx);

            initTest();

        }
        else
        {
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance().createSignInIntentBuilder().build(),
                    RC_SIGN_IN);
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
//                ref.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot snapshot) {
//                        if (snapshot.hasChild(uid)) {
//                            Log.d("test","in if");
//                        }
//                        else {
//                            Log.d("test","out if");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
                //String key = ref.child("users").push().getKey();

                String currentEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String currentName =  FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                ArrayList<String> re_usrs = new ArrayList<String>();
                re_usrs.add("aoaoaod231dsa");
                re_usrs.add("22ornddf8fndf");
//                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            //    ref.child("users").child(uid).setValue(new User(currentEmail,currentName,"CareProvider",re_usrs));
                //ref.child("users").child(key).setValue(new User(currentEmail,currentName));

                initTest();
            } else {
                finish();
                startActivity(getIntent());
            }
        }
    }

    public void initTest(){


        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ArrayList<String> re_usrs = new ArrayList<String>();
        re_usrs.add("aoaoaod231dsa");
        re_usrs.add("22ornddf8fndf");

        //P_Exercise p_exr = new P_Exercise(1542,"gaming","Dr.Cohen","how to perform bla bla","everyday2times","performanceofuser");
        //ArrayList<P_Exercise> p_exr_list = new ArrayList<P_Exercise>();
        //p_exr_list.add(p_exr);


       // Patient user1 = new Patient("m5555@gmail.com","pat levi","PT",re_usrs,p_exr_list);
        //ref.child("users").child(uid).setValue(user1);
        intentToUserType();
//        findViewById(R.id.btn_create_new_user).setOnClickListener(new View.OnClickListener() {
//            private ProgressDialog progressDialog;
//            public void onClick(View view) {
//                String xx = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                Log.d("create", "before creating: " + xx);
//               // mAuth.createUserWithEmailAndPassword("m5555@gmail.com", "idan1234");
//
//                xx = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                Log.d("create", "after creating: " + xx);
//                Intent intent = new Intent(MainActivity.this,CreateNewPatient.class);
//                startActivity(intent);
//                                                                      }
//                                                                  });

//        findViewById(R.id.btn_testScreen).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,InitActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//        findViewById(R.id.btn_lastResults).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (initial_flag) {
//                    Intent intent = new Intent(MainActivity.this,RecordsActivity.class);
//                    startActivity(intent);
//                }
//                else alertDialog();
//            }
//        });
//        findViewById(R.id.button_game).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (initial_flag) {
//                    Intent intent = new Intent(MainActivity.this,GameActivity.class);
//                    startActivity(intent);
//                }
//                else alertDialog();
//            }
//        });
//        findViewById(R.id.btn_spin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (initial_flag) {
//                    Intent intent = new Intent(MainActivity.this, SpinActivity.class);
//                    startActivity(intent);
//                }
//                else alertDialog();
//            }
//        });
//        findViewById(R.id.btn_high_scores).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,HighScoresActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        findViewById(R.id.btn_help).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Dialog dialog = new Dialog(MainActivity.this);
//                dialog.setContentView(R.layout.dialog);
//                dialog.setTitle("Help");
//                dialog.show();
//                Button okButton = (Button) dialog.findViewById(R.id.okButton);
//                // if decline button is clicked, close the custom dialog
//                okButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Close dialog
//                        dialog.dismiss();
//                    }
//                });
//            }
//        });
//
        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivityForResult(
                                        // Get an instance of AuthUI based on the default app
                                        AuthUI.getInstance().createSignInIntentBuilder().build(),
                                        RC_SIGN_IN);
                            }
                        });
            }
        });
//
//        txt_name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
   }



    boolean initializeDone(String str)
    {
        if (str == "na")
            return false;
        else
            return true;
    }

    void intentToUserType()
    {
        String xx = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d("usertype", "uid: " + xx);
        ref.child("users").child(xx).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
               String userType = snapshot.getValue().toString();
                if (userType.equals("CG")) {
                    Intent intent = new Intent(MainActivity.this, CaregiverMenuActivity.class);
                    startActivity(intent);
                }else {   // *****should check other types
                    Intent intent = new Intent(MainActivity.this, PatientMenuActivity.class);
                    startActivity(intent);
                }
                Log.d("usertype", "uid: " + snapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    void alertDialog()
    {
        new AlertDialog.Builder(this)
                .setTitle("Initial Error")
                .setMessage("Please click on INITIALIZE before your first use. ")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
