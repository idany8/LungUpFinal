package com.example.idan.lungupfinal.soundmeter;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.idan.lungupfinal.Classes.P_Exercise;
import com.example.idan.lungupfinal.Classes.PerfUnit;
import com.example.idan.lungupfinal.R;
import com.example.idan.lungupfinal.Classes.MySharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class HitGameActivity extends AppCompatActivity {

    private int mHeight;
    private int mWidth;
    private float mBulletX;
    private int mBulletSize;
    final int SECONDS_TO_TEST = 30;
    final long TIME_BETWEEN_SAMPLE = 50L;
    int testCounter = 0;
    int userInitialValue;
    long userInitialTime;
    private TextView txt_sound_level;
    private TextView txt_score;
   // private TextView txt_username;
   // private TextView txt_highscore;
    private Button btn_start_game;
    private MagicProgressCircle timer_panel;
    private TextView timer_LBL;
    private Handler mHandler = new Handler();
    private View blueBall;
    int score=0;
    String uName= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    DatabaseReference myRef;
    P_Exercise pexToPerform;
    private FirebaseAuth mAuth;
    private ArrayList<P_Exercise> patArrPex;

    private Runnable mUpdateTimer = new Runnable() {
        @Override
        public void run() {
            if (testCounter < ((SECONDS_TO_TEST*1000)/TIME_BETWEEN_SAMPLE)) {
                Recorder.getInstance(HitGameActivity.this).SoundDB();
                mHandler.postDelayed(mUpdateTimer, TIME_BETWEEN_SAMPLE);
                testCounter++;
                int time = SECONDS_TO_TEST - (testCounter/(int)(1000/TIME_BETWEEN_SAMPLE) ) ;
                timer_LBL.setText("" + time);
            }
            else {
                gameDone();
            }
        }
    };

    private void gameDone() {
        //Recorder.getInstance(HitGameActivity.this).saveData(0,0,score); // change to the right scores


        pexToPerform = new P_Exercise();
        patArrPex = new ArrayList<P_Exercise>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("p_exercises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    P_Exercise item = child.getValue(P_Exercise.class);
                    Log.d("checkPex", "" + item);
                    if (item.getId()== 100){
                        pexToPerform = item;
                    }else patArrPex.add(item);
                }
                pexToPerform.addRecords(new PerfUnit(System.currentTimeMillis(),Double.parseDouble(txt_score.getText().toString()),pexToPerform.getExercise_name()));
                patArrPex.add(pexToPerform);
                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("p_exercises").setValue(patArrPex);
                Toast.makeText(HitGameActivity.this, "Your Score is:"+ txt_score.getText().toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("checkPex", "rrrrr");
            }

        });

        //ScoreUnit su = new ScoreUnit(score, uName);
       // myRef.push().setValue(su);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(mUpdateTimer);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hit_game);
        setScreenSize();
        final RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
       // myRef = database.getReference("UsersScores");
        MySharedPreferences msp = new MySharedPreferences(this);
        //String tmp= msp.getStringFromSharedPrefernces("rec5", "na");
        checkMicPermission();


        userInitialValue = msp.getIntFromSharedPrefernces("MIC_INIT_VALUE", -1);
        userInitialTime = msp.getLongFromSharedPreferences("MIC_INIT_DATE",-1);
        if (userInitialValue!=-1){
            if (userInitialTime!=-1){
                long cu = System.currentTimeMillis();
                long xx= System.currentTimeMillis()-userInitialTime;
                if (System.currentTimeMillis()-userInitialTime > 600000) {
                    alertDialog("Your last microphone initialization is expired. please initial again.");
                }
            }
        }else alertDialog("Please initial your microphone");

        Log.d("micInit", ""+userInitialValue );
       // userInitialValue = 5000;
       // userInitialValue = calculateMicValues(tmp);

        txt_sound_level = (TextView) findViewById(R.id.txt_sound_level);
       // txt_username = (TextView) findViewById(R.id.txt_username);
       // txt_highscore = (TextView) findViewById(R.id.txt_high_score);

        txt_score = (TextView) findViewById(R.id.txt_score);
        txt_score.setText("  " + score + "  ");
      //  txt_username.setText("" + uName);
        btn_start_game = (Button) findViewById(R.id.btn_start_game);
     //   txt_highscore.setText("High Score:" + msp.getIntFromSharedPrefernces("highScore", 0));

        btn_start_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   btn_start_game.setVisibility(View.GONE);
                    startGame(container);

            }
        });


    }
    private void checkMicPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 111);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 111: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted
                    Toast.makeText(this, "grant the permission", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Until you grant the permission, App cannot work.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

private void startGame(final RelativeLayout container){

        final ImageView img_hit = (ImageView) findViewById(R.id.img_hit);
        final ImageView img_miss = (ImageView) findViewById(R.id.img_miss);
        timer_LBL = (TextView) findViewById(R.id.timer_LBL);
        timer_panel = (MagicProgressCircle) findViewById(R.id.timer_panel);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
            ObjectAnimator.ofFloat(timer_panel, "percent", 0, 100 / 100f)
        );
        set.setDuration(SECONDS_TO_TEST*1000);
        set.setInterpolator(new AccelerateInterpolator());
        set.start();


int timing =300;
    for (int i=0;i<3;i++) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View first = createRedBallView(false, getRandomImage());
                View second = createRedBallView(true,getRandomImage());
                View third = createRedBallView(true, false,getRandomImage());
                container.addView(first);
                container.addView(second);
                container.addView(third);
                startTranslateAnimationX(first);
                startTranslateAnimationY(second);
                startTranslateAnimationXY(third);
            }
        }, timing);
    timing+=1400;
    }




    blueBall = createBlueBallView();
        container.addView(blueBall);


    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {

            startTranslateAnimationX(blueBall);
        }
    }, 600);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        IRecorderUpdateListener recorderUpdateListener = new IRecorderUpdateListener() {
            @Override
            public void onSoundUpdate(float value) {
               // Log.d("micInit1",""+value);
                txt_sound_level.setText(value + " DB");
                if ((value > userInitialValue)) {
                    checkBall(img_hit, img_miss);

                }

            }
        };
        Recorder.getInstance(HitGameActivity.this).setRecorderUpdateListener(recorderUpdateListener);
        startTest();
        setVolumeControlStream(3);

        final View bullet = findViewById(R.id.bullet);
        bullet.post(new Runnable() {
            @Override
            public void run() {
                mBulletX = bullet.getX();
                mBulletSize = bullet.getWidth();
            }
        });

    }

    private void checkBall(final ImageView img_hit, final ImageView img_miss) {
        float translationBall = blueBall.getTranslationX()+15;
        if ((translationBall > mBulletX && translationBall < mBulletX + mBulletSize)  ) {

            img_hit.setVisibility(View.VISIBLE);
            score+=5;
            txt_score.setText(""+score);
          //  txt_score.setText(score);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    img_hit.setVisibility(View.INVISIBLE);

                }
            }, 400);


        } else {
           // missSound.start();

            img_miss.setVisibility(View.VISIBLE);
            score-=2;
            if (score<0) score=0;
            txt_score.setText(""+score);
            //txt_score.setText(score);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    img_miss.setVisibility(View.INVISIBLE);

                }

            },400);


        }


    }

    private void startTest() {
        Recorder.getInstance(HitGameActivity.this).startRecorder();
        mHandler.postDelayed(mUpdateTimer, TIME_BETWEEN_SAMPLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Recorder.getInstance(HitGameActivity.this).RecorderRel();
    }

    public static int getRandomImage() {
        ArrayList<Integer> pictures = new ArrayList<Integer>();

        pictures.add(R.drawable.apple);
        pictures.add(R.drawable.tomato);
        pictures.add(R.drawable.orange);
        pictures.add(R.drawable.banana);

        // Get Random Company Name from Arraylist using Random().nextInt()
        int pic = pictures.get(new Random().nextInt(pictures.size()));
        return pic;
    }
    private View createBlueBallView() {
        ImageView imageView = new ImageView(HitGameActivity.this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.drawable.strawberry);
        return imageView;
    }

    private View createRedBallView(boolean forVerticalMovement, int res) {
        return createRedBallView(false, forVerticalMovement, res);
    }

    private View createRedBallView(boolean withoutRules, boolean forVerticalMovement,int res) {
        ImageView imageView = new ImageView(HitGameActivity.this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (!withoutRules) {
            if (forVerticalMovement) {
                layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            } else {
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                //layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, RelativeLayout.TRUE);
            }
        }
        imageView.setLayoutParams(layoutParams);

        imageView.setImageResource(res);
        return imageView;
    }

    private void startTranslateAnimationXY(View view) {
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(view,
                "translationX", 0, mWidth);

        ObjectAnimator translationAnimatorY = ObjectAnimator.ofFloat(view,
                "translationY", 0, mHeight);
        translationAnimator.setDuration(4400);
        translationAnimator.setRepeatMode(ValueAnimator.REVERSE);
        translationAnimator.setRepeatCount(ValueAnimator.INFINITE);

        translationAnimatorY.setDuration(4400);
        translationAnimatorY.setRepeatMode(ValueAnimator.REVERSE);
        translationAnimatorY.setRepeatCount(ValueAnimator.INFINITE);


        AnimatorSet set = new AnimatorSet();
        set.playTogether(translationAnimator, translationAnimatorY);


        set.start();
    }

    private void startTranslateAnimationX(View view) {
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(view,
                "translationX", 0, mWidth);

        translationAnimator.setDuration(2800);
        if (view.equals(blueBall)){
            translationAnimator.setDuration(4000);
        }
        translationAnimator.setRepeatMode(ValueAnimator.REVERSE);
        translationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        translationAnimator.start();
    }

    private void startTranslateAnimationY(View view) {
        //view.setX(mWidth / 2);
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(view,
                "translationY", 0, mHeight);

        translationAnimator.setDuration(3800);
        translationAnimator.setRepeatMode(ValueAnimator.REVERSE);
        translationAnimator.setRepeatCount(20);
        translationAnimator.start();
    }

    private void setScreenSize() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mHeight = displaymetrics.heightPixels;
        mWidth = displaymetrics.widthPixels;
    }

    void alertDialog(String message)
    {
        new AlertDialog.Builder(this)
                .setTitle("Initial Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(HitGameActivity.this, InitActivity.class);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
