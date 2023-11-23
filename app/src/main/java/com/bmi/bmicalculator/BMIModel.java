package com.bmi.bmicalculator;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BMIModel {
    private String height;
    private String weight;
    private double bmi;
    private String risk;
    private JSONArray links;
    private Context context;
    private OnAPIFetchedListener listener;
    public BMIModel(Context context, String height, String weight) {
        this.context = context;
        this.height = height;
        this.weight = weight;
        calculateBMI();
    }
    private void calculateBMI() {
        String urlString = "http://webstrar99.fulton.asu.edu/page4/Service1.svc/calculateBMI?height=" + height + "&weight=" + weight;
        // use Volley library to handle Http requests
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        bmi = jsonResponse.getDouble("bmi");
                        risk = jsonResponse.getString("risk");
                        links = jsonResponse.getJSONArray("more");

                        if (listener != null) {
                            listener.onAPIFetched(bmi, risk, links);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.e("Volley Error", error.getMessage());
                });
        queue.add(stringRequest);
    }

    public void setOnAPIFetchedListener(OnAPIFetchedListener listener) {
        this.listener = listener;
    }

    public interface OnAPIFetchedListener {
        void onAPIFetched(double bmi, String riskMessage, JSONArray moreInfoLinks);
    }

    public double getBMI() {
        return bmi;
    }

    public String getRisk() {
        return risk;
    }

    public JSONArray getLinks() {
        return links;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public void setLinks(JSONArray links) {
        this.links = links;
    }
}
