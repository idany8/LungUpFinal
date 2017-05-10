package com.example.idan.lungupfinal;

import android.app.Application;
import android.util.Log;

/**
 * Created by Idan on 23/04/2017.
 */

public class App extends Application {

    private int mCreateAccountCounter;

    @Override
    public void onCreate() {
        super.onCreate();
        mCreateAccountCounter = 0;
        Log.d("increase:",""+mCreateAccountCounter );
    }

    public int getCreateAccountCounter() {
        return mCreateAccountCounter;
    }

    public void increaseCreateAccountCounter() {
        mCreateAccountCounter++;
        Log.d("increase:","+1 "+mCreateAccountCounter );
    }
}
