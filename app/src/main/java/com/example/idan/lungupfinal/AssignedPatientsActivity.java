package com.example.idan.lungupfinal;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.idan.lungupfinal.Adapters.CustomAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Shows a list that can be filtered in-place with a SearchView in non-iconified mode.
 */
public class AssignedPatientsActivity extends AppCompatActivity
         {

             //recyclerview objects
             private RecyclerView recyclerView;
             private RecyclerView.LayoutManager layoutManager;
             private RecyclerView.Adapter adapter;

             //model object for our list data
             private ArrayList<Patient> list;

             @Override
             protected void onCreate(Bundle savedInstanceState) {
                 super.onCreate(savedInstanceState);
                 setContentView(R.layout.activity_assigned_patients);

                 //initializing views
                 recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                 recyclerView.setHasFixedSize(true);
                 recyclerView.setLayoutManager(new LinearLayoutManager(this));

                 list = new ArrayList<Patient>();
                 loadRecyclerViewItem();
             }

             private void loadRecyclerViewItem() {
                 //you can fetch the data from server or some apis
                 //for this tutorial I am adding some dummy data directly
                 FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         User loggedUser= dataSnapshot.getValue(User.class);
                         final ArrayList<String> loggedUsrRu = loggedUser.getRelatedUsers();
                         FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot dataSnapshot) {
                                 for(DataSnapshot child: dataSnapshot.getChildren()){
                                     if(loggedUsrRu.contains(child.getKey())){
                                         //Log.d("testOmer",child.getKey());
                                         list.add(child.getValue(Patient.class));
                                     }
                                 }

//                                 Log.d("userslisttest2",""+testUsers);
//                                 mAdapter = new MyAdapter(testUsers);
//                                 recyclerView.setAdapter(mAdapter);

                                 adapter = new CustomAdapter(list,AssignedPatientsActivity.this);
                                 recyclerView.setAdapter(adapter);
                             }

                             @Override
                             public void onCancelled(DatabaseError databaseError) {

                             }
                         });

                        // /Patient xx = new Patient();
                         //FirebaseDatabase.getInstance().getReference().child("users").child(mAuth1.getCurrentUser().getUid()).setValue(user);
                     }


                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });



//
//                 for (int i = 1; i <= 5; i++) {
//                     MyList myList = new MyList(
//                             "Dummy Heading " + i,
//                             "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi molestie nisi dui."
//                     );
//                     list.add(myList);
//                 }

                 adapter = new CustomAdapter(list, this);
                 recyclerView.setAdapter(adapter);
             }



}