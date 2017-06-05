package com.example.idan.lungupfinal.Chat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.idan.lungupfinal.AllUsersActivities.LoginActivity;
import com.example.idan.lungupfinal.Classes.Patient;
import com.example.idan.lungupfinal.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.text.SimpleDateFormat;

public class ChatListActivity extends AppCompatActivity {

    public static final String CHATS = "chats/";
    private RecyclerView chatRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference ref;
    private FirebaseAuth mAuthLoggedUser;
    private TextView mUsrName;

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView chatName;
        public LinearLayout chatLayout;
        public TextView lastMessage;
        public TextView timeStamp;

        public ChatViewHolder(View itemView) {
            super(itemView);
            chatName = (TextView) itemView.findViewById(R.id.chat_name);
            chatLayout = (LinearLayout) itemView.findViewById(R.id.chat_layout);
            lastMessage = (TextView)itemView.findViewById(R.id.chat_last_message);
            timeStamp = (TextView)itemView.findViewById(R.id.chat_time_stamp);
        }
    }

    private FirebaseRecyclerAdapter<Chat, ChatViewHolder> firebaseRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        chatRecyclerView = (RecyclerView) findViewById(R.id.chat_list_recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        //emptyView = (LinearLayout)view.findViewById(R.id.empty_view);

        mAuthLoggedUser = FirebaseAuth.getInstance();
        mUsrName= (TextView)findViewById(R.id.user_name_tv);

        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mAuthLoggedUser.signOut();
                Intent intent = new Intent(ChatListActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuthLoggedUser.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient loggedPat = dataSnapshot.getValue(Patient.class);
                mUsrName.setText(loggedPat.getName());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        ref = FirebaseDatabase.getInstance().getReference();



        DatabaseReference userRef = ref.child(CHATS + FirebaseAuth.getInstance().getCurrentUser().getUid());



        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Chat, ChatViewHolder>(
                Chat.class,
                R.layout.chat_template,
                ChatViewHolder.class,
                userRef.orderByChild("timeStamp")) {
            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, Chat model, final int position) {
                final String key = firebaseRecyclerAdapter.getRef(position).getKey();
                viewHolder.chatName.setText(model.getName());
                if(model.getTimeStamp()!=0) {
                    viewHolder.timeStamp.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(model.getTimeStamp()));
                }else
                    viewHolder.timeStamp.setText("");
                viewHolder.lastMessage.setText(model.getLastMessage());
                viewHolder.chatLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accessChat(key);
                    }
                });



            }
        };

        chatRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .marginResId(R.dimen.chat_divider_left,R.dimen.chat_divider_right)
                .size(1)
                .color(R.color.iron)
                .build());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
        chatRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    private void accessChat(String key) {
        Intent i = new Intent(ChatListActivity.this, ChatActivity.class);
        i.putExtra("chatId", key);
        startActivity(i);
    }
}
