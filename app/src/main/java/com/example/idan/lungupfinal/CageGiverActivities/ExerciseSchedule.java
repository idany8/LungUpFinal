package com.example.idan.lungupfinal.CageGiverActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import com.example.idan.lungupfinal.Classes.P_Exercise;
import com.example.idan.lungupfinal.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExerciseSchedule extends AppCompatActivity {
    String[] items;

    ArrayList<P_Exercise> listItems;
    ArrayAdapter<P_Exercise> adapter;
    ListView listView;
    EditText searchEt;
    ArrayList<P_Exercise> exercisesList ;
    Button updateBtn;
    CheckBox cbUnlimited;
    P_Exercise selectedExercise;
    EditText et_su,et_mo,et_tu,et_we,et_th,et_fr,et_sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_schedule);

        listView=(ListView)findViewById(R.id.set_schedule_listview);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("listview", ""+ adapter.getItem(i));
                selectedExercise = adapter.getItem(i);

            }
        });

        findViewById(R.id.btn_sch_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( (validateDays("empty")) || (validateDays("unlimited")) ) {
                    int resultCode = 1;
                    Intent resultIntent = new Intent();
                    //P_Exercise pExToReturn = new P_Exercise(54545, "normal", "returned", "returned desc", null, "me", "fdsfds43", false, getSchedule());
                    if (selectedExercise==null) return;
                    selectedExercise.setSchedule(getSchedule());
                    resultIntent.putExtra("SCHEDULED_EXERCISE", selectedExercise);
                    setResult(resultCode, resultIntent);
                    finish();
                }
            }
        });
        cbUnlimited = (CheckBox) findViewById(R.id.checkBox_sch_unlimited);
        cbUnlimited.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbUnlimited.isChecked()){
                    setDaysUnlimited(true);
                } else setDaysUnlimited(false);
            }
        });


        searchEt=(EditText)findViewById(R.id.set_schedule_txtsearch);

        initList();
        initEtDays();


        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int
                    after) {
            }
            @Override

            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {
                if(s.toString().equals("")){
                    // reset listview
                    initList();
                }
                else{
                    // perform search
                    searchItem(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                            }
        });

    }

    public void onBackPressed(){
        int resultCode = 3;
        Intent resultIntent = new Intent();
        setResult(resultCode, resultIntent);
        finish();

    }


    public void searchItem(String textToSearch) {
       // for (String item : listItems) {
            for (int i = 0; i < listItems.size(); i++) {
                if (!listItems.get(i).getExercise_name().contains(textToSearch)) {
                    listItems.remove(i);
                }
            }
            adapter.notifyDataSetChanged();
        }
   // }
    public void initList(){

       // items=new String[]{"Canada","China","Japan","USA"};

      //  listItems=new ArrayList<>(Arrays.asList(items));

        FirebaseDatabase.getInstance().getReference().child("exercises").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                exercisesList = new ArrayList<P_Exercise>();
                for(DataSnapshot child: dataSnapshot.getChildren()) {
                    exercisesList.add(child.getValue(P_Exercise.class));
                }
                Log.d("exlist", ""+exercisesList);

                adapter=new ArrayAdapter<P_Exercise>(ExerciseSchedule.this,
                        R.layout.list_item, R.id.txtitem, exercisesList);


                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }



        });

        listItems=new ArrayList<P_Exercise>();
        listItems.add(new P_Exercise(2332,"sdsdd","exercisename1","Description1","","authorName1","fdsfsf",false,"2uduasu2"));
        listItems.add(new P_Exercise(4546512,"sdsdd","exercisename2","Description2","","authorName2","fdsfsf",false,"2uduasu2"));
        listItems.add(new P_Exercise(454532,"sdsdd","exercisename3","Description3","","authorName3","fdsfsf",false,"2uduasu2"));
        listItems.add(new P_Exercise(89452,"sdsdd","exercisename4","Description4","","authorName4","fdsfsf",false,"2uduasu2"));
        listItems.add(new P_Exercise(74512,"sdsdd","exercisename5","Description5","","authorName5","fdsfsf",false,"2uduasu2"));



