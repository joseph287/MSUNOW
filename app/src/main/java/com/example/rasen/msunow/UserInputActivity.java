package com.example.rasen.msunow;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rasen.msunow.InputTopic.Topic;

import java.util.Calendar;

//import static com.example.rasen.msunow.LoginActivity.user;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserInputActivity extends AppCompatActivity {


    private EditText title;
    private EditText body;
    private String[] items;
    private Spinner spinner;


    private String input_title;
    private String input_body;
    private String input_room;
    private String author;
    private String time;
    private String photoURL;
    private int karma;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private final String TOPIC="topic";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(TOPIC);



        title = (EditText) findViewById(R.id.topic_title);
        body = (EditText) findViewById(R.id.topic_body);

        items = getResources().getStringArray(R.array.rooms);
        spinner = (Spinner) findViewById(R.id.rooms_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.rooms, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                input_room = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    //intent to previous page
    public void BackToPre(View view) {


    }

    //cancel current post, and intent to other page
    public void cancel(View view) {
    }

    //insert data to database
    public void post(View view) {

        input_title = title.getText().toString();
        input_body = body.getText().toString();
        //this can be replaced by user in login
        author = "user1@montclair.edu";
        time = Calendar.getInstance().getTime().toString();
        photoURL = null;
        karma = 0;

        Topic input = new Topic(input_title, input_body, input_room, author, time, photoURL, karma);
        myRef.push().setValue(input);

        startActivity(new Intent(this, topics_listview.class));


    }

}



