package com.example.covid_dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class Recycler_View extends AppCompatActivity {

    //Initialize variables
    RecyclerView rvGroup;
    LinearLayoutManager layoutManagerGroup;
    GroupAdp adapterGroup;
    String result="";
    String url="https://api.covid19api.com/summary";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_recycler__view);
        rvGroup=findViewById(R.id.rv_group);

        //Initialize group adapter
        adapterGroup =new GroupAdp(Recycler_View.this,MainActivity.Country_Data);

        //Initialize layout manager
        layoutManagerGroup = new LinearLayoutManager(this);

        //set Layout Manager
        rvGroup.setLayoutManager(layoutManagerGroup);

        //set Adapter
        rvGroup.setAdapter(adapterGroup);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        MenuItem searchitem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView)searchitem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapterGroup.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

}
