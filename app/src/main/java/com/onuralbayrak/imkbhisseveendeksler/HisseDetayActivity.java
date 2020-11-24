package com.onuralbayrak.imkbhisseveendeksler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class HisseDetayActivity extends AppCompatActivity {

    public String authorization;
    public String key;
    public String ivs;
    public String id;
    public MyCipher myCipher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hisse_detay);

        Intent intent = getIntent();
        id = intent.getExtras().getString("id");

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("com.onuralbayrak.imkbhisseveendeksler", Context.MODE_PRIVATE);
        key = sharedPreferences.getString("key", "-");
        ivs = sharedPreferences.getString("ivs", "-");
        authorization = sharedPreferences.getString("authorization", "-");

        getData();
    }


    public void getData() {

        //Base64 Decode
        byte[] keyDecoded = Base64.decode(key, Base64.DEFAULT);
        byte[] ivsDecoded = Base64.decode(ivs, Base64.DEFAULT);

        myCipher = new MyCipher(keyDecoded, ivsDecoded, id);
        String ciphertext = myCipher.encryption();

        //Post Request to get Data
        String detailURL = "https://mobilechallenge.veripark.com/api/stocks/detail";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());

        JSONObject postData = new JSONObject();
        try {
            postData.put("id", ciphertext);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, detailURL, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //System.out.println(response);
                System.out.println(response);
                fillTheBlanks(response);
                //insertDataToTable(null);

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
    }

    public void fillTheBlanks(JSONObject jsonObject) {
        try {
            String symbol = myCipher.decryption(jsonObject.getString("symbol"));
            TextView symbolTv = findViewById(R.id.symbolText);
            symbolTv.setText("Sembol: " + symbol);

            String price = jsonObject.getString("price");
            TextView priceTv = findViewById(R.id.priceText);
            priceTv.setText("Fiyat: " + price);

            String difference = jsonObject.getString("difference");
            TextView differenceTv = findViewById(R.id.differenceText);
            differenceTv.setText("% Fark: " + difference);

            String bid = jsonObject.getString("bid");
            TextView bidTv = findViewById(R.id.bidText);
            bidTv.setText("Alis: " + bid);

            String volume = jsonObject.getString("volume");
            TextView volumeTv = findViewById(R.id.volumeText);
            volumeTv.setText("Hacim: " + Double.parseDouble(volume));

            String offer = jsonObject.getString("offer");
            TextView offerTv = findViewById(R.id.offerText);
            offerTv.setText("Satis: " + offer);

            String highest = jsonObject.getString("highest");
            TextView highestTv = findViewById(R.id.highestText);
            highestTv.setText("Gunluk Yuksek: " + highest);

            String lowest = jsonObject.getString("lowest");
            TextView lowestTv = findViewById(R.id.lowestText);
            lowestTv.setText("Gunluk Dusuk: " + lowest);

            String count = jsonObject.getString("count");
            TextView countTv = findViewById(R.id.countText);
            countTv.setText("Adet: " + count);

            String maximum = jsonObject.getString("maximum");
            TextView maximumTv = findViewById(R.id.maximumText);
            maximumTv.setText("Tavan: " + maximum);

            String minimum = jsonObject.getString("minimum");
            TextView minimumTv = findViewById(R.id.minimumText);
            minimumTv.setText("Taban: " + minimum);

            String change = jsonObject.getString("channge");
            TextView changeTv = findViewById(R.id.changeText);
            changeTv.setText("% Degisim: " + change);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}