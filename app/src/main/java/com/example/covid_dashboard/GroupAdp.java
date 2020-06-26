package com.example.covid_dashboard;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Member;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GroupAdp extends RecyclerView.Adapter<GroupAdp.ViewHolder> implements Filterable{
    //Initalize activity and arraylist
    private Activity activity;
    ArrayList<GlobalDataFetch>arrayListGroup;
    ArrayList<GlobalDataFetch>arrayListGroupfull;
    ArrayList<String>Listmember;
    String result="";
    static int i=0;


    //Create Constructor
    GroupAdp(Activity activity,ArrayList<GlobalDataFetch>arrayListGroup)
    {
        this.activity=activity;
        this.arrayListGroup=arrayListGroup;
        arrayListGroupfull=new ArrayList<>(arrayListGroup);
        //Listmember=new ArrayList<>(MainActivity.CountryData);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_group,parent,false);
        return new GroupAdp.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //set group name on textview
        GlobalDataFetch obj=arrayListGroup.get(position);
        holder.tvName.setText(obj.getCountry_Name());

        //Intialize member arraylist
         ArrayList<String>arrayListMember=new ArrayList<>();
         //Using for loop to add multiple member

        arrayListMember.add("Total Cases : "+obj.getTotal_Case());

        arrayListMember.add("New Cases : "+obj.getNew_Cases());

        arrayListMember.add("New Deaths : "+obj.getNew_Deaths());

        arrayListMember.add("Total Deaths : "+obj.getTotal_Deaths());

        arrayListMember.add("New Recovered : "+obj.getNew_Recovery());

        arrayListMember.add("Total Recovered : "+obj.getTotal_Recovery());
        //Initialize member adapter
        MemberAdp adapterMember =new MemberAdp(arrayListMember);

        //Initialize layout manager
        LinearLayoutManager layoutManagerMember=new LinearLayoutManager(activity);

        //set Layout Manager
        holder.rvMember.setLayoutManager(layoutManagerMember);

        //set Adapter
        holder.rvMember.setAdapter(adapterMember);



    }

    @Override
    public int getItemCount() {
        return arrayListGroup.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<GlobalDataFetch>filteredList=new ArrayList<>();
            if(constraint==null || constraint.length()==0)
            {
                filteredList.addAll(arrayListGroupfull);
            }
            else
            {
                String filterpattern=constraint.toString().toLowerCase().trim();
                for(GlobalDataFetch item : arrayListGroupfull)
                {
                    if(item.getCountry_Name().toLowerCase().contains(filterpattern))
                    {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayListGroup.clear();
            arrayListGroup.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Initialize variables
        TextView tvName;
        RecyclerView rvMember;


        public ViewHolder(View itemView) {
            super(itemView);

            tvName=itemView.findViewById(R.id.tv_name);
            rvMember=itemView.findViewById(R.id.rv_member);
        }
    }
}
