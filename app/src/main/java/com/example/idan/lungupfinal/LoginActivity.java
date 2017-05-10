package com.example.idan.lungupfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";
    public static final int RC_SIGN_IN = 0;
    DatabaseReference ref;
    private FirebaseAuth mAuth;
    private TextView mStatusTextView;

    private EditText mEmailField;
    private EditText mPasswordField;
    private ImageButton mLoginBtn;
    private TextView mRegisterTv;

    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        // mStatusTextView = (TextView) findViewById(R.id.status);
        //  mDetailTextView = (TextView) findViewById(R.id.detail);
        mEmailField = (EditText) findViewById(R.id.et_username);
        mPasswordField = (EditText) findViewById(R.id.et_password);
        mPasswordField.setTransformationMethod(new PasswordTransformationMethod());
        mLoginBtn = (ImageButton) findViewById(R.id.btn_signin);
        mRegisterTv = (TextView) findViewById(R.id.tv_register);
        // Buttons
        mRegisterTv.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        //findViewById(R.id.btn_login_signup).setOnClickListener(this);
        findViewById(R.id.btn_qr_scan).setOnClickListener(this);
        // findViewById(R.id.verify_email_button).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        ref = FirebaseDatabase.getInstance().getReference();

    }

    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            intentToUserType();
        }



    }

//// https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/EmailPasswordActivity.java
//
//        }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
                String[] crdtList = result.getContents().split(";");

                mEmailField.setText(crdtList[0]);
                mPasswordField.setText(crdtList[1]);
                mLoginBtn.performClick();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


        private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
          final String nUserEmail = email;
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

                            User createdUser = new User(nUserEmail,"","PT",mAuth.getCurrentUser().getUid().toString(),false,null);
                            FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid().toString()).setValue(createdUser);
                            intentToUserType();
                            //FirebaseUser user = mAuth.getCurrentUser();
                          //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
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

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }



        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            intentToUserType();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
//                        if (!task.isSuccessful()) {
//                            mStatusTextView.setText(R.string.auth_failed);
//                        }


                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    void intentToUserType()
    {
        Log.d("usertype", "uid: " + mAuth.getCurrentUser().getUid());
        ref.child("users").child(mAuth.getCurrentUser().getUid()).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String userType = snapshot.getValue().toString();
                if (userType.equals("CG")) {
                    Intent intent = new Intent(LoginActivity.this, CaregiverMenuActivity.class);
                    startActivity(intent);

                }else {   // *****should check other types
                    Intent intent = new Intent(LoginActivity.this, PatientMenuActivity.class);
                    startActivity(intent);
                }
                Log.d("usertype", "uid: " + snapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i== R.id.btn_signin){
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }else if (i== R.id.btn_qr_scan){
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan");
            integrator.setCameraId(0);
            integrator.setBeepEnabled(false);
            integrator.setBarcodeImageEnabled(false);
            integrator.initiateScan();
        }else if (i==R.id.tv_register){
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}
