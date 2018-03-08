package com.example.root.akuvo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CallActivity extends AppCompatActivity {
    EditText inputmsg;
    ImageButton send;
    DatabaseReference dbref;
    RecyclerView Mlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        inputmsg = (EditText) findViewById(R.id.inputmsg);
        send = (ImageButton) findViewById(R.id.send);
        dbref = FirebaseDatabase.getInstance().getReference().child("Messages");
        Mlist =(RecyclerView) findViewById(R.id.msgrec);
        Mlist.setHasFixedSize(true);
        LinearLayoutManager Lman = new LinearLayoutManager(this);
        Lman.setStackFromEnd(true);
        Mlist.setLayoutManager(Lman);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Msg = inputmsg.getText().toString().trim();
                if (!TextUtils.isEmpty(Msg)) {
                    DatabaseReference NewMsg = dbref.push();
                    NewMsg.child("content").setValue(Msg);

                }
                inputmsg.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Message,MessageHolderView> FBRA=new FirebaseRecyclerAdapter<Message, MessageHolderView>(
                Message.class,R.layout.call_msg,MessageHolderView.class,dbref
        ) {
            @Override
            protected void populateViewHolder(MessageHolderView viewHolder, Message model, int position) {
                viewHolder.setContent(model.getContent());
            }
        };
        Mlist.setAdapter(FBRA);
    }

    public static class MessageHolderView extends RecyclerView.ViewHolder {
        View Mview;

        public MessageHolderView(View itemView) {
            super(itemView);
            Mview = itemView;
        }

        public void setContent(String Msg) {
            TextView msgview = (TextView) Mview.findViewById(R.id.msg);
            msgview.setText(Msg);
        }
    }
}
