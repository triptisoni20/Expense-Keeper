package com.project.tripti.ems;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends StringRequest {
    private static final String URL = 	"http://nilotpal.netai.net/t_Login.php";
    private Map<String, String> params;

    public LoginActivity(String username, String password,String token, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("firebaseid", token);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
