package com.example.rasen.msunow;

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

    //layout items declared
    private EditText etEmail;
    private EditText etPassword;
    private Button bLogin;
    private Button bResisterLink;
    private Button bForgotPasswordLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // binding the layout with activity
        etEmail = (EditText) findViewById(R.id.log_email);
        etPassword = (EditText) findViewById(R.id.log_pass);
        bLogin = (Button) findViewById(R.id.bttn_login);

        //login click onClick Listener
        findViewById((R.id.bttn_login)).setOnClickListener(new MyLsnr());
        //Not Registered yet? Register Here onClick Listener
        findViewById((R.id.bRegisterLink)).setOnClickListener(new MyLsnr());
        //Forgot Passpord onClick Listener
        findViewById((R.id.bForgotPasswordLink)).setOnClickListener(new MyLsnr());
    }
    //used to test the input page, can be deleted when I am done-Wenjie
    public void test(View view) {

        startActivity(new Intent(this, UserInputActivity.class));
    }

    // OnClick Listener for Login,Forgot Password and new registration.
    private class MyLsnr implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            //Login
            if (v.getId() == R.id.bttn_login) {

//                final String eMail = etEmail.getText().toString();
//                final String passwd = etPassword.getText().toString();
//                Response.Listener<String> responseListener = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonResponse = new JSONObject(response);
//                            boolean success = jsonResponse.getBoolean("success");
//
//                            if (success) {
//                                String email = jsonResponse.getString("email");
//                                int age = jsonResponse.getInt("age");
//                                String fName = jsonResponse.getString("First name");
//                                String lName = jsonResponse.getString("Last name");
//
//                                Intent intent = new Intent(LoginActivity.this, RoomPage.class);
//                                intent.putExtra("email", email);
//                                intent.putExtra("first name", fName);
//                                intent.putExtra("last name", lName);
//                                intent.putExtra("age", age);
//                                LoginActivity.this.startActivity(intent);
////
//                            } else {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                                builder.setMessage("Login Failed")
//                                        .setNegativeButton("Retry", null)
//                                        .create()
//                                        .show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                LoginRequest loginRequest = new LoginRequest(eMail, passwd, responseListener);
//                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
//                queue.add(loginRequest);

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

                etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                startActivity(intent);
            }

            //Redirecting to Register Page
            if (v.getId() == R.id.bRegisterLink) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }

            //Redirecting to Forgot Password Page
            if (v.getId() == R.id.bForgotPasswordLink) {
                Intent forgotIntent = new Intent(LoginActivity.this, ForgetActivity.class);
                LoginActivity.this.startActivity(forgotIntent);
            }

        }
    }
}