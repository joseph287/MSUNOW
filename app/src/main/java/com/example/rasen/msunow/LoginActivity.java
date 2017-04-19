package com.example.rasen.msunow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rasen.msunow.Utils.Utils;
import com.facebook.stetho.Stetho;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    //layout items declared
    private Button bLogin;
    private Button bResisterLink;
    private Button bForgotPasswordLink;
    private static final String TAG = "SignIn";
    private Button btnLogin, btnLinkToSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private EditText loginInputEmail, loginInputPassword;
    private TextInputLayout loginInputLayoutEmail, loginInputLayoutPassword;
    private SharedPreferences shprefs;
    private SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Stetho.initializeWithDefaults(this);

        auth = FirebaseAuth.getInstance();

        loginInputLayoutEmail = (TextInputLayout) findViewById(R.id.login_input_layout_email);
        loginInputLayoutPassword = (TextInputLayout) findViewById(R.id.login_input_layout_password);

        // binding the layout with activity
        loginInputEmail = (EditText) findViewById(R.id.login_input_email);
        loginInputPassword = (EditText) findViewById(R.id.login_input_password);
        bLogin = (Button) findViewById(R.id.blogin);

        //login click onClick Listener
        findViewById((R.id.blogin)).setOnClickListener(new MyLsnr());

        //Not Registered yet? Register Here onClick Listener
        findViewById((R.id.bRegisterLink)).setOnClickListener(new MyLsnr());

        //Forgot Passpord onClick Listener
        findViewById((R.id.bForgotPasswordLink)).setOnClickListener(new MyLsnr());
    }

    private void loginUser() {

        final String email = loginInputEmail.getText().toString().trim();
        String password = loginInputPassword.getText().toString().trim();

        if (!checkEmail()) {
            return;
        }
        if (!checkPassword()) {
            return;
        }
        loginInputLayoutEmail.setErrorEnabled(false);
        loginInputLayoutPassword.setErrorEnabled(false);

        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, Log a message to the LogCat. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                        } else {
                            shprefs = getSharedPreferences(Utils.SHPRFN, MODE_APPEND);
                            editor = shprefs.edit();
                            editor.putString(Utils.CURRUSER, email);
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                            startActivity(intent);
//                            finish();
                        }
                    }
                });
    }

    private boolean checkEmail() {
        String email = loginInputEmail.getText().toString().trim();
        if (email.isEmpty() || !isEmailValid(email)) {

            loginInputLayoutEmail.setErrorEnabled(true);
            loginInputLayoutEmail.setError(getString(R.string.err_msg_email));
            loginInputEmail.setError(getString(R.string.err_msg_required));
            requestFocus(loginInputEmail);
            return false;
        }
        loginInputLayoutEmail.setErrorEnabled(false);
        return true;
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkPassword() {

        String password = loginInputPassword.getText().toString().trim();
        if (password.isEmpty() || !isPasswordValid(password)) {

            loginInputLayoutPassword.setError(getString(R.string.err_msg_password));
            loginInputPassword.setError(getString(R.string.err_msg_required));
            requestFocus(loginInputPassword);
            return false;
        }
        loginInputLayoutPassword.setErrorEnabled(false);
        return true;
    }

    private static boolean isPasswordValid(String password){
        return (password.length() >= 6);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            if (v.getId() == R.id.blogin) {
                loginUser();
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