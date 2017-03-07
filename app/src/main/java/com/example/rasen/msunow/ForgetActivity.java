package com.example.rasen.msunow;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.rasen.msunow.R.styleable.View;

public class ForgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);


        final EditText fEmail = (EditText) findViewById(R.id.forgot_email);
        final EditText fPassw = (EditText) findViewById(R.id.forgot_password);
        final Button btnSubmit = (Button) findViewById(R.id.btn_changePw);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
