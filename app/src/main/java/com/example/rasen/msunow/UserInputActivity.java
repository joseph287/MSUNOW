package com.example.rasen.msunow;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rasen.msunow.InputTopic.Topic;

import java.text.SimpleDateFormat;
import java.util.Calendar;


import com.example.rasen.msunow.Utils.Utils;
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
    private boolean subscribe;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private final String TOPIC="topic";

    private SharedPreferences shprefs;
    private SharedPreferences.Editor editor;

    private CustomDialog dialog;

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


        shprefs = getSharedPreferences(Utils.SHPRFN,
                Context.MODE_PRIVATE);

        dialog= new CustomDialog(this);
        dialog.setTitle("Your Topic has been posted");

    }

    //intent to previous page
    public void BackToPre(View view) {
        startActivity(new Intent(this, Dashboard.class));
    }

    //cancel current post, and intent to other page
    public void cancel(View view) {
        title.setText("");
        body.setText("");
    }

    //insert data to database
    public void post(View view) {

        input_title = title.getText().toString();
        input_body = body.getText().toString();
        //this can be replaced by user in login
       //author = "user1@montclair.edu";
        author= shprefs.getString(Utils.CURRUSER, "the user's email");
        if(dataInserted(input_title,input_body)) {
            if (!input_room.equals("") && !input_body.equals("") && !author.equals("")) {

                time = getCurrentTime();
                photoURL = null;
                karma = 0;

                Topic input = new Topic(input_title, input_body, input_room, author, time, photoURL, karma, subscribe);
                myRef.push().setValue(input);
                dialog.show();

            } else {
                Toast.makeText(this, "Please say something", Toast.LENGTH_LONG);
            }
        }

    }

    private boolean dataInserted(String input_title, String input_body) {
        Boolean isValid = true;

        if(TextUtils.isEmpty(input_title)){
            title.setError("Please enter a title");
            isValid = false;
        }
        if(TextUtils.isEmpty(input_body)){
            body.setError("Please enter a body");
            isValid = false;
        }
        return isValid;
    }

    public String getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat(Utils.DATEFORMAT);
        return format.format(Calendar.getInstance().getTime());
    }
    public class CustomDialog extends Dialog implements View.OnClickListener {
        Button toMyTopic;
        Activity mActivity;

        public CustomDialog(Activity activity) {
            super(activity);
            mActivity = activity;
            setContentView(R.layout.dialog);
            toMyTopic = (Button) findViewById(R.id.diglog_btn);
            toMyTopic.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v==toMyTopic){
                Intent i = new Intent(getApplicationContext(), ForumPage.class);
                i.putExtra("TITLE", input_title);
                i.putExtra("ROOM", input_room);
                startActivity(i);
            }
    }


}

}



