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

import java.util.ArrayList;
import java.util.Set;

public class District extends AppCompatActivity {

    //Initialize variables
    RecyclerView rvGroupDist;
    GroupAdapterDist adapterDist;
    LinearLayoutManager layoutManagerGroup;
    ArrayList<String>StateNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);
        StateNames=new ArrayList<>();
        BuildRecyclerView();
    }



    public void BuildRecyclerView() {
        StateNames.addAll(MainActivity.DistrictDataFetch.keySet());
        rvGroupDist=findViewById(R.id.rv_group_dist);

        //Initialize group adapter
        adapterDist =new GroupAdapterDist(District.this,StateNames);

        //Initialize layout manager
        layoutManagerGroup = new LinearLayoutManager(this);

        //set Layout Manager
        rvGroupDist.setLayoutManager(layoutManagerGroup);

        //set Adapter
        rvGroupDist.setAdapter(adapterDist);

        adapterDist.setOnCardClickListener(new GroupAdapterDist.OnCardClickListener() {
            @Override
            public void OnCardClick(int position) {
                //Toast.makeText(District.this, "Card Clicked  :  "+StateNames.get(position), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),District_Wise_Data.class);
                intent.putExtra("Clicked_Card",StateNames.get(position));
                startActivity(intent);
            }
        });
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
                adapterDist.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
