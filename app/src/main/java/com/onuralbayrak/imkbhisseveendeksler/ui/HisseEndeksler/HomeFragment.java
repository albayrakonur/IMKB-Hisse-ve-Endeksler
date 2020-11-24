package com.onuralbayrak.imkbhisseveendeksler.ui.HisseEndeksler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.onuralbayrak.imkbhisseveendeksler.MyCipher;
import com.onuralbayrak.imkbhisseveendeksler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    public final String period = "all";
    public TableLayout tableLayout;
    public ScrollView scrollView;
    public int size = 0;
    public SearchView searchView;
    public String authorization;
    public String key;
    public String ivs;
    public JSONArray stocksData;
    public boolean finishedGettingData;
    private HomeViewModel homeViewModel;
    private SharedPreferences sharedPreferences;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hisse_endeksler, container, false);
        tableLayout = root.findViewById(R.id.mainTable);
        initializeEmptyTable();
        scrollView = new ScrollView(getContext());
        size += 1;

        sharedPreferences = getActivity().getSharedPreferences("com.onuralbayrak.imkbhisseveendeksler", Context.MODE_PRIVATE);

        key = sharedPreferences.getString("key", "-");
        ivs = sharedPreferences.getString("ivs", "-");
        authorization = sharedPreferences.getString("authorization", "-");

        finishedGettingData = false;

        getDataAndFillTheTable();

        searchView = root.findViewById(R.id.searchBoxText);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fillTheTable(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.equals("")) {
                    fillTheTable(null);
                }
                return false;
            }
        });

        return root;
    }

    public void getDataAndFillTheTable() {
        //Base64 Decode
        byte[] keyDecoded = Base64.decode(key, Base64.DEFAULT);
        byte[] ivsDecoded = Base64.decode(ivs, Base64.DEFAULT);

        MyCipher myCipher = new MyCipher(keyDecoded, ivsDecoded, period);
        String ciphertext = myCipher.encryption();

        //Post Request to get Data
        String stocksListUrl = "https://mobilechallenge.veripark.com/api/stocks/list";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JSONObject postData = new JSONObject();
        try {
            postData.put("period", ciphertext);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, stocksListUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //System.out.println(response);
                finishedGettingData = true;
                try {
                    stocksData = response.getJSONArray("stocks");
                    for (int i = 0; i < stocksData.length(); i++) {
                        JSONObject tmp = stocksData.getJSONObject(i);
                        String sembol = myCipher.decryption(tmp.getString("symbol"));
                        String fiyat = tmp.getString("price");
                        String fark = tmp.getString("difference");
                        double hacim = Double.parseDouble("801457.5");
                        String alis = tmp.getString("bid");
                        String satis = tmp.getString("offer");
                        boolean isDown;
                        isDown = tmp.getString("isDown").equals("true");
                        boolean isUp;
                        isUp = tmp.getString("isUp").equals("true");

                        TableRow tableRow = new TableRow(getContext());
                        TextView textView1 = new TextView(getContext());
                        textView1.setText(sembol);
                        TextView textView2 = new TextView(getContext());
                        textView2.setText(fiyat);
                        TextView textView3 = new TextView(getContext());
                        textView3.setText(fark);
                        TextView textView4 = new TextView(getContext());
                        textView4.setText(hacim + "");
                        TextView textView5 = new TextView(getContext());
                        textView5.setText(alis);
                        TextView textView6 = new TextView(getContext());
                        textView6.setText(satis);
                        TextView textView7 = new TextView(getContext());
                        if (isDown) {
                            textView7.setText("Azalan");
                        } else {
                            textView7.setText("Yukselen");
                        }
                        tableRow.addView(textView1);
                        tableRow.addView(textView2);
                        tableRow.addView(textView3);
                        tableRow.addView(textView4);
                        tableRow.addView(textView5);
                        tableRow.addView(textView6);
                        tableRow.addView(textView7);

                        if (size % 2 == 0) {
                            tableRow.setBackgroundColor(Color.parseColor("#eeeeee"));
                        }
                        size += 1;
                        tableLayout.addView(tableRow);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    public void initializeEmptyTable() {
        tableLayout.removeAllViews();
        TableRow tableRow = new TableRow(getContext());
        TextView textView1 = new TextView(getContext());
        TextView textView2 = new TextView(getContext());
        TextView textView3 = new TextView(getContext());
        TextView textView4 = new TextView(getContext());
        TextView textView5 = new TextView(getContext());
        TextView textView6 = new TextView(getContext());
        TextView textView7 = new TextView(getContext());
        textView1.setText("Sembol");
        textView2.setText("Fiyat");
        textView3.setText("Fark");
        textView4.setText("Hacim");
        textView5.setText("Alis");
        textView6.setText("Satis");
        textView7.setText("Degisim");
//        textView3.setGravity(Gravity.CENTER);
        tableRow.addView(textView1);
        tableRow.addView(textView2);
        tableRow.addView(textView3);
        tableRow.addView(textView4);
        tableRow.addView(textView5);
        tableRow.addView(textView6);
        tableRow.addView(textView7);
        tableRow.setClickable(true);
        tableRow.setBackgroundColor(Color.parseColor("#BEBEBE"));
        tableLayout.addView(tableRow);
    }

    public void fillTheTable(String filter) {
        initializeEmptyTable();
        for (int i = 0; i < stocksData.length(); i++) {
            try {
                System.out.println(stocksData.getString(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//    public void getData(String filter) {
//        System.out.println("filter: " + filter);
//        String dataURL = "https://lit-meadow-91580.herokuapp.com/rest/getPotholes/";
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, dataURL, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                            // Loop through the array elements
//                            for (int i = 0; i < response.length(); i++) {
//                                // Get current json object
//                                JSONObject jsonObject = response.getJSONObject(i);
//                                JSONObject jsonObject1 = new JSONObject(jsonObject.get("addr").toString());
//                                if (filter != null && !jsonObject1.get("district").toString().toLowerCase().contains(filter.toLowerCase())) {
//                                    continue;
//                                }
////                                System.out.println(jsonObject.get("creation_date"));
////                                System.out.println(jsonObject.get("label"));
////                                System.out.println(jsonObject1.get("district"));
//                                TableRow tableRow = new TableRow(getContext());
//
//                                TextView textView1 = new TextView(getContext());
//                                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
//                                Date date;
//                                String creationDate = "";
//                                try {
//                                    date = fmt.parse(jsonObject.get("creation_date").toString()); //Button Text: Remind on: 15 SEP 2017 ( 10:10 ) PM
//                                    String newDateString = fmt.format(date);
//                                    String tempDate = newDateString;
//                                    creationDate = tempDate;
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                textView1.setText(creationDate);
//                                TextView textView2 = new TextView(getContext());
//                                textView2.setText(jsonObject.get("label").toString());
//                                TextView textView3 = new TextView(getContext());
//                                textView3.setText(jsonObject1.get("district").toString());
//                                textView3.setTag(jsonObject1);
//                                textView3.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        try {
//                                            JSONObject tmp = new JSONObject(v.getTag().toString());
//                                            System.out.println(tmp.get("district").toString());
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });
//                                tableRow.addView(textView1);
//                                tableRow.addView(textView2);
//                                tableRow.addView(textView3);
//                                if (size % 2 == 0) {
//                                    tableRow.setBackgroundColor(Color.parseColor("#eeeeee"));
//                                }
//                                tableRow.setClickable(true);
//                                tableLayout.addView(tableRow);
//                                size += 1;
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getContext(), "Verileri Getirirken Hata!", Toast.LENGTH_LONG);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Do something when error occurred
//                    }
//                }
//        );
//
//        // Add the request to the RequestQueue.
//        requestQueue.add(jsonArrayRequest);
//
//
//    }
}