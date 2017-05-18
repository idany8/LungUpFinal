package com.example.idan.lungupfinal.soundmeter;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.idan.lungupfinal.R;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;

public class InitActivity extends Activity {

    final int SECONDS_TO_TEST = 5;
    final long TIME_BETWEEN_SAMPLE = 50L;
    int testCounter = 0;
private boolean flagButton=true;
    final String ACTIVITY_NAME = this.getClass().getSimpleName();
    private Button panel_LBL_start;
    private TextView panel_LBL_level;
    private TextView txtMax;
    private TextView txtAvg, panel_LBL_time;
    private ProgressBar progressBar;
    private Handler mHandler = new Handler();
    private float mMaxSound;
    private float mAvgSound;
    private float mSumSound;
    private int mCounter;

    //private MagicProgressCircle panel_PRG_time;

    private Runnable mUpdateTimer = new Runnable() {
        @Override
        public void run() {
            if (testCounter < ((SECONDS_TO_TEST*1000)/TIME_BETWEEN_SAMPLE)) {
                Recorder.getInstance(InitActivity.this).SoundDB();
                mHandler.postDelayed(mUpdateTimer, TIME_BETWEEN_SAMPLE);
                testCounter++;
                int time = SECONDS_TO_TEST - (testCounter/(int)(1000/TIME_BETWEEN_SAMPLE) ) ;
                panel_LBL_time.setText("" + time);
            }
            else {
                panel_LBL_time.setText("0");
                testDone();
            }
        }
    };

    private void testDone() {
        //Recorder.getInstance(InitActivity.this).saveData(mAvgSound, mMaxSound,-9999);
        Recorder.getInstance(InitActivity.this).saveMicInitialValue(mAvgSound,mMaxSound);

        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        mMaxSound = 0;
        mAvgSound = 0;
        mSumSound = 0;
        mCounter = 0;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtMax = (TextView) findViewById(R.id.txt_max);
        txtAvg = (TextView) findViewById(R.id.txt_avg);
        initializeVariables();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        checkMicPermission();

        IRecorderUpdateListener recorderUpdateListener = new IRecorderUpdateListener() {
            @Override
            public void onSoundUpdate(float value) {
                panel_LBL_level.setText(Math.round(value) + " DB");
                if (mMaxSound < value)
                {
                    txtMax.setText( (int)(20* Math.log10(value)) + " DB");
                    mMaxSound = Math.round(value);
                }
                mSumSound +=value;
                mAvgSound = Math.round(mSumSound / mCounter++);
                txtAvg.setText( (int)(20* Math.log10(mAvgSound)) + " DB");
                progressBar.setProgress((int) value);
            }
        };

        Recorder.getInstance(InitActivity.this).setRecorderUpdateListener(recorderUpdateListener);

        setVolumeControlStream(3);


    }

    protected void onDestroy() {
        super.onDestroy();

        if (Build.VERSION.SDK_INT < 14)
            System.exit(0);
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
        Recorder.getInstance(InitActivity.this).RecorderRel();
        mHandler.removeCallbacks(mUpdateTimer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

    private void startTest() {
        Recorder.getInstance(InitActivity.this).startRecorder();
        AnimatorSet set = new AnimatorSet();
 //       set.playTogether(
               // ObjectAnimator.ofFloat(panel_PRG_time, "percent", 0, 100 / 100f)
 //       );
        set.setDuration(5000);
        set.setInterpolator(new AccelerateInterpolator());
        set.start();

        mHandler.postDelayed(mUpdateTimer, TIME_BETWEEN_SAMPLE);
    }

    private void initializeVariables() {
      //  panel_PRG_time = (MagicProgressCircle) findViewById(R.id.panel_PRG_time);
        panel_LBL_time = (TextView) findViewById(R.id.panel_LBL_time);
        panel_LBL_level = (TextView) findViewById(R.id.panel_LBL_level);
        panel_LBL_start = (Button) findViewById(R.id.panel_LBL_start);
        panel_LBL_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flagButton) {
                    flagButton = false;
                    startTest();
                }


            }
        });
    }
}
