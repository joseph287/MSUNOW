package com.example.rasen.msunow;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;

public class ActivitySplashActivity extends Activity  {

    private ImageView msunow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        msunow = (ImageView) findViewById(R.id.msunow);
    }

}
