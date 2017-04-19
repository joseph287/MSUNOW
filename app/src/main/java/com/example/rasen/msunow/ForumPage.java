package com.example.rasen.msunow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ForumPage extends AppCompatActivity {

    ListView list;
    ForumPostAdapter mAdapt;
    DatabaseReference myRef;
    FirebaseDatabase database;

    ArrayList<ForumPost> posts;
    ChildEventListener mChildEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_page);

        String title = getIntent().getStringExtra("TITLE");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(title);

        TextView tvTitle = (TextView) findViewById(R.id.forum_title);
        tvTitle.setText(title);

        posts = new ArrayList<>();

        list = (ListView) findViewById(R.id.fp_list);

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ForumPost post = dataSnapshot.getValue(ForumPost.class);
                mAdapt.add(post);
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
