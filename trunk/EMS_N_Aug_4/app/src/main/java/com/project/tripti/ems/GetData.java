package com.project.tripti.ems;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Owner-Pc on 24/08/2016.
 */
public class GetData extends StringRequest {
    private static final String URL = 	"http://nilotpal.netai.net/t_getData.php";
    private Map<String, String> params;

    public GetData(String username, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
