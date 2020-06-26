package com.example.covid_dashboard;

import android.os.AsyncTask;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GlobalDataFetch
{
    private String Country_Name;
    private String Total_Case;
    private String New_Cases;
    private String New_Recovery;
    private String Total_Recovery;
    private String New_Deaths;
    private String Total_Deaths;

    GlobalDataFetch(String country_Name,String total_Case,String new_Cases,String new_Recovery,String total_Recovery,String new_Deaths,String total_Deaths)
    {
        Country_Name = country_Name;
        Total_Case = total_Case;
        New_Cases = new_Cases;
        New_Recovery = new_Recovery;
        Total_Recovery = total_Recovery;
        New_Deaths = new_Deaths;
        Total_Deaths = total_Deaths;

    }

    public String getCountry_Name() {
        return Country_Name;
    }

    public String getTotal_Case() {
        return Total_Case;
    }

    public String getNew_Cases() {
        return New_Cases;
    }

    public String getNew_Recovery() {
        return New_Recovery;
    }

    public String getTotal_Recovery() {
        return Total_Recovery;
    }

    public String getNew_Deaths() {
        return New_Deaths;
    }

    public String getTotal_Deaths() {
        return Total_Deaths;
    }
}
