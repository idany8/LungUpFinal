package com.example.idan.lungupfinal.soundmeter;

import android.content.Context;
import android.media.MediaRecorder;
import android.widget.Toast;

import com.example.idan.lungupfinal.Classes.MySharedPreferences;
import com.example.idan.lungupfinal.R;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class Recorder {


    private IRecorderUpdateListener mRecorderUpdateListener;
    private MediaRecorder mMediaRecorder = null;
    private Context mContext;

    private static Recorder mRecorder;

    public static Recorder getInstance(Context context) {
        if (mRecorder == null) {
            mRecorder = new Recorder(context);
        }

        mRecorder.setContext(context);
        return mRecorder;
    }

    private Recorder(Context applicationContext) {
        mContext = applicationContext;
    }

    public void setRecorderUpdateListener(IRecorderUpdateListener recorderUpdateListener) {
        mRecorderUpdateListener = recorderUpdateListener;
    }

    private void RecorderErr()
    {
        mMediaRecorder = null;
        Toast.makeText(mContext, mContext.getString(R.string.msg_mic_error), Toast.LENGTH_LONG).show();
    }

    public void startRecorder() {
        if (mMediaRecorder != null)
            return;

        try
        {
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(1);
            mMediaRecorder.setOutputFormat(1);
            mMediaRecorder.setAudioEncoder(1);
            mMediaRecorder.setOutputFile("/dev/null");
            mMediaRecorder.prepare();
            mMediaRecorder.start();

        }
        catch (IllegalStateException e) {
            e.printStackTrace();
            RecorderErr();
        }
        catch (IOException e) {
            e.printStackTrace();
            RecorderErr();
        }
        catch (Exception e) {
            e.printStackTrace();
            RecorderErr();
        }
    }

    public void RecorderRel() {

        if (mMediaRecorder != null) {
            try {
                mMediaRecorder.stop();
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
            catch (IllegalStateException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void SoundDB()
    {
        float f1 = mMediaRecorder.getMaxAmplitude();
        float f2 = 0;


        if (f1 > 0.0F)
        {
            f2 =f1;
        }

        if (mRecorderUpdateListener != null) {
            mRecorderUpdateListener.onSoundUpdate(f2);
        }
    }

    public void setContext(Context context) {
        mContext = context;
    }


    public void saveMicInitialValue(float average, float max) {
        MySharedPreferences msp = new MySharedPreferences(mContext);

        float val;
        if (max  > 32000)
            val= max-8000;
        else {
            if (max + 8000 > 32000) {
                val=  average - 8000;
            } else
                val=  max + 8000;
        }

        msp.putIntIntoSharedPrefernces("MIC_INIT_VALUE",(int)val);
        msp.putLongIntoSharedPrefernces("MIC_INIT_DATE",System.currentTimeMillis());

    }

    public void saveData(float average, float max, int score) {
        MySharedPreferences msp = new MySharedPreferences(mContext);

        if (score == -9999) {
            //msp.putIntIntoSharedPrefernces("MIC_INIT",);
            for (int i = 2; i <= 5; i++) {
                msp.putStringIntoSharedPrefernces("rec" + (i - 1), msp.getStringFromSharedPrefernces("rec" + (i), "na"));
            }

            String s = "Date: " + DateFormat.getDateTimeInstance().format(new Date()) + "\nAverage: " + (int) average + "        Max: " + (int) max;
            msp.putStringIntoSharedPrefernces("rec5", s);

        }
        else {
            int tmpHighScore = msp.getIntFromSharedPrefernces("highScore",0);
            if ( (score > tmpHighScore) || (tmpHighScore==0) )
                msp.putIntIntoSharedPrefernces("highScore",score);
        }

    }



}