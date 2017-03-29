//edited stjoy

package com.example.rasen.msunow;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etDOB;
    private Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFirstName = (EditText) findViewById(R.id.reg_firstName);
        etLastName = (EditText) findViewById(R.id.reg_lastName);
        etEmail = (EditText) findViewById(R.id.reg_Email);
        etPassword = (EditText) findViewById(R.id.reg_Passw);
        etDOB = (EditText) findViewById(R.id.DOB);
        bRegister = (Button) findViewById(R.id.bttn_register);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            private void updateLabel() {
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                etDOB.setText(sdf.format(myCalendar.getTime()));
            }

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        etFirstName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView txtView, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String inputText = txtView.getText().toString();
                    Toast.makeText(RegisterActivity.this, "Your first name is: " + inputText, Toast.LENGTH_SHORT).show();
                    handled = true;
                }
                return handled;
            }
        });

        etLastName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView txtView, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String inputText = txtView.getText().toString();
                    Toast.makeText(RegisterActivity.this, "Your last name is: " + inputText, Toast.LENGTH_SHORT).show();
                    handled = true;
                }
                return handled;
            }
        });

        etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView txtView, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String inputText = txtView.getText().toString();
                    Toast.makeText(RegisterActivity.this, "Your email address is: " + inputText, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RegisterActivity.this, "Your password is: " + inputText, Toast.LENGTH_SHORT).show();
                    handled = true;
                }
                return handled;
            }
        });

        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    //Validation of the data entered by the user
    public boolean verifyData(String fName, String lName, String email, String password, String dob) {
        Boolean isValid = true;

        // FirstName validation
        if (TextUtils.isEmpty(fName)) {
            etFirstName.setError("Please enter a first name");
            isValid = false;
        }

        // LastName validation
        if (TextUtils.isEmpty(lName)) {
            etLastName.setError("Please enter your last name");
            isValid = false;
        }

        // Email validation specific for montclair.edu
        String emailPattern = "[a-zA-Z0-9._-]+@(montclair)\\.edu";
        if (TextUtils.isEmpty(email) || !(email.matches(emailPattern))) {
            etEmail.setError("Email ID must contain montclair.edu");
            isValid = false;
        }

        // DoB validation
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        if (TextUtils.isEmpty(dob) | !validateDoB(dob) | dob.compareTo((dateFormat.format(date)).toString()) >= 0) {
            etDOB.setError("Please select valid date of birth");
            isValid = false;
        }

        // Password validation, minimum 6 characters required
        String passwordPattern = "(?=.*[A-Za-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$";
        if (TextUtils.isEmpty(password) || !(password.matches(passwordPattern))) {
            etPassword.setError("Password should include numbers and characters with min length 6");
            isValid = false;

        }

        return isValid;
    }

    //DOB Validation
    private boolean validateDoB(String dob) {
        Pattern pattern;
        Matcher matcher;
        final String dateOfBirthPattern = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/((19|20)\\d\\d)";
        pattern = Pattern.compile(dateOfBirthPattern);


        matcher = pattern.matcher(dob);

        if (matcher.matches()) {
            matcher.reset();

            if (matcher.find()) {
                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") &&
                        (month.equals("4") || month.equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month.equals("06") ||
                                month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if (year % 4 == 0) {
                        if (day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        if (day.equals("29") || day.equals("30") || day.equals("31")) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void onRegisterClick(View view) {

        final String fName = etFirstName.getText().toString();
        final String lName = etLastName.getText().toString();
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String dob = etDOB.getText().toString().trim();
        Response.Listener<String> responseListener = null;

        clearErrors();
        if (verifyData(fName, lName, email, password, dob)) {

            responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            RegisterActivity.this.startActivity(intent);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("Register Failed")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

        }
        RegisterRequest registerRequest = new RegisterRequest(fName, lName, email, password, dob, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);
    }

    private void clearErrors() {
        etFirstName.setError(null);
        etLastName.setError(null);
        etEmail.setError(null);
        etPassword.setError(null);
        etDOB.setError(null);
    }
}
