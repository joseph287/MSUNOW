package com.example.rasen.msunow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rasen.msunow.InputTopic.Topic;
import com.example.rasen.msunow.InputTopic.TopicAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class topics_listview extends AppCompatActivity {

    ListView list;
    TextView roomname;
    DatabaseReference myRef;
    FirebaseDatabase database;

    ArrayList<Topic> topic;
    ChildEventListener mChildEventListener;
    TopicAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics_listview);

        list = (ListView) findViewById(R.id.topics_list);
        roomname= (TextView) findViewById(R.id.room_name);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("topic");

        topic= new ArrayList<>();
        adapter= new TopicAdapter(this, R.layout.topic_list,topic);
        list.setAdapter(adapter);

        mChildEventListener= new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Topic topics= dataSnapshot.getValue(Topic.class);
                adapter.add(topics);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        myRef.addChildEventListener(mChildEventListener);

    }
}
