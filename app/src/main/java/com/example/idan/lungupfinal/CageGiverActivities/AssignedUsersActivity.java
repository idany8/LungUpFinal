package com.example.idan.lungupfinal.CageGiverActivities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.idan.lungupfinal.Adapters.MyAdapter;
import com.example.idan.lungupfinal.Classes.User;
import com.example.idan.lungupfinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssignedUsersActivity extends Activity {
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<User> testUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_users);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        testUsers = new ArrayList<>();
        FirebaseAuth mAuthLoggedUser = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuthLoggedUser.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User loggedUser= dataSnapshot.getValue(User.class);
                final ArrayList<String> loggedUsrRu = loggedUser.getRelatedUsers();
                FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot child: dataSnapshot.getChildren()){
                            if(loggedUsrRu.contains(child.getKey())){
                                Log.d("testOmer",child.getKey());
                                testUsers.add(child.getValue(User.class));
                            }
                        }

                        Log.d("userslisttest2",""+testUsers);
                        mAdapter = new MyAdapter(testUsers,AssignedUsersActivity.this);
                        recyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //Patient xx = new Patient();
                //FirebaseDatabase.getInstance().getReference().child("users").child(mAuth1.getCurrentUser().getUid()).setValue(user);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
    }
}