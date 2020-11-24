package com.onuralbayrak.imkbhisseveendeksler;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class fetchData {

    private String period;
    private String key;
    private String iv;
    private Context context;

//    public void handshake() {
//        String handshakeURL = "https://mobilechallenge.veripark.com/api/handshake/start";
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//
//        JSONObject postData = new JSONObject();
//        try {
//            postData.put("deviceId", uniqueID);
//            postData.put("systemVersion", versionRelease);
//            postData.put("platformName", "Android");
//            postData.put("deviceModel", model);
//            postData.put("manifacturer", manufacturer);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, handshakeURL, postData, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    aesKey = response.getString("aesKey");
//                    aesIV = response.getString("aesIV");
//                    authorization = response.getString("authorization");
//                    button.setClickable(true);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        requestQueue.add(jsonObjectRequest);
//    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
