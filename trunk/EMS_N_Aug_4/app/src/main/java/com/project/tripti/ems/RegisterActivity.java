package com.project.tripti.ems;

/**
 * Created by Miss_T on 27/07/2016.
 */
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends StringRequest {

    private static final String URL = 	"http://nilotpal.netai.net/t_Register.php";
    private Map<String, String> params;

    public RegisterActivity(String username, String password, String name, String email, String role,String token, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("name", name);
        params.put("email", email);
        params.put("role", role);
        params.put("firebaseid", token);


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}