package com.onuralbayrak.imkbhisseveendeksler.ui.imkbDusenler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.onuralbayrak.imkbhisseveendeksler.HisseDetayActivity;
import com.onuralbayrak.imkbhisseveendeksler.MyCipher;
import com.onuralbayrak.imkbhisseveendeksler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.fontawesome.FontDrawable;

public class DusenlerFragment extends Fragment {

    public final String period = "decreasing";
    public TableLayout tableLayout;
    public ScrollView scrollView;
    public int size = 0;
    public SearchView searchView;
    public String authorization;
    public String key;
    public String ivs;
    public JSONArray stocksData;
    public MyCipher myCipher = null;
    private SharedPreferences sharedPreferences;
    private DusenlerViewModel dusenlerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dusenlerViewModel =
                new ViewModelProvider(this).get(DusenlerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_imkb_dusenler, container, false);

        tableLayout = root.findViewById(R.id.mainTable);
        initializeEmptyTable();
        scrollView = new ScrollView(getContext());
        size += 1;

        sharedPreferences = getActivity().getSharedPreferences("com.onuralbayrak.imkbhisseveendeksler", Context.MODE_PRIVATE);

        key = sharedPreferences.getString("key", "-");
        ivs = sharedPreferences.getString("ivs", "-");
        authorization = sharedPreferences.getString("authorization", "-");

        getData();

        searchView = root.findViewById(R.id.searchBoxText);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                insertDataToTable(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                insertDataToTable(newText);
                return false;
            }
        });

        return root;
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

    public void getData() {
        //Base64 Decode
        byte[] keyDecoded = Base64.decode(key, Base64.DEFAULT);
        byte[] ivsDecoded = Base64.decode(ivs, Base64.DEFAULT);

        myCipher = new MyCipher(keyDecoded, ivsDecoded, period);
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
                try {
                    stocksData = response.getJSONArray("stocks");
                    insertDataToTable(null);

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

    public void insertDataToTable(String filter) {
        initializeEmptyTable();
        for (int i = 0; i < stocksData.length(); i++) {
            try {
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

                if (filter != null && !sembol.toLowerCase().contains(filter.toLowerCase())) {
                    continue;
                }

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
                ImageView imageView = new ImageView(getContext());
                FontDrawable drawable = new FontDrawable(getContext(), R.string.fa_sort_down_solid, true, false);
                drawable.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
                imageView.setImageDrawable(drawable);
                tableRow.addView(textView1);
                tableRow.addView(textView2);
                tableRow.addView(textView3);
                tableRow.addView(textView4);
                tableRow.addView(textView5);
                tableRow.addView(textView6);
                tableRow.addView(imageView);
                tableRow.setTag(tmp.getString("id"));

                if (size % 2 == 0) {
                    tableRow.setBackgroundColor(Color.parseColor("#eeeeee"));
                }
                size += 1;
                tableRow.setClickable(true);
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), HisseDetayActivity.class);
                        intent.putExtra("id", v.getTag().toString());
                        startActivity(intent);
                    }
                });
                tableLayout.addView(tableRow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}