package com.example.rasen.msunow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.rasen.msunow.Utils.Utils;
import com.facebook.stetho.common.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        HomePage.OnFragmentInteractionListener, AccountSettings.OnFragmentInteractionListener, SubsPage.OnFragmentInteractionListener {

    String mode;
    DatabaseReference myRef;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String curuser;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        auth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView useremail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_email);
        SharedPreferences shprefs = getSharedPreferences(Utils.SHPRFN, MODE_APPEND);
        editor = shprefs.edit();
        curuser = shprefs.getString(Utils.CURRUSER, "");
        useremail.setText(curuser);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String > activeUser = getUsers((Map<String, Object>) dataSnapshot.getValue());
                if (activeUser != null) {
                    editor.putString(Utils.UID, activeUser.get("userId"));
                    editor.commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mode = "HOME";
        setFragment();
    }

    private Map<String, String> getUsers(Map<String, Object> user) {
        for (Map.Entry<String, Object> entry : user.entrySet()) {
            Map<String, String> singleUser = (Map<String, String>) entry.getValue();
            if (singleUser.get("email").equals(curuser)) {
                return singleUser;

            }
        }
        return null;
    }

    private void setFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new HomePage());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();
        int fid = 0;

        if (id == R.id.nav_home) {
            fragment = new HomePage();
            fid = R.id.fraghome;
        } else if (id == R.id.nav_subs) {
            fragment = new SubsPage();
            fid = R.id.fragsubs;
        } else if (id == R.id.nav_settings) {
            fragment = new AccountSettings();
            fid = R.id.fragacset;
        } else if (id == R.id.nav_logout){
            auth.signOut();
            finish();
            Intent intent = new Intent(Dashboard.this,LoginActivity.class);
            startActivity(intent);
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
