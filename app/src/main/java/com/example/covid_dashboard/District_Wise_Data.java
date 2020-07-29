package com.example.covid_dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class District_Wise_Data extends AppCompatActivity {

    //Initialize variables
    RecyclerView rvGroupDistWise;
    GroupAdpDistWise adapterDistWise;
    LinearLayoutManager layoutManagerGroup;
    String StateName;
    ArrayList<String> distnames;
    Iterator<String>keys;
    JSONObject statedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district__wise__data);
        distnames=new ArrayList<>();
        Intent intent=getIntent();
        StateName=intent.getStringExtra("Clicked_Card");
        statedata=MainActivity.DistrictDataFetch.get(StateName);
        //Toast.makeText(this, "StateName "+ StateName, Toast.LENGTH_SHORT).show();
        keys=statedata.keys();
        while(keys.hasNext())
        {
            distnames.add(keys.next().toString());
        }
        BuildRecyclerView();
    }

    private void BuildRecyclerView() {
        rvGroupDistWise=findViewById(R.id.rv_grp_distwise);

        //Initialize group adapter
        adapterDistWise =new GroupAdpDistWise(District_Wise_Data.this,distnames,statedata);

        //Initialize layout manager
        layoutManagerGroup = new LinearLayoutManager(this);

        //set Layout Manager
        rvGroupDistWise.setLayoutManager(layoutManagerGroup);

        //set Adapter
        rvGroupDistWise.setAdapter(adapterDistWise);
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
                adapterDistWise.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
