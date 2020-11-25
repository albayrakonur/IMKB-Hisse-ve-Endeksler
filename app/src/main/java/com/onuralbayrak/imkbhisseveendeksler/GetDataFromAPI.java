package com.onuralbayrak.imkbhisseveendeksler;

import android.content.Context;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetDataFromAPI {

    public String authorization;
    public String key;
    public String ivs;
    public String period;
    public Context context;
    public JSONObject result = null;

    public GetDataFromAPI(String authorization, String key, String ivs, String period, Context context) {
        this.authorization = authorization;
        this.key = key;
        this.ivs = ivs;
        this.period = period;
        this.context = context;
    }

    public JSONObject getData() {

        //Base64 Decode
        byte[] keyDecoded = Base64.decode(key, Base64.DEFAULT);
        byte[] ivsDecoded = Base64.decode(ivs, Base64.DEFAULT);

        MyCipher myCipher = new MyCipher(keyDecoded, ivsDecoded, period);
        String ciphertext = myCipher.encryption();

        //Post Request to get Data
        String stocksListUrl = "https://mobilechallenge.veripark.com/api/stocks/list";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("period", ciphertext);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, stocksListUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                result = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println(error.networkResponse.data.toString());
            }

        }) {    //this is the part, that adds the header to the request
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-VP-Authorization", authorization);
                params.put("content-type", "application/json");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);

        return null;
    }
}
