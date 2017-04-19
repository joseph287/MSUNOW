package com.example.rasen.msunow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rasen.msunow.InputTopic.Topic;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RoomPage extends AppCompatActivity {

    private ArrayList<String> topics;
    private String room;
    DatabaseReference myRef;
    FirebaseDatabase database;
    ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_page);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("topic");

        room = getIntent().getStringExtra("ROOM");

        TextView tvs = (TextView) findViewById(R.id.rp_tvs);
        tvs.setText(tvs.getText()+room+" ");

        TextView tvt = (TextView) findViewById(R.id.rp_tvt);
        tvt.setText(tvt.getText()+room+" ");

        loadUI();
    }

    private void loadUI() {
        AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.rp_searchbox);

        Button searchbtn = (Button) findViewById(R.id.rp_searchbtn);

        Spinner trendSpin = (Spinner) findViewById(R.id.rp_trending_spinner);
        String[] trendTimes = {"Past Hour", "Past Day", "Past Week", "Past Month", "All Time"};
        ArrayAdapter<String> spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, trendTimes);
        trendSpin.setAdapter(spAdapter);

        ListView lvTrend = (ListView) findViewById(R.id.rp_listview_trending);
        final String[] exampleTrend = {"TEST1", "TEST2", "TEST3", "TEST4", "TEST5"};
        ArrayAdapter<String> tAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exampleTrend);
        lvTrend.setAdapter(tAdapter);
        lvTrend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(RoomPage.this, "TODO: Move to topic", Toast.LENGTH_SHORT).show();
            }
        });

        ListView lvTopics = (ListView) findViewById(R.id.rp_listview_topics);
        topics = new ArrayList<>();
        final ArrayAdapter<String> topAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, topics);
        lvTopics.setAdapter(topAdapter);
        lvTopics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(RoomPage.this, "This is : "+topics.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Topic topic = dataSnapshot.getValue(Topic.class);
                if(topic.getRoom().equals(room))
                    topics.add(topic.getTitle());
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
