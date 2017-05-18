package com.example.idan.lungupfinal.soundmeter;

/**
 * Created by Idan on 14/11/2016.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.idan.lungupfinal.Classes.MySharedPreferences;
import com.example.idan.lungupfinal.R;

public class SpinActivity extends AppCompatActivity {

    ImageView imgview;
    private TextView txt_cur;
    Handler m_handler;
    Runnable m_handlerTask;
    final int SECONDS_TO_TEST = 50;
    final long TIME_BETWEEN_SAMPLE = 50L;
    int testCounter = 0;
    float cur_db=0;
    float sum_db=0;
    int count_db=0;
    int userInitialValue;
    long userInitialTime;
    private Handler dbHandler = new Handler();

    private Runnable mUpdateTimer = new Runnable() {
        @Override
        public void run() {
            if (testCounter < ((SECONDS_TO_TEST*1000)/TIME_BETWEEN_SAMPLE)) {
                Recorder.getInstance(SpinActivity.this).SoundDB();
                dbHandler.postDelayed(mUpdateTimer, TIME_BETWEEN_SAMPLE);
                testCounter++;
            }
            else {
                testDone();
            }
        }
    };
    private void testDone() {

        onBackPressed();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin);
        imgview = (ImageView) findViewById(R.id.imageView1);
        txt_cur = (TextView) findViewById(R.id.txt_cur);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setScaleY(3f);
        MySharedPreferences msp = new MySharedPreferences(this);
        String tmp;
//        tmp = msp.getStringFromSharedPrefernces("rec5", "na");
//        userInitialValue= HitGameActivity.calculateMicValues(tmp);

        userInitialValue = msp.getIntFromSharedPrefernces("MIC_INIT_VALUE", -1);
        userInitialTime = msp.getLongFromSharedPreferences("MIC_INIT_DATE",-1);
        if (userInitialValue!=-1){
            if (userInitialTime!=-1){
                if (userInitialTime-System.currentTimeMillis() > 1000*60*60*24) {
                    alertDialog("Your last microphone initialization is expired. please initial again.");
                }
            }
        }else alertDialog("Please initial your microphone first");

        Log.d("micInit", ""+userInitialValue );
        userInitialValue = 6000;
        m_handler = new Handler();
        m_handlerTask = new Runnable() {
            int i2 = 0;
            int delay = 200;
            public void run() {
                m_handler.postDelayed(m_handlerTask, delay);
                sum_db+=cur_db;
                count_db++;
                int avg_db =  ((int)sum_db) / count_db;
                delay =  getDelay(avg_db);
                if (count_db==5){
                    count_db=0;
                    sum_db=0;
                }
                progressBar.setProgress(400-delay);
                imgview.setRotation((float) ++i2);
            }
        };
        m_handlerTask.run();

        IRecorderUpdateListener recorderUpdateListener = new IRecorderUpdateListener() {
            @Override
            public void onSoundUpdate(float value) {
                txt_cur.setText(""+value);
                cur_db=value;
            }
        };
        Recorder.getInstance(SpinActivity.this).setRecorderUpdateListener(recorderUpdateListener);
        startTest();
        setVolumeControlStream(3);

    }

    private int getDelay(int i) {
        double tmp1,tmp2;
        if (i>=userInitialValue)
            return 1;
        tmp2= ((double)i/(double)userInitialValue);
        tmp1= (1.0-(tmp2))*400.0;
        return (int)tmp1;
    }

    private void startTest() {

        Recorder.getInstance(SpinActivity.this).startRecorder();
        dbHandler.postDelayed(mUpdateTimer, TIME_BETWEEN_SAMPLE);
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
        m_handler.removeCallbacks(m_handlerTask);
        dbHandler.removeCallbacks(mUpdateTimer);
        Recorder.getInstance(SpinActivity.this).RecorderRel();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    private void initializeVariables() {
        imgview = (ImageView) findViewById(R.id.imageView1);
    }
    void alertDialog(String message)
    {
        new AlertDialog.Builder(this)
                .setTitle("Initial Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SpinActivity.this, InitActivity.class);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
