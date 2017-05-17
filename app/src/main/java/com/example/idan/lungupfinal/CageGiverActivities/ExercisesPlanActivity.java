package com.example.idan.lungupfinal.CageGiverActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.idan.lungupfinal.Classes.P_Exercise;
import com.example.idan.lungupfinal.Classes.Patient;
import com.example.idan.lungupfinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExercisesPlanActivity extends AppCompatActivity {
    public final static int REQ_CODE_OK = 1;
    public final static int REQ_CODE_CANCEL = 3;
private String patUid;
    ExerciseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_plan);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ex_recycler_view);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                patUid = null;
                Log.d("checkerror", "its null");
            } else {
                patUid = extras.getString("PATIENT_UID");
            }
        }
//

        FirebaseAuth mAuthLoggedUser = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference().child("users").child(patUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient loggedPat = dataSnapshot.getValue(Patient.class);
                if (loggedPat.getP_exercises()!=null) {
                    ArrayList<P_Exercise> loggedUsrEL = loggedPat.getP_exercises();
                    // for (P_Exercise ex : loggedUsrEL)
                    // exercisesArray.add(ex);
                    adapter = new ExerciseAdapter(loggedUsrEL,loggedPat);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);

                }else {
                    Log.d("checkerror", "nothing in user EL ");
                    ArrayList<P_Exercise> loggedUsrEL = new ArrayList<P_Exercise>();
                    adapter = new ExerciseAdapter(loggedUsrEL,loggedPat);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                }
//
//                Log.d("userslisttest2", "" + exercisesArray);
//                mAdapter = new ExercisesAdapter(exercisesArray,ExercisesPlanActivity.this);
//                recyclerView.setAdapter(mAdapter);

                //final MoviesAdapter adapter = new MoviesAdapter(getMovies());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });




//
//        //final MoviesAdapter adapter = new MoviesAdapter(getMovies());
//        final ExerciseAdapter adapter = new ExerciseAdapter()
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);
        findViewById(R.id.ex_save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.saveToDB();
            }
        });
        findViewById(R.id.add_row_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent child = new Intent(ExercisesPlanActivity.this, ExerciseSchedule.class);
                startActivityForResult(child, REQ_CODE_OK);

//                P_Exercise p_ex1 = new P_Exercise(54545,"normal","the best","this is the best",null,"me","fdsfds43",false,"S2M2T2W2T2F2S2");
//                adapter.addItem(p_ex1);
            }
        });

    }
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode==1) {
            if (resultCode == REQ_CODE_OK) {
                P_Exercise returnedExercise = (P_Exercise) data.getExtras().getSerializable("SCHEDULED_EXERCISE");
                adapter.addItem(returnedExercise);
            }
            if (resultCode == REQ_CODE_CANCEL) {
                Log.d("oaoa", "in");
            }

        }

    }



    public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.MyViewHolder> {

        private ArrayList<P_Exercise> pexList;
        private Patient patient= new Patient();

        public void saveToDB() {

            patient.setP_exercises(pexList);
            FirebaseDatabase.getInstance().getReference().child("users").child(patient.getUid()).setValue(patient);


        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, year, genre;
            public View deleteBtn;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                genre = (TextView) view.findViewById(R.id.genre);
                year = (TextView) view.findViewById(R.id.year);
                deleteBtn = view.findViewById(R.id.delete_btn);
            }
        }


        public ExerciseAdapter(ArrayList<P_Exercise> pexList, Patient patient) {
            this.pexList = pexList;
            this.patient = patient;

        }

        public void addItem(P_Exercise p_ex) {
            pexList.add(p_ex);
            notifyItemInserted(pexList.size() - 1);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            P_Exercise pex = pexList.get(position);
            holder.title.setText(pex.getExercise_name());

            holder.genre.setText(pex.getDescription());
            holder.year.setText(pex.getSchedule());
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("delete", ""+ position);
                    pexList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                }
            });
        }

        @Override
        public int getItemCount() {
            return pexList.size();
        }
    }
}
