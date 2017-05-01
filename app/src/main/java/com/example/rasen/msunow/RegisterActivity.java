//edited stjoy

package com.example.rasen.msunow;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private TextView etDOB;
    private Button bRegister;
    private FirebaseAuth auth;
    private static final String TAG = "SignUp";
    private ProgressBar progressBar;

    private DatabaseReference database;
    private RegisterRequest registerRequest;

    private static final String USER = "USER";

    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();

        etFirstName = (EditText) findViewById(R.id.reg_firstName);
        etLastName = (EditText) findViewById(R.id.reg_lastName);
        etEmail = (EditText) findViewById(R.id.reg_Email);
        etPassword = (EditText) findViewById(R.id.reg_Passw);
        etDOB = (TextView) findViewById(R.id.DOB);
        bRegister = (Button) findViewById(R.id.btn_register);

        database = FirebaseDatabase.getInstance().getReference();

        registerRequest = getIntent().getParcelableExtra(USER);
        if (registerRequest != null) {
            etFirstName.setText(registerRequest.getFirstName());
            etLastName.setText(registerRequest.getLastName());
            etEmail.setText(registerRequest.getEmail());
            etPassword.setText(registerRequest.getPassword());
            etDOB.setText(registerRequest.getDob());
        }

        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();

            }
        });
    }

    private void submitForm() {

        // get the user entered data in variable
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String dob = etDOB.getText().toString().trim();

        clearErrors();
        //validating the entered data
        if (verifyData(firstName, lastName, email, password, dob)) {
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();

            //create user
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            // If sign in fails, Log the message to the LogCat. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.d(TAG, "Authentication failed." + task.getException());

                            } else {
                                startActivity(new Intent(RegisterActivity.this, Dashboard.class));
                                finish();
                            }
                        }
                    });
            Toast.makeText(getApplicationContext(), "You are successfully Registered !!", Toast.LENGTH_SHORT).show();
            // registering the new user in the firebase database
            if (registerRequest == null) {
                registerRequest = new RegisterRequest();
                registerRequest.setUserId(database.child("users").push().getKey());
            }

            //adding each parameter in database
            registerRequest.setFirstName(firstName);
            registerRequest.setLastName(lastName);
            registerRequest.setEmail(email);
            registerRequest.setPassword(password);
            registerRequest.setDob(dob);
            database.child("users").child(registerRequest.getUserId()).setValue(registerRequest);

            finish();
            Intent intent = new Intent(RegisterActivity.this, Dashboard.class);
            startActivity(intent);
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDoB();
        }
    };

    private void updateDoB() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDOB.setText(sdf.format(myCalendar.getTime()));
    }

    //Validation of the data entered by the user
    public boolean verifyData(String firstName, String lastName, String email, String password, String dob) {
        Boolean isValid = true;

        // FirstName validation
        if (TextUtils.isEmpty(firstName)) {
            etFirstName.setError("Please enter a first name");
            isValid = false;
        }

        // LastName validation
        if (TextUtils.isEmpty(lastName)) {
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
        if (TextUtils.isEmpty(dob)) {
            etDOB.setError("Please select valid date of birth");
            isValid = false;
        }

        // Password validation, minimum 6 characters required
        String passwordPattern = "(?=.*[A-Za-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$";
        if (TextUtils.isEmpty(password) || !(password.matches(passwordPattern))) {
            etPassword.setError("Password with min length 6 should include a-z/A-z,special char & no.");
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

    //Clear errors from the register form
    private void clearErrors() {
        etFirstName.setError(null);
        etLastName.setError(null);
        etEmail.setError(null);
        etPassword.setError(null);
        etDOB.setError(null);
    }
}