//        adapter=new ArrayAdapter<P_Exercise>(this,
//                R.layout.list_item, R.id.txtitem, exercisesList);
//
//
//        listView.setAdapter(adapter);




    }
    public void initEtDays(){
        et_su = (EditText) findViewById(R.id.et_sch_su);
        et_mo = (EditText) findViewById(R.id.et_sch_mo);
        et_tu = (EditText) findViewById(R.id.et_sch_tu);
        et_we = (EditText) findViewById(R.id.et_sch_we);
        et_th = (EditText) findViewById(R.id.et_sch_th);
        et_fr = (EditText) findViewById(R.id.et_sch_fr);
        et_sa = (EditText) findViewById(R.id.et_sch_sa);

        et_su.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_su.getText().toString().length()==1) et_mo.requestFocus();
            }
            @Override public void afterTextChanged(Editable editable) {}
        });
        et_mo.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_mo.getText().toString().length()==1) et_tu.requestFocus();
            }
            @Override public void afterTextChanged(Editable editable) {}
        });
        et_tu.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_tu.getText().toString().length()==1) et_we.requestFocus();
            }
            @Override public void afterTextChanged(Editable editable) {}
        });
        et_we.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_we.getText().toString().length()==1) et_th.requestFocus();
            }
            @Override public void afterTextChanged(Editable editable) {}
        });
        et_th.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_th.getText().toString().length()==1) et_fr.requestFocus();
            }
            @Override public void afterTextChanged(Editable editable) {}
        });
        et_fr.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_fr.getText().toString().length()==1) et_sa.requestFocus();
            }
            @Override public void afterTextChanged(Editable editable) {}
        });
        et_sa.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (et_sa.getText().toString().length()==1) {
                    if (et_mo.getText().toString().length()==0) et_mo.requestFocus();
                    else if (et_tu.getText().toString().length()==0) et_tu.requestFocus();
                    else if (et_tu.getText().toString().length()==0) et_tu.requestFocus();
                    else if(et_we.getText().toString().length()==0) et_we.requestFocus();
                    else if(et_th.getText().toString().length()==0) et_th.requestFocus();
                    else if(et_fr.getText().toString().length()==0) et_fr.requestFocus();
                }
            }
            @Override public void afterTextChanged(Editable editable) {}
        });

    }
    private boolean validateDays(String type) {
        boolean valid = true;
        if (type.equals("empty")) {
            if (TextUtils.isEmpty(et_su.getText().toString())) {
                et_su.setError("Required");
                valid = false;
            }
            if (TextUtils.isEmpty(et_mo.getText().toString())) {
                et_mo.setError("Required");
                valid = false;
            }
            if (TextUtils.isEmpty(et_tu.getText().toString())) {
                et_tu.setError("Required");
                valid = false;
            }
            if (TextUtils.isEmpty(et_we.getText().toString())) {
                et_we.setError("Required");
                valid = false;
            }
            if (TextUtils.isEmpty(et_th.getText().toString())) {
                et_th.setError("Required");
                valid = false;
            }
            if (TextUtils.isEmpty(et_fr.getText().toString())) {
                et_fr.setError("Required");
                valid = false;
            }
            if (TextUtils.isEmpty(et_sa.getText().toString())) {
                et_sa.setError("Required");
                valid = false;

            }
        }else if (type.equals("unlimited")){
            if (!et_su.getText().toString().equals("U")) {
                valid = false;
            }
            if (!et_mo.getText().toString().equals("U")) {
                valid = false;
            }
            if (!et_tu.getText().toString().equals("U")) {
                valid = false;
            }
            if (!et_we.getText().toString().equals("U")) {
                valid = false;
            }
            if (!et_th.getText().toString().equals("U")) {
                valid = false;
            }
            if (!et_fr.getText().toString().equals("U")) {
                valid = false;
            }
            if (!et_sa.getText().toString().equals("U")) {
                valid = false;
            }
        }
        Log.d("checkdays",""+valid);
        return valid;
    }
    private void setDaysUnlimited(boolean flag) {
        if (flag) {
            et_su.setError(null);
            et_mo.setError(null);
            et_tu.setError(null);
            et_we.setError(null);
            et_th.setError(null);
            et_fr.setError(null);
            et_sa.setError(null);
            et_su.setText("U");
            et_mo.setText("U");
            et_tu.setText("U");
            et_we.setText("U");
            et_th.setText("U");
            et_fr.setText("U");
            et_sa.setText("U");
            et_su.setEnabled(false);
            et_mo.setEnabled(false);
            et_tu.setEnabled(false);
            et_we.setEnabled(false);
            et_th.setEnabled(false);
            et_fr.setEnabled(false);
            et_sa.setEnabled(false);
        }else{
            et_su.setEnabled(true);
            et_mo.setEnabled(true);
            et_tu.setEnabled(true);
            et_we.setEnabled(true);
            et_th.setEnabled(true);
            et_fr.setEnabled(true);
            et_sa.setEnabled(true);
            et_su.setText("");
            et_mo.setText("");
            et_tu.setText("");
            et_we.setText("");
            et_th.setText("");
            et_fr.setText("");
            et_sa.setText("");
            et_su.requestFocus();
        }
    }
    private String getSchedule(){
        String sched=
                "S"+et_su.getText().toString()
                        +"M"+et_mo.getText().toString()
                            +"T"+et_tu.getText().toString()
                                +"W"+et_we.getText().toString()
                                    +"T"+et_th.getText().toString()
                                        +"F"+et_fr.getText().toString()
                                            +"S"+et_sa.getText().toString();

        return sched;

    }



}
