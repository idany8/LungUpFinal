package com.example.idan.lungupfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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

public class PatientMenuActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView usr_name;
    private User current_user;
    private ImageView img_popup_qr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_menu);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
       // img_popup_qr = (ImageView)findViewById(R.id.qr_popup_image);
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
//                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//                try{
//
//                    BitMatrix bitMatrix = multiFormatWriter.encode("hello", BarcodeFormat.QR_CODE,200,200);
//                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//                    img_popup_qr.setImageBitmap(bitmap);
//                    Toast.makeText(PatientMenuActivity.this,"ok" ,
//                            Toast.LENGTH_SHORT).show();
//
//                }
//                catch (WriterException e){
//                    e.printStackTrace();
//                }


                LayoutInflater layoutInflater
                        = (LayoutInflater)getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup, null);

                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT);

                Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
                btnDismiss.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }});

                popupWindow.showAsDropDown(findViewById(R.id.btn_exercises_list_ptm),0, 0);


            }});



        findViewById(R.id.btn_messages_ptm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(PatientMenuActivity.this, ChatListActivity.class);
                startActivity(intent);
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
