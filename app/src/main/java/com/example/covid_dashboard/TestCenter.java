package com.example.covid_dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
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
    private TextView full_list_tv;
    RecyclerView rvgrp;
    GroupAdpTestCenter adapterGroup;

    //Initialize layout manager
    LinearLayoutManager layoutManagerGroup = new LinearLayoutManager(this);
    ArrayList<String>TestCenterState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testcenter);

        full_list_tv=(TextView)findViewById(R.id.list_click_tv);
        rvgrp=findViewById(R.id.rv_group_testcenter);

        TestCenterState=new ArrayList<>();

        for(Map.Entry<String,ArrayList<String >>entry:MainActivity.map.entrySet())
        {
            TestCenterState.add(entry.getKey());

        }
        //Initialize layout manager


//        full_list_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PdfDocument mypdf=new PdfDocument();
//                Paint mypaint=new Paint();
//
//                PdfDocument.PageInfo mypageInfo=new PdfDocument.PageInfo.Builder(250,400,1).create();
//                PdfDocument.Page mypage1=mypdf.startPage(mypageInfo);
//
//                Canvas canvas=mypage1.getCanvas();
//
//                mypaint.setTextAlign(Paint.Align.CENTER);
//                mypaint.setTextSize(16f);
//                canvas.drawText("LIST OF TEST CENTERS IN INDIA",mypageInfo.getPageWidth()/2,40,mypaint);
//
//                mypdf.finishPage(mypage1);
//
//                File file=new File(Environment.getExternalStorageDirectory(),"/TestCenterList.pdf");
//
//                try {
//                    mypdf.writeTo(new FileOutputStream(file));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                mypdf.close();
//
//            }
//        });
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
