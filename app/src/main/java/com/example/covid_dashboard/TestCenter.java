package com.example.covid_dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


public class TestCenter extends AppCompatActivity {

    RecyclerView rvgrp;
    GroupAdpTestCenter adapterGroup;

    //Initialize layout manager
    LinearLayoutManager layoutManagerGroup = new LinearLayoutManager(this);
    ArrayList<String>TestCenterState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testcenter);
        rvgrp=findViewById(R.id.rv_group_testcenter);

        TestCenterState=new ArrayList<>();

        for(Map.Entry<String,ArrayList<String >>entry:MainActivity.map.entrySet())
        {
            TestCenterState.add(entry.getKey());

        }
        //Initialize layout manager
        adapterGroup =new GroupAdpTestCenter(this,TestCenterState);

        //Initialize layout manager
        layoutManagerGroup = new LinearLayoutManager(this);

        //set Layout Manager
        rvgrp.setLayoutManager(layoutManagerGroup);

        //set Adapter
        rvgrp.setAdapter(adapterGroup);

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
        return true;    }
}
