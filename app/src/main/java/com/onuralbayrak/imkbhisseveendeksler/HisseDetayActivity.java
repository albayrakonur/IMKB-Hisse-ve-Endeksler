package com.onuralbayrak.imkbhisseveendeksler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.fontawesome.FontDrawable;

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
                //System.out.println(response);
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

            boolean isDown;
            isDown = jsonObject.getString("isDown").equals("true");
            boolean isUp;
            isUp = jsonObject.getString("isUp").equals("true");

            String change = jsonObject.getString("channge");
            ImageView changeTv = findViewById(R.id.changeText);
            //changeTv.setText("% Degisim: " + change);

            if (isDown) {
                FontDrawable drawable = new FontDrawable(getApplication(), R.string.fa_sort_down_solid, true, false);
                drawable.setTextColor(ContextCompat.getColor(getApplication(), android.R.color.holo_red_dark));
                changeTv.setImageDrawable(drawable);
            } else if (isUp) {
                FontDrawable drawable = new FontDrawable(getApplication(), R.string.fa_sort_up_solid, true, false);
                drawable.setTextColor(ContextCompat.getColor(getApplication(), android.R.color.holo_green_dark));
                changeTv.setImageDrawable(drawable);
            }

            JSONArray jsonArray = jsonObject.getJSONArray("graphicData");
            System.out.println(jsonArray.length());

            Chart chart = findViewById(R.id.chart);

            ArrayList<Entry> entries = new ArrayList<Entry>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject graphData = jsonArray.getJSONObject(i);
                entries.add(new Entry(Integer.parseInt(graphData.getString("day")), graphData.getInt("value")));
            }
            //System.out.println(entries);
            LineDataSet dataSet = new LineDataSet(entries, "STOCKS"); // add entries to dataset
            dataSet.setColor(Color.parseColor("#b71e46"));
            dataSet.setValueTextColor(Color.parseColor("#000000")); // styling, ...
            dataSet.setDrawIcons(false);
            dataSet.enableDashedLine(10f, 5f, 0f);
            dataSet.enableDashedHighlightLine(10f, 5f, 0f);
            dataSet.setColor(Color.DKGRAY);
            dataSet.setCircleColor(Color.DKGRAY);
            dataSet.setLineWidth(1f);
            dataSet.setCircleRadius(3f);
            dataSet.setDrawCircleHole(false);
            dataSet.setValueTextSize(9f);
            dataSet.setDrawFilled(true);
            dataSet.setFormLineWidth(1f);
            dataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            dataSet.setFormSize(15.f);
            dataSet.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(1);

                    return "" + df.format(value);
                }
            });
            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
                dataSet.setFillDrawable(drawable);
            } else {
                dataSet.setFillColor(Color.DKGRAY);
            }

            LineData lineData = new LineData(dataSet);

            chart.setData(lineData);
            chart.setBackgroundColor(Color.parseColor("#eeeeee"));
            chart.invalidate(); // refresh


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}