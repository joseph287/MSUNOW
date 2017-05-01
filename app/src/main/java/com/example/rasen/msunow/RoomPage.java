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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

public class RoomPage extends AppCompatActivity {

    private ArrayList<String> topics;
    private String room;
    DatabaseReference myRef;
    FirebaseDatabase database;
    ChildEventListener mChildEventListener;

    DatabaseReference myRef_post;
    FirebaseDatabase database_post;
    Button input;
    private ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_page);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("topic");
        room = getIntent().getStringExtra("ROOM");

        database_post= FirebaseDatabase.getInstance();
        myRef_post= database_post.getReference().child("post");

        input = (Button) findViewById(R.id.rp_input);
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RoomPage.this, UserInputActivity.class));
            }
        });

        TextView tvs = (TextView) findViewById(R.id.rp_tvs);
        tvs.setText(tvs.getText()+room+" ");

        TextView tvt = (TextView) findViewById(R.id.rp_tvt);
        tvt.setText(tvt.getText()+room+" ");

        loadUI();
    }


    private void loadUI() {
        final AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.rp_searchbox);
        Button searchbtn = (Button) findViewById(R.id.rp_searchbtn);
        titles= new ArrayList<>();

        final ArrayAdapter<String> adapter = new
                ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,titles);
        search.setAdapter(adapter);
        search.setThreshold(1);


        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!search.getText().toString().equals("")) {
                    Intent i = new Intent(RoomPage.this, ForumPage.class);
                    i.putExtra("TITLE", search.getText().toString());
                    i.putExtra("ROOM", room);
                    startActivity(i);
                }
                else {
                    search.setError("You must input something");
                }
            }
        });


        Spinner trendSpin = (Spinner) findViewById(R.id.rp_trending_spinner);
        String[] trendTimes = {"Past Hour", "Past Day", "Past Week", "Past Month", "All Time"};
        ArrayAdapter<String> spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, trendTimes);
        trendSpin.setAdapter(spAdapter);

        ListView lvTrend = (ListView) findViewById(R.id.rp_listview_trending);
        final String[] exampleTrend = {"TEST1", "TEST2", "TEST3", "TEST4", "TEST5"};
        final ArrayAdapter<String> tAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exampleTrend);
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
                Intent intent = new Intent(RoomPage.this, ForumPage.class);
                intent.putExtra("TITLE", topics.get(position));
                intent.putExtra("ROOM", room);
                startActivity(intent);
            }
        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Topic topic = dataSnapshot.getValue(Topic.class);
                if(topic.getRoom().equals(room))
                    topics.add(topic.getTitle());

                ForumPost posts = dataSnapshot.getValue(ForumPost.class);
                if(posts.getRoom().equals(room))
                    titles.add(posts.getTitle());
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
