package com.example.idan.lungupfinal;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;

public class CreateNewPatient extends AppCompatActivity {
    private FirebaseAuth mAuth1;
    private FirebaseAuth mAuth2;
    private FirebaseApp myApp;
    EditText inputName, inputEmail, inputPass,inputCPass;
    TextView info;
    ImageView image;
    String text2Qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_patient);

        mAuth1 = FirebaseAuth.getInstance();

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("[https://lungupfinal.firebaseio.com/]")
                .setApiKey("AIzaSyCmU0dV885n7zonicM7mToAjxbWs_CjuPI")
                .setApplicationId("lungupfinal").build();

        FirebaseApp.getApps(getApplicationContext());
        List<FirebaseApp> appsList =  FirebaseApp.getApps(getApplicationContext());
        appsList.size();
        Log.d("length:", " "+appsList.size() );

        if (appsList.size()<=1){
            myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions,
                    "LungUp" );
        } else{
            myApp = appsList.get(0);
        }

        mAuth2 = FirebaseAuth.getInstance(myApp);

        inputName =  (EditText) findViewById(R.id.et_cnp_name);
        inputEmail =  (EditText) findViewById(R.id.et_cnp_username);
        inputPass =  (EditText) findViewById(R.id.et_cnp_password);
        inputCPass =  (EditText) findViewById(R.id.et_cnp_c_password);
        info = (TextView) findViewById(R.id.tv_cnp_info);
        inputPass.setTransformationMethod(new PasswordTransformationMethod());
        inputCPass.setTransformationMethod(new PasswordTransformationMethod());
        findViewById(R.id.btn_create_new_patient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(inputEmail.getText().toString(), inputPass.getText().toString());


            }
        });

        image = (ImageView) findViewById(R.id.image);

    }

    private void createAccount(final String email, final String password) {

        mAuth2.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final String new_user_uid,current_user_uid;

                        if (!task.isSuccessful()) {
                            String ex = task.getException().toString();
                            Toast.makeText(CreateNewPatient.this, "Registration Failed" + ex,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreateNewPatient.this, "Registration successful",
                                    Toast.LENGTH_SHORT).show();

                            new_user_uid = mAuth2.getCurrentUser().getUid().toString();
                            ArrayList<String> newUsrRu = new ArrayList<String>();
                            newUsrRu.add(mAuth1.getCurrentUser().getUid().toString());
                            final User createdUser = new User(inputEmail.getText().toString(),inputName.getText().toString(),"PT",new_user_uid,false,newUsrRu);




                            FirebaseDatabase.getInstance().getReference().child("users").child(mAuth1.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    Log.d("usersdata",""+user.getName());
                                    Log.d("usersdata2",""+user.getRelatedUsers());
                                    ArrayList<String> loggedUsrRu = user.getRelatedUsers();
                                    loggedUsrRu.add(new_user_uid);
                                    user.setRelatedUsers(loggedUsrRu);
                                    FirebaseDatabase.getInstance().getReference().child("users").child(mAuth1.getCurrentUser().getUid()).setValue(user);
                                    FirebaseDatabase.getInstance().getReference().child("chats").child(mAuth1.getCurrentUser().getUid()).child(new_user_uid).setValue(new Chat(createdUser.getName()));
                                    FirebaseDatabase.getInstance().getReference().child("chats").child(new_user_uid).child(mAuth1.getCurrentUser().getUid()).setValue(new Chat(user.getName()));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




                            text2Qr=email+";"+password;

                            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                            try{
                                BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,200,200);
                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                image.setImageBitmap(bitmap);
                                Toast.makeText(CreateNewPatient.this,text2Qr ,
                                        Toast.LENGTH_SHORT).show();
                                info.setVisibility(View.VISIBLE);
                            }
                            catch (WriterException e){
                                e.printStackTrace();
                            }


                            Log.d("loginlogout2"," "+ mAuth2.getCurrentUser().getDisplayName() );
                            mAuth2.signOut();
                            Log.d("loginlogout1"," "+ mAuth1.getCurrentUser().getDisplayName() );
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            ref.child("users").child(new_user_uid).setValue(createdUser);
                        }


                        // ...
                    }
                });

    }
}