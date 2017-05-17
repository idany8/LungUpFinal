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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class PatientDetailedPerformance extends AppCompatActivity {

    LineChart lineChart;
    BarChart barChart;
    private String patUid;
    private TextView mTvHeader;
    private ListView exercisesLv;
    private ArrayAdapter<P_Exercise> listAdapter;
    ArrayList<P_Exercise> patientPExercises;
    ArrayList<PerfUnit> arrPatLU ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detailed_performance);
        lineChart = (LineChart) findViewById(R.id.det_per_lineChart);
        barChart = (BarChart) findViewById(R.id.det_per_barchart);
        mTvHeader = (TextView) findViewById(R.id.det_per_header);
        exercisesLv = (ListView) findViewById(R.id.det_per_lv);
        exercisesLv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lineChart.setVisibility(View.GONE);
        barChart.setVisibility(View.GONE);


        FirebaseAuth mAuthLoggedUser = FirebaseAuth.getInstance();

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


    }

    private void initListView(ArrayList<P_Exercise> listViewItems) {
        listViewItems.add(0,new P_Exercise(123,"ALL",""));

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<P_Exercise>(this, R.layout.list_item, R.id.txtitem, listViewItems);
        // Setup Adapter for ListActivity using in ListView
        exercisesLv.setAdapter(listAdapter);

       // ListView listView = getListView();
        // Setup listerner for clicks
        exercisesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String item = adapterView.getItemAtPosition(pos).toString();
                barChart.setVisibility(View.GONE);
                lineChart.setVisibility(View.GONE);

                if (pos==0){
                    createChartsArrays(arrPatLU);
                }else{



                ArrayList<PerfUnit> chosenPatLU = new ArrayList<PerfUnit>();

                for (int t=0;t<arrPatLU.size();t++)
                {
                    if (arrPatLU.get(t).getEx_name().equals(item)){
                        chosenPatLU.add(arrPatLU.get(t));
                    }
                }
                createChartsArrays(chosenPatLU);
                }

            }
        });

    }

    private void getPatientPExercisesList() {


        FirebaseDatabase.getInstance().getReference().child("users").child(patUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient loggedPat = dataSnapshot.getValue(Patient.class);
                mTvHeader.setText(loggedPat.getName() + " Performance");
                if (loggedPat.getP_exercises() != null) {
                    patientPExercises = loggedPat.getP_exercises();
                    initListView(patientPExercises);
                    arrPatLU = getAllRecords(patientPExercises);
                    if (arrPatLU.size()>0)
                        createChartsArrays(arrPatLU);
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
    }

    private void initLineChart(ArrayList<Entry> scoresEntryList,ArrayList<String> xVals) {
        LineDataSet set = new LineDataSet(scoresEntryList, "Usage");
        set.setColor(Color.BLACK);
        LineData data = new LineData(xVals, set);

        data.setValueTextSize(9f);
        data.setDrawValues(false);
        lineChart.setData(data);

        lineChart.setVisibleXRangeMaximum(10);
        lineChart.setVisibility(View.VISIBLE);
    }

    private void initBarChart(ArrayList<BarEntry> entries,ArrayList<String> xVals ) {
        BarDataSet dataset = new BarDataSet(entries, "Usage number");
        BarData data = new BarData(xVals, dataset);
        barChart.setData(data);
        barChart.setVisibleXRangeMaximum(10L);
        barChart.setVisibility(View.VISIBLE);
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
