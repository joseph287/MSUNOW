package com.example.rasen.msunow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.example.rasen.msunow.Utils.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class RoomPage extends AppCompatActivity {

    private ArrayList<String> topics;
    private ArrayList<String> trending;
    private String room;
    DatabaseReference myRef;
    FirebaseDatabase database;
    ChildEventListener mChildEventListener;
    Spinner trendSpin;
    ArrayList<Topic> trendingTopics;
    ArrayAdapter<String> tAdapter;

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
        tvs.setText(tvs.getText() + room + " ");

        TextView tvt = (TextView) findViewById(R.id.rp_tvt);
        tvt.setText(tvt.getText() + room + " ");

        loadUI();
    }


    private void loadUI() {
        final AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.rp_searchbox);
        Button searchbtn = (Button) findViewById(R.id.rp_searchbtn);
        trendingTopics = new ArrayList<>();
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


        trendSpin = (Spinner) findViewById(R.id.rp_trending_spinner);
        String[] trendTimes = {"Past Hour", "Past Day", "Past Week", "Past Month", "All Time"};
        ArrayAdapter<String> spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, trendTimes);
        trendSpin.setAdapter(spAdapter);
        trendSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setTrending();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ListView lvTrend = (ListView) findViewById(R.id.rp_listview_trending);
        final String[] exampleTrend = {"TEST1", "TEST2", "TEST3", "TEST4", "TEST5"};
        trending = new ArrayList<>();
        tAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, trending);
        lvTrend.setAdapter(tAdapter);
        lvTrend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RoomPage.this, ForumPage.class);
                intent.putExtra("TITLE", trending.get(position));
                intent.putExtra("ROOM", room);
                startActivity(intent);
            }
        });

        ListView lvTopics = (ListView) findViewById(R.id.rp_listview_topics);
        topics = new ArrayList<>();
        final ArrayAdapter<String> topAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, topics);
        lvTopics.setAdapter(topAdapter);
        lvTopics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(RoomPage.this, "This is : "+topics.get(position), Toast.LENGTH_SHORT).show();
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
                if (topic.getRoom().equals(room)) {
                    trendingTopics.add(topic);
                    topics.add(topic.getTitle());
                }

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

        sortTopics();
        //setTrending();

    }

    private void sortTopics() {
        for(int i=0;i<trendingTopics.size();i++){
            int highestKarma = i;
            for(int j=i+1;j<trendingTopics.size();j++){
                if(trendingTopics.get(highestKarma).getKarma()<trendingTopics.get(j).getKarma()){
                    highestKarma = j;
                }
            }
            trendingTopics.add(i, trendingTopics.remove(highestKarma));
        }
    }

    public long differencceInTime(String date) {
        SimpleDateFormat format = new SimpleDateFormat(Utils.DATEFORMAT);
        try {
            return Calendar.getInstance().getTime().getTime() - format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void setTrending() {
        trending.clear();
        tAdapter.clear();
        for(Topic topic: trendingTopics) {
            if (trending.size() < 5) {
                Log.i("TIME", String.valueOf(differencceInTime(topic.getTime())));
                switch (trendSpin.getSelectedItemPosition()) {
                    case 0:
                        if (differencceInTime(topic.getTime()) <= 3600000)
                            trending.add(topic.getTitle());
                        break;
                    case 1:
                        if (differencceInTime(topic.getTime()) <= 86400000)
                            trending.add(topic.getTitle());
                        break;
                    case 2:
                        if (differencceInTime(topic.getTime()) <= 604800000)
                            trending.add(topic.getTitle());
                        break;
                    case 3:
                        long num = (long) 259200000*10;
                        if (differencceInTime(topic.getTime()) <= num)
                            trending.add(topic.getTitle());
                        break;
                    case 4:
                        trending.add(topic.getTitle());
                        break;
                    default:
                        break;

                }
            }
        }

    }
}
