package com.onuralbayrak.imkbhisseveendeksler.ui.HisseEndeksler;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.onuralbayrak.imkbhisseveendeksler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {

    public TableLayout tableLayout;
    public ScrollView scrollView;
    public int size = 0;
    public SearchView searchView;
    private HomeViewModel homeViewModel;
    public JSONArray jsonArray;

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
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        getData(null);

        searchView = root.findViewById(R.id.searchBoxText);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                initializeEmptyTable();
                getData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.equals("")) {
                    initializeEmptyTable();
                    getData(null);
                }
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
        textView1.setText("Date");
        textView2.setText("Label");
        textView3.setText("District");
//        textView3.setGravity(Gravity.CENTER);
        tableRow.addView(textView1);
        tableRow.addView(textView2);
        tableRow.addView(textView3);
        tableRow.setClickable(true);
        tableRow.setBackgroundColor(Color.parseColor("#BEBEBE"));
        tableLayout.addView(tableRow);
    }

    public void getData(String filter) {
        System.out.println("filter: " + filter);
        String dataURL = "https://lit-meadow-91580.herokuapp.com/rest/getPotholes/";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, dataURL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject jsonObject = response.getJSONObject(i);
                                JSONObject jsonObject1 = new JSONObject(jsonObject.get("addr").toString());
                                if (filter != null && !jsonObject1.get("district").toString().toLowerCase().contains(filter.toLowerCase())) {
                                    continue;
                                }
//                                System.out.println(jsonObject.get("creation_date"));
//                                System.out.println(jsonObject.get("label"));
//                                System.out.println(jsonObject1.get("district"));
                                TableRow tableRow = new TableRow(getContext());

                                TextView textView1 = new TextView(getContext());
                                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                Date date;
                                String creationDate = "";
                                try {
                                    date = fmt.parse(jsonObject.get("creation_date").toString()); //Button Text: Remind on: 15 SEP 2017 ( 10:10 ) PM
                                    String newDateString = fmt.format(date);
                                    String tempDate = newDateString;
                                    creationDate = tempDate;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                textView1.setText(creationDate);
                                TextView textView2 = new TextView(getContext());
                                textView2.setText(jsonObject.get("label").toString());
                                TextView textView3 = new TextView(getContext());
                                textView3.setText(jsonObject1.get("district").toString());
                                textView3.setTag(jsonObject1);
                                textView3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            JSONObject tmp = new JSONObject(v.getTag().toString());
                                            System.out.println(tmp.get("district").toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                tableRow.addView(textView1);
                                tableRow.addView(textView2);
                                tableRow.addView(textView3);
                                if (size % 2 == 0) {
                                    tableRow.setBackgroundColor(Color.parseColor("#eeeeee"));
                                }
                                tableRow.setClickable(true);
                                tableLayout.addView(tableRow);
                                size += 1;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Verileri Getirirken Hata!", Toast.LENGTH_LONG);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                    }
                }
        );

        // Add the request to the RequestQueue.
        requestQueue.add(jsonArrayRequest);


    }
}