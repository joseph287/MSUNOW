

package com.example.rasen.msunow;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stjoy on 3/3/2017.
 */

public class LoginRequest extends StringRequest{
    private static final String LOGIN_REQUEST_URL = "database.com/Login.php";
    private Map<String,String> params;

    public LoginRequest( String email, String password, Response.Listener<String> listener){
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("Email",email);
        params.put("Password",password);

    }
    @Override
    public Map<String,String> getParams(){
        return params;
    }
}
