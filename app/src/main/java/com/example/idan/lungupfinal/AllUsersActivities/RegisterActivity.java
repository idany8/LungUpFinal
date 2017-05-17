package com.example.idan.lungupfinal.AllUsersActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.idan.lungupfinal.CageGiverActivities.CaregiverMenuActivity;
import com.example.idan.lungupfinal.Classes.User;
import com.example.idan.lungupfinal.PatientActivities.PatientMenuActivity;
import com.example.idan.lungupfinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";

    DatabaseReference ref;
    private FirebaseAuth mAuth;
    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mC_PasswordField;
    private ImageButton mCreateAccountBtn;
    private SwitchCompat mSwitch;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();

        initialRegisterScreen();


        mCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(mEmailField.getText().toString(),mPasswordField.getText().toString(),mNameField.getText().toString());
            }
        });

    }
    private void initialRegisterScreen(){
        mNameField = (EditText) findViewById(R.id.et_name);
        mEmailField = (EditText) findViewById(R.id.et_username);
        mPasswordField = (EditText) findViewById(R.id.et_password);
        mPasswordField.setTransformationMethod(new PasswordTransformationMethod());
        mC_PasswordField = (EditText) findViewById(R.id.et_c_password);
        mC_PasswordField.setTransformationMethod(new PasswordTransformationMethod());
        mCreateAccountBtn = (ImageButton) findViewById(R.id.btn_signup);
        mSwitch = (SwitchCompat) findViewById(R.id.switch_register);

    }
    private void createAccount(String email, String password,String name) {
        Log.d(TAG, "createAccount:" + email);
        final String nUserEmail = email;
        final String nUserName = name;
        if (!validateForm()) {
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Log.d(TAG, ""+  mAuth.getCurrentUser().getUid().toString());

                            String ut= getUserType();
                            User createdUser = new User(nUserEmail,nUserName,ut,mAuth.getCurrentUser().getUid().toString(),false,null);
                            FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid().toString()).setValue(createdUser);
                            intentToUserType();
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Registration failed." +task.getException() ,
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // [START_EXCLUDE]
                        // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }
    private boolean validateForm() {
        boolean valid = true;

        String name = mNameField.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mEmailField.setError("Required");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String c_password = mC_PasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mC_PasswordField.setError("Required");
            valid = false;
        }else if (!password.equals(c_password)){
            mC_PasswordField.setError("Try Again");
            valid = false;
        } else {
            mC_PasswordField.setError(null);
        }

        return valid;
    }
    private String getUserType(){
        if (mSwitch.isChecked()){
            return "CG";
        }
        return "PT";
    }
    void intentToUserType()
    {
        Log.d("usertype", "uid: " + mAuth.getCurrentUser().getUid());
        ref.child("users").child(mAuth.getCurrentUser().getUid()).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String userType = snapshot.getValue().toString();
                if (userType.equals("CG")) {
                    Intent intent = new Intent(RegisterActivity.this, CaregiverMenuActivity.class);
                    startActivity(intent);

                }else {   // *****should check other types
                    Intent intent = new Intent(RegisterActivity.this, PatientMenuActivity.class);
                    startActivity(intent);
                }
                Log.d("usertype", "uid: " + snapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
