package com.onuralbayrak.imkbhisseveendeksler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public String aesKey;
    public String aesIV;
    public String authorization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String uniqueID = UUID.randomUUID().toString();
        String manufacturer = Build.MANUFACTURER;   //Phone Manufacturer
        String model = Build.MODEL; //Phone Model
        String versionRelease = Build.VERSION.RELEASE;  // Android Version
        Button button = findViewById(R.id.button);
        button.setClickable(false);

        String handshakeURL = "https://mobilechallenge.veripark.com/api/handshake/start";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("deviceId", uniqueID);
            postData.put("systemVersion", versionRelease);
            postData.put("platformName", "Android");
            postData.put("deviceModel", model);
            postData.put("manifacturer", manufacturer);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, handshakeURL, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    aesKey = response.getString("aesKey");
                    aesIV = response.getString("aesIV");
                    authorization = response.getString("authorization");
                    button.setClickable(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void redirect(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("aesKey", aesKey);
        intent.putExtra("aesIV", aesIV);
        intent.putExtra("authorization", authorization);
        startActivity(intent);
    }
}