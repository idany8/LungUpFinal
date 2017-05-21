package com.example.idan.lungupfinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idan.lungupfinal.Classes.MySharedPreferences;
import com.example.idan.lungupfinal.soundmeter.IRecorderUpdateListener;
import com.example.idan.lungupfinal.soundmeter.InitActivity;
import com.example.idan.lungupfinal.soundmeter.Recorder;
import com.example.idan.lungupfinal.soundmeter.SpinActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SpinnerGame extends AppCompatActivity {


    final int SECONDS_TO_TEST = 60;
    final long TIME_BETWEEN_SAMPLE = 50L;
    private ImageView spinnerImg;
    private TextView mMax, mAvg, mCur;
    private Button btnStart;
    private ImageButton mCancelBtn, mDoneBtn;
    private Handler dbHandler = new Handler();
    int testCounter = 0;
    Handler m_handler;
    Runnable m_handlerTask;
    int userInitialValue;
    long userInitialTime;
    boolean spinnerStarted=false;
    float curDb=0;
    private ViewPropertyAnimator mAnimation;
    private boolean isAnimationRunning,isTimerRunning=false;
    private Timer t=new Timer();
    private double TimeCounter = 0;
    private double maxValue=0,sumValue=0, triesNum=0;


    private Runnable mUpdateTimer = new Runnable() {
        @Override
        public void run() {
            if (testCounter < ((SECONDS_TO_TEST*1000)/TIME_BETWEEN_SAMPLE)) {

                Recorder.getInstance(SpinnerGame.this).SoundDB();
                dbHandler.postDelayed(mUpdateTimer, TIME_BETWEEN_SAMPLE);
                testCounter++;

            }
            else {

                testDone();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_game);

        initScreen();
        startRotationAnimation();
        MySharedPreferences msp = new MySharedPreferences(this);
        userInitialValue = msp.getIntFromSharedPrefernces("MIC_INIT_VALUE", -1);
        userInitialTime = msp.getLongFromSharedPreferences("MIC_INIT_DATE",-1);
        if (userInitialValue!=-1){
            if (userInitialTime!=-1){
                if (userInitialTime-System.currentTimeMillis() > 1000*60*60*24) {
                    alertDialog("Your last microphone initialization is expired. please initial again.");
                }
            }
        }else alertDialog("Please initial your microphone first");


        IRecorderUpdateListener recorderUpdateListener = new IRecorderUpdateListener() {
            @Override
            public void onSoundUpdate(float value) {
//                mCur.setText(""+value);
                curDb=value;
                Log.d("soundd", curDb+"");
                Log.d("soundd2", userInitialValue+"");


                if (spinnerStarted) {
                    if (curDb > userInitialValue )
                    {
                        if (!isTimerRunning)
                        {
                            isTimerRunning=true;
                            TimeCounter=0;
                            t.scheduleAtFixedRate(new TimerTask() {

                                @Override
                                public void run() {
                                    // TODO Auto-generated method stub
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            mCur.setText(String.valueOf(TimeCounter)); // you can set it to a textView to show it to the user to see the time passing while he is writing.
                                            TimeCounter+=0.5;
                                        }
                                    });

                                }
                            }, 500, 500); // 1000 means start from 1 sec, and the second 1000 is do the loop each 1 sec.
                        }
                        mAnimation.start();
                        startRotationAnimation();

                    } else if (isAnimationRunning ){
                        isAnimationRunning = false;
                        if (isTimerRunning) {
                            isTimerRunning = false;
                            t.cancel();//stopping the timer when ready to stop.
                            Log.d("sound3", ""+ (TimeCounter-0.5));
                            updateSessionVals(TimeCounter-0.5);
                            t= new Timer();
                        }
                        mAnimation.cancel();

                    }
                }
            }
        };
        Recorder.getInstance(SpinnerGame.this).setRecorderUpdateListener(recorderUpdateListener);

        startTest();
        setVolumeControlStream(3);



    }

    private void updateSessionVals(double val) {
        if (val>maxValue){
            maxValue=val;
            mMax.setText(maxValue+"s\nMax");
        }
        triesNum++;
        sumValue+=val;
        mAvg.setText(String.format("%.1fs"+"\nAvg",sumValue/triesNum));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // m_handler.removeCallbacks(m_handlerTask);
        dbHandler.removeCallbacks(mUpdateTimer);
        Recorder.getInstance(SpinnerGame.this).RecorderRel();
    }




    private void testDone() {

        onBackPressed();
    }
    private void startTest() {
        Recorder.getInstance(SpinnerGame.this).startRecorder();

        dbHandler.postDelayed(mUpdateTimer, TIME_BETWEEN_SAMPLE);
    }
    private void initScreen() {
        spinnerImg = (ImageView) findViewById(R.id.spn_img);
        mMax = (TextView) findViewById(R.id.spn_tv_max);

        mAvg = (TextView) findViewById(R.id.spn_tv_avg);
        mCur = (TextView) findViewById(R.id.spn_tv_current_time);
 //       btnStart = (Button) findViewById(R.id.spn_btn_start);
        mCancelBtn = (ImageButton) findViewById(R.id.spn_btn_cancel);
        mDoneBtn = (ImageButton) findViewById(R.id.spn_btn_done);
        t = new Timer();
    }

    private void startRotationAnimation() {
        spinnerStarted=true;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                isAnimationRunning = true;
                mAnimation = spinnerImg.animate().rotationBy(360).withEndAction(this).setDuration(1000).setInterpolator(new LinearInterpolator());
                //mAnimation.start();
            }
        };

        spinnerImg.animate().rotationBy(360).withEndAction(runnable).setDuration(1000).setInterpolator(new LinearInterpolator()).start();

    }
    void alertDialog(String message)
    {
        new AlertDialog.Builder(this)
                .setTitle("Initial Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SpinnerGame.this, InitActivity.class);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
