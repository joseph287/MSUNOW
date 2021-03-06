package com.example.rasen.msunow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rasen.msunow.InputTopic.Topic;
import com.example.rasen.msunow.Utils.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ForumPage extends AppCompatActivity {

    ListView list;
    Button postBtn, subBtn;
    EditText edtxt;
    String username, title, room;
    ForumPostAdapter mAdapt;
    static ArrayList<String> mKeys;
    DatabaseReference myRef;
    FirebaseDatabase database;
    static String UID;

    static ArrayList<ForumPost> posts;
    ChildEventListener mChildEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_page);

        username = getSharedPreferences(Utils.SHPRFN, MODE_APPEND).getString(Utils.CURRUSER, "");
        UID = getSharedPreferences(Utils.SHPRFN, MODE_APPEND).getString(Utils.UID, "");
        title = getIntent().getStringExtra("TITLE");
        room = getIntent().getStringExtra("ROOM");
        subBtn = (Button) findViewById(R.id.sub);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("post");

        TextView tvTitle = (TextView) findViewById(R.id.forum_title);
        tvTitle.setText(title);

        posts = new ArrayList<>();
        setTopic();
        edtxt = (EditText) findViewById(R.id.msg_box);
        list = (ListView) findViewById(R.id.fp_list);
        mAdapt = new ForumPostAdapter(this, R.layout.forum_post, posts);
        mKeys = new ArrayList<>();
        list.setAdapter(mAdapt);
        postBtn = (Button) findViewById(R.id.send_msg);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtxt.getText().toString().length()>0){
                    myRef.push().setValue(new ForumPost(edtxt.getText().toString(), username, getCurrentTime(), null, 0, title, room, true));
                    edtxt.setText("");
                }
            }
        });

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ForumPost post = dataSnapshot.getValue(ForumPost.class);
                if(post!=null && title!=null && room!=null && post.getTitle().equals(title) && post.getRoom().equals(room)) {
                    mAdapt.add(post);
                    mKeys.add(dataSnapshot.getKey());
                }
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

    public String getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat(Utils.DATEFORMAT);
        return format.format(Calendar.getInstance().getTime());
    }

    public void setTopic(){
        DatabaseReference tRef = database.getReference("topic");
        ArrayList<Topic> topics = new ArrayList<>();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Topic topic = dataSnapshot.getValue(Topic.class);
                if(topic.getTitle().equals(title) && topic.getRoom().equals(room)) {
                    posts.add(new ForumPost(topic.getBody(), topic.getAuthor(), topic.getTime(), topic.getPhotoURL(), topic.getKarma(), topic.getTitle(), topic.getRoom(), topic.getSubscribe()));
                    mKeys.add(dataSnapshot.getKey());
                }
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
        tRef.addChildEventListener(childEventListener);
    }

    public static void setVote(int position) {
        DatabaseReference initref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref;
        if(position == 0)
            ref = initref.child("topic");
        else
            ref = initref.child("post");
        Map<String, Object> update = new HashMap<>();
        update.put("karma", posts.get(position).getKarma());
        ref.child(mKeys.get(position)).updateChildren(update);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
