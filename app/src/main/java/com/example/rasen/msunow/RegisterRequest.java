package com.example.rasen.msunow;

import android.support.v7.widget.LinearLayoutCompat;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rasen on 2/26/2017.
 */

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "database.com/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String firstName, String lastName, String email, String password, String dob, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("First Name", firstName);
        params.put("Last Name", lastName);
        params.put("Email", email);
        params.put("Password", password);
        params.put("Date of Birth", dob);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
