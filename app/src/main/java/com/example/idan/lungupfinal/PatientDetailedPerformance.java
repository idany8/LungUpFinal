package com.example.idan.lungupfinal;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class PatientDetailedPerformance extends AppCompatActivity {

    LineChart lineChart;
    BarChart barChart;
    private String patUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detailed_performance);


        FirebaseAuth mAuthLoggedUser = FirebaseAuth.getInstance();
        Log.d("starting App", "i131232132");

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                patUid = mAuthLoggedUser.getCurrentUser().getUid();
                Log.d("starting App", "get patUid from fb");
            } else {
                patUid = extras.getString("PATIENT_UID");
            }
        }

        getPatientPExercisesList();

        //initLineChart();
        //initBarChart();
    }

    private void getPatientPExercisesList() {


        FirebaseDatabase.getInstance().getReference().child("users").child(patUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient loggedPat = dataSnapshot.getValue(Patient.class);
                if (loggedPat.getP_exercises() != null) {
                    ArrayList<P_Exercise> patientPExercises = loggedPat.getP_exercises();
                    ArrayList<PerfUnit> arrPatLU = getAllRecords(patientPExercises);
                    if (arrPatLU.size()>0)
                        createChartsArrays(arrPatLU);

                    //initLineChart();




                } else {
                    Log.d("checkerror", "nothing in user EL ");
                    ArrayList<P_Exercise> loggedUsrEL = new ArrayList<P_Exercise>();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void createChartsArrays(ArrayList<PerfUnit> arrPatLU) {

        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat fmtToChart = new SimpleDateFormat("dd/MM");
        Calendar cal = Calendar.getInstance();
        Date d = new Date(arrPatLU.get(0).getTime());
        cal.setTime(d);
        float tmpCount=0,tmpScore=0;
        Date tmp_d=null;
        int dayNumber=0;
        ArrayList<Entry> scoresEntryList = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i=0;i<arrPatLU.size();i++){
            Calendar tmpCal = Calendar.getInstance();
            tmp_d = new Date(arrPatLU.get(i).getTime());
            tmpCal.setTime(tmp_d);

            if (fmt.format(d).equals(fmt.format(tmp_d))){
                tmpCount++;
                tmpScore+=tmpScore;

            }else {
                //savetmps;
                scoresEntryList.add(new Entry((tmpScore/tmpCount), dayNumber));
                xVals.add(dayNumber, String.valueOf(fmtToChart.format(cal.getTime())));
                entries.add(new BarEntry(tmpCount, dayNumber));
                dayNumber++;
                Log.d("daysstamps", "count"+ tmpCount + " score" + tmpScore + " day" + fmt.format(cal.getTime()));
                tmpCount = 0;
                tmpScore = 0;
                cal.add(Calendar.DATE, -1);
                d = cal.getTime();
                while (!(fmt.format(d).equals(fmt.format(tmp_d)))) {
                    scoresEntryList.add(new Entry((tmpScore/tmpCount), dayNumber));
                    xVals.add(dayNumber, String.valueOf(fmtToChart.format(cal.getTime())));
                    entries.add(new BarEntry(tmpCount, dayNumber));
                    dayNumber++;
                    Log.d("daysstamps", "count" + tmpCount + " score" + tmpScore + " day" + fmt.format(cal.getTime()));
                    cal.add(Calendar.DATE, -1);
                    d = cal.getTime();
                }
//                if (!(fmt.format(d).equals(fmt.format(tmp_d)))) {
//                    //savetmps;
//                    Log.d("daysstamps", "count"+ tmpCount + " score" + tmpCount + " day" + fmt.format(cal.getTime()));
//                }
                    tmpCount++;
                    tmpScore+=tmpScore;
            }
        }
        scoresEntryList.add(new Entry((tmpScore/tmpCount), dayNumber));
        xVals.add(dayNumber, String.valueOf(fmtToChart.format(cal.getTime())));
        entries.add(new BarEntry(tmpCount, dayNumber));
        dayNumber++;
        Log.d("daysstamps", "count"+ tmpCount + " score" + tmpScore + " day" + fmt.format(tmp_d));


        initLineChart(scoresEntryList,xVals);
        initBarChart(entries,xVals);

//        lineChart = (LineChart) findViewById(R.id.det_per_lineChart);
//        LineDataSet set = new LineDataSet(scoresEntryList, "Usage");
//        set.setColor(Color.BLACK);
//        LineData data = new LineData(xScoresVals, set);
//
//        data.setValueTextSize(9f);
//        data.setDrawValues(false);
//        lineChart.setData(data);
//
//        lineChart.setVisibleXRangeMaximum(10);
    }

    private void initLineChart(ArrayList<Entry> scoresEntryList,ArrayList<String> xVals) {
        lineChart = (LineChart) findViewById(R.id.det_per_lineChart);


//        ArrayList<Entry> scoresEntryList = new ArrayList<>();
//        ArrayList<String> xVals = new ArrayList<>();


//        for (int i = 0; i < 30; i++) {
//            float x = i % 3;
//            scoresEntryList.add(new Entry(x, i));
//            xVals.add(i, String.valueOf(i) + ".12");
//        }

        LineDataSet set = new LineDataSet(scoresEntryList, "Usage");
        set.setColor(Color.BLACK);
        LineData data = new LineData(xVals, set);

        data.setValueTextSize(9f);
        data.setDrawValues(false);
        lineChart.setData(data);

        lineChart.setVisibleXRangeMaximum(10);


    }

    private void initBarChart(ArrayList<BarEntry> entries,ArrayList<String> xVals ) {
        barChart = (BarChart) findViewById(R.id.det_per_barchart);

//        ArrayList<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(3f, 0));
//        entries.add(new BarEntry(2f, 1));
//        entries.add(new BarEntry(0f, 2));
//        entries.add(new BarEntry(1f, 3));
//        entries.add(new BarEntry(3f, 4));
//        entries.add(new BarEntry(3, 5));

        BarDataSet dataset = new BarDataSet(entries, "Usage number");
        BarData data = new BarData(xVals, dataset);
        barChart.setData(data);
        barChart.setVisibleXRangeMaximum(10L);
    }

    private ArrayList<PerfUnit> getAllRecords(ArrayList<P_Exercise> patPExs) {
        //ArrayList<String> arrLU = new ArrayList<>();
        ArrayList<PerfUnit> arrLU = new ArrayList<PerfUnit>();
        for (int i = 0; i < patPExs.size(); i++) {
            arrLU.addAll(patPExs.get(i).getRecords());
        }

        Collections.sort(arrLU, new Comparator<PerfUnit>() {
            @Override
            public int compare(PerfUnit pf1, PerfUnit pf2) {
                return (int) (pf2.getTime() - pf1.getTime());
            }
        });
        return arrLU;

    }
}
