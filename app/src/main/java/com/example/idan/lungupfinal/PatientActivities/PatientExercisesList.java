package com.example.idan.lungupfinal.PatientActivities;

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

public class PatientExercisesList extends AppCompatActivity {

    public final static int REQ_CODE_OK = 1;
    public final static int REQ_CODE_CANCEL = 3;
    private String patUid;
    ExerciseAdapterPat adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_exercises_list);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pat_el_recycler_view);

        FirebaseAuth mAuthLoggedUser = FirebaseAuth.getInstance();
        patUid =mAuthLoggedUser.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(patUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient loggedPat = dataSnapshot.getValue(Patient.class);
                if (loggedPat.getP_exercises()!=null) {
                    ArrayList<P_Exercise> loggedUsrEL = loggedPat.getP_exercises();
                    // for (P_Exercise ex : loggedUsrEL)
                    // exercisesArray.add(ex);

                    adapter = new ExerciseAdapterPat(loggedUsrEL,loggedPat);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);

                }else {
                    Log.d("checkerror", "nothing in user EL ");
                    ArrayList<P_Exercise> loggedUsrEL = new ArrayList<P_Exercise>();
                    adapter = new ExerciseAdapterPat(loggedUsrEL,loggedPat);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



    }
    public class ExerciseAdapterPat extends RecyclerView.Adapter<PatientExercisesList.ExerciseAdapterPat.MyViewHolder> {

        private ArrayList<P_Exercise> pexList;
        private Patient patient= new Patient();

        public void saveToDB() {

            patient.setP_exercises(pexList);
            FirebaseDatabase.getInstance().getReference().child("users").child(patient.getUid()).setValue(patient);


        }
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, year, genre;
            public View playBtn;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                genre = (TextView) view.findViewById(R.id.genre);
                year = (TextView) view.findViewById(R.id.year);
                playBtn = view.findViewById(R.id.btn_pat_el_play);
            }
        }

        public ExerciseAdapterPat(ArrayList<P_Exercise> pexList, Patient patient) {
            this.pexList = pexList;
            this.patient = patient;

        }

        public void addItem(P_Exercise p_ex) {
            pexList.add(p_ex);
            notifyItemInserted(pexList.size() - 1);
        }

        @Override
        public ExerciseAdapterPat.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_ex_line, parent, false);

            return new PatientExercisesList.ExerciseAdapterPat.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder,final int position) {

            P_Exercise pex = pexList.get(position);
            holder.title.setText(pex.getExercise_name());

            holder.genre.setText(pex.getDescription());
            holder.year.setText(pex.getSchedule());
            holder.playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("chosen", ""+ position);
                    Intent intent = new Intent(PatientExercisesList.this, PerformCustomExercise.class);
                    intent.putExtra("P_EXERCISE_TO_PERFORM", pexList.get(position).getId()); //second param is Serializable
                    startActivity(intent);

                    // BUG in the second time using this intent ************

//                    pexList.remove(position);
//                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position, getItemCount());
                }
            });
        }



        @Override
        public int getItemCount() {
            return pexList.size();
        }
    }
}
