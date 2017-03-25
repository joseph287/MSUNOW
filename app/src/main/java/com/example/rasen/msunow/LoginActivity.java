package com.example.rasen.msunow;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etEmail = (EditText) findViewById(R.id.log_email);
        final EditText etPasswrd = (EditText) findViewById(R.id.log_pass);
        final Button bLogin = (Button) findViewById(R.id.bttn_login);
        final TextView registerLink = (TextView) findViewById(R.id.tv_Register);
        final TextView forgotPass = (TextView) findViewById(R.id.passReset);


        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotIntent = new Intent(LoginActivity.this, ForgetActivity.class);
                LoginActivity.this.startActivity(forgotIntent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String eMail = etEmail.getText().toString();
                final String passwd = etPasswrd.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                String email = jsonResponse.getString("email");
                                int age = jsonResponse.getInt("age");
                                String fName = jsonResponse.getString("First name");
                                String lName = jsonResponse.getString("Last name");

                                Intent intent = new Intent(LoginActivity.this, RoomPage.class);
                                intent.putExtra("email", email);
                                intent.putExtra("first name", fName);
                                intent.putExtra("last name", lName);
                                intent.putExtra("age", age);
                                LoginActivity.this.startActivity(intent);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("Login Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(eMail, passwd, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

                etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView txtView, int actionId, KeyEvent event) {
                        boolean handled = false;
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            String inputText = txtView.getText().toString();
                            Toast.makeText(LoginActivity.this, "Your email address is: " + inputText, Toast.LENGTH_SHORT).show();
                            handled = true;
                        }
                        return handled;
                    }
                });

                etPasswrd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView txtView, int actionId, KeyEvent event) {
                        boolean handled = false;
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            String inputText = txtView.getText().toString();
                            Toast.makeText(LoginActivity.this, "Your email address is: " + inputText, Toast.LENGTH_SHORT).show();
                            handled = true;
                        }
                        return handled;
                    }
                });

            }
        });
    }
}