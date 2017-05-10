package com.example.idan.lungupfinal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.idan.lungupfinal.Adapters.ExercisesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExercisesPlanActivity extends Activity {
    private RecyclerView recyclerView;
    private ExercisesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<P_Exercise> exercisesArray;
    private String patUid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_plan);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                patUid = null;
            } else {
                patUid = extras.getString("PATIENT_UID");
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.ex_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        // Use the default animator
        // recyclerView.setItemAnimator(new DefaultItemAnimator());
        // you could add item decorators
        //	RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        //	recyclerView.addItemDecoration(itemDecoration);

//        ArrayList<String> values = new ArrayList<String>();
//        for (int i = 0; i < 100; i++) {
//            values.add("Test" + i);
//        }
        //mycode
        exercisesArray = new ArrayList<P_Exercise>();
        FirebaseAuth mAuthLoggedUser = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference().child("users").child(patUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient loggedPat = dataSnapshot.getValue(Patient.class);
                if (loggedPat.getP_exercises()!=null) {
                    ArrayList<P_Exercise> loggedUsrEL = loggedPat.getP_exercises();
                   // for (P_Exercise ex : loggedUsrEL)
                       // exercisesArray.add(ex);
                }
                P_Exercise ex1 = new P_Exercise(456454,"Custom","Exercise1", "Description of exercise1",null,"Dr. Cohen","fdhjsf43f",false,"Unlimited");
                P_Exercise ex2 = new P_Exercise(456454,"Custom","Exercise2", "Description of exercise2",null,"Dr. Cohen","fdhjsf43f",false,"2 times every day");
                P_Exercise ex3 = new P_Exercise(456454,"Custom","Exercise3", "Description of exercise3",null,"Dr. Cohen","fdhjsf43f",false,"Once a week");
                P_Exercise ex4 = new P_Exercise(456454,"Custom","Exercise4", "Description of exercise4",null,"Dr. Cohen","fdhjsf43f",false,"Unlimited");

                exercisesArray.add(ex1);
                exercisesArray.add(ex2);
                exercisesArray.add(ex3);
                exercisesArray.add(ex4);
                Log.d("userslisttest2", "" + exercisesArray);
                mAdapter = new ExercisesAdapter(exercisesArray,ExercisesPlanActivity.this);
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

                //Patient xx = new Patient();
                //FirebaseDatabase.getInstance().getReference().child("users").child(mAuth1.getCurrentUser().getUid()).setValue(user);




        //end

//        User first = new User("aks@dd.com","Idan y", "PT",false,null);
//        User sec = new User("ssdf@dd.com","Moshe ga", "PT",false,null);
//
//        ArrayList<User> values= new ArrayList<User>();
//        values.add(first);
//        values.add(sec);
        // specify an adapter (see also next example)
//        mAdapter = new MyAdapter(values);
//        recyclerView.setAdapter(mAdapter);





