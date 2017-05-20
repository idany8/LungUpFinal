package com.example.idan.lungupfinal.Chat;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.idan.lungupfinal.Classes.Notification;
import com.example.idan.lungupfinal.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    public static final String CHATS = "chats/";
    private RecyclerView messageRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference ref;
    private EditText textMessage;
    private FloatingActionButton sendButton;
    private String chatId;
    private String currentUserName;
    private String currentRecevierId;
    private String receiverToken=null;



    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        //public TextView sender;
        public TextView timeStamp;
        public LinearLayout messageLayout;

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.message_text);
            //sender= (TextView)itemView.findViewById(R.id.sender);
            timeStamp = (TextView) itemView.findViewById(R.id.time_stamp);
            messageLayout = (LinearLayout) itemView.findViewById(R.id.message_layout);

        }
    }

    private void getCurrentUserName() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("name");
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUserName = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private FirebaseRecyclerAdapter<Message, MessageViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        textMessage = (EditText) findViewById(R.id.messageEditText);
        sendButton = (FloatingActionButton) findViewById(R.id.sendMessageButton);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            chatId = extras.getString("chatId");
            currentRecevierId = chatId;
        }
        getCurrentUserName();
        ref = FirebaseDatabase.getInstance().getReference();

        //get data from activity
        //getRecieverToken(chatId);

//        FirebaseDatabase.getInstance().getReference().child("users").child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                setTitle("Chat with " + ((String) dataSnapshot.child("name").getValue()).split(" ")[0]);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        FirebaseDatabase.getInstance().getReference().child("users").child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("token"))
                    receiverToken = dataSnapshot.child("token").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messageRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);

        String chatRef = CHATS + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + chatId + "/messages";
        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.message_template,
                MessageViewHolder.class,
                ref.child(chatRef)) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                viewHolder.messageText.setText(model.getMessage());


                if (model.getSenderId().equals(currentUserId)) {
                    viewHolder.messageText.setBackgroundResource(R.drawable.bubble_in);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.messageLayout.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    viewHolder.messageLayout.setLayoutParams(params);

                } else {
                    viewHolder.messageText.setBackgroundResource(R.drawable.bubble_out);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.messageLayout.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    viewHolder.messageLayout.setLayoutParams(params);


                }


                //viewHolder.sender.setText(model.getSender());
                viewHolder.timeStamp.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(model.getTime()));
            }

        };
        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int chatCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 || (positionStart >= (chatCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    messageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
        messageRecyclerView.setLayoutManager(linearLayoutManager);
        messageRecyclerView.setAdapter(firebaseRecyclerAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!textMessage.getText().toString().equals("")) {
                    //String replaced = textMessage.getText().toString().replaceAll("\n","\\n");
                    sendMessage(textMessage.getText().toString());
                    textMessage.setText("");
                }
            }
        });

    }

    private void sendMessage(String messageText) {
        String senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference().child("chats").child(senderId).child(currentRecevierId);
        DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference().child("chats").child(currentRecevierId).child(senderId);
        if (receiverToken!=null)
            FirebaseDatabase.getInstance().getReference().child("notifications").push().setValue(new Notification(currentUserName,messageText,receiverToken));

        String key = senderRef.push().getKey();
        Message message = new Message(messageText, currentUserName, senderId);
//        senderRef.child("messages").child(key).setValue(message);
//        receiverRef.child("messages").child(key).setValue(message);
//        senderRef.child("lastMessage").setValue(messageText);
//        receiverRef.child("lastMessage").setValue(messageText);
//        senderRef.child("timeStamp").setValue(message.getTime());
//        receiverRef.child("timeStamp").setValue(message.getTime());
        Map senderFanOut = new HashMap();
        Map receiverFanOut = new HashMap();
        senderFanOut.put("/messages/" + key,message);
        senderFanOut.put("/lastMessage",messageText);
        senderFanOut.put("/timeStamp",message.getTime());
        receiverFanOut.put("/messages/" + key,message);
        receiverFanOut.put("/lastMessage",messageText);
        receiverFanOut.put("/timeStamp",message.getTime());

        senderRef.updateChildren(senderFanOut);
        receiverRef.updateChildren(receiverFanOut);

    }



}
