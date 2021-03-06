package com.example.idan.lungupfinal.CageGiverActivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.idan.lungupfinal.AllUsersActivities.LoginActivity;
import com.example.idan.lungupfinal.Classes.Exercise;
import com.example.idan.lungupfinal.Classes.Patient;
import com.example.idan.lungupfinal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateNewExerciseActivity extends AppCompatActivity {
    private ImageButton mUploadImage;
    private ImageButton mSubmit;
    private EditText mExersiceName, mDescription;
    private RadioGroup mRadioGroup;
    private RadioButton mRbPublic,mRbprivate;
    private ProgressBar pbar;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private String imagePath="";
    private Boolean isPrivate = false;
    private static final int GALLERY_INTENT =2;
    private TextView mUsrName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_exercise);
        mAuth = FirebaseAuth.getInstance();

        mStorage = FirebaseStorage.getInstance().getReference();

        mRadioGroup = (RadioGroup) findViewById(R.id.rgroup_nExercise);
        mRbprivate = (RadioButton) findViewById(R.id.rb_nExercise_private);
        mRbPublic = (RadioButton) findViewById(R.id.rb_nExercise_public);
        mRbPublic.setChecked(true);
        mUploadImage = (ImageButton) findViewById(R.id.btn_nExercise_uploadImage);
        mSubmit = (ImageButton) findViewById(R.id.btn_nExercise_submit);
        mExersiceName = (EditText) findViewById(R.id.et_nExercise_name);
        mDescription = (EditText) findViewById(R.id.et_nExercise_Description);
        pbar = (ProgressBar)findViewById(R.id.progressBar2);
        pbar.setVisibility(View.GONE);
        mUsrName= (TextView)findViewById(R.id.user_name_tv);
        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(CreateNewExerciseActivity.this, LoginActivity.class);
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


        //Toast.makeText(CreateNewExerciseActivity.this,""+mAuth.getCurrentUser().getEmail(),Toast.LENGTH_LONG);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("checked button",""+ mRadioGroup.getCheckedRadioButtonId());
                if (mRbprivate.getId()==mRadioGroup.getCheckedRadioButtonId()){
                    isPrivate = true;
                }else {
                    isPrivate = false;
                }
                Log.d("checked button",""+ isPrivate);

                Exercise newExercise = new Exercise((int)(System.currentTimeMillis()),"type",mExersiceName.getText().toString(),mDescription.getText().toString(),
                        imagePath,mAuth.getCurrentUser().getEmail(),mAuth.getCurrentUser().getUid(),isPrivate);

                FirebaseDatabase.getInstance().getReference().child("exercises").push().setValue(newExercise);
                onBackPressed();

            }
        });

        mUploadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,GALLERY_INTENT);
                                            }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== GALLERY_INTENT && resultCode==RESULT_OK){
            pbar.setVisibility(View.VISIBLE);
            Uri uri = data.getData();
            final StorageReference filepath= mStorage.child("photos").child(Long.toString(System.currentTimeMillis()));

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    imagePath=downloadUri.toString();

                    pbar.setVisibility(View.GONE);
                   // imagePath=filepath.toString();
                    mUploadImage.setImageResource(R.drawable.btn_upload_media_v);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }

            });
        }

    }
}
